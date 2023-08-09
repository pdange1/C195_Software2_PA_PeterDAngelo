package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DAO.AppointmentDAO;
import sample.DAO.ContactDAO;
import sample.DAO.CustomerDAO;
import sample.DAO.UserDAO;
import sample.Main;
import sample.model.Appointment;
import sample.model.Contact;
import sample.model.Customer;
import sample.model.User;
import sample.utilities.BusinessRules;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The End-user uses various methods within this class to interact with the ModifyAppointments Controller.
 */
public class ModifyAppointmentController implements Initializable {

    public TextField appointmentIdTF;
    public TextField descriptionTF;
    public TextField locationTF;
    public Button saveBTN;
    public Button cancelBTN;
    public ComboBox<Contact> contactCB;
    public TextField titleTF;
    public DatePicker startDateDP;
    public DatePicker endDateDP;
    public ComboBox<LocalTime> startTimeCB;
    public ComboBox<LocalTime> endTimeCB;
    public ComboBox<Customer> customerCB;
    public ComboBox<User> userCB;
    public TextField typeTF;
    private Appointment appointmentToModify;

    /**
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Need to pass the appointment object from the previous controller.
        // Create three DAO instances.
        CustomerDAO customerDAO = new CustomerDAO();
        ContactDAO contactDAO = new ContactDAO();
        UserDAO userDAO = new UserDAO();

        // Load the start and end time list.
        startTimeCB.setItems(BusinessRules.getStartTimeList());
        endTimeCB.setItems(BusinessRules.getEndTimeList());

        try {
            contactCB.setItems(contactDAO.get());
            customerCB.setItems(customerDAO.get());
            userCB.setItems(userDAO.get());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Grab the appointment from the main controller and initialize the controls with data.
        appointmentToModify = AppointmentsAndCustomersController.getAppointmentToModify();

        startTimeCB.setValue(appointmentToModify.getStart().toLocalTime());
        endTimeCB.setValue(appointmentToModify.getEnd().toLocalTime());

        appointmentIdTF.setText(String.valueOf(appointmentToModify.getApptId()));
        descriptionTF.setText(appointmentToModify.getDescription());
        locationTF.setText(appointmentToModify.getLocation());
        titleTF.setText(appointmentToModify.getTitle());
        try {
            contactCB.setValue(contactDAO.get(appointmentToModify.getContactId()));
            userCB.setValue(userDAO.get(appointmentToModify.getUserId()));
            customerCB.setValue(customerDAO.get(appointmentToModify.getCustomerId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        startDateDP.setValue(LocalDate.from(appointmentToModify.getStart()));
        endDateDP.setValue(LocalDate.from(appointmentToModify.getEnd()));
        typeTF.setText(appointmentToModify.getType());

    }

    /**
     * Saves the appointment to the database.
     * @param actionEvent
     */
    public void onSaveButton(ActionEvent actionEvent) throws IOException {

        // Gather data from the form.
        String apptId = appointmentIdTF.getText();
        String title = titleTF.getText();
        String desc = descriptionTF.getText();
        String location = locationTF.getText();
        String type = typeTF.getText();
        User user = userCB.getValue();
        Customer customer = customerCB.getValue();
        Contact contact = contactCB.getValue();
        LocalDate startDate = startDateDP.getValue();
        LocalDate endDate = endDateDP.getValue();
        LocalTime startTime = startTimeCB.getValue();
        LocalTime endTime = endTimeCB.getValue();

        // Validate Data
        // Write alerts if things aren't filled in correctly.
        if (title.isBlank() || desc.isBlank() || location.isBlank() || type.isBlank()) {

            // Add alert box.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing text in text box");
            alert.setContentText("Title, Description, Location and Type text boxes cannot be blank.");

            alert.showAndWait();

            return;
        }

        // Check user not null, check contact not null, check customer not null.
        // Could do individual checks or group (Like above)
        if (customer == null || contact == null || user == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Null value not allowed.");
            alert.setContentText("Customer, Contact and User cannot be null. Please choose a value");

            alert.showAndWait();

            return;
        }

        // Check start and end just to be sure they're not null.
        if (startDate == null || endDate == null || startTime == null || endTime == null) {

            // Add an alert box if null to return to the form.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Null value not allowed.");
            alert.setContentText("Start and end dates/times cannot be empty. Please choose a value.");

            alert.showAndWait();

            return;
        }

        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime end = LocalDateTime.of(endDate, endTime);

        // Run a check on the data in the form using the business rules.
        if (end.isBefore(start)) {
            // Warn user start must be before end in an alert box and send back to form.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Start/End time invalid:");
            alert.setContentText("Appointment end time must be after the appointment start time. Please fix.");

            alert.showAndWait();

            return;
        }

        // Check Appointment overlap.                                                      Ask juan if this is the right way to the the ID.
        if (BusinessRules.isAppointmentOverlapping(start, end, customer.getCustomerId(), appointmentToModify.getApptId())) { // ExcludeAppointentID will be the actual appointmentID (apptId) instead of -1 on add appointment.

            // Add alert to warn user what they need to fix.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Overlapped:");
            alert.setContentText("Appointment time conflicts with another appointment for this customer.");

            alert.showAndWait();

            return;

        }


        //Create an appointment object.    and here too.
        Appointment appt = new Appointment(appointmentToModify.getApptId(), title,desc,location,type, start, end, customer.getCustomerId(), user.getUserId(), contact.getContactId());

        //Create a DAO object.
        AppointmentDAO aDAO = new AppointmentDAO();

        try {
            // Call insert from the DAO.
            aDAO.update(appt);


            // Go back to the AppointmentsAndCustomersController.
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/AppointmentsAndCustomers.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Back to main-screen: AppointmentAndCustomersController");
            stage.setScene(scene);
            stage.show();

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }

    }

    /**
     * Closes the current screen and takes the user back to the AppointmentsAndCustomerController.
     * @param actionEvent On button click action event.
     */
    public void onCancelButton(ActionEvent actionEvent) throws IOException {

        // Add alert notifying end-user that they are going back to the main screen and ask them if they wanted to or not.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return to main screen");
        alert.setHeaderText("You are heading back to the main screen.");
        alert.setContentText("Are you sure you want to do this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            // Go back to the AppointmentsAndCustomersController.
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/AppointmentsAndCustomers.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Back to main-screen: AppoinmentAndCustomersController");
            stage.setScene(scene);
            stage.show();

        } else {
            // Do nothing... user chose CANCEL or closed the dialog
        }

    }

}
