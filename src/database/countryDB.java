package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** All callable functions that access either the countries or first level divisions table of the database.
 */
public class countryDB {
    /** Lists all divisions of a specified country.
     *
     */
    public static ObservableList<String> getDivs(int countryID) throws SQLException {
        ObservableList<String> allDivisions = FXCollections.observableArrayList();
        String sql = "SELECT Division from first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,countryID);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            String division = result.getString("Division");
            allDivisions.add(division);
        }
        return allDivisions;
    }

    /** Translates division ID to division name.
     *
     */
    public static String getDivName(int divID) throws SQLException {
        String divName = "";
        String sql = "SELECT Division from first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,divID);
        ResultSet result = ps.executeQuery();
        while(result.next()) {
            divName = result.getString("Division");
        }
        return divName;
    }

    /** Finds country ID of specified division. Used when updating a customer.
     *
     */
    public static int getCountryIDFromDiv(int divID) throws SQLException {
        int countryID = 0;
        String sql = "SELECT Country_ID from first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,divID);
        ResultSet result = ps.executeQuery();
        while(result.next()) {
            countryID = result.getInt("Country_ID");
        }
        return countryID;
    }

    /** Translates division name to division ID.
     *
     */
    public static int getDivID(String divName) throws SQLException {
        int divisionID = 0;
        String sql = "SELECT Division_ID from first_level_divisions WHERE division = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,divName);
        ResultSet result = ps.executeQuery();
        while(result.next()) {
            divisionID = result.getInt("Division_ID");
        }
        return divisionID;
    }

    /** Lists all countries in countries table of database.
     *
     */
    public static ObservableList<String> getCountries() throws SQLException {
        ObservableList<String> allCountries = FXCollections.observableArrayList();
        String sql = "SELECT Country from countries";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            String country = result.getString("Country");
            allCountries.add(country);
        }
        return allCountries;
    }

    /** Translates country name to country ID.
     *
     */
    public static int getCountryID(String country) throws SQLException {
        int countryID = 0;
        String sql = "SELECT Country_ID from countries WHERE Country = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,country);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            countryID = result.getInt("Country_ID");
        }
        return countryID;

    }

    /** Translates country ID to country name. I don't think I need this function.
     *
     */
    public static String getCountryName(int countryID) throws SQLException {
        String country = "";
        String sql = "SELECT Country from countries WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,countryID);
        ResultSet result = ps.executeQuery();
        while(result.next()) {
            country = result.getString("Country");
        }
        return country;
    }
}
