package View_Controller;

import Utility.DBConnection;
import Utility.DBQuery;
import Utility.UserDB;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;


public class LoginScreenController implements Initializable {

    @FXML private Button loginButton;
    @FXML private Button exitButton;
    @FXML private Label usernameText;
    @FXML private Label titleText;
    @FXML private Label passwordText;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;


    //FXML Loader Variables
    Stage stage;
    Parent scene;

    private String errorTitle;
    private String errorText;

    //Event handlers
    public boolean logIn(MouseEvent mouseEvent) throws IOException, SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean validUser = UserDB.login(username, password);

        if(validUser) {

            //for overlapping appointments
            Connection conn = DBConnection.getConnection();
            DBQuery.setStatement(conn); //create statement object
            Statement statement = DBQuery.getStatement(); //get statement reference
            String selectStatement = "SELECT start FROM appointment";
            statement.execute(selectStatement); //Execute statement
            ResultSet rs = statement.getResultSet(); //Get ResultSet

            while (rs.next()) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                String startDateTimeString = rs.getString("start");
                LocalDateTime aptSoonCheck = LocalDateTime.parse(startDateTimeString, dtf);
                System.out.println("aptSoonCheck times: " + aptSoonCheck);

                LocalDateTime now = LocalDateTime.now().plusHours(5);
                System.out.println("Time now: " + now);

                //checks if any overlapping appointments
                if (now.isBefore(aptSoonCheck) && now.isAfter(aptSoonCheck.minusMinutes(15))) {
                    Alert overlapAlert = new Alert(Alert.AlertType.INFORMATION);
                    overlapAlert.setTitle("Appointment soon");
                    overlapAlert.setContentText("You have an appointment starting within the next 15 minutes");
                    overlapAlert.showAndWait();
                }
            }

                stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(errorTitle);
            alert.setContentText(errorText);
            alert.showAndWait();
        }
        return false;
}

    public void exitProgram(MouseEvent mouseEvent) {
        DBConnection.closeConnection();
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundle.getBundle("Language.Nat", Locale.getDefault());
        titleText.setText(rb.getString("title"));
        usernameText.setText(rb.getString("username"));
        passwordText.setText(rb.getString("password"));
        loginButton.setText(rb.getString("login"));
        exitButton.setText(rb.getString("exit"));
        errorTitle = rb.getString("errorTitle");
        errorText = rb.getString("errorText");
    }

}
