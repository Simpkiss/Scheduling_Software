package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** All callable functions that access the user table of the database. */
public class userDB {
    /** Lists all user names.
     *
     */
    public static ObservableList<String> getAllUsers() throws Exception{
        ObservableList<String> allUsers = FXCollections.observableArrayList();
        String sql = "SELECT User_Name from users";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            String userName = result.getString("User_Name");
            allUsers.add(userName);
        }
        return allUsers;
    }

    /** Checks if selected user and password are correct according to database. Could be done better, has an intermediate step that deals with null values that could be made obsolete by checking for null before calling this.
     *
     */
    public static boolean userCheck(String user, String password) throws SQLException{
        String sql = "SELECT * from users WHERE User_Name=? AND Password=?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,user);
        ps.setString(2,password);
        ResultSet result = ps.executeQuery();
        String un = user + "entered";
        String pw = password + "entered";
        while(result.next()){
            un = result.getString("User_Name");
            pw = result.getString("Password");
        }
        return un.equals(user) && pw.equals(password);
    }

    /** Translates user name to user ID.
     *
     */
    public static int getUserID(String user) throws SQLException {
        int userID = 0;
        String sql = "Select * FROM users WHERE User_Name = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,user);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            userID = result.getInt("User_ID");
        }
        return userID;
    }

    /** Generates report of all users and the number of appointments they each have, calls appointmentDB. This is the third report.
     *
     */
    public static ObservableList<report> userAppts() throws Exception{
        ObservableList<report> allUsers = FXCollections.observableArrayList();
        String sql = "SELECT User_Name from users";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            String userName = result.getString("User_Name");
            report tempList = new report(userName,appointmentDB.getApptCount(getUserID(userName)));
            allUsers.add(tempList);
        }
        return allUsers;
    }
}
