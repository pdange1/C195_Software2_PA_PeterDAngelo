package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Division;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class implements the Data Access Layer for the first_level_divisions table.
 * @author Peter Joseph D'Angelo
 */
public class DivisionDAO implements DAO<Division> {

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
     * Creates an observable list of divisions from the database.
     * @return
     */
    @Override
    public ObservableList<Division> get() throws SQLException {

        ObservableList<Division> list = FXCollections.observableArrayList();
        //Create SQL statemennt.
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS";                                      // String to pass through to the SQL database to retrieve all Division data.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ResultSet rs = ps.executeQuery();                                               // Assign the query

        while(rs.next()) {
            int divisionId = rs.getInt("Division_Id");
            String divisionName = rs.getString("division");
            int countryId = rs.getInt("Country_Id");


            // Create a new division to add to the list.
            Division division = new Division(divisionId, divisionName,countryId);

            // Add the division to the list.
            list.add(division);
        }
        return list;
    }


    /**
     * Gets the division object based on the division ID
     * @param id
     * @return Division by ID.
     */
    @Override
    public Division get(int id) throws SQLException {

        Division division = null;

        //Create SQL statemennt.
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE DIVISION_ID = ?";                                      // String to pass through to the SQL database to retrieve all Division data.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        int paramIndex = 1;
        ps.setInt(paramIndex, id);
        // fix all gets with id (Need result set) (Need ps.setInt(paramIndex, id))
        ResultSet rs = ps.executeQuery();                                               // Assign the query

        while(rs.next()) {
            int divisionId = rs.getInt("Division_Id");
            String divisionName = rs.getString("division");
            int countryId = rs.getInt("Country_Id");

            // Create a new Division
            division = new Division(divisionId, divisionName,countryId);

            // Return the division
            return division;

        }

        return division;
    }

    /**
     * Adds the division
     * @param division
     */
    public void insert(Division division) throws SQLException {

//        insert(division.getDivisionId(),
//                division.getDivision(),
//                division.getCountryId());
        throw new RuntimeException("This is an unused method");

    }

    /**
     * Adds the division
     * @param division
     */
    public void insert(int divisionId, String division, int postalCode) throws SQLException {

//
//        // Create a SQL insert query (Watch video on this)
//        String sql = "INSERT INTO FIRST_LEVEL_DIVISIONS (division_id, division, division_ID)  " +
//                "values(?, ?, ?)";
//
//        // Create prepared statement
//        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
//        // Bind the parameters.
//        int paramIndex = 1;
//        ps.setInt(paramIndex++, divisionId);
////        ps.setInt(paramIndex++, postalCode);
//        ps.setString(paramIndex++, division);
//        ps.setInt(paramIndex++, postalCode); // Comment out this line when executing to experience error and understand why it happens.
//
//        ps.executeUpdate();

        throw new RuntimeException("This is an unused method");
    }

    /**
     * Updates the division in the database.
     * @param division
     */
    public void update(Division division) throws SQLException {

//        // Create sql query for updating first_level_divisions.
//        String sql = "UPDATE FIRST_LEVEL_DIVISIONS SET division_id = ?, division = ?, country_id = ?";
//        // Create prepared statement
//        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
//        // Bind the parameters.
//        int paramIndex = 1;
//        ps.setInt(paramIndex++, division.getDivisionId());
//        ps.setString(paramIndex++, division.getDivision());
//        ps.setInt(paramIndex++, division.getCountryId()); // Comment out this line when executing to experience error and understand why it happens.
//
//        ps.executeUpdate();
        throw new RuntimeException("This is an unused method");

    }

    /**
     * Deletes the Division from the database.
     *
     * @param id Id of division to be deleted.
     * @return The rows that are affected.
     */
    public int delete(int id) throws SQLException {
//        // Create a SQL delete query (Watch video on this)
//        String sql = "DELETE FROM FIRST_LEVEL_DIVISIONS WHERE division_id = ?";
//
//        // Create prepared statement
//        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
//
//        // Execute prepared statement.
//        ps.setInt(1, id);
//        int rowsAffected = ps.executeUpdate();
//
//        return rowsAffected;
        throw new RuntimeException("This is an unused method");
    }

    public ObservableList<Division> getCountryDivisions(int countryId) throws SQLException {
        ObservableList<Division> list = FXCollections.observableArrayList();
        //Create SQL statemennt.
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = ?";                                      // String to pass through to the SQL database to retrieve all Division data.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ps.setInt(1, countryId);
        ResultSet rs = ps.executeQuery();                                               // Assign the query

        while(rs.next()) {
            int divisionId = rs.getInt("Division_Id");
            String divisionName = rs.getString("division");
//            int countryId = rs.getInt("Country_Id");


            // Create a new division to add to the list.
            Division division = new Division(divisionId, divisionName,countryId);

            // Add the division to the list.
            list.add(division);
        }
        return list;


    }

}