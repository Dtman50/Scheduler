package com.example.softwareii_dt;

import com.example.softwareii_dt.JDBC_helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

/**
 *  Class for Customers Records
 * @author dariustaylor
 */
public abstract class Record {

    private int id;
    private String name;
    private String address;
    private String postal;
    private String phone;
    private int country_ID;
    private String country;
    private int division_ID;
    private String division;
    private String classification;
//    private String status;

    /**
     *  Basic Constructor
     */
    public Record() {
        id = -1;
    }

    /**
     * Fully parameterized constructor
     * @param id sets customer id
     * @param name sets customer name
     * @param address sets customer address
     * @param postal sets customer postal code
     * @param phone sets customer phone number
     * @param country_ID sets country id
     * @param country sets country name
     * @param division_ID sets division id
     * @param division sets division name
     */
    public Record(int id, String name, String address, String postal, String phone, int country_ID, String country, int division_ID, String division, String classification) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.country_ID = country_ID;
        this.country = country;
        this.division_ID = division_ID;
        this.division = division;
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    /**
     * Gets Country id
     * @return country id
     */
    public int getCountry_ID() {
        return country_ID;
    }

    /**
     *  Set country ID
     * @param country_ID sets country ID
     */
    public void setCountry_ID(int country_ID) {
        this.country_ID = country_ID;
    }

    /**
     * Gets division id
     * @return division id
     */
    public int getDivision_ID() {
        return division_ID;
    }

    /**
     *  Set division ID
     * @param division_ID sets division id
     */
    public void setDivision_ID(int division_ID) {
        this.division_ID = division_ID;
    }

    /**
     * Gets division name
     * @return division name
     */
    public String getDivision() {
        return division;
    }

    /**
     *  Set Division Name
     * @param division sets division name
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     *  Gets country name
     * @return country name
     */
    public String getCountry() {
        return country;
    }

    /**
     *  Set Country Name
     * @param country sets country name
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets customer id
     * @return customer id
     */
    public int getId() {
        return id;
    }

    /**
     *  Set Customer ID
     * @param id sets customer id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets customer name
     * @return customer name
     */
    public String getName() {
        return name;
    }

    /**
     *  Set Customer Name
     * @param name sets customer name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets customer address
     * @return customer address
     */
    public String getAddress() {
        return address;
    }

    /**
     *  Set Customer address
     * @param address sets customer address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets customer postal code
     * @return postal code
     */
    public String getPostal() {
        return postal;
    }

    /**
     *  Set Postal code
     * @param postal sets customer postal code
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     * Gets customer phone number
     * @return phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     *  Set Phone Number
     * @param phone sets phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *  Overridden toString
     * @return Overridden toString method to return id, name, phone, address, country, division, and postal code instead
     *          of Object hash
     */
    @Override
    public String toString(){
        return id + " " + name + " " + phone + " " + address + " " + country + " " + division + " " + postal;
    }


}
