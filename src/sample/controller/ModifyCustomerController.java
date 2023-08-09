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
import sample.utilities.BusinessRules;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {
    public TextField customerIdTF;
    public TextField nameTF;
    public TextField addressTF;
    public TextField postalCodeTF;
    public TextField phoneTF;
    public Button saveCustomer;
    public Button cancelModifyCustomer;
    public ComboBox<Division> divisionsCB;
    public ComboBox<Country> countryCB;
    private Customer customerToModify;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Instantiate DAO objects.
        CustomerDAO customerDAO = new CustomerDAO();
        CountryDAO countryDAO = new CountryDAO();
        DivisionDAO divisionDAO = new DivisionDAO();

        // Get the customer from the main controller
        customerToModify = AppointmentsAndCustomersController.getCustomerToModify();

        customerIdTF.setText(String.valueOf(customerToModify.getCustomerId()));
        nameTF.setText(customerToModify.getCustomerName());
        addressTF.setText(customerToModify.getAddress());
        postalCodeTF.setText(customerToModify.getPostalCode());
        phoneTF.setText(customerToModify.getPhone());
        try {
            Country country = countryDAO.getCountryByDivision(customerToModify.getDivisionId());
            countryCB.setItems(countryDAO.get());
            divisionsCB.setItems(divisionDAO.getCountryDivisions(country.getCountryId()));
            countryCB.setValue(country);
            divisionsCB.setValue(divisionDAO.get(customerToModify.getDivisionId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
    public void onSaveButton(ActionEvent actionEvent) {

        // Gather data from the form.
        // Generate customer ID
        // Run a generator and generate the next customer ID.
//        String customerId = customerIdTF.getText();
        String name = nameTF.getText();
        String address = addressTF.getText();
        String postalCode = postalCodeTF.getText();
        String phone = phoneTF.getText();
        Country country = countryCB.getValue();
        Division division = divisionsCB.getValue();

        // Save it to the database.
        // Create Customer object.
        Customer customer = new Customer(customerToModify.getCustomerId(), name, address, postalCode, phone, division.getDivisionId());

        // Create CustomerDAO object.
        CustomerDAO cDAO = new CustomerDAO();

        try {

            // Call insert from the customerDAO
            cDAO.update(customer);

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

    public void onCancelButton(ActionEvent actionEvent) throws IOException {

        // Add alert notifying end-user that they are going back to the main screen and ask them if they wanted to or not.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return to main screen");
        alert.setHeaderText("You are heading back to the main screen.");
        alert.setContentText("Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            // ... user chose OK
            // Go back to the AppointmentsAndCustomersController.
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/AppointmentsAndCustomers.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Back to main-screen: AppointmentAndCustomersController");
            stage.setScene(scene);
            stage.show();

        } else {
            // Do nothing... user chose CANCEL or closed the dialog
        }

    }

    /**
     * After setting the country combo box, this loads the division combo box with the divisions tied to the country.
     * @param actionEvent
     */
    public void onCountryChange(ActionEvent actionEvent) {

        // Declare a division DAO object.
        DivisionDAO dDAO = new DivisionDAO();

        // Get the country that was selected.
        Country country = countryCB.getValue();

        // Load the divisions Combo box with a list based on the country ID.
        try {
            divisionsCB.setItems(dDAO.getCountryDivisions(country.getCountryId()));
            divisionsCB.setValue(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
