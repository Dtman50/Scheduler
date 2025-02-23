package com.example.softwareii_dt;

import com.example.softwareii_dt.JDBC_helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PremiumRecord extends Record{

    private String status;

    /**
     * Fully parameterized constructor
     * @param id sets the premium record id
     * @param name sets the premium record name
     * @param address sets the premium record address
     * @param postal sets the premium record postal code
     * @param phone sets the premium record phone number
     * @param country_ID sets the premium record country ID
     * @param country sets the premium record country
     * @param division_ID sets the premium record division ID
     * @param division sets the premium record division
     * @param status sets the premium record status
     */
    public PremiumRecord(int id, String name, String address, String postal, String phone, int country_ID, String country, int division_ID, String division, String classification, String status) {
        super(id, name, address, postal, phone, country_ID, country, division_ID, division, classification);
        this.status = status;
    }

    /**
     * Gets premium record status
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set premium record status
     * @param status sets status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *  Get list of all Premium Customers from DB
     * @return List of all Premium Customer Records from database
     * throws SQLException if database has issues
     */
    public static ObservableList<PremiumRecord> getAllPremiumRecords() {
        ObservableList<PremiumRecord> premiums = FXCollections.observableArrayList();

        String query = "SELECT c.*, cs.Country, fld.Division, fld.Country_ID " +
                "FROM CUSTOMERS as c, FIRST_LEVEL_DIVISIONS as fld, COUNTRIES as cs " +
                "WHERE c.Division_ID = fld.Division_ID AND fld.Country_ID = cs.Country_ID AND c.classification = 'Premium'" +
                "ORDER BY Customer_ID";
        try {
            Statement statement = JDBC.conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                premiums.add(new PremiumRecord(rs.getInt("Customer_ID"), rs.getString("Customer_Name"),
                        rs.getString("Address"), rs.getString("Postal_Code"), rs.getString("Phone"),
                        rs.getInt("Country_ID"), rs.getString("Country"), rs.getInt("Division_ID"),
                        rs.getString("Division"), rs.getString("Classification"), rs.getString("Status")));
            }

        } catch (SQLException se){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        }
        return premiums;
    }

}
