package com.example.softwareii_dt;

import com.example.softwareii_dt.JDBC_helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  Class for States and Divisions
 * @author dariustaylor
 */
public class FirstLevelDivisions {

    private int id;
    private String division_name;
    private int country_id;

    /**
     * Basic Constructor
     */
    public FirstLevelDivisions() {
        id = -1;
    }

    /**
     * Fully parameterized constructor
     * @param id sets divisions ID
     * @param division_name sets divisions name
     * @param country_id sets country id
     */
    public FirstLevelDivisions(int id, String division_name, int country_id) {
        this.id = id;
        this.division_name = division_name;
        this.country_id = country_id;
    }

    /**
     *  Gets divisions ID
     * @return divisions ID
     */
    public int getId() {
        return id;
    }

    /**
     *  Set Division ID
     * @param id sets divisions ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *  Gets divisions name
     * @return division name
     */
    public String getDivisionName() {
        return division_name;
    }

    /**
     *  Set Division Name
     * @param division_name sets divisions name
     */
    public void setDivisionName(String division_name) {
        this.division_name = division_name;
    }

    /**
     *  Gets country ID
     * @return country id
     */
    public int getCountry_id() {
        return country_id;
    }

    /**
     *  Set Country ID
     * @param country_id sets country ID
     */
    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    /**
     *  Get list of all divisions from DB
     * @return List of all Divisions from Database
     * throws SQLException if database has issues
     */
    public static ObservableList<FirstLevelDivisions> getAllDivisions (){
        ObservableList<FirstLevelDivisions> divisions = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = JDBC.conn.prepareStatement("SELECT FIRST_LEVEL_DIVISIONS.*, COUNTRIES.Country FROM FIRST_LEVEL_DIVISIONS JOIN COUNTRIES ON FIRST_LEVEL_DIVISIONS.Country_ID = COUNTRIES.Country_ID");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                divisions.add(new FirstLevelDivisions(rs.getInt("Division_ID"), rs.getString("Division"), rs.getInt("Country_ID")));
            }
        } catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        }
        return divisions;
    }

    /**
     *  Overridden toString
     * @return Overridden toString method to return Division name instead of Object hash
     */
    @Override
    public String toString() {
        return this.division_name;
    }
}
