package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Appointment;
import sample.model.ReportItem;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Class to access the Appointments table within the database.
 * @author Peter Joseph D'Angelo
 */
public class AppointmentDAO implements DAO<Appointment> {

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
     * Retrieves all appointments.
     * @return A list of all appointments.
     */
    @Override
    public ObservableList<Appointment> get() throws SQLException {

        ObservableList<Appointment> list = FXCollections.observableArrayList();         // Observable array list of appointments

        String sql = "SELECT * FROM APPOINTMENTS";                                      // String to pass through to the SQL database to retrieve all appointment data.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ResultSet rs = ps.executeQuery();                                               // Assign the query
        while(rs.next()) {
            int apptId = rs.getInt("Appointment_Id");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerId = rs.getInt("Customer_Id");
            int userId = rs.getInt("User_Id");
            int contactId = rs.getInt("Contact_Id");

            // Create a new appointment to add to the list.
            Appointment appt = new Appointment(apptId, title, description, location, type, start, end, customerId, userId, contactId);

            // Add the appointment to the list.
            list.add(appt);
        }

        // Return the list variable.
        return list;
    }

    // Ask Juan how to prpoerly call this within the Main Class in order to test. I was
    // Trying to test but I wasn't able to figure out how to call it within the main class. (Done)
    /**
     * Returns an appointment which id matches the id parameter
     * @param id Appointment Id.
     * @return The appointment matching the id.
     */
    @Override
    public Appointment get(int id) throws SQLException {

        Appointment appt = null;

        String sql = "SELECT * FROM APPOINTMENTS WHERE APPOINTMENT_ID = ?";                                      // String to pass through to the SQL database to retrieve all appointment data.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();                                               // Assign the query

        while(rs.next()) {
            int apptId = rs.getInt("Appointment_Id");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerId = rs.getInt("Customer_Id");
            int userId = rs.getInt("User_Id");
            int contactId = rs.getInt("Contact_Id");

            appt = new Appointment(apptId, title, description, location, type, start, end, customerId, userId, contactId);

            return appt;
        }
        // Add code to get appointment object
        // Could write a SQL statement and qualify with work loss to only pull appt record with apptid (Use with large ammounts of data)
        // Could use lambda expression to filter results. (Use for small ammounts of data)
        // Recommended to write an SQL query but we need to use at least two lambda expression as per requirements. (Done)

        // Return the appointment object with the properly assigned Appointment ID.
        return appt;
    }

    /**
     * This method adds the appointment to the database.
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param contactId
     * @param userId
     * @throws SQLException
     */
    public void insert(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int contactId, int userId) throws SQLException {
        // Create a SQL insert query (Watch video on this)
        String sql = "INSERT INTO APPOINTMENTS (title, description, location, type, start, end, customer_id, contact_id, user_id) " +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Create prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
        // Bind the parameters.
        int paramIndex = 1;
        ps.setString(paramIndex++, title);
        ps.setString(paramIndex++, description);
        ps.setString(paramIndex++, location);
        ps.setString(paramIndex++, type);
        ps.setTimestamp(paramIndex++, Timestamp.valueOf(start));
        ps.setTimestamp(paramIndex++, Timestamp.valueOf(end));
        ps.setInt(paramIndex++, customerId);
        ps.setInt(paramIndex++, contactId);
        ps.setInt(paramIndex, userId); // Comment out this line when executing to experience error and understand why it happens.

        ps.executeUpdate();

    }

    /**
     * This method adds the appointment to the database.
     * @param appointment
     */
    public void insert(Appointment appointment) throws SQLException {

        insert(appointment.getTitle(), appointment.getDescription(), appointment.getLocation(), appointment.getType(),
                appointment.getStart(), appointment.getEnd(), appointment.getCustomerId(), appointment.getContactId(),
                appointment.getUserId()); // Comment out this line when executing to experience error and understand why it happens.

    }

    /**
     * This method updates the Appointment Object
     * @param appointment
     */
    public void update(Appointment appointment) throws SQLException {

        // Create a SQL update query
        // Start by creating a String variable that will contain your message for the sql prepared statement.
        String sql = "UPDATE APPOINTMENTS SET title = ?, description = ?, location = ?, type = ?, start = ?, end = ?, customer_id = ?, contact_id = ?, user_id = ? WHERE appointment_id = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
        // Bind the parameters.
        int paramIndex = 1;
        ps.setString(paramIndex++, appointment.getTitle());
        ps.setString(paramIndex++, appointment.getDescription());
        ps.setString(paramIndex++, appointment.getLocation());
        ps.setString(paramIndex++, appointment.getType());
        ps.setTimestamp(paramIndex++, Timestamp.valueOf(appointment.getStart()));
        ps.setTimestamp(paramIndex++, Timestamp.valueOf(appointment.getEnd()));
        ps.setInt(paramIndex++, appointment.getCustomerId());
        ps.setInt(paramIndex++, appointment.getContactId());
        ps.setInt(paramIndex++, appointment.getUserId());
        ps.setInt(paramIndex++, appointment.getApptId());

        ps.executeUpdate();

    }

    // Do I need to use this method at all throughout the program?
    /**
     * Changed return value from void to int.
     * @param apptId Id of appointment to be deleted.
     * @return Returns the rows affected.
     */
    public int delete(int apptId) throws SQLException {
        // Create a SQL delete query (Watch video on this)
        String sql = "DELETE FROM APPOINTMENTS WHERE appointment_id = ?";

        // Create prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.

        // Execute prepared statement.
        ps.setInt(1, apptId);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;


    }

    /**
     * Changed return value from void to int.
     * @param customerId Id of appointment to be deleted.
     * @return Returns the rows affected.
     */
    public int deleteCustomerAppointments(int customerId) throws SQLException {
        // Create a SQL delete query (Watch video on this)
        String sql = "DELETE FROM APPOINTMENTS WHERE customer_id = ?";

        // Create prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.

        // Execute prepared statement.
        ps.setInt(1, customerId);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;

    }

    /**
     * This method retrieves the appointments for a given customer.
     * LAMBDA 1 : This Lambda expression filters appointments by customerId. The rationale was to re-use the get() method and simplify filtering.
     * @param customerId
     * @return Return the list of customer appointments.
     * @throws SQLException
     */
    public ObservableList<Appointment> getCustomerAppointments(int customerId) throws SQLException {

        ObservableList<Appointment> list = get().stream()
                .filter(a -> a.getCustomerId() == customerId)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        return list;

    }

    /**
     * This method retrieves the appointments for a given Contact.
     * LAMBDA 1 : This Lambda expression filters appointments by contactId. The rationale was to re-use the get() method and simplify filtering.
     * @param contactId
     * @return Return the list of customer appointments.
     * @throws SQLException
     */
    public ObservableList<Appointment> getContactAppointments(int contactId) throws SQLException {

        ObservableList<Appointment> list = get().stream()
                .filter(a -> a.getContactId() == contactId)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        return list;

    }

    /**
     * LAMBDA 4: This lambda gets appointments within a designated period and returns those dates within a list.
     * @param start
     * @param end
     * @return Returns the list of dates.
     * @throws SQLException
     */
    public ObservableList<Appointment> getAppointmentsInPeriod(LocalDate start, LocalDate end) throws SQLException {

        ObservableList<Appointment> list = get().stream()
                .filter(a -> !(a.getEnd().toLocalDate().isBefore(start) || a.getStart().toLocalDate().isAfter(end)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        return list;

    }

    /**
     * This bulds a list of report items for the customer by country count report.
     * @return list Returns an observable list of ReportItems.
     * @throws SQLException
     */
    public ObservableList<ReportItem> appointmentsTypeMonthReport() throws SQLException {

        ObservableList<ReportItem> list = FXCollections.observableArrayList();         // Observable array list of appointments

        String sql = "SELECT COUNT(*) AS total, monthname(START) AS themonth, TYPE, month(start) FROM APPOINTMENTS " +
                "GROUP BY month(start),  monthname(START), TYPE ORDER BY month(START), TYPE";                                      // String to pass through to the SQL database to retrieve all appointment data.

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ResultSet rs = ps.executeQuery();                                               // Assign the query

        while(rs.next()) {
            int total = rs.getInt("total");
            String type = rs.getString("type");
            String month = rs.getString("themonth");

            // Create a new appointment to add to the list.
            ReportItem reportItem = new ReportItem(month,type, total);

            // Add the appointment to the list.
            list.add(reportItem);
        }

        // Return the list variable.
        return list;
    }

}
