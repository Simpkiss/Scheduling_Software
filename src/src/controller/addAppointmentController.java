package controller;

import database.appointmentDB;
import database.contactDB;
import database.customerDB;
import database.userDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utility.tool;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** Controller for add appointments screen, has lambda #2 in initialize. */
public class addAppointmentController implements Initializable {
    /** Start time combo box. */
    public ComboBox startDrop;
    /** End time combo box. */
    public ComboBox endDrop;
    /** Label that displays the duration of the appointment. */
    public Label durText;
    /** Date selector. */
    public DatePicker dateBox;
    /** Gets offset of eastern time to use in regards to business hours. */
    public ZoneOffset eastOffset = ZoneId.of("US/Eastern").getRules().getOffset(Instant.now());
    /** Business open time in eastern time. */
    public OffsetTime openTimeEast = OffsetTime.parse("08:00:00"+eastOffset);
    /** Business close time in eastern time. */
    public OffsetTime closeTimeEast = OffsetTime.parse("22:00:00"+eastOffset);
    /** Open time translated to local time zone. */
    public OffsetTime openTime = openTimeEast.withOffsetSameInstant(logInController.localOffset);
    /** Close time translated to local time zone. */
    public OffsetTime closeTime = closeTimeEast.withOffsetSameInstant(logInController.localOffset);
    /** User selector. */
    public ComboBox userDrop;
    /** Type field. */
    public TextField typeBox;
    /** Contact selector. */
    public ComboBox contactDrop;
    /** Title field. */
    public TextField titleBox;
    /** Description field. */
    public TextField descBox;
    /** Location field. */
    public TextField locBox;
    /** Customer selector. */
    public ComboBox customerDrop;
    /** Exit button. */
    public Button exitButton;
    /** Time formatter for easier to read time. */
    DateTimeFormatter ampm = DateTimeFormatter.ofPattern("h:mm a");

    /** When a start time is set or changed, but not when the value is reset to null, temporarily holds the value of the end time selector, resets both the end time and the duration text,
     * calls smartEnd to set possible end times, then checks if the held value is still a valid option, and if it is, sets the end time value back to what it was.
     *
     */
    public void timePicked() throws SQLException {
        if (startDrop.getValue() != null) {
            int customerID = customerDB.getCustomerID(customerDrop.getValue().toString());
            OffsetTime startTime = OffsetTime.of(LocalTime.parse(startDrop.getValue().toString(), ampm), logInController.localOffset);
            Object hold = endDrop.getValue();
            endDrop.setValue(null);
            durText.setText("");
            endDrop.setItems(tool.smartEnd(startTime, closeTime, dateBox.getValue(), customerID, 0));
            if (endDrop.getItems().contains(hold)) {
                endDrop.setValue(hold);
            }
        }
    }

    /** When the end time of an appointment is selected or changed, but not reset to null, calls lengthText to generate a readable string stating the duration of the appointment and sets the label to this string.
     *
     */
    public void lengthPicked() {
        if (endDrop.getValue() != null) {
            durText.setText(tool.lengthText(LocalTime.parse(startDrop.getValue().toString(),ampm),LocalTime.parse(endDrop.getValue().toString(),ampm)));
        }
    }

    /** If a date has already been selected, sets available start times for an appointment and checks that a possible existing value for start time is still valid for selected customer and date.
     *
     */
    public void custSelected() throws SQLException {
        if (!(dateBox.getValue() == null) && !(customerDrop.getValue() == null)) {
            int customerID = customerDB.getCustomerID(customerDrop.getValue().toString());
            Object hold = startDrop.getValue();
            startDrop.setValue(null);
            startDrop.setItems(tool.smartStart(openTime, closeTime, dateBox.getValue(), customerID, 0));
            if(startDrop.getItems().contains(hold)){
                startDrop.setValue(hold);
            }
        }
    }

    /** If a customer has already been selected, sets available start times for an appointment and checks that a possible existing value for start time is still valid for selected customer and date.
     *
     */
    public void datePicked() throws SQLException {
        if (!(customerDrop.getValue() == null) && !(dateBox.getValue() == null)) {
            int customerID = customerDB.getCustomerID(customerDrop.getValue().toString());
            Object hold = startDrop.getValue();
            startDrop.setValue(null);
            startDrop.setItems(tool.smartStart(openTime, closeTime, dateBox.getValue(), customerID, 0));
            if(startDrop.getItems().contains(hold)){
                startDrop.setValue(hold);
            }
        }
    }

    /** Adds new appointment to database. This simplified the code when I had one button to save and reset in order to add multiple appointments in a row as well as a button to save the appointment and close the page.
     *
     */
    public void saveData() throws SQLException {
        String title = titleBox.getText();
        String description = descBox.getText();
        String location = locBox.getText();
        String type = typeBox.getText();
        LocalDateTime start = LocalDateTime.of(dateBox.getValue(), LocalTime.parse(startDrop.getValue().toString(), ampm));
        LocalDateTime end = LocalDateTime.of(dateBox.getValue(), LocalTime.parse(endDrop.getValue().toString(), ampm));
        String createdBy = userDrop.getValue().toString();
        int customerID = customerDB.getCustomerID(customerDrop.getValue().toString());
        int userID = userDB.getUserID(userDrop.getValue().toString());
        int contactID = contactDB.getContactID(contactDrop.getValue().toString());
        appointmentDB.addAppointment(title, description, location, type, start, end, createdBy, createdBy, customerID, userID, contactID);
    }

    /** Resets highlighting on all fields, then checks all fields for missing values and highlights empty fields, then returns true if no fields were missing.
     *
     */
    private boolean weGood(){
        boolean checkFilled = false;
        int emptyCheck = 0;
        titleBox.setStyle("");
        descBox.setStyle("");
        locBox.setStyle("");
        typeBox.setStyle("");
        dateBox.setStyle("");
        startDrop.setStyle("");
        endDrop.setStyle("");
        contactDrop.setStyle("");
        customerDrop.setStyle("");

        if(titleBox.getText().isEmpty()){
            titleBox.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(descBox.getText().isEmpty()){
            descBox.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(locBox.getText().isEmpty()){
            locBox.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(typeBox.getText().isEmpty()){
            typeBox.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(dateBox.getValue() == null){
            dateBox.setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(startDrop.getValue() == null){
            startDrop.setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(endDrop.getValue() == null){
            endDrop.setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(contactDrop.getValue() == null){
            contactDrop.setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(customerDrop.getValue() == null){
            customerDrop.setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}

        if(emptyCheck == 0){
            checkFilled = true;
        }
        return checkFilled;
    }

    /** Clears all fields on form.
     *
     */
    public void clearForm(){
        titleBox.setStyle("");
        descBox.setStyle("");
        locBox.setStyle("");
        typeBox.setStyle("");
        dateBox.setStyle("");
        startDrop.setStyle("");
        endDrop.setStyle("");
        contactDrop.setStyle("");
        customerDrop.setStyle("");

        titleBox.clear();
        descBox.clear();
        locBox.clear();
        typeBox.clear();
        dateBox.setValue(null);
        startDrop.setValue(null);
        endDrop.setValue(null);
        contactDrop.setValue(null);
        customerDrop.setValue(null);

        titleBox.requestFocus();
    }

    /** Deleted button that would save and clear, letting multiple appointments be added in a row. Was gently encouraged to remove this option.
     *
     */
    public void savePressed() throws Exception {
        if(weGood()) {
            saveData();
            clearForm();
        }
    }

    /** Saves new appointment and returns to main appointments screen if weGood is true.
     *
     */
    public void saveAndExitPressed(ActionEvent actionEvent) throws Exception {
        if(weGood()){
            saveData();
            Parent root= FXMLLoader.load(getClass().getResource("/view/appointmentsScreen.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Turbo Scheduler 3000");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** Returns to main appointments screen.
     *
     */
    public void exitPressed(ActionEvent actionEvent) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/view/appointmentsScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Turbo Scheduler 3000");
        stage.setScene(scene);
        stage.show();
    }

    /** Button that clears the form.
     *
     */
    public void clearPressed() {
        clearForm();
    }

    /** Populates user, contact, and customer combo boxes, sets user as logged in user, Lambda #2 that disables past dates in date picker so new appointments cannot be booked in the past.
     *
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            userDrop.setItems(userDB.getAllUsers());
            customerDrop.setItems(customerDB.getCustomerNames());
            contactDrop.setItems(contactDB.getAllContacts());
        } catch (Exception e) {
            e.printStackTrace();
        }
        userDrop.setValue(logInController.userLoggedIn);

        dateBox.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate pickableDate, boolean empty) {
                super.updateItem(pickableDate, empty);
                setDisable(pickableDate.isBefore(LocalDate.now()));
            }
        });
    }
}



