package sample.controller;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.DAO.AppointmentDAO;
import sample.DAO.ContactDAO;
import sample.DAO.CustomerDAO;
import sample.Main;
import sample.model.Appointment;
import sample.model.Contact;
import sample.model.ReportItem;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
public class ReportsController implements Initializable {
    public ComboBox<Contact> selectContactCB;
    public Button customerScreenButton;
    public Button mainScreenButton;
    public TableView reportTable1;
    public TableColumn apptIdTC;
    public TableColumn titleTC;
    public TableColumn descTC;
    public TableColumn typeTC;
    public TableColumn startDateTimeTC;
    public TableColumn endDateTimeTC;
    public TableColumn customerIdTC;
    public TableView<ReportItem> reportsTable2;
    public TableColumn monthRT2TC;
    public TableColumn typeRT2TC;
    public TableColumn countRT2TC;
    public TableView<ReportItem> reportsTable3;
    public TableColumn countryRT3TC;
    public TableColumn countRT3TC;
    private CustomerDAO customerDAO = new CustomerDAO();
    private ContactDAO contactDAO = new ContactDAO();
    AppointmentDAO appointmentDAO = new AppointmentDAO();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Fill first report table.

        //Contact combo box needs to be filled

        // Insert that data into the table views.
        apptIdTC.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        titleTC.setCellValueFactory(new PropertyValueFactory<>("title"));
        descTC.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeTC.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startDateTimeTC.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateTimeTC.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdTC.setCellValueFactory(new PropertyValueFactory<>("customerId"));



        try {
//            reportTable1.setItems(appointmentDAO.get());
            selectContactCB.setItems(contactDAO.get());
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO: Un-comment before submission.
//            throw new RuntimeException(e);
        }


        // Fill second report table.
        // Set cell value factories for each column in the table view.

        // Fill second report table.
        // Set cell value factories for each column in the table view.
        typeRT2TC.setCellValueFactory(new PropertyValueFactory<>("valueString2"));
        monthRT2TC.setCellValueFactory(new PropertyValueFactory<>("valueString1"));
        countRT2TC.setCellValueFactory(new PropertyValueFactory<>("valueInt"));

        try {
            ObservableList<ReportItem> reportItems = appointmentDAO.appointmentsTypeMonthReport();
            reportsTable2.setItems(reportItems);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Fill third report table.
        // What type of report/information would you like to display in this table?

        countryRT3TC.setCellValueFactory(new PropertyValueFactory<>("valueString1"));
        countRT3TC.setCellValueFactory(new PropertyValueFactory<>("valueInt"));



        try {
            ObservableList<ReportItem> reportItems = customerDAO.customersByCountryReport();
            reportsTable3.setItems(reportItems);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void onSelectContactCB(ActionEvent actionEvent) {

        // TODO: Load information related to the selection in the combo box.
        Contact selectedContact = selectContactCB.getValue();

        try {
            reportTable1.setItems(appointmentDAO.getContactAppointments(selectedContact.getContactId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void onCustomerScreenButton(ActionEvent actionEvent) throws IOException {

        // Go to AddCustomerController
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/AddCustomer.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("To Add Customer screen.");
        stage.setScene(scene);
        stage.show();
    }

    public void onMainScreenButton(ActionEvent actionEvent) throws IOException {

        // Go to main screen.
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/AppointmentsAndCustomers.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("To Main screen.");
        stage.setScene(scene);
        stage.show();

    }


    public void OnAppointmentScreenBTN(ActionEvent actionEvent) throws IOException {

        // Go to the AddAppointmentController
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/AddAppointment.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("To Add Appointment screen.");
        stage.setScene(scene);
        stage.show();

    }
}
