package Utility;

import java.io.IOException;
import java.sql.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Utility.DBConnection;
import Model.Customer;
import View_Controller.*;

public class CustomerDB {

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();


    public static Customer getCustomer(int id) throws IOException, SQLException {
        try {
            Statement statement = DBConnection.getConnection().createStatement();
            String query = "SELECT * FROM customer WHERE customerId='" + id + "'";
            ResultSet results = statement.executeQuery(query);
            if (results.next()) {
                Customer customer = new Customer();
                customer.setCustomerName(results.getString("customerName"));
                statement.close();
                return customer;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }


    // Return all customers
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        allCustomers.clear();

        Connection conn = DBConnection.getConnection();

        DBQuery.setStatement(conn); //create statement object
        Statement statement = DBQuery.getStatement(); //get statement reference
        //SELECT statement
        String selectStatement = "SELECT customer.customerId, customer.customerName, address.address, address.phone, address.postalCode, city.city"
                + " FROM customer INNER JOIN address ON customer.addressId = address.addressId "
                + "INNER JOIN city ON address.cityId = city.cityId ORDER BY customerName";

        try {
            statement.execute(selectStatement); //Execute statement
            ResultSet rs = statement.getResultSet(); //Get ResultSet
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customerId"),
                        rs.getString("customerName"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("phone"),
                        rs.getString("postalCode"));
                allCustomers.add(customer);
            }
            statement.close();
            return allCustomers;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }


    // Save new customer
    public static boolean saveNewCustomer(String customerName, String customerAddress, int cityId, String customerZip, String customerPhone) {
        try {
            Statement statement = DBConnection.getConnection().createStatement();
            String customerAddress2 = "";
            LocalDateTime createDate = LocalDateTime.now();
            String queryAddress = "INSERT INTO address SET address='" + customerAddress + "', address2='" + customerAddress2 + "', cityId='" + cityId + "', postalCode='" + customerZip + "', phone='" + customerPhone + "', createDate='" + createDate + "', createdBy='admin" + "', lastUpdate='" + createDate + "', lastUpdateBy='admin'";
            System.out.println(queryAddress);
            int updateAddress = statement.executeUpdate(queryAddress);
            if (updateAddress == 1) {
                String queryCustomer = "INSERT INTO customer SET customerName='" + customerName + "', addressId=" + "LAST_INSERT_ID()" + ", active='1'" + ", createDate='" + createDate + "', createdBy='admin" + "', lastUpdate='" + createDate + "', lastUpdateBy='admin'";
                System.out.println(queryCustomer);
                int updateCustomer = statement.executeUpdate(queryCustomer);
                if (updateCustomer == 1) {
                    getAllCustomers();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    // Update customer
    public static boolean updateCustomer(int id, String customerName, String customerAddress, int cityId, String customerZip, String customerPhone) {
        try {
            Statement statement = DBConnection.getConnection().createStatement();
            String customerAddress2 = "";
            LocalDateTime createDate = LocalDateTime.now();
            String queryOne = "UPDATE address SET address='" + customerAddress + "', address2='" + customerAddress2 + "', cityId=" + cityId + ", postalCode='" + customerZip + "', phone='" + customerPhone + "', createDate='" + createDate + "', createdBy='admin" + "', lastUpdate='" + createDate + "', lastUpdateBy='admin'"
                    + " WHERE addressId=" + id;
            int updateOne = statement.executeUpdate(queryOne);
            if (updateOne == 1) {
                String queryTwo = "UPDATE customer SET customerName='" + customerName + "', addressId=" + id + ", active='1'" + ", createDate='" + createDate + "', createdBy='admin" + "', lastUpdate='" + createDate + "', lastUpdateBy='admin'"
                      + " WHERE customerId=" + id;
                int updateTwo = statement.executeUpdate(queryTwo);
                if (updateTwo == 1) {
                    getAllCustomers();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    // Delete customer
    public static boolean deleteCustomer(int id) {
        try {
            Statement statement = DBConnection.getConnection().createStatement();
            String query = "DELETE FROM appointment WHERE customerId=" + id;
            statement.executeUpdate(query);
            System.out.println(query);
            String queryOne = "DELETE FROM customer WHERE customerId=" + id;
            statement.executeUpdate(queryOne);
            System.out.println(queryOne);
            String queryTwo = "DELETE FROM address WHERE addressId=" + id;
            statement.executeUpdate(queryTwo);
            System.out.println(queryTwo);

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

}
