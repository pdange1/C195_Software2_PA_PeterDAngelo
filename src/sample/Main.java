package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.DAO.AppointmentDAO;
import sample.DAO.JDBC;
import sample.model.Appointment;
import sample.utilities.BusinessRules;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/Login.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        // This changes the Locale (or region) to French.
        // Comment out this line after testing languages.
//        Locale.setDefault(new Locale("fr", "FR"));

        //Connect to database
        JDBC.getConnection();

//        try {
            testOverlap();

//            AppointmentDAO appointmentDAO = new AppointmentDAO();               //Instantiate AppointmentDAO class.
//            ObservableList<Appointment> appointmentList = appointmentDAO.get(); //Retrieve the list of appointments
//
//            for (Appointment appointment : appointmentList) {
//                displayAppointmentDetails(appointment); // Use separate method to display details.
//            }
//        } catch (SQLException e) {
//             Catch any exceptions.
//            e.printStackTrace();
//        }

        launch(args);

        // Close database connection.
        JDBC.closeConnection();
    }

    // Create a method within the main class to query the database for any information within a DAO class to test functionality.
    /**
     * This method displays the appointment details within the program that came from that came fromt the database.
     */
    private static void displayAppointmentDetails(Appointment appointment) {
        System.out.println("Appointment ID: " + appointment.getApptId());
        System.out.println("Title: " + appointment.getTitle());
        System.out.println("Description: " + appointment.getDescription());
        System.out.println("Location: " + appointment.getLocation());
        System.out.println("Type: " + appointment.getType());
        System.out.println("Start: " + appointment.getStart());
        System.out.println("End: " + appointment.getEnd());
        System.out.println("Customer ID: " + appointment.getCustomerId());
        System.out.println("User ID: " + appointment.getUserId());
        System.out.println("Contact ID: " + appointment.getContactId());

    }

    /**
     * This method is a test to see if the Business rules for overlapping are working correctly.
     */
    private static void testOverlap() {
        // Create a new appointment
        Appointment appointmentOne = new Appointment(-1, "", "", "", "",
                LocalDateTime.of(2020, 5, 28, 8, 0), LocalDateTime.of(2020, 5, 28, 9, 0), 1,1,1);


        boolean overlaps = BusinessRules.isAppointmentOverlapping(appointmentOne.getStart(), appointmentOne.getEnd(), appointmentOne.getCustomerId(), appointmentOne.getApptId());
        if (overlaps) {
            System.out.println("Appointment OVERLAPPED");
        } else {
            System.out.println("Appointment Not Overlapped");
        }
        System.out.println("\n ------------------------------1--------------------------------- \n");

        appointmentOne.setStart(appointmentOne.getStart().minusMinutes(90));
        overlaps = BusinessRules.isAppointmentOverlapping(appointmentOne.getStart(), appointmentOne.getEnd(), appointmentOne.getCustomerId(), appointmentOne.getApptId());
        if (overlaps) {
            System.out.println("Appointment OVERLAPPED");
        } else {
            System.out.println("Appointment Not Overlapped");
        }
        System.out.println("\n ------------------------------2--------------------------------- \n");

        appointmentOne.setEnd(appointmentOne.getEnd().minusMinutes(60));
        overlaps = BusinessRules.isAppointmentOverlapping(appointmentOne.getStart(), appointmentOne.getEnd(), appointmentOne.getCustomerId(), appointmentOne.getApptId());
        if (overlaps) {
            System.out.println("Appointment OVERLAPPED");
        } else {
            System.out.println("Appointment Not Overlapped");
        }
        System.out.println("\n ------------------------------3--------------------------------- \n");

        appointmentOne.setStart(appointmentOne.getStart().plusMinutes(60));
        overlaps = BusinessRules.isAppointmentOverlapping(appointmentOne.getStart(), appointmentOne.getEnd(), appointmentOne.getCustomerId(), appointmentOne.getApptId());
        if (overlaps) {
            System.out.println("Appointment OVERLAPPED");
        } else {
            System.out.println("Appointment Not Overlapped");
        }
        System.out.println("\n -----------------------------4---------------------------------- \n");

        appointmentOne.setStart(appointmentOne.getStart().minusMinutes(30));
        appointmentOne.setEnd(appointmentOne.getEnd().plusMinutes(30));
        overlaps = BusinessRules.isAppointmentOverlapping(appointmentOne.getStart(), appointmentOne.getEnd(), appointmentOne.getCustomerId(), appointmentOne.getApptId());
        if (overlaps) {
            System.out.println("Appointment OVERLAPPED");
        } else {
            System.out.println("Appointment Not Overlapped");
        }
        System.out.println("\n -------------------------------5-------------------------------- \n");

        appointmentOne.setStart(appointmentOne.getStart().minusMinutes(60));
        appointmentOne.setEnd(appointmentOne.getEnd().minusMinutes(90));
        overlaps = BusinessRules.isAppointmentOverlapping(appointmentOne.getStart(), appointmentOne.getEnd(), appointmentOne.getCustomerId(), appointmentOne.getApptId());
        if (overlaps) {
            System.out.println("Appointment OVERLAPPED");
        } else {
            System.out.println("Appointment Not Overlapped");
        }
        System.out.println("\n --------------------------------6------------------------------- \n");

        appointmentOne.setStart(appointmentOne.getStart().plusMinutes(150));
        appointmentOne.setEnd(appointmentOne.getEnd().plusMinutes(150));
        overlaps = BusinessRules.isAppointmentOverlapping(appointmentOne.getStart(), appointmentOne.getEnd(), appointmentOne.getCustomerId(), appointmentOne.getApptId());
        if (overlaps) {
            System.out.println("Appointment OVERLAPPED");
        } else {
            System.out.println("Appointment Not Overlapped");
        }
        System.out.println("\n --------------------------------7------------------------------- \n");

        appointmentOne.setStart(appointmentOne.getStart().minusMinutes(150));
        appointmentOne.setEnd(appointmentOne.getEnd().minusMinutes(150));
        appointmentOne.setApptId(3);
        overlaps = BusinessRules.isAppointmentOverlapping(appointmentOne.getStart(), appointmentOne.getEnd(), appointmentOne.getCustomerId(), appointmentOne.getApptId());
        if (overlaps) {
            System.out.println("Appointment OVERLAPPED");
        } else {
            System.out.println("Appointment Not Overlapped");
        }
        System.out.println("\n --------------------------------8------------------------------- \n");

//        appointmentOne.setStart(appointmentOne.getStart().minusMinutes(150));
//        appointmentOne.setEnd(appointmentOne.getEnd().minusMinutes(150));
        appointmentOne.setApptId(-1);
        appointmentOne.setCustomerId(2);
        overlaps = BusinessRules.isAppointmentOverlapping(appointmentOne.getStart(), appointmentOne.getEnd(), appointmentOne.getCustomerId(), appointmentOne.getApptId());
        if (overlaps) {
            System.out.println("Appointment OVERLAPPED");
        } else {
            System.out.println("Appointment Not Overlapped");
        }
        System.out.println("\n --------------------------------9------------------------------- \n");
    }


}