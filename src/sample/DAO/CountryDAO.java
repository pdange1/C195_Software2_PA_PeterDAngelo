package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Country;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class implements the Data Access Layer for the Contacts table.
 * @author Peter Joseph D'Angelo
 */
public class CountryDAO implements DAO<Country> {

    /**
     * Gets a connection to the database.
     * @return Returns java.sql.Connection.
     */
    @Override
    public Connection getConnection() { return JDBC.getConnection(); }

    /**
     * Closes the connection to the database.
     */
    @Override
    public void closeConnection() {
        JDBC.closeConnection();
    }

    /**
     * Gets an observable list of countries.
     * @return Returns the list of countries.
     */
    @Override
    public ObservableList<Country> get() throws SQLException {

        // Create Observable Country list locally.
        ObservableList<Country> list = FXCollections.observableArrayList();

        //Create SQL statemennt.
        String sql = "SELECT * FROM COUNTRIES";

        //Create Prepared Statement to pass SQL through.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ResultSet rs = ps.executeQuery();                                               // Assign the query
        while(rs.next()) {
            int countryId = rs.getInt("Country_Id");
            String countryName = rs.getString("Country");


            // Create a new country to add to the list.
            Country country = new Country(countryId, countryName);

            // Add the country to the list.
            list.add(country);
        }

        return list;

    }

    /**
     * Gets the country by ID.
     * @param id
     * @return Returns the country.
     */
    @Override
    public Country get(int id) throws SQLException {

        // Create null object instance.
        Country country = null;

        //Create SQL statemennt.
        String sql = "SELECT * FROM COUNTRIES WHERE COUNTRY_ID = ?";

        //Create Prepared Statement to pass SQL through.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        // Assign the query
        while(rs.next()) {
            int countryId = rs.getInt("Country_Id");
            String countryName = rs.getString("Country");

            // Create a new country to add to the list.
            country = new Country(countryId, countryName);

            // Add the country to the list.
            return country;
        }

        return country;
    }

    /**
     * Adds the countries to the database.
     * @param country
     */
    public void insert(Country country) throws SQLException {

        // Call the insert above.
//        insert(country.getCountryId(), country.getCountryName());
        throw new RuntimeException("This is an unused method");

    }

    /**
     * Inserts the countries.
     * @param countryId
     * @param countryName
     * @throws SQLException
     */
    public void insert(int countryId, String countryName) throws SQLException {

//        // Create a SQL insert query (Watch video on this)
//        String sql = "INSERT INTO COUNTRIES (country_id, country) " +
//                "values(?, ?)";
//
//        // Create prepared statement
//        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
//        // Bind the parameters.
//        int paramIndex = 1;
//        ps.setInt(paramIndex++, countryId);
//        ps.setString(paramIndex++, countryName) ; Comment out this line when executing to experience error and understand why it happens.
//
//        ps.executeUpdate();

        throw new RuntimeException("This is an unused method");

    }

    /**
     * Updates the countries.
     * @param country
     */
    public void update(Country country) throws SQLException {

//        // Create sql query for updating country.
//        String sql = "UPDATE COUNTRIES SET country_id = ?, country = ? WHERE country_id = ?";
//        // Create prepared statement to pass sql string through.
//        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
//
//        // Bind the parameters
//        int paramIndex = 1;
//        ps.setInt(paramIndex++, country.getCountryId());
//        ps.setString(paramIndex++, country.getCountryName());
//
//        ps.executeUpdate();
        throw new RuntimeException("This is an unused method");

    }

    /**
     * This method deletes a country via country ID.
     * @param countryId Id of country to be deleted.
     * @return Returns the rows that are affected.
     */
    public int delete(int countryId) throws SQLException {
//        // Create a SQL delete query (Watch video on this)
//        String sql = "DELETE FROM COUNTRIES WHERE country_id = ?";
//
//        // Create prepared statement
//        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
//
//        // Execute prepared statement.
//        ps.setInt(1, countryId);
//        int rowsAffected = ps.executeUpdate();
//
//        return rowsAffected;
        throw new RuntimeException("This is an unused method");

    }

    /**
     * Gets the country by the Division ID.
     * @param divisionId
     * @return Returns country.
     * @throws SQLException
     */
    public Country getCountryByDivision(int divisionId) throws SQLException {

        //Create SQL statemennt.
        String sql = "SELECT C.* FROM COUNTRIES AS C INNER JOIN FIRST_LEVEL_DIVISIONS AS D ON C.COUNTRY_ID = D.COUNTRY_ID AND D.DIVISION_ID = ?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        // Set division ID.
        ps.setInt(1, divisionId);

        // Execute query.
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int countryId = rs.getInt("Country_Id");
            String countryName = rs.getString("Country");

            // Create a new country to add to the list.
            Country country = new Country(countryId, countryName);

            // Add the country to the list.
            return country;

        }

        return null;

    }

}