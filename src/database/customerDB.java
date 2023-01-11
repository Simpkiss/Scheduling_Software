package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

/** All callable functions that access customer table of database.
 *
 */
public class customerDB {
    /** Lists the names of every customer.
     *
     */
    public static ObservableList<String> getCustomerNames() throws SQLException{
        ObservableList<String> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT Customer_Name from customers";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            String userName = result.getString("Customer_Name");
            allCustomers.add(userName);
        }
        return allCustomers;
    }

    /** Gets a list of every customer.
     *
     */
    public static ObservableList<customer> getAllCustomers() throws SQLException{
        ObservableList<customer> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT * from customers";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            int customerID = result.getInt("Customer_ID");
            String customerName = result.getString("Customer_Name");
            String customerAddress = result.getString("Address");
            String customerPostCode = result.getString("Postal_Code");
            String customerPhone = result.getString("Phone");
            String customerCreatedBy = result.getString("Created_By");
            int divisionID = result.getInt("Division_ID");
            String divisionName = countryDB.getDivName(divisionID);
            customer custResult = new customer(customerID,customerName,customerAddress,customerPostCode,customerPhone,customerCreatedBy,divisionID,divisionName);
            allCustomers.add(custResult);
        }
        return allCustomers;
    }

    /** Translates customer name to customer ID.
     *
     */
    public static int getCustomerID(String customer) throws SQLException {
        int custID = 0;
        String sql = "Select * FROM customers WHERE Customer_Name = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,customer);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            custID = result.getInt("Customer_ID");
        }
        return custID;
    }

    /** Translates customer ID to customer name.
     *
     */
    public static String getCustomerName(int customer) throws SQLException {
        String customerName = null;
        String sql = "Select * FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,customer);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            customerName = result.getString("Customer_Name");
        }
        return customerName;
    }

    /** selects a customer based on a customer ID. I don't think I need this one.
     *
     */
    public static customer getSelectCustomer(int customer) throws  SQLException{
        customer selectCust = null;
        String sql = "SELECT * FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,customer);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            int customerID = result.getInt("Custom_ID");
            String customerName = result.getString("Customer_Name");
            String customerAddress = result.getString("Address");
            String customerPostCode = result.getString("Postal_Code");
            String customerPhone = result.getString("Phone");
            String customerCreatedBy = result.getString("Created_By");
            int divisionID = result.getInt("Division_ID");
            String divisionName = countryDB.getDivName(divisionID);
            selectCust = new customer(customerID,customerName,customerAddress,customerPostCode,customerPhone,customerCreatedBy,divisionID,divisionName);
        }
        return selectCust;
    }

    /** Adds a new customer to the database.
     *
     */
    public static void addCustomer(String name, String address, String postCode, String phone, String user, int division) throws SQLException{
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,name);
        ps.setString(2,address);
        ps.setString(3,postCode);
        ps.setString(4,phone);
        ps.setTimestamp(5, Timestamp.from(Instant.now()));
        ps.setString(6, user);
        ps.setTimestamp(7, Timestamp.from(Instant.now()));
        ps.setString(8, user);
        ps.setInt(9,division);
        ps.executeUpdate();
    }

    /** Updates specified customer based on customer ID.
     *
     */
    public static void updateCustomer(String name, String address, String postCode, String phone, String user, int division, int id) throws SQLException{
        String sql = "UPDATE customers " +
                "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ?\n" +
                "WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,name);
        ps.setString(2,address);
        ps.setString(3,postCode);
        ps.setString(4,phone);
        ps.setTimestamp(5, Timestamp.from(Instant.now()));
        ps.setString(6, user);
        ps.setInt(7,division);
        ps.setInt(8,id);
        ps.executeUpdate();
    }

    /** Deletes specified customer.
     *
     */
    public static void deleteCustomer(int custID) throws SQLException {
        String sql = "DELETE FROM customers where Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,custID);
        ps.executeUpdate();
    }
}
