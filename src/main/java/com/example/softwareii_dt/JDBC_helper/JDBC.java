package com.example.softwareii_dt.JDBC_helper;

import java.sql.*;

/**
 *  JDBC Driver Manager Database Connection
 *  Creates Database Connection
 *  @author dariustaylor
 */
public abstract class JDBC {
    private static final String url2 = "jdbc:mysql://localhost:3306/MyOffice?connectionTimeZone = SERVER";
//    private static final String user = "sqlUser";
    private static final String user = "root";
//    private static final String pass = "Passw0rd!";
    private static final String pass = "password";
    /**
     * public Connection variable for database uses
     */
    public static Connection conn;
    /**
     * Prints the status of the database connection
     */
    public static String connStatus;

    /**
     * Uses the instance variables to create a Database Connection.
     * throws SQLException if the database connection fails
     */
    public static void makeConnection() {
        try {
            conn = DriverManager.getConnection(url2, user, pass);

            if ((conn != null)) {
                connStatus = "Connection Success";
                System.out.println(connStatus);
            }


        } catch (SQLException se) {
            System.out.println("Database Connection Failed: " + se.getMessage());
        }

    }

    /**
     * Closes the database Connection
     */
    public static void closeConnection() {
        try{
            conn.close();
            connStatus = "Database Connection Closed";
            System.out.println(connStatus);
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

}
