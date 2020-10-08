package View_Controller;

import Model.Customer;
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

import javax.xml.soap.Text;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CustomersUpdateController implements Initializable {

    private int id;

    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private ComboBox cityComboBox;
    @FXML private TextField phoneField;
    @FXML private TextField zipField;
    @FXML private Button saveButton;
    @FXML private Button backButton;

    private ObservableList<String> cities = FXCollections.observableArrayList(
            "Chicago","London","Versailles");

    //FXML Loader Variables
    Stage stage;
    Parent scene;



    public boolean saveUpdate(MouseEvent mouseEvent) throws IOException, SQLException {

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

            System.out.println("customerName is: " + customerName);

            return CustomerDB.updateCustomer(id, customerName, customerAddress, customerCity, customerZip, customerPhone);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing selection");
            alert.setContentText("Please enter a customer name, address, city, zip code, and phone number for customer");
            alert.showAndWait();
        }
        return false;
    }


    public void sendCustomer(Customer customer) {
        id = customer.getCustomerId();
        nameField.setText(customer.getCustomerName());
        addressField.setText(customer.getCustomerAddress());
        cityComboBox.getSelectionModel().select(customer.getCustomerCity());
        zipField.setText(customer.getCustomerZip());
        phoneField.setText(customer.getCustomerPhone());
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
