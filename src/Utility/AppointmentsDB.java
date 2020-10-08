package Utility;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import Model.Appointment;
import Model.Customer;
import View_Controller.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

public class AppointmentsDB {

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();



    // Return all appointments
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        allAppointments.clear();

        //zone and date variables
        DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId localZoneID = ZoneId.systemDefault();
        ZoneId utcZoneID = ZoneId.of("UTC");

        Connection conn = DBConnection.getConnection();

        DBQuery.setStatement(conn); //create statement object
        Statement statement = DBQuery.getStatement(); //get statement reference

        //SELECT statement
        String selectStatement = "SELECT * FROM appointment, customer WHERE appointment.customerId=customer.customerId ORDER BY start ASC";
        //System.out.println("All apts SQL statement: " + selectStatement);

        try {
            statement.execute(selectStatement); //Execute statement
            ResultSet rs = statement.getResultSet(); //Get ResultSet

            while (rs.next()) {

                //get database start time stored as UTC
                String startUTC = rs.getString("start").substring(0, 19);

                //get database end time stored as UTC
                String endUTC = rs.getString("end").substring(0, 19);

                //convert database UTC to LocalDateTime
                LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
                LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);
                //System.out.println("UTC start time: " + utcStartDT);

                //convert times UTC zoneId to local zoneId
                ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

                //convert ZonedDateTime to a string for insertion into AppointmentsTableView
                String localStartDT = localZoneStart.format(datetimeDTF);
                String localEndDT = localZoneEnd.format(datetimeDTF);
                //System.out.println("Converted to local start time: " + localStartDT);

                int aptId = rs.getInt("appointmentId");
                int custId = rs.getInt("customerId");
                String type = rs.getString("type");
                String desc = rs.getString("description");
                String loc = rs.getString("location");
                String cont = rs.getString("contact");
                String custName = rs.getString("customerName");

                allAppointments.add(new Appointment(aptId, custId, localStartDT, localEndDT, type, desc, loc, cont, custName));
            }
            statement.close();
            return allAppointments;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }


    // Return weekly appointments
    public static ObservableList<Appointment> getWeeklyAppointments() throws SQLException {
        weeklyAppointments.clear();

        //zone and date variables
        DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId localZoneID = ZoneId.systemDefault();
        ZoneId utcZoneID = ZoneId.of("UTC");

        Connection conn = DBConnection.getConnection();

        DBQuery.setStatement(conn); //create statement object
        Statement statement = DBQuery.getStatement(); //get statement reference

        LocalDate weekBegin = LocalDate.now();
        LocalDate weekEnd = LocalDate.now().plusWeeks(1);

        //SELECT statement
        String selectStatement = "SELECT * FROM appointment, customer WHERE appointment.customerId=customer.customerId"
                + " AND start >= '" + weekBegin + "' AND start <= '" + weekEnd + "' ORDER BY start ASC";
        //System.out.println("Weekly SQL statement: " + selectStatement);

        try {
            statement.execute(selectStatement); //Execute statement
            ResultSet rs = statement.getResultSet(); //Get ResultSet
            while (rs.next()) {
                //get database start time stored as UTC
                String startUTC = rs.getString("start").substring(0, 19);

                //get database end time stored as UTC
                String endUTC = rs.getString("end").substring(0, 19);

                //convert database UTC to LocalDateTime
                LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
                LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);
                //System.out.println("UTC start time: " + utcStartDT);

                //convert times UTC zoneId to local zoneId
                ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

                //convert ZonedDateTime to a string for insertion into AppointmentsTableView
                String localStartDT = localZoneStart.format(datetimeDTF);
                String localEndDT = localZoneEnd.format(datetimeDTF);
                //System.out.println("Converted to local start time: " + localStartDT);

                int aptId = rs.getInt("appointmentId");
                int custId = rs.getInt("customerId");
                String type = rs.getString("type");
                String desc = rs.getString("description");
                String loc = rs.getString("location");
                String cont = rs.getString("contact");
                String custName = rs.getString("customerName");

                weeklyAppointments.add(new Appointment(aptId, custId, localStartDT, localEndDT, type, desc, loc, cont, custName));

            }
            statement.close();
            return weeklyAppointments;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }


    // Return monthly appointments
    public static ObservableList<Appointment> getMonthlyAppointments() throws SQLException {
        monthlyAppointments.clear();

        //zone and date variables
        DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId localZoneID = ZoneId.systemDefault();
        ZoneId utcZoneID = ZoneId.of("UTC");

        Connection conn = DBConnection.getConnection();

        DBQuery.setStatement(conn); //create statement object
        Statement statement = DBQuery.getStatement(); //get statement reference

        LocalDate monthBegin = LocalDate.now();
        LocalDate monthEnd = LocalDate.now().plusMonths(1);

        //SELECT statement
        String selectStatement = "SELECT * FROM appointment, customer WHERE appointment.customerId=customer.customerId"
                + " AND start >= '" + monthBegin + "' AND start <= '" + monthEnd + "' ORDER BY start ASC";
        //System.out.println("Monthly SQL statement: " + selectStatement);

        try {
            statement.execute(selectStatement); //Execute statement
            ResultSet rs = statement.getResultSet(); //Get ResultSet
            while (rs.next()) {
                //get database start time stored as UTC
                String startUTC = rs.getString("start").substring(0, 19);

                //get database end time stored as UTC
                String endUTC = rs.getString("end").substring(0, 19);

                //convert database UTC to LocalDateTime
                LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
                LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);
                //System.out.println("UTC start time: " + utcStartDT);

                //convert times UTC zoneId to local zoneId
                ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

                //convert ZonedDateTime to a string for insertion into AppointmentsTableView
                String localStartDT = localZoneStart.format(datetimeDTF);
                String localEndDT = localZoneEnd.format(datetimeDTF);
                //System.out.println("Converted to local start time: " + localStartDT);

                int aptId = rs.getInt("appointmentId");
                int custId = rs.getInt("customerId");
                String type = rs.getString("type");
                String desc = rs.getString("description");
                String loc = rs.getString("location");
                String cont = rs.getString("contact");
                String custName = rs.getString("customerName");

                monthlyAppointments.add(new Appointment(aptId, custId, localStartDT, localEndDT, type, desc, loc, cont, custName));

            }
            statement.close();
            return monthlyAppointments;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    // Save new appointment
    public static boolean saveNewAppointment(int id, LocalDateTime startTimeFormatted, LocalDateTime endTimeFormatted, String type) {

        try {
            Statement statement = DBConnection.getConnection().createStatement();
            LocalDateTime createDate = LocalDateTime.now();
            String queryAppointment = "INSERT INTO appointment SET customerId='" + id + "', type='" + type
                    + "', start='" + startTimeFormatted + "', end='" + endTimeFormatted + "', createDate='" + createDate
                    + "', createdBy='admin', title='" + type + "', description='TBD', location='TBD', contact='TBD', url='appointmentinfo.com'"
                    + ", lastUpdate='" + createDate + "', lastUpdateBy='admin'" + ", userId='1'";
            //System.out.println(queryAppointment);

            int updateAddress = statement.executeUpdate(queryAppointment);

            if (updateAddress == 1) {
                getAllAppointments();
                getWeeklyAppointments();
                getMonthlyAppointments();

                    return true;
                }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }


    //update appointment
    public static boolean updateAppointment(int aptId, LocalDateTime startTimeFormatted, LocalDateTime endTimeFormatted, String type) {

        try {
            Statement statement = DBConnection.getConnection().createStatement();
            LocalDateTime createDate = LocalDateTime.now();
            String queryAppointment = "UPDATE appointment SET appointmentId='" + aptId + "', type='" + type
                    + "', start='" + startTimeFormatted + "', end='" + endTimeFormatted + "', createDate='" + createDate
                    + "', createdBy='admin', title='TBD', description='TBD', location='TBD', contact='TBD', url='appointmentinfo.com'"
                    + ", lastUpdate='" + createDate + "', lastUpdateBy='admin'" + ", userId='1'"
                    + " WHERE appointmentId=" + aptId;;
            System.out.println(queryAppointment);
            int updateAddress = statement.executeUpdate(queryAppointment);
            if (updateAddress == 1) {
                getAllAppointments();
                getWeeklyAppointments();
                getMonthlyAppointments();

                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }



    // Delete appointment
    public static boolean deleteAppointment(int id) {
        try {
            Statement statement = DBConnection.getConnection().createStatement();
            String query = "DELETE FROM appointment WHERE appointmentId=" + id;
            statement.executeUpdate(query);

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;


    }


}
