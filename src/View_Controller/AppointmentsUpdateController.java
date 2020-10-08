package View_Controller;

import Model.Appointment;
import Model.Customer;
import Utility.AppointmentsDB;
import Utility.CustomerDB;
import Utility.DBConnection;
import Utility.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import View_Controller.AppointmentsAddController;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class AppointmentsUpdateController implements Initializable {

    @FXML private TextField nameField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox startTimeComboBox;
    @FXML private ComboBox typeComboBox;
    @FXML private Button saveButton;
    @FXML private Button backButton;
    @FXML private Label customerLabel;
    @FXML private Label endTimeLabel;

    private int aptId;
    private final ZoneId localZoneID = ZoneId.systemDefault(); //local zoneId
    private final ZoneId utcZoneID = ZoneId.of("UTC");

    private ObservableList<String> type = FXCollections.observableArrayList(
            "Huddle", "Consult", "Meeting");


    //FXML Loader Variables
    Stage stage;
    Parent scene;

    public boolean saveUpdate(MouseEvent mouseEvent) throws IOException, SQLException {
        LocalDate dateCheck = datePicker.getValue();
        String typeCheck = (String) typeComboBox.getSelectionModel().getSelectedItem();
        LocalTime timeCheck = (LocalTime) startTimeComboBox.getSelectionModel().getSelectedItem();
        if (dateCheck != null && timeCheck !=null && typeCheck != null) {

            //get start time
            LocalTime start = (LocalTime) startTimeComboBox.getSelectionModel().getSelectedItem();
            LocalDate date = datePicker.getValue();
            LocalDateTime startTimeFormatted = LocalDateTime.of(date, start);

            //get end time
            String endText = endTimeLabel.getText();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime endTime = LocalTime.parse(endText, dtf);
            LocalDateTime endTimeFormatted = LocalDateTime.of(date, endTime);

            //convert startDT and endDT to UTC
            ZonedDateTime startUTC = startTimeFormatted.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endUTC = endTimeFormatted.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));

            //convert UTC to LDT for database insertion
            LocalDateTime sqlStartLDT = startUTC.toLocalDateTime();
            LocalDateTime sqlEndLDT = endUTC.toLocalDateTime();

            //get type from combo box
            Object typeObject = typeComboBox.getSelectionModel().getSelectedItem();
            String type = typeObject.toString();


            //checks if appointments are within business hours
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
                DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                String startDateTimeString = rs.getString("start");
                LocalDateTime overlapCheck = LocalDateTime.parse(startDateTimeString, dtf2);
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

                return AppointmentsDB.updateAppointment(aptId, sqlStartLDT, sqlEndLDT, type);
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

    public void sendAppointment(Appointment appointment) {

        aptId = appointment.getAptId();

        //get customer cell from tableview
        String selectedCustomer = appointment.getAptCustomerName();

        //convert appointment string to LocalDateTime
        String str = appointment.getAptStart();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        System.out.println("Here is the time newly formatted as LDT: " + dateTime);

        //split LocalDateTime to Time only
        LocalTime timeFormatted = dateTime.toLocalTime();
        System.out.println("Here is the time newly formatted as time only: " + timeFormatted);

        //split LocalDateTime to Date only
        LocalDate dateFormatted = dateTime.toLocalDate();
        System.out.println("Here is the date newly formatted as date only: " + dateFormatted);

        //setting selected values from main appointments page
        customerLabel.setText(selectedCustomer);
        datePicker.setValue(dateFormatted);
        startTimeComboBox.setValue(timeFormatted);
        typeComboBox.getSelectionModel().select(appointment.getAptType());
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        stage = (Stage)((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/AppointmentsMain.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeComboBox.setItems(type);

        //populates start time combo box items
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(18,0);

        while(start.isBefore(end.plusSeconds(1)))
        {
            startTimeComboBox.getItems().add(start);
            start = start.plusMinutes(30);
        }

        //populates end time label
        startTimeComboBox.getSelectionModel().selectedItemProperty().addListener(event-> //Lambda expression more efficiently tells the listener to use the user-selected start time to set the end time label
        {
            LocalTime selectedTime = (LocalTime) startTimeComboBox.getSelectionModel().getSelectedItem();
            endTimeLabel.setText(String.valueOf(selectedTime.plusMinutes(30)));
            System.out.print("End time set correctly. \n");
        });
    }

}
