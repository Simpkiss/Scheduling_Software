package controller;

import database.appointmentDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.appointment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/** Controller for the main appointment screen, has lambda #1 in fillTable2. */
public class appointmentsController implements Initializable {
    /** Toggle group for the three view radio buttons below. */
    public ToggleGroup viewSelect;
    /** Radio button to view all appointments. */
    public RadioButton apptsAll;
    /** Radio button to view next weeks appointments. */
    public RadioButton apptsWeek;
    /** Radio button to view next months appointments. */
    public RadioButton apptsMonth;
    /** Table of selected appointments. */
    public TableView<appointment> apptTable;
    /** Appointment ID column of table. */
    public TableColumn<?,?> apptNumCol;
    /** Title column of table. */
    public TableColumn<?,?> apptTitleCol;
    /** Description column of table. */
    public TableColumn<?,?> apptDescCol;
    /** Location column of table. */
    public TableColumn<?,?> apptLocCol;
    /** Contact ID column of table. */
    public TableColumn<?,?> apptContactCol;
    /** Type column of table. */
    public TableColumn<?,?> apptTypeCol;
    /** Date column of table. */
    public TableColumn<?,?> apptDateCol;
    /** Start time column of table. */
    public TableColumn<?,?> apptStartCol;
    /** End time column of table. */
    public TableColumn<?,?> apptEndCol;
    /** Customer ID column of table. */
    public TableColumn<?,?> apptCustCol;
    /** User ID column of table. */
    public TableColumn<?,?> apptUserCol;
    /** Selected appointment to either be deleted or modified. */
    public static appointment selectedAppt = null;
    /** Action event used when modifying an appointment. */
    public static ActionEvent modifyButton;
    /** String indicating selected view so that view selection is maintained when leaving and returning to tis window */
    public static String selectMode = "all";


    /** Selects all appointments and calls to fill table.
     *
     */
     public void apptsAllPressed() throws Exception {
        selectMode = "all";
        fillTable2();
    }

    /** Selects appointments for the next week and calls to fill the table.
     *
     */
    public void apptsWeekPressed() throws Exception {
        selectMode = "week";
        fillTable2();
    }

    /** Selects appointments for the next month and calls to fill the table.
     *
     */
     public void apptsMonthPressed() throws Exception {
        selectMode = "month";
        fillTable2();
    }

    /** Fills appointment table depending on what view has been selected,
     * RENDERED OBSOLETE BY LAMBDA FUNCTION BELOW!!!
     */
     public void fillTable() throws Exception {
        if(selectMode.equals("all")){
            apptTable.setItems(appointmentDB.getAllAppts());
        }
        else if (selectMode.equals("week")){
            LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.ofSecondOfDay(0));
            LocalDateTime end = start.plusWeeks(1);
            apptTable.setItems(appointmentDB.getSomeAppts(start,end));
        }
        else {
            LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.ofSecondOfDay(0));
            LocalDateTime end = start.plusMonths(1);
            apptTable.setItems(appointmentDB.getSomeAppts(start,end));
        }
    }

    /** Fills appointment table depending on what view has been selected using, Lambda #1 filters appointments by selected view.
     *
     */
    public void fillTable2() throws Exception {
        ObservableList<appointment> allAppointments = appointmentDB.getAllAppts();
        if(selectMode.equals("week")){
            ObservableList<appointment> filteredAppointments = allAppointments
                    .stream()
                    .filter(a -> a.getApptStart().isAfter(LocalDateTime.now()) && a.getApptStart().isBefore(LocalDateTime.now().plusWeeks(1)))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            apptTable.setItems(filteredAppointments);
        }else if(selectMode.equals("month")){
            ObservableList<appointment> filteredAppointments =  allAppointments
                    .stream()
                    .filter(a -> a.getApptStart().isAfter(LocalDateTime.now()) && a.getApptStart().isBefore(LocalDateTime.now().plusMonths(1)))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            apptTable.setItems(filteredAppointments);
        }else{
            apptTable.setItems(allAppointments);
        }
    }

    /** Sets columns of table, sets previously selected radio button, and fills table when window is initialized.
     *
     */

     @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        try {
            apptNumCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
            apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
            apptDescCol.setCellValueFactory(new PropertyValueFactory<>("apptDesc"));
            apptLocCol.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
            apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
            apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("apptType"));
            apptDateCol.setCellValueFactory(new PropertyValueFactory<>("dateNice"));
            apptStartCol.setCellValueFactory(new PropertyValueFactory<>("startTimeNice"));
            apptEndCol.setCellValueFactory(new PropertyValueFactory<>("endTimeNice"));
            apptCustCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            apptUserCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
            if(selectMode.equals("all")){apptsAll.setSelected(true);}
            if(selectMode.equals("week")){apptsWeek.setSelected(true);}
            if(selectMode.equals("month")){apptsMonth.setSelected(true);}
            fillTable2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Opens add appointment page.
     *
     */
    public void addApptPressed(ActionEvent actionEvent) throws IOException {
        Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/addAppointmentScreen.fxml")));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add an appointment");
        stage.setScene(scene);
        stage.show();
    }

    /** Opens modify appointment page.
     *
     */
    public void modifyPressed(ActionEvent actionEvent) throws IOException {
        selectedAppt = apptTable.getSelectionModel().getSelectedItem();
        if(selectedAppt == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No appointment selected");
            alert.setContentText("Select an appointment and try again");
            alert.showAndWait();
        } else {
            modifyButton = actionEvent;
            Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/updateAppointmentScreen.fxml")));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Modify appointment");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** Deletes selected appointment, but throws an alert if none is selected.
     *
     */
    public void deletePressed() throws Exception {
        selectedAppt = apptTable.getSelectionModel().getSelectedItem();
        int apptID = selectedAppt.getApptID();
        String apptType = selectedAppt.getApptType();
        if(selectedAppt == null){
            Alert error = new Alert(Alert.AlertType.WARNING);
            error.setHeaderText("No appointment selected");
            error.setContentText("Select an appointment and try again");
            error.showAndWait();
        } else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("WARNING!");
            confirm.setHeaderText("You are about to delete this appointment");
            confirm.setContentText("Are you sure you want to do this?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) { //Delete confirmed
                appointmentDB.deleteAppointment(selectedAppt.getApptID());
                fillTable2();
                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setHeaderText("Appointment successfully canceled.");
                done.setContentText(apptType + " appointment ID number " + apptID + " canceled.");
                done.showAndWait();
            }
        }
    }

    /** Opens customer page.
     *
     */
    public void customerPressed(ActionEvent actionEvent) throws IOException {
        Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/customerScreen.fxml")));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Modify appointment");
        stage.setScene(scene);
        stage.show();
    }

    /** Opens reports page.
     *
     */
    public void reportsPressed(ActionEvent actionEvent) throws IOException {
        Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/reportsScreen.fxml")));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }
}

