package Utility;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    //JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U06aqy";

    //JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    //Driver and connection interface reference
    private static final String MYSQLDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    private static final String username = "U06aqy"; //Username
    private static final String password = "53688711931"; //Password

    public static Connection getConnection(){

        try {
            Class.forName(MYSQLDriver);
            conn = (Connection)DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successful");
        }
        catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        catch(SQLException e){
            System.out.println("Error " + e.getMessage());
        }

        return conn;
    }

    public static void closeConnection(){

        try{
            conn.close();
            System.out.println("Connection closed");
        }

        catch(SQLException e){
            System.out.println("Error " + e.getMessage());
        }
    }

}
