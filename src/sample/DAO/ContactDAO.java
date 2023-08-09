package sample.DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Contact;
import java.sql.*;

/**
 * This class implements the Data Access Layer for the Contacts table.
 * @author Peter Joseph D'Angelo
 */
public class ContactDAO implements DAO<Contact> {

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
     * Gets an observable list of contacts.
     * @return Returns the list of contacts.
     */
    @Override
    public ObservableList<Contact> get() throws SQLException {

        // Create Observable Contact list locally.
        ObservableList<Contact> list = FXCollections.observableArrayList();
        //Create SQL statemennt.
        String sql = "SELECT * FROM CONTACTS";                                      // String to pass through to the SQL database to retrieve all Contact data.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ResultSet rs = ps.executeQuery();                                               // Assign the query
        while(rs.next()) {
            int contactId = rs.getInt("Contact_Id");
            String name = rs.getString("Contact_Name");
            String email = rs.getString("Email");


            // Create a new contact to add to the list.
            Contact contact = new Contact(contactId, name, email);

            // Add the contact to the list.
            list.add(contact);
        }

        return list;
    }



    /**
     * Gets the contact by contactID
     * @param id Id of contact to be returned.
     * @return Contact object of the matching ID.
     */
    @Override
    public Contact get(int id) throws SQLException {

        Contact contact = null;

        String sql = "SELECT * FROM CONTACTS WHERE CONTACT_ID = ?";                                      // String to pass through to the SQL database to retrieve all contact data.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();                                               // Assign the query

        while (rs.next()) {
            int contactId = rs.getInt("Contact_Id");
            String name = rs.getString("Contact_Name");
            String email = rs.getString("Email");

            contact = new Contact(contactId, name, email);

            return contact;

        }

        return contact;
    }

    /**
     * Inserts the contact.
     * @param contact
     * @throws SQLException
     */
    public void insert(Contact contact) throws SQLException {

        insert(contact.getContactId(), contact.getName(), contact.getEmail());

    }

    /**
     *
     * @param contactId
     * @param name
     * @param email
     * @throws SQLException
     */
    public void insert(int contactId, String name, String email) throws SQLException {

            // Create a SQL insert query (Watch video on this)
            String sql = "INSERT INTO APPOINTMENTS (contact_id, name, email) " +
                    "values(?, ?, ?)";

            // Create prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
            // Bind the parameters.
            int paramIndex = 1;
            ps.setInt(paramIndex++, contactId);
            ps.setString(paramIndex++, name);
            ps.setString(paramIndex++, email); // Comment out this line when executing to experience error and understand why it happens.

            ps.executeUpdate();
        }

    /**
     *
     * @param contact
     * @throws SQLException
     */
    public void update(Contact contact) throws SQLException {

        // Create sql query for updating contact.
        String sql = "UPDATE CONTACTS SET contact_id = ?, name = ?, location = ? WHERE contact_id = ?";
        // Create prepared statement to pass sql string through.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        // Bind the parameters
        int paramIndex = 1;
        ps.setInt(paramIndex++, contact.getContactId());
        ps.setString(paramIndex++, contact.getName());
        ps.setString(paramIndex++, contact.getEmail());

        ps.executeUpdate();

    }

    /**
     * This deletes contacts by Contact ID
     * @param contactId Id of contact to be deleted.
     * @return Returns the rows that are affected.
     */
    public int delete(int contactId) throws SQLException {
        // Create a SQL delete query (Watch video on this)
        String sql = "DELETE FROM CONTACTS WHERE contact_id = ?";

        // Create prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.

        // Execute prepared statement.
        ps.setInt(1, contactId);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

}