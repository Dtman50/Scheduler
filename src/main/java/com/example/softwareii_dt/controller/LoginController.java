package com.example.softwareii_dt.controller;

import com.example.softwareii_dt.Appointment;
import com.example.softwareii_dt.JDBC_helper.JDBC;
import com.example.softwareii_dt.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *  Login Controller for login.fxml
 *  Logs User into app
 *  @author dariustaylor
 *
 */
public class LoginController implements Initializable {
    // Resource bundle for language
    public ResourceBundle rb;
    // Stage variable for page switch
    public Stage stage;

    @FXML
    private Label login_locationLabel;

    @FXML
    private Label login_location_label;

    @FXML
    private Label login_login_label;

    @FXML
    private Label login_username_label;

    @FXML
    private Label login_pw_label;

    @FXML
    private PasswordField login_passTF;

    @FXML
    private TextField login_uNameTF;

    @FXML
    private Button login_exitBtn;

    @FXML
    private Button login_loginBtn;

    /**
     * Exits App
     * @param event closes App when exit button is pressed
     */
    @FXML
    void exitApp(ActionEvent event) {
        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Logs user into app.
     * @param event login button. Logs the user into the app if there are no errors
     *  IOException  if login failed
     *  SQLException if database has issues
     */
    @FXML
    void login(ActionEvent event) {
        String username = login_uNameTF.getText();
        String pw = login_passTF.getText();
        Alert alert;
        // Gets language resource bundle
        rb = ResourceBundle.getBundle("bundle");
        // filename for logger
        String fileName = "login_activity";

        try {
            if (username.isBlank()) {
                throw new IOException();
            }
            if (pw.isBlank()) {
                throw new IOException();
            }
            // checks if the username and passwords are valid
            if (isValidPassword(username, pw)) {
                try {
                    // create buffered writer and file writer for logging. Opens and closes when done writing to file. For successful login
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
                    writer.append(String.valueOf(LocalDateTime.now())).append(" ").append("Username: ").append(username).append(" ").append("Successful Login. \n");
                    System.out.println("New login logged to log file.");
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                // Sets labels to language of default system language settings
                if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
                    alert = new Alert(Alert.AlertType.INFORMATION, rb.getString("Login_Success"));
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // check for appointments within 15 minutes of logging in
                        checkUpcomingAppointment(username);

                        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("gateway.fxml"));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                        stage.setTitle("MyOffice");
                        stage.setScene(scene);
                        stage.show();
                    }
                }
            } else {
                // create buffered writer and file writer for logging. Opens and closes when done writing to file. For failed login
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
                writer.append(String.valueOf(LocalDateTime.now())).append(" ").append("Username: ").append(username).append(" ").append("Failed Attempt.\n");
                System.out.println("New login logged to log file.");
                writer.flush();
                writer.close();
                throw new IOException();
            }

        } catch (IOException ie) {
            if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
                alert = new Alert(Alert.AlertType.ERROR, rb.getString("Bad_User_or_Pw"));
                alert.show();
            }

        } catch (SQLException se) {
            if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
                alert = new Alert(Alert.AlertType.ERROR, rb.getString("SQL_Error") + ": " + se.getMessage());
                alert.show();
            }

        }

    }

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * *  Shows the full parts and product lists.
     *
     * @param url            location - The location used to resolve relative paths for the root object,
     *                       or null if the location is not known. *Java8 docs*
     * @param resourceBundle resources - The resources used to localize the root object,
     *                       or null if the root object was not localized. *Java8 docs*
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = ResourceBundle.getBundle("bundle");

        login_locationLabel.setText(ZoneId.systemDefault().toString());

        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
            login_location_label.setText(rb.getString("Location"));
            login_login_label.setText(rb.getString("Login"));
            login_username_label.setText(rb.getString("Username"));
            login_pw_label.setText(rb.getString("Password"));
            login_loginBtn.setText(rb.getString("Login"));
            login_exitBtn.setText(rb.getString("Exit"));
        }

    }

    /**
     *  Validates if username and password is correct
     * @param username accepts users username
     * @param password accepts users password
     * @return true if the password matches the username entered
     * false if the password doesn't match a username entered
     * @throws SQLException if database has issues
     */
    public boolean isValidPassword(String username, String password) throws SQLException {
        PreparedStatement statement = JDBC.conn.prepareStatement("SELECT password FROM USERS WHERE User_Name = '"+ username +"'");
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            if (rs.getString("password").equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there are any appointments for current users within 15 minutes
     * @param user accepts the user that has logged in
     *             Gets a list of appointments based on the user that has logged in and filters it to see if that user has any appointments
     *             within 15 minutes of now. Sends an alert to the UI if there is an appointment and also if there isn't one.
     * LAMBDA EXPRESSION -- used while filtering the filtered list of user appointments for more efficient code. Sets the predicate
     *          on the filtered list to filter appointments from the appointment users appointment list based on the datetime being 15
     *          minutes after the current
     */
    public void checkUpcomingAppointment(String user) {
        FilteredList<Appointment> filtered = new FilteredList<>(getUserAppointmentList(user));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plus15Min = now.plusMinutes(15);

        // lambda used for more efficient code
        filtered.setPredicate(apt -> {
            LocalDateTime aptDate = apt.getStart().toLocalDateTime();
            return aptDate.isAfter(now.minusMinutes(1)) && aptDate.isBefore(plus15Min);
        });

        if (!filtered.isEmpty()) {
            int id = filtered.get(0).getId();
            Timestamp dateTS = filtered.get(0).getStart();
            LocalDateTime dateDT = dateTS.toLocalDateTime();
            LocalDate localDate = dateDT.toLocalDate();
            LocalTime localTime = dateDT.toLocalTime();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointment Reminder");
            alert.setHeaderText("Reminder - You have an appointment scheduled within the next 15 minutes.");
            alert.setContentText("The Appointment ID is " + id +
                    "\nThe Appointment Date is " + localDate +
                    "\nThe Appointment Time is " + localTime + "\n");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have no upcoming appointments within the next 15 minutes.");
            alert.show();
        }
    }

    /**
     * Gets List of Appointments based on the User
     * @param user takes in the user that has logged in successfully
     * @return a list of the appointments of the user that has logged in.
     * throws SQLException for database issues
     */
    public ObservableList<Appointment> getUserAppointmentList(String user) {
        ObservableList<Appointment> userAppts = FXCollections.observableArrayList();

        String query = "SELECT appointments.Appointment_ID, appointments.Start, appointments.user_id, users.user_name " +
                "FROM APPOINTMENTS, USERS " +
                "WHERE APPOINTMENTS.User_ID = USERS.User_ID AND User_Name = ?";

        try {
            PreparedStatement statement = JDBC.conn.prepareStatement(query);
            statement.setString(1, user);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Appointment_ID");
                int user_id = rs.getInt("User_ID");
                // get UTC date from database
                Timestamp date = rs.getTimestamp("Start");

                userAppts.add(new Appointment(id, date, user_id));
            }


        } catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        }

        return userAppts;
    }
}