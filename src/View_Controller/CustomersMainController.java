package View_Controller;

import Utility.CustomerDB;
import Model.Customer;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomersMainController implements Initializable {
    
    @FXML private TableView<Customer> customersTableView;
    @FXML private TableColumn<Customer, Integer> idColumn;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button exitButton;

    @FXML private ObservableList<Customer> customers = FXCollections.observableArrayList();

    //FXML Loader Variables
    Stage stage;
    Parent scene;



    public void back(MouseEvent mouseEvent) throws IOException {
        stage = (Stage)((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }



    public void addCustomer(MouseEvent mouseEvent) throws IOException {
        stage = (Stage)((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/CustomersAdd.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }



    public void updateCustomer(MouseEvent mouseEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View_Controller/CustomersUpdate.fxml"));
            loader.load();

            CustomersUpdateController CUController = loader.getController();
            Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
            CUController.sendCustomer(selectedCustomer);

            stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No customer selected");
            alert.setContentText("Please select a customer to update.");
            alert.showAndWait();
        }
    }



    public void deleteCustomer(MouseEvent mouseEvent) throws IOException, SQLException {
        try {
            Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Delete Customer Record");
            alert.setContentText("Delete " + selectedCustomer.getCustomerName() + " ?");
            alert.showAndWait().ifPresent((response -> { //the lambda expression conveniently implements the response of the user from the "Delete Customer" warning window
                if(response == ButtonType.OK) {
                    CustomerDB.deleteCustomer(selectedCustomer.getCustomerId());
                    try {
                        customersTableView.setItems(CustomerDB.getAllCustomers());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
        catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No customer selected");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
        }

    }

    //Initialize Customer Tableview
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        try {
            customersTableView.setItems(CustomerDB.getAllCustomers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
