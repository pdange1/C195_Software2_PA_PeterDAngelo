package sample.controller;

import com.mysql.cj.log.Log;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.DAO.*;
import sample.Main;
import sample.model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The End-user uses various methods within this class to interact with the AppointmentAndCustomers GUI.
 */
public class AppointmentsAndCustomersController implements Initializable {
    public Button addAppointmentButton;
    public Button modifyAppointmentButton;
    public Button deleteAppointmentButton;
    public TableView<Appointment> appointmentsTable;

    public Button addCustomerButton;
    public Button modifyCustomerButton;
    public Button deleteCustomerButton;
    public Button exitButton;
    public Button reportsButton;

    private static Appointment appointmentToModify;
    private static Customer customerToModify;
    public TableColumn apptIdTC;
    public TableColumn titleTC;
    public TableColumn descTC;
    public TableColumn locationTC;
    public TableColumn contactTC;
    public TableColumn typeTC;
    public TableColumn startDateTimeTC;
    public TableColumn endDateTimeTC;
    public TableColumn customerIdTC;
    public TableColumn userIdTC;
    public TableView<Customer> customersTable;
    public TableColumn<Customer, Integer> customerIdTable2TC;
    public TableColumn<Customer, String> customerNameTC;
    public TableColumn<Customer, String> addressTC;
    public TableColumn<Customer, String> postalCodeTC;
    public TableColumn<Customer, String> phoneTC;
    public TableColumn<Customer, Division> divisionTC;
    public TableColumn<Customer, Country> countryTC;
    public RadioButton allDatesRB;
    public RadioButton currentWeekDatesRB;
    public RadioButton currentMonthDatesRB;
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private UserDAO userDAO = new UserDAO();
    private DivisionDAO divisionDAO = new DivisionDAO();

    /**
     * Gets an Appointment Object and returns it.
     * @return Returns the appointment to modify.
     */
    public static Appointment getAppointmentToModify() {
        return appointmentToModify;
    }

    /**
     * Gets a Customer Object and returns it.
     * @return Returns the Customer to modify.
     */
    public static Customer getCustomerToModify() {
        return customerToModify;
    }

    /**
     * This is initialized when the user opens the main controller.
     * Lambda expressions 2 and 3 are located here.
     * LAMBDA: 2 - 2nd Lambda expression is used to retrieve the name of the division connected to the Division_ID.
     * LAMBDA: 3 - 3rd lambda expression is used to retrieve the name of the country connected to the Country_ID.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Need to get appointment and customer tables filled with the proper information.

        // Create appointmentDAO object.
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        // Get the currently logged-in user from the login controller.
//        LoginController.getCurrentUser();

        // Initialize  data for appointments and customers tables

        // Insert that data into the table views.
        apptIdTC.setCellValueFactory((new PropertyValueFactory<>("apptId")));
        titleTC.setCellValueFactory((new PropertyValueFactory<>("title")));
        descTC.setCellValueFactory((new PropertyValueFactory<>("description")));
        locationTC.setCellValueFactory((new PropertyValueFactory<>("location")));
        contactTC.setCellValueFactory((new PropertyValueFactory<>("contactId")));
        typeTC.setCellValueFactory((new PropertyValueFactory<>("Type")));
        startDateTimeTC.setCellValueFactory((new PropertyValueFactory<>("start")));
        endDateTimeTC.setCellValueFactory((new PropertyValueFactory<>("end")));
        customerIdTC.setCellValueFactory((new PropertyValueFactory<>("customerId")));
        userIdTC.setCellValueFactory((new PropertyValueFactory<>("userId")));

        try {
            appointmentsTable.setItems(appointmentDAO.get());
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO: Un-comment before submission.
//            throw new RuntimeException(e);
        }

        // Create customerDAO object.
        CustomerDAO customerDAO = new CustomerDAO();

        // Create countryDAO object.
        CountryDAO countryDAO = new CountryDAO();

        // Create divisionDAO
        DivisionDAO divisionDAO = new DivisionDAO();

        // When adding and updating a customer,
        // text fields are used to collect the following data:
        // customer name, address, postal code, and phone number.

        // Insert data into the table views.
        customerIdTable2TC.setCellValueFactory((new PropertyValueFactory<>("customerId")));
        customerNameTC.setCellValueFactory((new PropertyValueFactory<>("customerName")));
        addressTC.setCellValueFactory((new PropertyValueFactory<>("Address")));
        postalCodeTC.setCellValueFactory((new PropertyValueFactory<>("postalCode")));
        phoneTC.setCellValueFactory((new PropertyValueFactory<>("phone")));
//        divisionTC.setCellValueFactory((new PropertyValueFactory<>("divisionId")));


        /**
         * LAMBDA: 2nd Lambda expression to retrieve the name of the division connected to the Division_ID
         */
        divisionTC.setCellValueFactory(CellData -> {
            ObservableValue<Division> Result = null;
            try {
//                System.out.println("...... customer = " + CellData.getValue() + "division_id = " + CellData.getValue().getDivisionId());
                int divId = CellData.getValue().getDivisionId();
                Result = new SimpleObjectProperty(divisionDAO.get((divId)));
//                Result = new SimpleObjectProperty(CellData.getValue().getDivisionId());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return (Result); });



        /**
         * LAMBDA: 3rd lambda expression to retrieve the name of the country connected to the Country_ID
         */
        countryTC.setCellValueFactory(CellData -> {
            ObservableValue<Country> Result = null;
            try {
//                System.out.println("...division_id = " + CellData.getValue().getDivisionId());
                Result = new SimpleObjectProperty(countryDAO.getCountryByDivision((CellData.getValue().getDivisionId())));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return (Result); });

        try {
            customersTable.setItems(customerDAO.get());
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO: Uncomment before submission.
//            throw new RuntimeException(e);
        }

    }

    /**
     * This sends the user to the AddAppointment screen.
     * @param actionEvent
     */
    public void onAddAppointment(ActionEvent actionEvent) throws IOException {

        // Go to the AddAppointmentController
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/AddAppointment.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("To Add Appointment screen.");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This sends the user to the ModifyAppointment screen.
     * @param actionEvent
     */
    public void onModifyAppointment(ActionEvent actionEvent) throws IOException {

        // Assign selected appointment within the table.
        appointmentToModify = appointmentsTable.getSelectionModel().getSelectedItem();

        // Check to see if the appointment is valid.
        if (appointmentToModify == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modify Appointment");
            alert.setHeaderText("No appointment selected.");
            alert.setContentText("Please select an appointment.");

            alert.showAndWait();

            return;

        }
        // Send the appointment to modify to the ModifyAppointmentController.

        // Go to the ModifyAppointmentController
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/ModifyAppointment.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("To Modify Appointment screen.");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This deletes the selected appointment from the database and removes it from the Table View in the GUI.
     * @param actionEvent
     */
    public void onDeleteAppointment(ActionEvent actionEvent) throws SQLException {

        // Create new appointment object for table view selection to reference.
        Appointment selectedAppointment;

        // Get currently selected appointment.
        selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        // Check if the selected appointment is null.
        if (selectedAppointment == null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Null Appointment");
            alert.setHeaderText("No appointment selected.");
            alert.setContentText("Please select an appointment.");

            alert.showAndWait();

            return;

        }

        // Alert user they're about to delete an appointment.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Appointment");
        alert.setHeaderText("You are about to delete appointment from the table");
        alert.setContentText("Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            // Remove the Appointment from table and remove it from the database.
            // Create appointmentDAO to delete appointment.
            AppointmentDAO aDAO = new AppointmentDAO();
            // Pass through appointment ID through appointmentDAO.
            alert.setTitle("Appointment Deleted");
            alert.setHeaderText("Below is the appointment you removed.");
            alert.setContentText("Appointment ID: " + selectedAppointment.getApptId() + "\nAppointment Type: " + selectedAppointment.getType());

            alert.showAndWait();

            aDAO.delete(selectedAppointment.getApptId());

            try {
                appointmentsTable.setItems(aDAO.get());
            } catch (SQLException e) {
                e.printStackTrace();
                //TODO: Un-comment before submission.
//            throw new RuntimeException(e);
            }

        } else {

            // Do nothing... user chose CANCEL or closed the dialog
        }


    }

    /**
     * This sends the user to the AddCustomer screen.
     * @param actionEvent
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {

        // Go to AddCustomerController
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/AddCustomer.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("To Add Customer screen.");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This sends the user to the ModifyCustomer screen.
     * @param actionEvent
     */
    public void onModifyCustomer(ActionEvent actionEvent) throws IOException {

        // Go to modify customer screen and pass the highlighted customer information from the
        // table to the form in the ModifyCustomerController.
        // Assign selected appointment within the table.
        customerToModify = customersTable.getSelectionModel().getSelectedItem();

        // Pass information

        // Go to ModifyCustomerController
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/ModifyCustomer.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("To Modify Customer screen.");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This deletes the selected Customer from the database and removes it from the Table View in the GUI.
     * @param actionEvent
     */
    public void onDeleteCustomer(ActionEvent actionEvent) throws IOException, SQLException {

        //  Must delete all appointments associated with the customer to be deleted BEFORE
        //  deleting the customer associated to the appointments due to foreign key constraints.

        // Create Customer Object.
        Customer selectedCustomer;

        // Get currently selected appointment.
        selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Null Customer");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please select a customer to delete.");

            alert.showAndWait();

            return;

        }

        // Remove the selected customer from both the database and the table view
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer");
        alert.setHeaderText("You are about to delete a customer from the table, this will delete all associated customer appointments.");
        alert.setContentText("Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK

            // TODO: REVIEW WITH TEACHER
            // Create appointmentDAO
            AppointmentDAO aDAO = new AppointmentDAO();
            // Get all appointments related to the customer ID
            aDAO.deleteCustomerAppointments(selectedCustomer.getCustomerId());

            //Create customerDAO to delete appointment.
            CustomerDAO cDAO = new CustomerDAO();
            // Pass through appointment ID through appointmentDAO.
            cDAO.delete(selectedCustomer.getCustomerId());
//            System.out.println("Customer by ID to be deleted" + selectedCustomer.getCustomerId());
            appointmentsTable.setItems(aDAO.get());
            customersTable.setItems(cDAO.get());

        } else {
            // Do nothing... user chose CANCEL or closed the dialog
        }
    }

    /**
     * This method closes the program when the user clicks the exit button.
     * @param actionEvent
     */
    public void onExit(ActionEvent actionEvent) throws IOException {
        // Alert the user they are about to end the session and leave the program and prompt them
        // if they want to continue.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit program");
        alert.setHeaderText("You close and exit the program.");
        alert.setContentText("Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            // Close the program.
            Platform.exit();

        } else {
            // Do nothing... user chose CANCEL or closed the dialog
        }
    }

    /**
     * This method sends the user to the ModifyCustomer screen.
     * @param actionEvent
     */
    public void onGoToReports(ActionEvent actionEvent) throws IOException {

        // Go to reports screen.
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/Reports.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("To Reports screen.");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This sets the appointments table to list all appointments
     * @param actionEvent
     */
    public void onAllRB(ActionEvent actionEvent) {

        //
        try {
            appointmentsTable.setItems(appointmentDAO.get());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This sets the appointments table to list appointments within the current week.
     * @param actionEvent
     */
    public void onWeekRB(ActionEvent actionEvent) {

        // Write code to find beginning day of the week.
        DayOfWeek dow = DayOfWeek.from(LocalDate.now());
        int day = dow.getValue();
        // Determine the start date.
        LocalDate localDateStart = LocalDate.now().minusDays(day);
        LocalDate localDateEnd = localDateStart.plusDays(7);

        // Call appointment DAO
        try {
            appointmentsTable.setItems(appointmentDAO.getAppointmentsInPeriod(localDateStart,localDateEnd));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This sets the appointments table to list appointments within the current month.
     * @param actionEvent
     */
    public void onMonthRB(ActionEvent actionEvent) {

        YearMonth ym = YearMonth.from(LocalDate.now());

        LocalDate localDateStart = ym.atDay(1);
        LocalDate localDateEnd = ym.atEndOfMonth();

        // Call appointment DAO
        try {
            appointmentsTable.setItems(appointmentDAO.getAppointmentsInPeriod(localDateStart,localDateEnd));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
