package View_Controller;

import Model.Appointment;
import Model.Customer;
import Utility.CustomerDB;
import Utility.AppointmentsDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.ResourceBundle;

public class AppointmentsMainController implements Initializable {

    @FXML private Tab allTab;
    @FXML private TableView<Appointment> allTableView;
    @FXML private TableColumn<Appointment, Time> allStartTimeColumn;
    @FXML private TableColumn<Appointment, Time> allEndTimeColumn;
    @FXML private TableColumn<Appointment, String> allTypeColumn;
    @FXML private TableColumn<Appointment, String> allCustomerColumn;

    @FXML private Tab weeklyTab;
    @FXML private TableView<Appointment> weeklyTableView;
    @FXML private TableColumn<Appointment, Time> weeklyStartTimeColumn;
    @FXML private TableColumn<Appointment, Time> weeklyEndTimeColumn;
    @FXML private TableColumn<Appointment, String> weeklyTypeColumn;
    @FXML private TableColumn<Appointment, String> weeklyCustomerColumn;

    @FXML private Tab monthlyTab;
    @FXML private TableView<Appointment> monthlyTableView;
    @FXML private TableColumn<Appointment, Time> monthlyStartTimeColumn;
    @FXML private TableColumn<Appointment, Time> monthlyEndTimeColumn;
    @FXML private TableColumn<Appointment, String> monthlyTypeColumn;
    @FXML private TableColumn<Appointment, String> monthlyCustomerColumn;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;


        //FXML Loader Variables
        Stage stage;
        Parent scene;

        //Event handlers
        public void addAppointment(MouseEvent mouseEvent) throws IOException {
            stage = (Stage)((Button)mouseEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/AppointmentsAdd.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    public void updateAppointment(MouseEvent mouseEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View_Controller/AppointmentsUpdate.fxml"));
            loader.load();

            AppointmentsUpdateController AUController = loader.getController();
            Appointment selectedAppointment = allTableView.getSelectionModel().getSelectedItem();
            AUController.sendAppointment(selectedAppointment);

            stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No appointment selected");
            alert.setContentText("Please select an appointment to update.");
            alert.showAndWait();
        }
    }

    public void deleteAppointment(MouseEvent mouseEvent) throws IOException, SQLException {
        try {
            Appointment selectedAllAppointment = allTableView.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Delete Appointment");
            alert.setContentText("Delete " + selectedAllAppointment.getAptType() + " ?");
            alert.showAndWait().ifPresent((response -> { //Lambda expression is used here to conveniently receive the response of the user's input on the "Delete Customer" window to decide whether to proceed with the delete
                if(response == ButtonType.OK) {
                    AppointmentsDB.deleteAppointment(selectedAllAppointment.getAptId());
                    try {
                        allTableView.setItems(AppointmentsDB.getAllAppointments());
                        weeklyTableView.setItems(AppointmentsDB.getWeeklyAppointments());
                        monthlyTableView.setItems(AppointmentsDB.getMonthlyAppointments());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
        catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No appointment selected");
            alert.setContentText("Please select an appointment from the 'All' tab to delete.");
            alert.showAndWait();
        }

    }

        public void back(MouseEvent mouseEvent) throws IOException {
            stage = (Stage)((Button)mouseEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    //Initialize All Appointments Tableview
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //All appointments tableview
        allStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("aptStart"));
        allEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("aptEnd"));
        allTypeColumn.setCellValueFactory(new PropertyValueFactory<>("aptType"));
        allCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("aptCustomerName"));

        //Weekly appointments tableview
        weeklyStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("aptStart"));
        weeklyEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("aptEnd"));
        weeklyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("aptType"));
        weeklyCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("aptCustomerName"));

        //Monthly appointments tableview
        monthlyStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("aptStart"));
        monthlyEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("aptEnd"));
        monthlyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("aptType"));
        monthlyCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("aptCustomerName"));
        try {
            allTableView.setItems(AppointmentsDB.getAllAppointments());
            weeklyTableView.setItems(AppointmentsDB.getWeeklyAppointments());
            monthlyTableView.setItems(AppointmentsDB.getMonthlyAppointments());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
