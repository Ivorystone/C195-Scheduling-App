package View_Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScreenController {

    @FXML private Button customersButton;
    @FXML private Button appointmentsButton;
    @FXML private Button reportsButton;
    @FXML private Button exitButton;

    //FXML Loader Variables
    Stage stage;
    Parent scene;

    public void manageCustomers(MouseEvent mouseEvent) throws IOException {
        stage = (Stage)((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/CustomersMain.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    public void manageAppointments(MouseEvent mouseEvent) throws IOException {
        stage = (Stage)((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/AppointmentsMain.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void createReports(MouseEvent mouseEvent) throws IOException {
        stage = (Stage)((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void logOut(MouseEvent mouseEvent) throws IOException {
        stage = (Stage)((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/LoginScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
