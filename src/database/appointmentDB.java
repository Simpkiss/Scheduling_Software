package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.appointment;
import model.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;

/** All callable methods that access the appointments table of the database. */
public class appointmentDB {
    /** Selects all appointments and orders them chronologically.
     *
     */
    public static ObservableList<appointment> getAllAppts() throws Exception{
        DateTimeFormatter date = DateTimeFormatter.ofPattern("MM/dd/yy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("h:mm a");
        ObservableList<appointment> allAppts = FXCollections.observableArrayList();
        String sql = "SELECT * from appointments ORDER BY Start";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            int apptID = result.getInt("Appointment_ID");
            String apptTitle = result.getString("Title");
            String apptDesc = result.getString("Description");
            String apptLocation = result.getString("Location");
            String apptType = result.getString("Type");
            String dateNice = result.getTimestamp("Start").toLocalDateTime().format(date);
            String startTimeNice = result.getTimestamp("Start").toLocalDateTime().format(time);
            String endTimeNice = result.getTimestamp("End").toLocalDateTime().format(time);
            LocalDateTime apptStart = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime apptEnd  = result.getTimestamp("End").toLocalDateTime();
            LocalDateTime apptCreated = result.getTimestamp("Create_Date").toLocalDateTime();
            String apptBookedBy = result.getString("Created_By");
            LocalDateTime apptUpdated = result.getTimestamp("Last_Update").toLocalDateTime();
            String apptUpdatedBy = result.getString("Last_Updated_by");
            int customerID = result.getInt("Customer_ID");
            int userID = result.getInt("User_ID");
            int contactID = result.getInt("Contact_ID");
            appointment apptResult = new appointment(apptID, apptTitle, apptDesc, apptLocation, apptType, dateNice, startTimeNice, endTimeNice, apptStart,
                    apptEnd, apptCreated, apptBookedBy, apptUpdated, apptUpdatedBy, customerID, userID, contactID);
            allAppts.add(apptResult);
        }
        return allAppts;
    }

    /** Selects appointments that start between two points in time and orders them chronologically.
     *  RENDERED UNNECESSARY BY LAMBDA FILTER FUNCTION ON appointmentsController!!!!
     */
    public static ObservableList<appointment> getSomeAppts(LocalDateTime start, LocalDateTime end) throws Exception {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("MM/dd/yy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("h:mm a");
        ObservableList<appointment> someAppts = FXCollections.observableArrayList();
        String sql = "SELECT * from appointments WHERE Start between ? and ? ORDER BY Start";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, start.toString());
        ps.setString(2, end.toString());
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            int apptID = result.getInt("Appointment_ID");
            String apptTitle = result.getString("Title");
            String apptDesc = result.getString("Description");
            String apptLocation = result.getString("Location");
            String apptType = result.getString("Type");
            String dateNice = result.getTimestamp("Start").toLocalDateTime().format(date);
            String startTimeNice = result.getTimestamp("Start").toLocalDateTime().format(time);
            String endTimeNice = result.getTimestamp("End").toLocalDateTime().format(time);
            LocalDateTime apptStart = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime apptEnd = result.getTimestamp("End").toLocalDateTime();
            LocalDateTime apptCreated = result.getTimestamp("Create_Date").toLocalDateTime();
            String apptBookedBy = result.getString("Created_By");
            LocalDateTime apptUpdated = result.getTimestamp("Last_Update").toLocalDateTime();
            String apptUpdatedBy = result.getString("Last_Updated_by");
            int customerID = result.getInt("Customer_ID");
            int userID = result.getInt("User_ID");
            int contactID = result.getInt("Contact_ID");
            appointment apptResult = new appointment(apptID, apptTitle, apptDesc, apptLocation, apptType, dateNice, startTimeNice, endTimeNice, apptStart,
                    apptEnd, apptCreated, apptBookedBy, apptUpdated, apptUpdatedBy, customerID, userID, contactID);
            someAppts.add(apptResult);
        }
        return someAppts;
    }

    /** Selects future appointments for a specified contact and orders them chronologically. Used for second report.
     *
     */
    public static ObservableList<appointment> getSchedule(int contact_ID) throws Exception {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("MM/dd/yy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("h:mm a");
        ObservableList<appointment> schedule = FXCollections.observableArrayList();
        String sql = "SELECT * from appointments WHERE Start > ? AND Contact_ID = ? ORDER BY Start";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, LocalDateTime.now().toString());
        ps.setInt(2, contact_ID);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            int apptID = result.getInt("Appointment_ID");
            String apptTitle = result.getString("Title");
            String apptDesc = result.getString("Description");
            String apptLocation = result.getString("Location");
            String apptType = result.getString("Type");
            String dateNice = result.getTimestamp("Start").toLocalDateTime().format(date);
            String startTimeNice = result.getTimestamp("Start").toLocalDateTime().format(time);
            String endTimeNice = result.getTimestamp("End").toLocalDateTime().format(time);
            LocalDateTime apptStart = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime apptEnd = result.getTimestamp("End").toLocalDateTime();
            LocalDateTime apptCreated = result.getTimestamp("Create_Date").toLocalDateTime();
            String apptBookedBy = result.getString("Created_By");
            LocalDateTime apptUpdated = result.getTimestamp("Last_Update").toLocalDateTime();
            String apptUpdatedBy = result.getString("Last_Updated_by");
            int customerID = result.getInt("Customer_ID");
            int userID = result.getInt("User_ID");
            int contactID = result.getInt("Contact_ID");
            appointment apptResult = new appointment(apptID, apptTitle, apptDesc, apptLocation, apptType, dateNice, startTimeNice, endTimeNice, apptStart,
                    apptEnd, apptCreated, apptBookedBy, apptUpdated, apptUpdatedBy, customerID, userID, contactID);

            schedule.add(apptResult);
        }
        return schedule;
    }

    /** selects an appointment based on an appointment ID, I don't think I need this one.
     *
     */
    public static appointment selectedAppt(int selectedID) throws Exception{
        DateTimeFormatter date = DateTimeFormatter.ofPattern("MM/dd/yy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("h:mm a");
        appointment selectAppt = null;
        String sql = "SELECT * from appointments Where Appointment_ID = ? ORDER BY Start";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, selectedID);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            int apptID = result.getInt("Appointment_ID");
            String apptTitle = result.getString("Title");
            String apptDesc = result.getString("Description");
            String apptLocation = result.getString("Location");
            String apptType = result.getString("Type");
            String dateNice = result.getTimestamp("Start").toLocalDateTime().format(date);
            String startTimeNice = result.getTimestamp("Start").toLocalDateTime().format(time);
            String endTimeNice = result.getTimestamp("End").toLocalDateTime().format(time);
            LocalDateTime apptStart = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime apptEnd  = result.getTimestamp("End").toLocalDateTime();
            LocalDateTime apptCreated = result.getTimestamp("Create_Date").toLocalDateTime();
            String apptBookedBy = result.getString("Created_By");
            LocalDateTime apptUpdated = result.getTimestamp("Last_Update").toLocalDateTime();
            String apptUpdatedBy = result.getString("Last_Updated_by");
            int customerID = result.getInt("Customer_ID");
            int userID = result.getInt("User_ID");
            int contactID = result.getInt("Contact_ID");
            selectAppt= new appointment(apptID, apptTitle, apptDesc, apptLocation, apptType, dateNice, startTimeNice, endTimeNice, apptStart,
                    apptEnd, apptCreated, apptBookedBy, apptUpdated, apptUpdatedBy, customerID, userID, contactID);
        }
        return selectAppt;
    }

    /** Adds a new appointment to database.
     *
     */
    public static void addAppointment(String title, String description, String location, String type,
                                      LocalDateTime start, LocalDateTime end, String createdBy,
                                      String updatedBy, int custID, int userID, int contactID) throws SQLException{
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, " +
                "Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)\n" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(start));
        ps.setTimestamp(6, Timestamp.valueOf(end));
        ps.setTimestamp(7, Timestamp.from(Instant.now()));
        ps.setString(8, createdBy);
        ps.setTimestamp(9, Timestamp.from(Instant.now()));
        ps.setString(10, updatedBy);
        ps.setInt(11, custID);
        ps.setInt(12, userID);
        ps.setInt(13, contactID);
        ps.executeUpdate();
    }

    /** Updates an existing appointment in database.
     *
     */
    public static void updateAppointment(int apptID, String title, String description, String location, String type,
                                      LocalDateTime start, LocalDateTime end, String updatedBy, int custID, int userID, int contactID) throws SQLException{
        String sql = "UPDATE appointments " +
                "SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, " +
                "Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?\n" +
                "WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(start));
        ps.setTimestamp(6, Timestamp.valueOf(end));
        ps.setTimestamp(7, Timestamp.from(Instant.now()));
        ps.setString(8, updatedBy);
        ps.setInt(9, custID);
        ps.setInt(10, userID);
        ps.setInt(11, contactID);
        ps.setInt(12, apptID);
        ps.executeUpdate();
    }

    /** Counts appointments for a given customer, could be done better with SELECT COUNT sql command.
     *
     */
    public static int appointmentCount(int custID) throws SQLException {
        int apptCount = 0;
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,custID);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            apptCount = apptCount+1;
        }
        return apptCount;
    }

    /** Finds any appointment for a specified customer that is scheduled to be happening at a specified time. Used to prevent scheduling overlapping appointment start times for a customer.
     *
     */
    public static int appointmentConflict(int custID, LocalDateTime timeCheck) throws SQLException {
        int apptID;
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ? and Start <= ? and end > ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,custID);
        ps.setTimestamp(2, Timestamp.valueOf(timeCheck));
        ps.setTimestamp(3, Timestamp.valueOf(timeCheck));
        ResultSet result = ps.executeQuery();
        if(result.next()){
            apptID = result.getInt("Appointment_ID");
        }
        else{
            apptID = -1;
        }
        return apptID;
    }

    /** Looks for the appointment ID of an appointment for a specified customer at a specified time. Used to prevent scheduling an appointment for a customer that extends into an existing appointment. Probably unnecessary.
     *
     */
    public static int appointmentNext(int custID, LocalDateTime timeCheck) throws SQLException {
        int apptID;
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ? and Start = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,custID);
        ps.setTimestamp(2, Timestamp.valueOf(timeCheck));
        ResultSet result = ps.executeQuery();
        if(result.next()){
            apptID = result.getInt("Appointment_ID");
        }
        else{
            apptID = -1;
        }
        return apptID;
    }

    /** Finds appointments starting in the next 15 minutes for a specified user and returns a string listing the appointments and some information about them to be used in an alert.
     *
     */
    public static String appointmentsSoonText(int userID) throws SQLException {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("MM/dd/yy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("h:mm a");
        StringBuilder upcoming = new StringBuilder("Appointments for you starting in the next 15 minutes:\n");
        String sql = "SELECT * FROM appointments WHERE User_ID = ? AND Start BETWEEN ? AND ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, userID);
        ps.setTimestamp(2, Timestamp.from(Instant.now()));
        ps.setTimestamp(3, Timestamp.from(Instant.now().plusSeconds(900)));
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            int apptID = result.getInt("Appointment_ID");
            String dateNice = result.getTimestamp("Start").toLocalDateTime().format(date);
            String startTimeNice = result.getTimestamp("Start").toLocalDateTime().format(time);
            String appointmentText = "Appointment #" + apptID + ", " + dateNice + " begins at " + startTimeNice + "\n";
            upcoming.append(appointmentText);
        }
        return upcoming.toString();
    }

    /** Counts appointments that are starting in the next 15 minutes for a specified user to be used in an alert, could be done better with SELECT COUNT sql command.
     *
     */
    public static int appointmentsSoon(int userID) throws SQLException {
        int upcoming = 0;
        String sql = "SELECT * FROM appointments WHERE User_ID = ? AND Start BETWEEN ? AND ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, userID);
        ps.setTimestamp(2, Timestamp.from(Instant.now()));
        ps.setTimestamp(3, Timestamp.from(Instant.now().plusSeconds(900)));
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            upcoming = upcoming + 1;
        }
        return upcoming;
    }

    /** Deletes an appointment from database based on the appointment ID.
     *
     */
    public static void deleteAppointment(int apptID) throws SQLException{
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,apptID);
        ps.executeUpdate();
    }

    /** Deletes all appointments for a specified customer.
     *
     */
    public static void deleteCustomersAppointments(int custID) throws SQLException{
        String sql = "DELETE * FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,custID);
        ps.executeUpdate();
    }

    /** Finds and lists all different types of appointments that exist in database.
     *
     */
    public static ObservableList<String> appointmentTypes() throws SQLException{
        ObservableList<String> types = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT Type FROM appointments";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while (result.next()){
            String type = result.getString("Type");
            types.add(type);
        }
        return types;
    }

    /** Generates report of months in the current year and the number of appointments of a specified type in each month. Used to populate first report.
     *
     */
    public static ObservableList<report> typeCount(String type) throws SQLException{
        int month = 1;
        ObservableList<report> typeReport = FXCollections.observableArrayList();
        while(month<13){
            LocalDateTime start = LocalDateTime.of(Year.now().getValue(),month,1,0,0);
            report monthCount = new report(Month.of(month).toString(), getTypeCount(type,start));
            typeReport.add(monthCount);
            month = month+1;
        }
        return typeReport;
    }

    /** Counts the number of appointments of a specified type within a specified space of time. Feeds above report.
     *
     */
    public static int getTypeCount(String type, LocalDateTime start) throws SQLException {
        int count = 0;
        String sql = "SELECT COUNT(Appointment_ID) FROM appointments WHERE Type = ? AND Start BETWEEN ? AND ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, type);
        ps.setTimestamp(2, Timestamp.valueOf(start));
        ps.setTimestamp(3, Timestamp.valueOf(start.plusMonths(1)));
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            count = result.getInt(1);
        }
        return count;
    }

    /** Counts the number of appointments a specified user has. Called by userDB to generate third report.
     *
     */
    public static int getApptCount(int user) throws SQLException {
        int count = 0;
        String sql = "SELECT COUNT(Appointment_ID) FROM appointments WHERE User_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, user);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            count = result.getInt(1);
        }
        return count;
    }
}
