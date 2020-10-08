package View_Controller;

import Utility.*;
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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CustomersAddController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private ComboBox cityComboBox;
    @FXML private TextField zipField;
    @FXML private TextField phoneField;
    @FXML private Button saveButton;
    @FXML private Button backButton;

    private ObservableList<String> cities = FXCollections.observableArrayList(
            "Chicago","London","Versailles");

    //FXML loader variables
    Stage stage;
    Parent scene;



    public boolean save(MouseEvent mouseEvent) throws IOException, SQLException {

        String customerName = nameField.getText();
        String customerAddress = addressField.getText();
        int customerCity = cityComboBox.getSelectionModel().getSelectedIndex() + 1;
        String customerZip = zipField.getText();
        String customerPhone = phoneField.getText();

        if (!customerName.equals("") && !customerAddress.equals("") && customerCity !=0 && !customerZip.equals("") && !customerPhone.equals("")) {

            stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/CustomersMain.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

            return CustomerDB.saveNewCustomer(customerName, customerAddress, customerCity, customerZip, customerPhone);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing selection");
            alert.setContentText("Please enter a customer name, address, city, zip code, and phone number for customer");
            alert.showAndWait();
        }
        return false;
    }



    public void back(MouseEvent mouseEvent) throws IOException {
        stage = (Stage)((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/CustomersMain.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cityComboBox.setItems(cities);
    }
}
