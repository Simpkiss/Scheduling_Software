package controller;

import database.JDBC;
import database.appointmentDB;
import database.userDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utility.logInLogger;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
/** Controller for log in screen. */
public class logInController implements Initializable {
    /** Label for username field. */
    public Label userNameLabel;
    /** Username field. */
    public TextField userNameField;
    /** Label for password field. */
    public Label passwordLabel;
    /** Password field. */
    public TextField passwordField;
    /** Label for language selector. */
    public Label languageLabel;
    /** Language selector. */
    public MenuButton languageDrop;
    /** English option for language. */
    public MenuItem englishSelect;
    /** French option for language. */
    public MenuItem frenchSelect;
    /** Label for time zone display. */
    public Label timeZoneLabel;
    /** Time zone display. */
    public Label timeZoneText;
    /** Log in button. */
    public Button logInButton;
    /** Error text that can be set and displayed. */
    public Label errorText;
    /** Locale for setting language automatically. */
    public Locale locale;
    /** Local time zone ID that is used elsewhere for converting times. */
    public static ZoneId localTZ = null;
    /** Local time zone offset that is used elsewhere for converting times. */
    public static ZoneOffset localOffset = null;
    /** Saves username of user that is logged in for use elsewhere. */
    public static String userLoggedIn = null;

    /** Checks if entered username and password credentials are valid, sets user logged in if valid, highlights username and password fields red if not. Sends username and success to log-in logger.
     *
     */
    private boolean credCheck() throws SQLException {
        boolean pass = false;
        if ((database.userDB.userCheck(userNameField.getText(), passwordField.getText()))) {
            pass = true;
            userLoggedIn = userNameField.getText();
        }
        else {
            errorText.setTextFill(Color.RED);
            userNameField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            passwordField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
        }
        logInLogger.newLog(userNameField.getText(),pass);
        return pass;
    }

    /** Calls credential check and opens main appointment screen if successful on enter key pressed in username or password field.
     *
     */
    public void submitKeyed(KeyEvent event) throws SQLException, IOException {
        int key = event.getCode().getCode();
        if (key == 10){
            if (credCheck()) {
                upcomingAppts();
                Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/appointmentsScreen.fxml")));
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    /** Calls credential check and opens main appointment screen if successful on submit button press.
     *
     */
    public void submitClicked(ActionEvent event) throws SQLException, IOException {
        if (credCheck()) {
            upcomingAppts();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/appointmentsScreen.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /** Sets language of all text on login screen depending on the set locale.
     *
     */
    public void setLang(){
        ResourceBundle resource = ResourceBundle.getBundle("language/logIn",locale);
        userNameLabel.setText(resource.getString("userNameLabel"));
        passwordLabel.setText(resource.getString("passwordLabel"));
        languageLabel.setText(resource.getString("languageLabel"));
        languageDrop.setText(resource.getString("languageDrop"));
        timeZoneLabel.setText(resource.getString("timeZoneLabel"));
        logInButton.setText(resource.getString("logInButton"));
        errorText.setText(resource.getString("errorText"));
    }

    /** Manually sets locale to english and calls setLang.
     *
     */
    public void englishSelected() {
        locale = Locale.ENGLISH;
        setLang();
    }

    /** Manually sets locale to french and calls setLang.
     *
     */
    public void frenchSelected() {
        locale = Locale.FRENCH;
        setLang();
    }

    /** Checks for any appointments for the user that just logged in that begin in the next 15 minutes and gives an alert indicating what was found.
     *
     */
    public void upcomingAppts() throws SQLException {
        int userID = userDB.getUserID(userLoggedIn);
        int upcomingAppts = appointmentDB.appointmentsSoon(userID);
        String warningText = "There are no appointments for you starting in the next 15 minutes";
        if(upcomingAppts > 0) {
            warningText = appointmentDB.appointmentsSoonText(userID);
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Upcoming Appointments");
        alert.setContentText(warningText);
        alert.showAndWait();
    }

    /** Records local zone ID and zone offset, sets text that displays time zone, sets locale from system default, and calls setLang upon opening window.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        localTZ = ZoneId.systemDefault();
        localOffset = OffsetTime.now().getOffset();
        timeZoneText.setText(localTZ.toString());
        locale =Locale.getDefault();
        setLang();
    }
}
