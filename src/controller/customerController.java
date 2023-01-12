package controller;

import database.appointmentDB;
import database.countryDB;
import database.customerDB;
import database.userDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller for customer screen. */
public class customerController implements Initializable {
    /** Table that lists all existing customers. */
    public TableView <customer> customerTable;
    /** ID column of customer table. */
    public TableColumn <?,?> idCol;
    /** Customer name column of customer table. */
    public TableColumn <?,?> nameCol;
    /** Customer division column of customer table. */
    public TableColumn <?,?> divCol;
    /** Customer name field. */
    public TextField nameBox;
    /** Customer address field. */
    public TextField addressBox;
    /** Customer post code field. */
    public TextField postBox;
    /** Customer phone number field. */
    public TextField phoneBox;
    /** Combo box for division IDs. */
    public ComboBox divisionDrop;
    /** Customer ID field. */
    public TextField idBox;
    /** Combo box for countries. */
    public ComboBox countryDrop;
    /** Combo box for users. */
    public ComboBox userDrop;
    /** Button to add a customer, is disabled when an existing customer is selected. */
    public Button addButton;
    /** Button to update a selected customer, is disabled unless a customer is selected. */
    public Button updateButton;
    /** Button to delete a selected customer, is disabled unless a customer is selected. */
    public Button deleteButton;
    /** Selected customer. */
    public customer selectedCustomer = null;
    /** Exit Button. */
    public Button exitButton;

    /** Resets borders of all fields necessary to add a new customer, then checks each one for missing values and borders empty fields with red. If no fields are empty, it returns true.
     *
     */
    private boolean weGood(){
        boolean checkFilled = false;
        int emptyCheck = 0;
        nameBox.setStyle("");
        addressBox.setStyle("");
        postBox.setStyle("");
        phoneBox.setStyle("");
        divisionDrop.setStyle("");
        countryDrop.setStyle("");
        userDrop.setStyle("");
        if(nameBox.getText().isEmpty()){
            nameBox.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(addressBox.getText().isEmpty()){
            addressBox.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(postBox.getText().isEmpty()){
            postBox.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(divisionDrop.getValue() == null){
            divisionDrop.setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(countryDrop.getValue() == null){
            countryDrop.setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(userDrop.getValue() == null){
            userDrop.setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
            emptyCheck++;}
        if(emptyCheck == 0){
            checkFilled = true;
        }
        return checkFilled;
    }

    /** Resets all fields on form, sets add button to enabled, sets update and delete buttons to disabled.
     *
     */
    public void resetForm(){
        nameBox.clear();
        addressBox.clear();
        postBox.clear();
        phoneBox.clear();
        idBox.clear();
        divisionDrop.setValue(null);
        countryDrop.setValue(null);
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    /** Populates table with all customers.
     *
     */
    public void resetTable() throws SQLException {
        customerTable.setItems(customerDB.getAllCustomers());
    }

    /** If a customer from customer table is highlighted, selects highlighted customer, disables add button, and enables update and delete buttons.
     *
     */
    public void selectPressed() throws SQLException {
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No customer selected");
            alert.setContentText("Select a customer and try again");
            alert.showAndWait();
        } else {
            idBox.setText(Integer.toString(selectedCustomer.getCustomerID()));
            nameBox.setText(selectedCustomer.getCustomerName());
            addressBox.setText(selectedCustomer.getCustomerAddress());
            postBox.setText(selectedCustomer.getCustomerPostCode());
            phoneBox.setText(selectedCustomer.getCustomerPhone());
            countryDrop.setValue(countryDB.getCountryIDFromDiv(selectedCustomer.getDivisionID()));
            divisionDrop.setValue(countryDB.getDivName(selectedCustomer.getDivisionID()));
            userDrop.setValue(logInController.userLoggedIn);
            addButton.setDisable(true);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    /** Checks pertinent fields with weGood function, then adds new customer to database if weGood is true, then resets both the form and table.
     *
     */
    public void addPressed() throws SQLException {
        if(weGood()) {
            customerDB.addCustomer(nameBox.getText(), addressBox.getText(), postBox.getText(), phoneBox.getText(),
                    userDrop.getValue().toString(), countryDB.getDivID(divisionDrop.getValue().toString()));
            resetForm();
            resetTable();
        }
    }

    /** Button that calls reset function.
     *
     */
    public void resetPressed() {
        resetForm();
    }

    /** Checks pertinent fields with weGood function, then updates selected customer in database if weGood is true, then resets both the form and table.
     *
     */
    public void updatePressed() throws SQLException {
        if(weGood()) {
            customerDB.updateCustomer(nameBox.getText(), addressBox.getText(), postBox.getText(), phoneBox.getText(),
                    userDrop.getValue().toString(), countryDB.getDivID(divisionDrop.getValue().toString()), selectedCustomer.getCustomerID());
            resetForm();
            resetTable();
        }
    }

    /** Deletes selected customer after asking for confirmation, checking for any existing appointments for that customer, getting a second confirmation if there are existing appointments, and deleting any appointments for the customer.
     *
     */
    public void deletePressed() throws SQLException {
        String customerName = selectedCustomer.getCustomerName();
        int objections = 0;
        int apptCount = appointmentDB.appointmentCount(selectedCustomer.getCustomerID()); //Check for appointments
        if (apptCount == 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("WARNING!");
            alert.setHeaderText("You are about to delete this customer");
            alert.setContentText("Are you sure you want to do this?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) { //Delete confirmed
            }else{
                objections++;
            }
        } else if (apptCount > 0) {
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            alert2.setHeaderText("This customer has " + apptCount + " appointments booked already");
            alert2.setContentText("Are you sure you want to delete this customer and all associated appointments?");
            Optional<ButtonType> result2 = alert2.showAndWait();
            if (result2.isPresent() && result2.get() == ButtonType.OK) { //Delete confirmed
            }
            else{
                objections++;
            }
        }
        if(objections == 0){
            if(apptCount>0){
                appointmentDB.deleteCustomersAppointments(selectedCustomer.getCustomerID());
            }
            customerDB.deleteCustomer(selectedCustomer.getCustomerID());
            Alert done = new Alert(Alert.AlertType.INFORMATION);
            done.setHeaderText("Customer successfully removed.");
            done.setContentText(customerName + " and " + apptCount + " related appointments removed from system.");
            done.showAndWait();
            resetForm();
            resetTable();
        }
    }

    /** Exits page and returns to appointment screen.
     *
     */
    public void closePressed(ActionEvent actionEvent) throws IOException {
        Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/appointmentsScreen.fxml")));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Turbo Scheduler 3000");
        stage.setScene(scene);
        stage.show();
    }

    /** Populates divisions combo box with relevant first level divisions after a country is selected.
     *
     */
    public void countryPicked() throws SQLException {
        if(countryDrop.getValue()!=null){
            String country = countryDrop.getValue().toString();
            int countryID = countryDB.getCountryID(country);
            divisionDrop.setValue(null);
            divisionDrop.setItems(countryDB.getDivs(countryID));
        }
    }

    /** Populates customer table, fills users combo box and countries combo box from database, then sets user as the logged in user.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            customerTable.setItems(customerDB.getAllCustomers());
            idCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            divCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
            userDrop.setItems(userDB.getAllUsers());
            countryDrop.setItems(countryDB.getCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }
        userDrop.setValue(logInController.userLoggedIn);
        resetForm();
    }
}
