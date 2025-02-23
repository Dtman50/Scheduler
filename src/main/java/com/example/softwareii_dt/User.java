package com.example.softwareii_dt;

import com.example.softwareii_dt.JDBC_helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

/**
 *  Class for login User
 * @author dariustaylor
 */
public class User {
    private int id;
    private String username;
    private String password;

    /**
     * Basic Constructor
     */
    public User() {
        id = 0;
        username = null;
        password = null;
    }


    /**
     * Fully parameterized constructor
     * @param id sets the user id
     * @param username sets the user username
     * @param password sets the user password
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     *  Gets User ID
     * @return user id
     */
    public int getId() {
        return id;
    }

    /**
     *  Set user ID
     * @param id sets user ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *  Gets User username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     *  Set username
     * @param username sets users username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *  Gets user password
     * @return users password
     */
    public String getPassword() {
        return password;
    }

    /**
     *  Set password
     * @param password Sets user password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *  Get list of all users from DB
     * @return List of All Users from the database
     * throws SQLException if the database has issues
     */
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT User_ID, User_Name, Password FROM USERS";

        try{
            PreparedStatement statement = JDBC.conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt("User_ID"), rs.getString("User_Name"), rs.getString("Password")));
            }

        } catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        }

        return users;
    }

    /**
     *  Overridden toString
     * @return Overridden toString method to return id and username instead of Object hash
     */
    @Override
    public String toString(){
        return id + " " + username;
    }
}
