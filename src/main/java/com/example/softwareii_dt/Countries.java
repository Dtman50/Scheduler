package com.example.softwareii_dt;

import com.example.softwareii_dt.JDBC_helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  Class for Countries
 * @author dariustaylor
 */
public class Countries {
    private int id;
    private String country_name;

    /**
     * Basic Constructor
     */
    public Countries() {
        id = -1;
    }

    /**
     * Fully parameterized constructor
     * @param id sets country id
     * @param country_name sets country name
     */
    public Countries(int id, String country_name) {
        this.id = id;
        this.country_name = country_name;
    }

    /**
     * Gets country ID
     * @return country id
     */
    public int getId() {
        return id;
    }

    /**
     *  Set Country ID
     * @param id sets ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *  Gets Country Name
     * @return country name
     */
    public String getCountryName() {
        return country_name;
    }

    /**
     *  Set Country Name
     * @param country_name Sets name
     */
    public void setCountryName(String country_name) {
        this.country_name = country_name;
    }

    /**
     *  Get list of all Countries from DB
     * @return List of all Countries from the Database usable project-wide
     * throws SQLException if database has issues
     */
    public static ObservableList<Countries> getAllCountries() {
        ObservableList<Countries> allCountries = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = JDBC.conn.prepareStatement("SELECT Country_ID, Country FROM COUNTRIES");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                allCountries.add(new Countries(rs.getInt("Country_ID"), rs.getString("Country")));
            }
        } catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        }

        return allCountries;
    }

    /**
     *  Overridden toString
     * @return Overridden toString method to return Country name instead of Object hash
     */
    @Override
    public String toString(){
        return this.country_name;
    }
}
