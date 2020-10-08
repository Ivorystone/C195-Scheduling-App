package Main;

import Utility.DBConnection;
import Utility.DBQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class SchedulingApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/LoginScreen.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Scheduling Application");
        primaryStage.show();
    }


    public static void main(String[] args) {
      
        Connection conn = DBConnection.getConnection();
        launch(args);
        DBConnection.closeConnection();

        ResourceBundle rb = ResourceBundle.getBundle("Utility/Nat", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("fr"))
            System.out.println(rb.getString("hello") + " " + rb.getString("world"));


    }
}
