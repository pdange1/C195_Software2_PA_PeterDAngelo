package sample.model;

import java.time.LocalDateTime;

/**
 * Appointment class used to build appointment objects received from the database.
 * Depends on Contact, User and Customer Classes.
 */
public class Appointment {


//    private String month;
//    private int total;
    private int apptId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;
    private int contactId;

    /**
     * Constructor parameters for the Appointment Class.
     * @param apptId The Appointment ID from the appointment object in the Database.
     * @param title The Title from the appointment object in the Database.
     * @param description The Description from the appointment object in the Database.
     * @param location The Location from the appointment object in the Database.
     * @param type The Type of appointment from the appointment object in the Database.
     * @param start The Start time from the appointment object in the Database.
     * @param end The End time from the appointment in the Database.
     * @param customerId The Customer ID from the appointment object in the Database.
     * @param userId The User ID from the appointment object in the Database.
     * @param contactId The Contact ID from the appointment object in the Database.
     */
    public Appointment(int apptId, String title, String description,
                       String location, String type, LocalDateTime start,
                       LocalDateTime end, int customerId, int userId, int contactId) {

        this.apptId = apptId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }


//    public Appointment(int total, String month, String type) {
//
//        this.total = total;
//        this.type = type;
//        this.month = month;
//    }
//    public String getMonth() {
//        return month;
//    }
//
//    public void setMonth(String month) {
//        this.month = month;
//    }
//
//    public int getTotal() {
//        return total;
//    }
//
//    public void setTotal(int total) {
//        this.total = total;
//    }
    /**
     * Gets the Appointment Id
     * @return apptId
     */
    public int getApptId() {
        return apptId;
    }

    /**
     * Set the Appointment Id for instantiated object.
     * @param apptId The appointment ID of the
     */
    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    /**
     * Gets the title of the appointment.
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the Title for instantiated object.
     * @param title
     */
    public void setTitle(String title) {

        this.title = title;
    }

    /**
     * Gets the description of the appointment
     * @return description.
     */
    public String getDescription() {

        return description;
    }

    /**
     * Set the Description for instantiated object.
     * @param description
     */
    public void setDescription(String description) {

        this.description = description;

    }

    /**
     * Gets the location
     * @return location
     */
    public String getLocation() {

        return location;
    }

    /**
     * Set the Location for instantiated object.
     * @param location
     */
    public void setLocation(String location) {

        this.location = location;

    }

    /**
     * Gets the type of appointment.
     * @return
     */
    public String getType() {

        return type;

    }

    /**
     * Set the Appointment Id for instantiated object.
     * @param type
     */
    public void setType(String type) {

        this.type = type;

    }

    /**
     * Gets the start date and time of the appointment.
     * @return start.
     */
    public LocalDateTime getStart() {

        return start;

    }

    /**
     * Set the Appointment Id for instantiated object.
     * @param start
     */
    public void setStart(LocalDateTime start) {

        this.start = start;

    }

    /**
     * Gets the appointment end date and time.
     * @return Returns the end date and time as a LocalDateTime.
     */
    public LocalDateTime getEnd() {

        return end;

    }

    /**
     * Set the Appointment Id for instantiated object.
     * @param end
     */
    public void setEnd(LocalDateTime end) {

        this.end = end;

    }

    /**
     * Gets the customer Id related to the appointment.
     * @return Returns the customer ID as an int.
     */
    public int getCustomerId() {

        return customerId;

    }

    /**
     * Set the Appointment Id for instantiated object.
     * @param customerId
     */
    public void setCustomerId(int customerId) {

        this.customerId = customerId;

    }

    /**
     * Gets the User ID related to the appointment.
     * @return Returns the user id as an int.
     */
    public int getUserId() {

        return userId;

    }

    /**
     * Set the Appointment Id for instantiated object.
     * @param userId
     */
    public void setUserId(int userId) {

        this.userId = userId;

    }

    /**
     * Gets the Contact ID related to the appointment.
     * @return Returns the Contact ID related to the appointment.
     */
    public int getContactId() {

        return contactId;
    }

    /**
     * Set the Appointment Id for instantiated object.
     * @param contactId
     */
    public void setContactId(int contactId) {

        this.contactId = contactId;
    }

    /**
     * This method changes the title of the appointment to a string that can be returned.
     * @return Returns the title of the appointment as a string.
     */
    @Override
    public String toString() {

        return apptId + "\n" + title + "\n" + start + "\n" + end + "\n" + customerId;

    }

}
