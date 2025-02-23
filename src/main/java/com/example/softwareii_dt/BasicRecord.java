package com.example.softwareii_dt;

import com.example.softwareii_dt.JDBC_helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BasicRecord extends Record{
    /**
     * Fully parameterized constructor
     * @param id sets the basic record id
     * @param name sets the basic record name
     * @param address sets the basic record address
     * @param postal sets the basic record postal code
     * @param phone sets the basic record phone number
     * @param country_ID sets the basic record country ID
     * @param country sets the basic record country
     * @param division_ID sets the basic record division ID
     * @param division sets the basic record division
     */
    public BasicRecord(int id, String name, String address, String postal, String phone, int country_ID, String country, int division_ID, String division, String classification) {
        super(id, name, address, postal, phone, country_ID, country, division_ID, division, classification);
    }

    /**
     *  Get list of all Basic Customers from DB
     * @return List of all Basic Customer Records from database
     * throws SQLException if database has issues
     */
    public static ObservableList<BasicRecord> getAllBasicRecords() {
        ObservableList<BasicRecord> basics = FXCollections.observableArrayList();

        String query = "SELECT c.*, cs.Country, fld.Division, fld.Country_ID " +
                "FROM CUSTOMERS as c, FIRST_LEVEL_DIVISIONS as fld, COUNTRIES as cs " +
                "WHERE c.Division_ID = fld.Division_ID AND fld.Country_ID = cs.Country_ID AND c.classification = 'Basic'" +
                "ORDER BY Customer_ID";
        try {
            Statement statement = JDBC.conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                basics.add(new BasicRecord(rs.getInt("Customer_ID"), rs.getString("Customer_Name"),
                        rs.getString("Address"), rs.getString("Postal_Code"), rs.getString("Phone"),
                        rs.getInt("Country_ID"), rs.getString("Country"), rs.getInt("Division_ID"),
                        rs.getString("Division"), rs.getString("Classification")));
            }

        } catch (SQLException se){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        }

        return basics;

    }
}
