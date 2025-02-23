package com.example.softwareii_dt;

import com.example.softwareii_dt.JDBC_helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  Class for Reports
 * @author dariustaylor
 */
public class Report {
    private String month;
    private String type;
    private int total;

    /**
     * Fully parameterized constructor
     * @param month sets appointment date month name
     * @param type sets appointment type
     * @param total sets appointment total
     */
    public Report(String month, String type, int total) {
        this.month = month;
        this.type = type;
        this.total = total;
    }

    /**
     * Gets appointment date month name
     * @return month name
     */
    public String getMonth() {
        return month;
    }

    /**
     *  Set Month name
     * @param month sets appointment date month name
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Gets appointment type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     *  Set Type
     * @param type sets appointment type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets appointment total
     * @return total
     */
    public int getTotal() {
        return total;
    }

    /**
     *  Set Totals
     * @param total sets appointment total
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     *  Get list of filtered appointments from DB to make report
     * @return List of report of filtered appointments by Month and type from database
     * throws SQLException if database has issues
     */
    public static ObservableList<Report> getAllTotalByType_Month() {
        ObservableList<Report> reports = FXCollections.observableArrayList();
        String query = "SELECT MONTHNAME(Start) as Month, Type, COUNT(*) as Total " +
                "FROM APPOINTMENTS " +
                "GROUP BY MONTHNAME(Start), Type " +
                "ORDER BY MONTHNAME(Start) ";

        try {
            PreparedStatement statement = JDBC.conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                reports.add(new Report(rs.getString("Month"), rs.getString("Type"), rs.getInt("Total")));
            }

        } catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error" + se.getMessage());
            alert.show();
        }

        return reports;
    }
}
