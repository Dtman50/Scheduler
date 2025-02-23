package com.example.softwareii_dt;

import com.example.softwareii_dt.JDBC_helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  Class for Contacts
 * @author dariustaylor
 */
public class Contact {
    private int id;
    private String contact_name;
    private String email;

    /**
     * Basic Constructor
     */
    public Contact() {
        id = -1;
    }

    /**
     * Fully parameterized constructor
     * @param id sets Contact ID
     * @param contact_name sets Contact name
     * @param email sets Contact email
     */
    public Contact(int id, String contact_name, String email) {
        this.id = id;
        this.contact_name = contact_name;
        this.email = email;
    }

    /**
     *  Gets Contact ID
     * @return contact id
     */
    public int getId() {
        return id;
    }

    /**
     *  Set Contact ID
     * @param id sets ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *  Gets Contact name
     * @return contact name
     */
    public String getContact_name() {
        return contact_name;
    }

    /**
     *  Set Contact Name
     * @param contact_name sets name
     */
    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    /**
     *  Gets contacts Email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     *  Set Contact email
     * @param email sets email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *  Get list of all Contacts from DB
     * @return List of all Contacts from the Database
     * throws SQLException if database has issues
     */
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        String query = "SELECT Contact_ID, Contact_Name, Email FROM CONTACTS";
        try {
            PreparedStatement statement = JDBC.conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                contacts.add(new Contact(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email")));
            }
        } catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        }
        return contacts;
    }

    /**
     *  Overridden toString
     * @return Overridden toString method to return Contact name instead of Object hash
     */
    @Override
    public String toString() {
        return this.contact_name;
    }
}
