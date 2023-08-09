package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Division;
import sample.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class implements the Data Access Layer for the Users table.
 * @author Peter Joseph D'Angelo
 */
public class UserDAO implements DAO<User> {

    /**
     * This gets the database connection.
     * @return JDBC.get.Connection.
     */
    @Override
    public Connection getConnection() {
        return JDBC.getConnection();
    }

    /**
     * This class terminates the current connection to the database.
     */
    public void closeConnection() { JDBC.closeConnection(); }

    /**
     * Creates an observable list of users from the database.
     * @return The list of Users.
     * @throws SQLException
     */
    @Override
    public ObservableList<User> get() throws SQLException {
        ObservableList<User> list = FXCollections.observableArrayList();         // Observable array list of Users

        String sql = "SELECT * FROM USERS";                                      // String to pass through to the SQL database to retrieve all user data.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ResultSet rs = ps.executeQuery();                                               // Assign the query

        while(rs.next()) {
            String userName = rs.getString("User_name");
            int userId = rs.getInt("User_Id");

            // Create a new User to add to the list.
            User user = new User(userId, userName);

            // Add the User to the list.
            list.add(user);
        }

        // Return the list variable.
        return list;
    }

    /**
     * This gets the user ID of the user.
     * @param id
     * @return User by the ID.
     */
    @Override
    public User get(int id) throws SQLException {

        User user = null;

        ObservableList<Division> list = FXCollections.observableArrayList();
        //Create SQL statemennt.
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";                                      // String to pass through to the SQL database to retrieve all User data.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();                                               // Assign the query

        while(rs.next()) {
            int userId = rs.getInt("User_Id");
            String userName = rs.getString("User_Name");

            // Create a new User
            user = new User(userId, userName);

            // Return the user
            return user;

        }

        // Return the user
        return user;
    }

    /**
     * Adds the user.
     * @param user
     */
    public void insert(User user) throws SQLException{

        insert(user.getUserId(), user.getUserName());

    }

    /**
     * Adds the user to the database.
     * @param userId
     * @param userName
     * @throws SQLException
     */
    public void insert(int userId, String userName) throws SQLException{

        // Create a SQL insert query (Watch video on this)
        String sql = "INSERT INTO USERS (user_id, user_name)  " +
                "values(?, ?)";

        // Create prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
        // Bind the parameters.
        int paramIndex = 1;
        ps.setInt(paramIndex++, userId);
        ps.setString(paramIndex++, userName);// TODO: Comment out this line when executing to experience error and understand why it happens.

        ps.executeUpdate();

    }

    /**
     * Updates the user in the database.
     * @param user
     * @throws SQLException
     */
    @Override
    public void update(User user) throws SQLException {

        // Create sql query for updating first_level_divisions.
        String sql = "UPDATE USERS SET user_id = ?, user_name = ? WHERE USER_ID = ?";

        // Create prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
        // Bind the parameters.
        int paramIndex = 1;
        ps.setInt(paramIndex++, user.getUserId());
        ps.setString(paramIndex++, user.getUserName()); // TODO: Comment out this line when executing to experience error and understand why it happens.

        ps.executeUpdate();

    }

    /**
     * Deletes the user by the User ID.
     * @param id Id of user to be deleted.
     * @return The rows affected.
     */
    public int delete(int id) throws SQLException {

        // Create a SQL delete query (Watch video on this)
        String sql = "DELETE FROM USERS WHERE USER_ID = ?";

        // Create prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.

        // Execute prepared statement.
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;

    }

    /**
     * Verifies the user in the database.
     * @param userName
     * @param password
     * @return Returns the User.
     * @throws SQLException
     */
    public User verifyUser(String userName, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE user_name = ? AND password = ?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1, userName);
        ps.setString(2, password);

        // Execute query.
        ResultSet rs = ps.executeQuery();

        // Process the results. (only returning the first one. While would be necessary for more than one)
        if (rs.next()) {
            // Get the results of the fields we selected.
            int userId = rs.getInt("user_id");
            User user = new User(userId, userName);
            return user;
        }
        // No user found return null.
        return null;
    }


}