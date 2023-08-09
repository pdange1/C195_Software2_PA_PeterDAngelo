package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Customer;
import sample.model.ReportItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO implements DAO<Customer> {

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
     * Gets an observable list of customers.
     * @return ObservableList Returns the list of customers.
     */
    @Override
    public ObservableList<Customer> get() throws SQLException {

        ObservableList<Customer> list = FXCollections.observableArrayList();
        //Create SQL statemennt.
        String sql = "SELECT * FROM CUSTOMERS";                                      // String to pass through to the SQL database to retrieve all Contact data.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ResultSet rs = ps.executeQuery();                                               // Assign the query
        while(rs.next()) {
            int customerId = rs.getInt("Customer_Id");
            String customers = rs.getString("customer_name");
            String address = rs.getString("address");
            String postalCode = rs.getString("postal_code");
            String phone = rs.getString("phone");
            int divisionId = rs.getInt("Division_Id");

//            System.out.println("cutsomer_ID = " + customerId + "  division_ID = " + divisionId);

            // Create a new customer to add to the list.
            Customer customer = new Customer(customerId, customers, address, postalCode, phone, divisionId);

            // Add the customer to the list.
            list.add(customer);
        }
        return list;
    }

    /**
     * Gets the customer by customerID
     * @param id Id of a customer to be returned.
     * @return Customer by id.
     */
    @Override
    public Customer get(int id) throws SQLException {

        Customer customer = null;

        //Create SQL statemennt.
        String sql = "SELECT * FROM CUSTOMERS WHERE CUSTOMER_ID = ?";                                      // String to pass through to the SQL database to retrieve all Contact data.
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        // Assign the query
        while(rs.next()) {
            int customerId = rs.getInt("Customer_Id");
            String customers = rs.getString("customer_name");
            String address = rs.getString("address");
            String postalCode = rs.getString("postal_code");
            String phone = rs.getString("phone");
            int divisionId = rs.getInt("Division_Id");

            // Create a new Customer
            customer = new Customer(customerId, customers, address, postalCode, phone, divisionId);

            // Return the customer
            return customer;

        }

        return customer;

    }

    /**
     * Adds the customer.
     * @param customer
     */
    public void insert(Customer customer) throws SQLException {

        insert(customer.getCustomerId(),customer.getCustomerName(),
                customer.getAddress(), customer.getPostalCode(),
                customer.getPhone(), customer.getDivisionId());

    }

    /**
     * Adds the customer into the database.
     * @param customerId
     * @param customerName
     * @param address
     * @param postalCode
     * @param phone
     * @param divisionId
     * @throws SQLException
     */
    public void insert(int customerId, String customerName, String address, String postalCode, String phone, int divisionId) throws SQLException {

        // Create a SQL insert query (Watch video on this)
        String sql = "INSERT INTO CUSTOMERS (customer_id, customer_name, address, postal_code, phone, division_ID)  " +
                "values(NULL, ?, ?, ?, ?, ?)";

        // Create prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
        // Bind the parameters.
        int paramIndex = 1;
//        ps.setInt(paramIndex++, customerId);
        ps.setString(paramIndex++, customerName);
        ps.setString(paramIndex++, address);
        ps.setString(paramIndex++, postalCode);
        ps.setString(paramIndex++, phone);
        ps.setInt(paramIndex++, divisionId); // Comment out this line when executing to experience error and understand why it happens.

        ps.executeUpdate();
    }

    /**
     * Updates the customer in the database.
     * @param customer
     */
    public void update(Customer customer) throws SQLException {

        // Create sql query for updating customers.
        String sql = "UPDATE CUSTOMERS SET customer_name = ?, address = ?, postal_code = ?, phone = ?, division_ID = ? WHERE CUSTOMER_ID = ?";
        // Create prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.
        // Bind the parameters.
        int paramIndex = 1;
        ps.setString(paramIndex++, customer.getCustomerName());
        ps.setString(paramIndex++, customer.getAddress());
        ps.setString(paramIndex++, customer.getPostalCode());
        ps.setString(paramIndex++, customer.getPhone());
        ps.setInt(paramIndex++, customer.getDivisionId()); // Comment out this line when executing to experience error and understand why it happens.
        ps.setInt(paramIndex++, customer.getCustomerId());

        ps.executeUpdate();


    }

    /**
     * Deletes the customers in the database matching the Customer ID.
     * @param id Id of customer to be deleted.
     * @return Returns the rows that are affected.
     */
    public int delete(int id) throws SQLException {
        // Create a SQL delete query (Watch video on this)
        String sql = "DELETE FROM CUSTOMERS WHERE CUSTOMER_ID = ?";

        // Create prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);// Assign the above statement to a variable, connect to the database, forward the prepared statement.

        // Execute prepared statement.
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /**
     * Counts the countries by customer ID
     * @return list Returns an observable list of reportItem.
     * @throws SQLException
     */
    public ObservableList<ReportItem> customersByCountryReport() throws SQLException {

        ObservableList<ReportItem> list = FXCollections.observableArrayList();         // Observable array list of appointments

        String sql = "SELECT COUNT(*) AS total, CO.country AS thecountry FROM CUSTOMERS AS CU INNER JOIN FIRST_LEVEL_DIVISIONS AS D ON " +
                "CU.DIVISION_ID = D.DIVISION_ID INNER JOIN COUNTRIES AS CO ON CO.COUNTRY_ID = D.COUNTRY_ID " +
                "GROUP BY CO.COUNTRY ORDER BY CO.COUNTRY";                                      // String to pass through to the SQL database to retrieve all appointment data.

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);              // Assign the above statement to a variable, connect to the database, forward the prepared statement.
        ResultSet rs = ps.executeQuery();                                               // Assign the query
        while(rs.next()) {
            int total = rs.getInt("total");
            String month = rs.getString("thecountry");

            // Create a new appointment to add to the list.
            ReportItem reportItem = new ReportItem(month,"", total);

            // Add the appointment to the list.
            list.add(reportItem);
        }

        // Return the list variable.
        return list;
    }

}