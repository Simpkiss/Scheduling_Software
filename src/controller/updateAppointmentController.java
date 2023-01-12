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
import java.util.Objects;
import java.util.ResourceBundle;

/** Controller for update appointment screen, has lambda #2 in initialize. */
public class updateAppointmentController implements Initializable {
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
    /** Appointment ID field. */
    public TextField idBox;
    /** Appointment ID number. */
    public int idNum = 0;
    /** Exit button. */
    public Button exitButton;

    /** Time formatter to make time easy to read. */
    DateTimeFormatter ampm = DateTimeFormatter.ofPattern("h:mm a");
    /** Date formatter to make dates easy to read. */
    DateTimeFormatter date = DateTimeFormatter.ofPattern("MM/dd/yy");

    /** Populates all fields with values of the appointment selected on main appointments screen and uses the modifyButton event to set items in start and end time combo boxes,
     * has a copy of Lambda #2 that disables past dates in date picker.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idNum = appointmentsController.selectedAppt.getApptID();
        idBox.setText(Integer.toString(idNum));
        titleBox.setText(appointmentsController.selectedAppt.getApptTitle());
        descBox.setText(appointmentsController.selectedAppt.getApptDesc());
        locBox.setText(appointmentsController.selectedAppt.getApptLocation());
        userDrop.setValue(logInController.userLoggedIn);
        typeBox.setText(appointmentsController.selectedAppt.getApptType());
        dateBox.setValue(LocalDate.parse(appointmentsController.selectedAppt.getDateNice(),date));
        startDrop.setValue(appointmentsController.selectedAppt.getStartTimeNice());
        endDrop.setValue(appointmentsController.selectedAppt.getEndTimeNice());
        try {
            contactDrop.setValue(contactDB.getContactName(appointmentsController.selectedAppt.getContactID()));
            customerDrop.setValue(customerDB.getCustomerName(appointmentsController.selectedAppt.getCustomerID()));
            userDrop.setItems(userDB.getAllUsers());
            customerDrop.setItems(customerDB.getCustomerNames());
            contactDrop.setItems(contactDB.getAllContacts());
            datePicked();
            timePicked();
            lengthPicked();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        dateBox.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate pickableDate, boolean empty) {
                super.updateItem(pickableDate, empty);
                setDisable(pickableDate.isBefore(LocalDate.now()));
            }
        });
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
            checkFilled = true;}
        return checkFilled;
    }

    /** Updates appointment in database.
     *
     */
    public void saveData() throws SQLException {
        String title = titleBox.getText();
        String description = descBox.getText();
        String location = locBox.getText();
        String type = typeBox.getText();
        LocalDateTime start = LocalDateTime.of(dateBox.getValue(), LocalTime.parse(startDrop.getValue().toString(), ampm));
        LocalDateTime end = LocalDateTime.of(dateBox.getValue(), LocalTime.parse(endDrop.getValue().toString(), ampm));
        String updatedBy = userDrop.getValue().toString();
        int customerID = customerDB.getCustomerID(customerDrop.getValue().toString());
        int userID = userDB.getUserID(userDrop.getValue().toString());
        int contactID = contactDB.getContactID(contactDrop.getValue().toString());
        appointmentDB.updateAppointment(idNum,title,description,location,type,start,end,updatedBy,customerID,userID,contactID);
    }

    /** If a date has already been picked, refreshes list of available start times to reflect selected customer.
     *
     */
    public void custSelected() throws SQLException {
        if (!(dateBox.getValue() == null)) {
            int customerID = customerDB.getCustomerID(customerDrop.getValue().toString());
            Object hold = startDrop.getValue();
            startDrop.setValue(null);
            startDrop.setItems(tool.smartStart(openTime, closeTime, dateBox.getValue(), customerID, idNum));
            if(startDrop.getItems().contains(hold)){
                startDrop.setValue(hold);
            }
        }
    }

    /** If a customer has already been selected, refreshes list of available start times to reflect picked date.
     *
     */
    public void datePicked() throws SQLException {
        if (!(customerDrop.getValue() == null)) {
            int customerID = customerDB.getCustomerID(customerDrop.getValue().toString());
            Object hold = startDrop.getValue();
            startDrop.setValue(null);
            startDrop.setItems(tool.smartStart(openTime, closeTime, dateBox.getValue(), customerID, idNum));
            if(startDrop.getItems().contains(hold)){
                startDrop.setValue(hold);
            }
        }
    }

    /** When start time is selected or changed, but not reset to null, populates end time combo box and checks if end time value is still valid.
     *
     */
    public void timePicked() throws SQLException {
        if (startDrop.getValue() != null) {
            int customerID = customerDB.getCustomerID(customerDrop.getValue().toString());
            OffsetTime startTime = OffsetTime.of(LocalTime.parse(startDrop.getValue().toString(), ampm), logInController.localOffset);
            Object hold = endDrop.getValue();
            endDrop.setValue(null);
            durText.setText("");
            endDrop.setItems(tool.smartEnd(startTime, closeTime, dateBox.getValue(), customerID, idNum));
            if (endDrop.getItems().contains(hold)) {
                endDrop.setValue(hold);
            }
        }
    }

    /** When end time is changed or set, but not reset to empty, calls durText to get nice readable string of the duration of the appointment and sets the duration label to that string.
     *
     */
    public void lengthPicked() {
        if (endDrop.getValue() != null) {
            durText.setText(tool.lengthText(LocalTime.parse(startDrop.getValue().toString(),ampm),LocalTime.parse(endDrop.getValue().toString(),ampm)));
        }
    }

    /** Checks if weGood, and if weGood is true, updates the customer in the database and exits to the main appointments screen.
     *
     */
    public void saveAndExitPressed(ActionEvent actionEvent) throws SQLException, IOException {
        if (weGood()) {
            saveData();
            Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/appointmentsScreen.fxml")));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Turbo Scheduler 3000");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** Exits to the main appointments screen.
     *
     */
    public void exitPressed(ActionEvent actionEvent) throws IOException {
        Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/appointmentsScreen.fxml")));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Turbo Scheduler 3000");
        stage.setScene(scene);
        stage.show();
    }
}