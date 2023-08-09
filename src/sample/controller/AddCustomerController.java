package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DAO.CountryDAO;
import sample.DAO.CustomerDAO;
import sample.DAO.DivisionDAO;
import sample.Main;
import sample.model.Country;
import sample.model.Customer;
import sample.model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The End-user uses various methods within this class to interact with the AddCustomer GUI.
 */
public class AddCustomerController implements Initializable {

    public TextField customerIdTF;
    public TextField nameTF;
    public TextField addressTF;
    public TextField postalCodeTF;
    public TextField phoneTF;
    public Button saveCustomer;
    public Button cancelAddCustomer;
    public ComboBox<Division> divisionsCB;
    public ComboBox<Country> countryCB;

    /**
     * Initializes when the controller is opened
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Need to populate combo boxes.
        CountryDAO countryDAO = new CountryDAO();
        DivisionDAO divisionDAO = new DivisionDAO();

        try {
            countryCB.setItems(countryDAO.get());
            divisionsCB.setItems(divisionDAO.get());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Saves the current customer object to the database and sends the end-user back to the Main Screen(AppointmentandCustomers).
     * @param actionEvent Executes the OnSaveButton ActionEvent.
     */
    public void onSaveButton(ActionEvent actionEvent) {

        // Gather data from the form.
        // Generate customer ID
        // Run a generator and generate the next customer ID.
        String customerId = customerIdTF.getText();
        String name = nameTF.getText();
        String address = addressTF.getText();
        String postalCode = postalCodeTF.getText();
        String phone = phoneTF.getText();
        Country country = countryCB.getValue();
        Division division = divisionsCB.getValue();

        // Save it to the database.
        // Create Customer object.
        Customer customer = new Customer(-1, name, address, postalCode, phone, division.getDivisionId());

        // Create CustomerDAO object.
        CustomerDAO cDAO = new CustomerDAO();

        try {

            // Call insert from the customerDAO
            cDAO.insert(customer);

            // Send the user back to the "Main screen"
            // Go back to the AppointmentsAndCustomersController.
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/AppointmentsAndCustomers.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Back to main-screen: AppointmentAndCustomersController");
            stage.setScene(scene);
            stage.show();

            //Changes from SQLException.
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Closes the window and prompts the end-user to confirm returning to the Main Screen.
     * @param actionEvent Executes the OnCancelButton ActionEvent.
     */
    public void onCancelButton(ActionEvent actionEvent) throws IOException {

        // Add alert notifying end-user that they are going back to the main screen and ask them if they wanted to or not.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return to main screen");
        alert.setHeaderText("You are heading back to the main screen.");
        alert.setContentText("Are you sure you want to do this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
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

    public void onCountryChange(ActionEvent actionEvent) {

        // Declare a division DAO object.
        DivisionDAO dDAO = new DivisionDAO();

        // Get the country that was selected.
        Country country = countryCB.getValue();

        // Load the divisions Combo box with a list based on the country ID.
        try {
            divisionsCB.setItems(dDAO.getCountryDivisions(country.getCountryId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
