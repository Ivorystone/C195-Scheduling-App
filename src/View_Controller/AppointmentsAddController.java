package View_Controller;

import Model.Appointment;
import Model.Customer;
import View_Controller.AppointmentsMainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Utility.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;



public class AppointmentsAddController implements Initializable {

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button backButton;
    @FXML private Label endTimeLabel;


    private ObservableList<String> type = FXCollections.observableArrayList(
            "Huddle", "Consult", "Meeting");

    private final ZoneId localZoneID = ZoneId.systemDefault(); //local zoneId
    private final ZoneId utcZoneID = ZoneId.of("UTC");




    //FXML loader variables
    Stage stage;
    Parent scene;



    public boolean save(MouseEvent mouseEvent) throws IOException, SQLException {
        LocalDate dateCheck = datePicker.getValue();
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        String typeCheck = typeComboBox.getSelectionModel().getSelectedItem();
        LocalTime timeCheck = startTimeComboBox.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null && dateCheck != null && timeCheck !=null && typeCheck != null) {
            int id = selectedCustomer.getCustomerId();

            LocalDate date = datePicker.getValue();
            LocalTime startTime = startTimeComboBox.getSelectionModel().getSelectedItem();
            LocalTime endTime = LocalTime.parse(endTimeLabel.getText());

            LocalDateTime startTimeFormatted = LocalDateTime.of(date, startTime);
            LocalDateTime endTimeFormatted = LocalDateTime.of(date, endTime);
            System.out.println("startTimeFormatted: " + startTimeFormatted);

            //convert startDT and endDT to UTC
            ZonedDateTime startUTC = startTimeFormatted.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endUTC = endTimeFormatted.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));

            //convert UTC to LDT for database insertion
            LocalDateTime sqlStartLDT = startUTC.toLocalDateTime();
            LocalDateTime sqlEndLDT = endUTC.toLocalDateTime();

            String type = typeComboBox.getSelectionModel().getSelectedItem();

            LocalDate localDate = startTimeFormatted.toLocalDate();
            LocalTime early = LocalTime.of(9, 0);
            LocalTime late = LocalTime.of(16, 30);
            LocalDateTime open = LocalDateTime.of(localDate, early);
            LocalDateTime close = LocalDateTime.of(localDate, late);

//////////////////////////////////// for overlapping appointments
            Connection conn = DBConnection.getConnection();
            DBQuery.setStatement(conn); //create statement object
            Statement statement = DBQuery.getStatement(); //get statement reference
            String selectStatement = "SELECT start FROM appointment";
            statement.execute(selectStatement); //Execute statement
            ResultSet rs = statement.getResultSet(); //Get ResultSet

            while (rs.next()) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                String startDateTimeString = rs.getString("start");
                LocalDateTime overlapCheck = LocalDateTime.parse(startDateTimeString, dtf);
                System.out.println("startDateTime: " + overlapCheck);


                //checks if any overlapping appointments
                if (startTimeFormatted.equals(overlapCheck.minusHours(5))) {
                    Alert overlapAlert = new Alert(Alert.AlertType.ERROR);
                    overlapAlert.setTitle("Overlapping appointment");
                    overlapAlert.setContentText("Appointment time conflicts with an existing appointment. Please select a new time.");
                    overlapAlert.showAndWait();
                    return false;
                }
            }

                //checks if appointments are within business hours
                if (startTimeFormatted.isBefore(open) || startTimeFormatted.isAfter(close)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Appointment is outside of business hours");
                    alert.setContentText("Business hours are between 9:00 and 17:00. Please select a new time.");
                    alert.showAndWait();
                } else {
                    stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/View_Controller/AppointmentsMain.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();

                    return AppointmentsDB.saveNewAppointment(id, sqlStartLDT, sqlEndLDT, type);
                }

        }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Missing selection");
                alert.setContentText("Please select a customer, date, time, and type for appointment");
                alert.showAndWait();
            }

            return false;

    }



    public void back(MouseEvent mouseEvent) throws IOException {
        stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/AppointmentsMain.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //populates customer table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        try {
            customerTable.setItems(CustomerDB.getAllCustomers());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //populates type box items
        typeComboBox.setItems(type);


        //populates start time combo box items
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(18,0);

        while(start.isBefore(end.plusSeconds(1)))
        {
            startTimeComboBox.getItems().add(start);
            start = start.plusMinutes(30);
        }

        startTimeComboBox.getSelectionModel().selectedItemProperty().addListener(event->  //Lambda expression more efficiently tells the listener to use the user-selected start time to set the end time label
        {
            LocalTime selectedTime = startTimeComboBox.getSelectionModel().getSelectedItem();
            endTimeLabel.setText(String.valueOf(selectedTime.plusMinutes(30)));
            System.out.print("End time set correctly. ");
        });

    }
}
