package View_Controller;

import Utility.DBConnection;
import Utility.DBQuery;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class ReportsController {

    @FXML private TextArea numAppointmentsField;
    @FXML private Button numAppointmentsButton;
    @FXML private Button clearNumAppointmentsButton;
    @FXML private TextArea consultantScheduleField;
    @FXML private Button consultantScheduleButton;
    @FXML private Button clearConsultantScheduleButton;
    @FXML private TextArea customerPhoneField;
    @FXML private Button customerPhoneButton;
    @FXML private Button clearCustomerPhoneButton;
    @FXML private Button backButton;

    //FXML Loader Variables
    Stage stage;
    Parent scene;



    public void generateAppointmentsByMonth(MouseEvent mouseEvent) throws IOException, SQLException {
       //apptList = FXCollections.observableArrayList();

        try{
            Statement statement = DBConnection.getConnection().createStatement();
            String query = "SELECT MONTHNAME(`start`) AS \"Month\", type AS \"Type\", COUNT(*) as \"Amount\" "
                            + "FROM appointment "
                            + "GROUP BY MONTHNAME(`start`), type";
            ResultSet rs = statement.executeQuery(query);
            StringBuilder reportOneText = new StringBuilder();
            reportOneText.append(String.format("%1$-27s %2$-27s %3$-30s \n",
                    "Month", "Type", "Amount"));
            reportOneText.append(String.join("", Collections.nCopies(50, "-")));
            reportOneText.append("\n");
            while (rs.next()) {
                reportOneText.append(String.format("%1$-25s %2$-25s %3$-25s \n",
                rs.getString("Month"),
                rs.getString("Type"),
                rs.getString("Amount")));
            }
            statement.close();
            numAppointmentsField.setText(reportOneText.toString());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public void clearNumAppointments(){ numAppointmentsField.setText(""); }



    public void generateConsultantSchedule(MouseEvent mouseEvent) throws IOException, SQLException {
        try {
            //zone and date variables
            DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZoneId localZoneID = ZoneId.systemDefault();
            ZoneId utcZoneID = ZoneId.of("UTC");

            Statement statement = DBConnection.getConnection().createStatement();
            String query = "SELECT start, end, type FROM appointment ORDER BY start ASC";
            ResultSet rs = statement.executeQuery(query);
            StringBuilder reportTwoText = new StringBuilder();
            reportTwoText.append(String.format("%1$-40s %2$-40s %3$-30s \n",
                    "Start", "End", "Type"));
            reportTwoText.append(String.join("", Collections.nCopies(65, "-")));
            reportTwoText.append("\n");
            while(rs.next()) {
                //get database start time stored as UTC
                String startUTC = rs.getString("start").substring(0, 19);

                //get database end time stored as UTC
                String endUTC = rs.getString("end").substring(0, 19);

                //convert database UTC to LocalDateTime
                LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
                LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);
                System.out.println("UTC start time: " + utcStartDT);

                //convert times UTC zoneId to local zoneId
                ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

                //convert ZonedDateTime to a string for insertion into AppointmentsTableView
                String localStartDT = localZoneStart.format(datetimeDTF);
                String localEndDT = localZoneEnd.format(datetimeDTF);
                System.out.println("Converted to local start time: " + localStartDT);

                reportTwoText.append(String.format("%1$-25s %2$-25s %3$-25s \n",
                        //rs.getString("start"),
                        //rs.getString("end"),
                        localStartDT, localEndDT,
                        rs.getString("type")));
            }
            statement.close();
            consultantScheduleField.setText(reportTwoText.toString());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public void clearConsultantSchedule(){
        consultantScheduleField.setText("");
    }



    public void generateCustomerPhone(MouseEvent mouseEvent) throws IOException, SQLException {
        try {
            Statement statement = DBConnection.getConnection().createStatement();
            String query = "SELECT customer.customerName, address.phone FROM customer, address WHERE customer.customerId=address.addressId";
            ResultSet rs = statement.executeQuery(query);
            StringBuilder reportThreeText = new StringBuilder();
            reportThreeText.append(String.format("%1$-40s %2$-40s \n",
                    "Name", "Phone number"));
            reportThreeText.append(String.join("", Collections.nCopies(40, "-")));
            reportThreeText.append("\n");
            while(rs.next()) {
                reportThreeText.append(String.format("%1$-25s %2$-40s \n",
                        rs.getString("customerName"),
                        rs.getString("phone")));
            }
            statement.close();
            customerPhoneField.setText(reportThreeText.toString());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public void clearCustomerPhone(){ customerPhoneField.setText(""); }



    public void back(MouseEvent mouseEvent) throws IOException {
        stage = (Stage)((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


}
