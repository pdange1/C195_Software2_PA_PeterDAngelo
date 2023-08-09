package sample.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.DAO.AppointmentDAO;
import sample.DAO.UserDAO;
import sample.Main;
import sample.model.Appointment;
import sample.model.User;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The End-user uses various methods within this class to interact with the Login GUI.
 * This screen can also be translated to various languages.
 */
public class LoginController implements Initializable {

    // Create variable to track number of login attempts.
    private static int loginAttempts = 0;
    public Label userLoginLBL;
    public TextField usernameTextTF;
    public Text usernameLBL;
    public Text passwordLBL;
    public TextField userPasswordTF;
    public Label zoneIdLBL;
    public Button signInButton;
    public Button exitButton;
    private static User loggedUser;
    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    private ResourceBundle rb = ResourceBundle.getBundle("sample/Nat", Locale.getDefault());

    /**
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Create resource bundle in the local language (Did this in Main Cass).

        //Pass in all the labels to be changed and be sure to input them into the .properties file. (Only french is required.
        userLoginLBL.setText(rb.getString("UserLogin"));
        usernameLBL.setText(rb.getString("Username"));
        passwordLBL.setText(rb.getString("Password"));
        signInButton.setText(rb.getString("SignIn"));
        exitButton.setText(rb.getString("Exit"));
        zoneIdLBL.setText(ZoneId.systemDefault().toString());

    }

    /**
     * This method sends a verification check to the database and initiates a
     * handshake to connect the end-user to the database within the program after entering
     * their username and password into their corresponding text fields.
     * @param actionEvent
     */
    public void onLoginButton(ActionEvent actionEvent) throws IOException {

        //Increase login attempts by 1 every time the login button is clicked on the controller.
        loginAttempts++;

        // Create fileName variable for file reference.
        String fileName = "login_activity.txt";

        //Create FileWriter object.
        FileWriter fileWriter = new FileWriter(fileName, true);

        // Output username and password to file.
        PrintWriter outputFile = new PrintWriter(fileWriter);

        // Get instance of UserDAO
        UserDAO dao = new UserDAO();

        try {

            // Verify the user.
            loggedUser = dao.verifyUser(usernameTextTF.getText(), userPasswordTF.getText());

            // Translate within resource bundles.
            // Check if the login is valid.
            if (loggedUser == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(rb.getString("LoginErrorTitle"));
                alert.setHeaderText(rb.getString("LoginErrorHeader"));
                alert.setContentText(rb.getString("LoginErrorMessage"));

                alert.showAndWait();

                // Need to log the (unsuccessful) attempt to connect.

                // Add username and password to login_activity.txt.
                outputFile.println("Attempt: " + loginAttempts + " | UN-SUCCESSFUL" + " | Username : " + usernameTextTF.getText() + " | Password: " + userPasswordTF.getText() + " Time: " + LocalTime.now() + " | Date: " + LocalDate.now());
                System.out.println("Login Unsuccessful.");

                outputFile.close();


            } else {

                // Checking and prompting for upcoming appointments.
                upcomingAppointments();

                // login_activity.txt needs to log the (successful) attempt.

                // Add username and password to login_activity.txt.
                outputFile.println("Attempt: " + loginAttempts + " | SUCCESSFUL" + " | Username : " + usernameTextTF.getText() + " | Password: " + userPasswordTF.getText()+ " | Time: " + LocalTime.now() + " | Date: " + LocalDate.now());
                System.out.println("Login Successful.");

                outputFile.close();

                //Login is valid: go to the main screen.
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/sample/view/AppointmentsAndCustomers.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setTitle("To Main screen.");
                stage.setScene(scene);
                stage.show();

            }

        } catch (SQLException e) {

            throw new RuntimeException(e);

        }

    }

    /**
     * This method closes the program when the user clicks the exit button.
     * @param actionEvent
     */
    public void onExitButton(ActionEvent actionEvent) {

        // Alert the user they are about to end the session and leave the program and prompt them
        //  if they want to continue.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ExitTitle");
        alert.setHeaderText("ExitHeader");
        alert.setContentText("ExitText");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            // Close the program.
            Platform.exit();

        } else {
            // Do nothing... user chose CANCEL or closed the dialog
        }
    }


    // ask teacher about this and what it's used for. (done)
    /**
     *This method is used to verify the user information within the username text fields?
     * @return
     */
    public static User getCurrentUser() {

        return loggedUser;

    }

    private void upcomingAppointments() {

        // Need to talk to a teacher about this and also check for webinars. (Done)
        // Alert the user if they have an appointment related to their UserId within 15 mins.
        // Create user login time variables to compare.
        ObservableList<Appointment> appointments = null;
        try {
            appointments = appointmentDAO.get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Get the currently logged in user to compare ID's
        User currentUser = LoginController.getCurrentUser();

        //Check to see if appointment list is being populated.
        System.out.println("Current appointments listed in main controller " + appointments);
        System.out.println("Current user logged in. " + LoginController.getCurrentUser());

        LocalDateTime userLoginDateTime = LocalDateTime.now();
        LocalDateTime nowPlus15 = userLoginDateTime.plusMinutes(15);

//        LocalDate userLoginDate = userLoginDateTime.toLocalDate();
//        LocalTime userLoginTime = userLoginDateTime.toLocalTime();

        // Check for upcoming appointments within 15 minutes of login
        for (Appointment appointment : appointments) {

//            LocalDate appointmentDate = appointment.getStart().toLocalDate();
//            LocalTime appointmentTime = appointment.getStart().toLocalTime();

            if (appointment.getUserId() == currentUser.getUserId()
                    && !(appointment.getStart().isBefore(userLoginDateTime) || appointment.getStart().isAfter(nowPlus15))) {

                // Create format for the date and time to be displayed properly.
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                // Create a string of the appointment start times and dates with the format above.
                String formattedDateTime = appointment.getStart().format(formatter);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User Appointments");
                alert.setHeaderText("Upcoming appointments");
                alert.setContentText("Your upcoming appointment:\n" +
                        "Appointment ID: " + appointment.getApptId() + "\n" +
                        "Date and Time: " + formattedDateTime);

                alert.showAndWait();

                return;
            }

        }
        // This will not display when there is no appointment and I can't figure out why. (Fixed) Added showAndWait()
        // If there's no upcoming appointments, alert the user.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Appointments");
        alert.setHeaderText("Upcoming appointments.");
        alert.setContentText("You have no upcoming appointments within the next 15 minutes.");

        alert.showAndWait();

    }

}
