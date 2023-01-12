package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** All callable functions that access the contact table of the database.
 *
 */
public class contactDB {
    /** Lists names of all contacts.
     *
     */
    public static ObservableList<String> getAllContacts() throws Exception{
        ObservableList<String> allContacts = FXCollections.observableArrayList();
        String sql = "SELECT Contact_Name from contacts";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            String userName = result.getString("Contact_Name");
            allContacts.add(userName);
        }
        return allContacts;
    }

    /** Translates contact name to contact ID.
     *
     */
    public static int getContactID(String contact) throws SQLException {
        int contactID = 0;
        String sql = "Select * FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,contact);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            contactID = result.getInt("Contact_ID");
        }
        return contactID;
    }

    /** Translates contact ID to contact name.
     *
     */
    public static String getContactName(int contact) throws SQLException {
        String contactName = null;
        String sql = "Select * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,contact);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            contactName = result.getString("Contact_Name");
        }
        return contactName;
    }
}
