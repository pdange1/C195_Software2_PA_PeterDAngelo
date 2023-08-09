package sample.utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.DAO.AppointmentDAO;
import sample.controller.AddAppointmentController;
import sample.model.Appointment;

import java.sql.SQLException;
import java.time.*;

/**
 * This class is used to create the different rules related to the functionality of the Business.
 * It has different methods used to process various rules such as determining proper business hours
 * in order to keep the program notified of the different criteria when processing different requests by the end-user.
 */
public class BusinessRules {

    public static final ZonedDateTime START_BUSINESS_HOURS_ET = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8,0), ZoneId.of("America/New_York"));
    private static ObservableList<LocalTime> startTimeList = FXCollections.observableArrayList();
    private static ObservableList<LocalTime> endTimeList = FXCollections.observableArrayList();

    // Appointment cannot overlap the identical customer's appointment.
    //   when updating is when you will have the AppointmentId. When adding a appointment, appointment ID doesn't exist
    //  when calling this method from AddAppointmentController().

    /**
     * Checks an appointments against all appointments for a given customer for overlapping.
     * @param start The start of the appointment being checked.
     * @param end The end of the appointment being checked.
     * @param customerId The ID of the Customer of these appointments.
     * @param excludeAppointmentId The ID of the Appointment to be excluded. Use -1 when AppointmentId does not exist yet.
     * @return Returns true if the appointment overlaps, false otherwise.
     */
    public static boolean isAppointmentOverlapping(LocalDateTime start, LocalDateTime end, int customerId, int excludeAppointmentId) {

        // Pull variable inputs from the text fields in the addAppointment and modifyAppointment controllers.
        // Pass variables from the add/modifyAppointmentController
        // Assign variables to new/endAssignedStart

        // Assign variables passed through into local block.
//        LocalDateTime existingAssignedStart = start;
//        LocalDateTime existingAssignedEnd = end;
//        int custId = customerId;
//        int exclude = excludeAppointmentId;

        AppointmentDAO dao = new AppointmentDAO();

        try {

            for (Appointment a : dao.getCustomerAppointments(customerId) ) {
//                System.out.println("Appointment Overlap FOR LOOP");
//                System.out.println(a.toString());
                if (a.getApptId() == excludeAppointmentId) {
                    continue;
                }

                //  Check for overlapped appointment. 7 different cases:
                //  1) Overlap      NAS == EAS || NAE == EAE
                //  2) Overlap      NAS < EAS && NAE > EAS
                //  3) Overlap      NAS > EAS && NAS < EAE
                //  4) Overlap      NAS < EAS && NAE > EAE
                //  5) Overlap      NAS > EAS && NAE < EAE
                //  6) NOT-Overlap  NAS < EAS && NAE == EAS
                //  7) NOT-Overlap  NAS == EAE && NAE < EAE
                //  Which variables represent NAS, NAE, EAS, EAE?
                //  NAS new Appointment Start
                //  NAE New Appointment End
                //  EAS Existing Appointment Start
                //  EAE Existing Appointment End

                // 1 If the new appointment is identical to the existing appointment. Return True.
                if ( start.isEqual(a.getStart()) || end.isEqual(a.getEnd())) {
                    return true;
                    // 2 If the new appointment is between the already assigned appointment start time. Return True.
                } else if ( start.isBefore(a.getStart()) && end.isAfter(a.getStart()) ) {
                    return true;
                    // 3 If the  new appointment is between the existing appointment end time. Return True.
                } else if ( start.isAfter(a.getStart()) && start.isBefore(a.getEnd())) {
                    return true;
                    // 4 If the new appointment begins before the new appointment or ends at or after the existing appointment. Return True.
                } else if ( start.isBefore(a.getStart()) && end.isAfter(a.getEnd()) ) {
                    return true;
                    // 5 If the new appointment takes place within the time of the existing appointment. Return True.
                } else if ( start.isAfter(a.getStart()) && end.isBefore(a.getEnd()) ) {
                    return true;
                }
            }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        return false;
    }

    // Include a method that validates the appointment when creating and modifying new or existing appointments.
    // This needs to not overlap, and be within the business hours. Two separate methods are going to be needed.

    /**
     * Gets all the start-time list of times and returns it.
     * @return startTimeList Returns the start times in a list
     */
    public static ObservableList<LocalTime> getStartTimeList() {
        if (startTimeList.isEmpty()) {
            timeGenerator();
        }

        return startTimeList;
    }

    /**
     * This gets the end-time list of times and returns it.
     *@return endTimeList Returns the start times in a list
     */
    public static ObservableList<LocalTime> getEndTimeList() {

        if (endTimeList.isEmpty()) {
            timeGenerator();
        }

        return endTimeList;

    }

    // Need setter to set both lists.
    /**
     * This method generates the local times and designates the start and end of the business hours for the controllers to use.
     */
    private static void timeGenerator() {

        LocalDateTime localStart = START_BUSINESS_HOURS_ET.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localEnd = localStart.plusHours(14); //14 Hours between 8:00AM and 10:00PM



//        for (; localStart.isBefore(localEnd); localStart = localStart.plusMinutes(30)) {
//
//            startTimeList.add(localStart.toLocalTime()); // Start time is 8:00 AM
//            localStart = localStart.plusMinutes(30); // Increment the start time by 30 minutes.
//            endTimeList.add(localStart.toLocalTime()); // First end time is 8:30 AM
//
//        }

        // This caused a memory leak and kept building the list and never endede so I replaced it with a FOR loop instead of a
        // WHILE loop. This caused the program to function correctly.
        while (localStart.isBefore(localEnd)) {
            startTimeList.add(localStart.toLocalTime());// Start time is 8:00AM
            localStart = localStart.plusMinutes(30); // Only increment the start time. Increment by 30 minutes.
            endTimeList.add(localStart.toLocalTime()); // First end time is 8:30AM
        }

    }

//    public String loggedUser(String username) {
//
//        //
//
//        return username;
//    }

}
