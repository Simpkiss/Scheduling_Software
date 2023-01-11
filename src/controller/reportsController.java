package controller;

import database.appointmentDB;
import database.contactDB;
import database.userDB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.appointment;
import model.report;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** Controller for the reports page.
 * First report shows the number of a specified type of appointments in each month of the current year.
 * Second report shows all upcoming appointments for a selected contact, but does not show past appointments.
 * Third report shows how many total appointments each user has associated with them.
 */
public class reportsController implements Initializable {
    /** Table that displays the count of a selected appointment type in each month of the current calendar year. This is the first report.*/
    public TableView <report> typeByMonthTable;
    /** Month column of Type table. */
    public TableColumn <?,?> typeMonthCol;
    /** Count column of Type table. */
    public TableColumn <?,?> typeCountCol;
    /** Combo box for selecting appointment type for type table. */
    public ComboBox typePicker;
    /** Table that shows schedule of upcoming appointments for a selected contact. This is the second report.*/
    public TableView <appointment> contactScheduleTable;
    /** Appointment ID column of schedule table. */
    public TableColumn <?,?> scheduleApptIDCol;
    /** Title column of schedule table. */
    public TableColumn <?,?> scheduleTitleCol;
    /** Type column od schedule table. */
    public TableColumn <?,?> scheduleTypeCol;
    /** Description column of schedule table. */
    public TableColumn <?,?> scheduleDescCol;
    /** Date column of schedule table. */
    public TableColumn <?,?> scheduleDateCol;
    /** Start time column of schedule table. */
    public TableColumn <?,?> scheduleStartCol;
    /** End time column of schedule table. */
    public TableColumn <?,?> scheduleEndCol;
    /** Customer ID column of schedule table. */
    public TableColumn <?,?> scheduleCustIDCol;
    /** Combo box for selecting contact name for contact schedule. */
    public ComboBox contactPicker;
    /** Table that shows how many total appointments each user has. This is the third report. */
    public TableView userAppts;
    /** User column of user appointments table. */
    public TableColumn <?,?> userCol;
    /** Appointment count column of user appointments table. */
    public TableColumn <?,?> apptCountCol;

    /** Populates type table when a type is selected.
     *
     */
    public void typePicked() throws SQLException {
        typeByMonthTable.setItems(appointmentDB.typeCount(typePicker.getValue().toString()));
    }

    /** Populates schedule table when a contact is selected.
     *
     */
    public void contactPicked() throws Exception {
        int contactID = contactDB.getContactID(contactPicker.getValue().toString());
        ObservableList<appointment> schedule = appointmentDB.getSchedule(contactID);
        contactScheduleTable.setItems(schedule);
    }

    /** Returns to appointments screen.
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

    /** Prepares report tables, populates combo boxes for appointment type and contact names, generates third report.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scheduleApptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        scheduleTitleCol.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        scheduleTypeCol.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        scheduleDescCol.setCellValueFactory(new PropertyValueFactory<>("apptDesc"));
        scheduleDateCol.setCellValueFactory(new PropertyValueFactory<>("dateNice"));
        scheduleStartCol.setCellValueFactory(new PropertyValueFactory<>("startTimeNice"));
        scheduleEndCol.setCellValueFactory(new PropertyValueFactory<>("endTimeNice"));
        scheduleCustIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        typeMonthCol.setCellValueFactory(new PropertyValueFactory<>("stringValue"));
        typeCountCol.setCellValueFactory(new PropertyValueFactory<>("intValue"));
        userCol.setCellValueFactory(new PropertyValueFactory<>("stringValue"));
        apptCountCol.setCellValueFactory(new PropertyValueFactory<>("intValue"));

        try {
            contactPicker.setItems(contactDB.getAllContacts());
            typePicker.setItems(appointmentDB.appointmentTypes());
            userAppts.setItems(userDB.userAppts());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
