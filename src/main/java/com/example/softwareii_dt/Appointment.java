package com.example.softwareii_dt;

import com.example.softwareii_dt.JDBC_helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.*;
import java.util.TimeZone;

/**
 *  Class for Appointments
 *  @author dariustaylor
 */
public class Appointment {

    private int id;
    private String title;
    private String description;
    private String location;
    private String contact_name;
    private String user_name;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private int customer_id;
    private int user_id;
    private int contact_id;

    /**
     *  Basic Constructor sets the appointment ID
     */
    public Appointment() {
        id = -1;
    }

    /**
     *  Fully parameterized Constructor
     * @param id sets the appointment id
     * @param title sets the title
     * @param description sets the description
     * @param location sets the location
     * @param contact_name sets the contact name
     * @param type sets the type
     * @param start sets the start timestamp
     * @param end sets the end timestamp
     * @param customer_id sets the customer id
     * @param user_id sets the user id
     * @param contact_id sets the contact id
     */
    public Appointment(int id, String title, String description, String location, String contact_name, String type, Timestamp start, Timestamp end, int customer_id, int user_id, int contact_id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact_name = contact_name;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer_id = customer_id;
        this.user_id = user_id;
        this.contact_id = contact_id;
    }


    /**
     * Constructor for User uses
     * @param id sets the appointment id
     * @param start sets the start timestamp
     * @param user_id sets the user id
     */
    public Appointment(int id, Timestamp start, int user_id) {
        this.id = id;
        this.start = start;
        this.user_id = user_id;
    }

    /**
     * Constructor for contact filter
     * @param contact_name sets the contact name
     * @param id sets the appointment id
     * @param title sets the title
     * @param type sets the type
     * @param description sets the description
     * @param start sets the start timestamp
     * @param end sets the end timestamp
     * @param customer_id sets the customer id
     */
    public Appointment(String contact_name, int id, String title, String type, String description, Timestamp start, Timestamp end, int customer_id) {
        this.contact_name = contact_name;
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
        this.customer_id = customer_id;
    }
    //

    /**
     * Constructor for user filter
     * @param id sets the appointment id
     * @param user_name sets the users username
     * @param title sets the title
     * @param type sets the type
     * @param description sets the description
     * @param start sets the start timestamp
     * @param end sets the end timestamp
     * @param customer_id sets the customer id
     */
    public Appointment(int id, String user_name, String title, String type, String description, Timestamp start, Timestamp end, int customer_id) {
        this.user_name = user_name;
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
        this.customer_id = customer_id;
    }

    /**
     *  Gets Appointment ID
     * @return appointment ID
     */
    public int getId() {
        return id;
    }

    /**
     *  Set Appointment ID
     * @param id set appointment id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *  Gets Title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     *  Set Title
     * @param title sets title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *  Gets Description
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set Description
     * @param description sets Description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *  Gets Location
     * @return Location
     */
    public String getLocation() {
        return location;
    }

    /**
     *  Set Location
     * @param location sets Location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *  Gets Contact name
     * @return contact name
     */
    public String getContact_name() {
        return contact_name;
    }

    /**
     *  Set Contact name
     * @param contact_name sets contact name
     */
    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    /**
     *  Gets Users username
     * @return username
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     *  Set Username
     * @param user_name Sets users username
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     *  Gets Type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     *  Set Type
     * @param type sets type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *  Gets Start timestamp
     * @return start
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     *  Set Start
     * @param start sets start timestamp
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     *  Gets End timestamp
     * @return End timestamp
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     *  Set End
     * @param end sets End timestamp
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     *  Gets Customer ID
     * @return customer ID
     */
    public int getCustomer_id() {
        return customer_id;
    }

    /**
     *  Set Customer_ID
     * @param customer_id sets customer id
     */
    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    /**
     *  Gets User ID
     * @return user ID
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     *  Set User ID
     * @param user_id Sets User ID
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    /**
     *  Gets Contact ID
     * @return contact ID
     */
    public int getContact_id() {
        return contact_id;
    }

    /**
     *  Set Contact ID
     * @param contact_id Sets contact ID
     */
    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    /**
     *  Get List of all Appointments
     * @return List of all Appointments usable project-wide
     * throws SQLException if there are database issues
     */
    public static ObservableList<Appointment> getAllAppointments() {
        ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String query = "SELECT Appointment_ID, Title, Description, " +
                "Location, CONTACTS.Contact_Name, Type, Start, End, Customer_ID, User_ID, " +
                "APPOINTMENTS.Contact_ID FROM APPOINTMENTS, CONTACTS WHERE APPOINTMENTS.Contact_ID = CONTACTS.Contact_ID";
        try{
            PreparedStatement statement = JDBC.conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int appointment_id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String desc = rs.getString("Description");
                String loc = rs.getString("Location");
                String contact = rs.getString("Contact_Name");
                String type = rs.getString("Type");
                int customer_id = rs.getInt("Customer_ID");
                int user_id = rs.getInt("User_ID");
                int contact_id = rs.getInt("Contact_ID");

                // Get UTC start and end time from database
                Timestamp startUTC = rs.getTimestamp("Start");
                Timestamp endUTC = rs.getTimestamp("End");

                // Add a New Appointment using a constructor with the variables to control the appointment view
                appointments.add(new Appointment(appointment_id, title, desc, loc, contact, type, startUTC, endUTC, customer_id, user_id, contact_id));
            }
        }catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        }

        return appointments;
    }

    /**
     *  Gets list of all Appointments by Contacts
     * @return List of all Appointments filtered for contacts from the database
     * throws SQLException if database has issues.
     */
    public static ObservableList<Appointment> getAllByContact() {
        ObservableList<Appointment> byContact = FXCollections.observableArrayList();
        String query = "SELECT CONTACTS.Contact_Name as Contact, Appointment_ID, Title, Type, Description, Start, End, Customer_ID " +
                "FROM APPOINTMENTS, CONTACTS " +
                "WHERE APPOINTMENTS.Contact_ID = CONTACTS.Contact_ID " +
                "ORDER BY Contact_Name";

        try {
            PreparedStatement statement = JDBC.conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String contact = rs.getString("Contact");
                int appointment_id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                String desc = rs.getString("Description");
                int customer_id = rs.getInt("Customer_ID");

                // Get UTC start and end time from database
                Timestamp startUTC = rs.getTimestamp("Start");
                Timestamp endUTC = rs.getTimestamp("End");

                byContact.add(new Appointment(contact, appointment_id, title, type, desc, startUTC, endUTC, customer_id));
            }
        } catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        }

        return byContact;
    }

    /**
     *  Get list of appointments filtered by user
     * @return full List of Appointments with the filtered for Users from the database
     * throws SQLException if database has issues
     */
    public static ObservableList<Appointment> getAllByUser() {
        ObservableList<Appointment> byUser = FXCollections.observableArrayList();
        String query = "SELECT USERS.User_Name as User, Appointment_ID, Title, Type, Description, Start, End, Customer_ID " +
        "FROM APPOINTMENTS, USERS " +
        "WHERE APPOINTMENTS.User_ID = USERS.User_ID " +
        "ORDER BY User_Name";
        try {
            PreparedStatement statement = JDBC.conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String user = rs.getString("User");
                int appointment_id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                String desc = rs.getString("Description");
                int customer_id = rs.getInt("Customer_ID");

                // Get UTC start and end time from database
                Timestamp startUTC = rs.getTimestamp("Start");
                Timestamp endUTC = rs.getTimestamp("End");

                byUser.add(new Appointment(appointment_id, user, title, type, desc, startUTC, endUTC, customer_id));
            }
        } catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        }

        return byUser;
    }

    /**
     *  Overridden toString
     * @return Overridden to String for prints instead of showing the Object Hash
     */
    @Override
    public String toString() {
        return this.id + " " + this.start + " " + this.user_id;
    }
}
