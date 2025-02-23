package com.example.softwareii_dt.controller;

import com.example.softwareii_dt.*;
import com.example.softwareii_dt.JDBC_helper.JDBC;
import com.example.softwareii_dt.Record;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.*;

/**
 * Scheduler Controller for scheduler.fxml
 * Holds Customer Appointments
 * @author dariustaylor
 *
 */

public class SchedulerController implements Initializable {

    public Stage stage;
    // List for all contacts
    public final ObservableList<Contact> contacts = Contact.getAllContacts();
    // List for all users
    public final ObservableList<User> users = User.getAllUsers();
    // List for all customers
    public final ObservableList<Record> customers = Clients.getAllRecords();
    // List for types
    public final ObservableList<String> types = FXCollections.observableArrayList("Status Update", "Decision-making", "Problem-Solving", "Info-Sharing");
    // Local Zone ID
    public final ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());
    // Eastern Zone ID
    public final ZoneId easternZone = ZoneId.of("US/Eastern");
    // UTC Zone ID
    public final ZoneId utcZone = ZoneId.of("UTC");

    @FXML
    private Label scheduler_timeZoneLbl;

    @FXML
    private TableView<Appointment> scheduler_tableView;

    @FXML
    private TableColumn<Appointment, Integer> scheduler_AppointmentIDCol;

    @FXML
    private TableColumn<Appointment, Integer> scheduler_UserIDCol;

    @FXML
    private TableColumn<Appointment, String> scheduler_contactCol;

    @FXML
    private TableColumn<Appointment, Integer> scheduler_customerIDCol;

    @FXML
    private TableColumn<Appointment, String> scheduler_descCol;

    @FXML
    private TableColumn<Appointment, LocalDateTime> scheduler_startDTCol;

    @FXML
    private TableColumn<Appointment, LocalDateTime> scheduler_endDTCol;

    @FXML
    private TableColumn<Appointment, String> scheduler_locationCol;

    @FXML
    private TableColumn<Appointment, String> scheduler_titleCol;

    @FXML
    private TableColumn<Appointment, String> scheduler_typeCol;

    @FXML
    private ComboBox<Contact> scheduler_contactCombo;

    @FXML
    private ComboBox<LocalTime> scheduler_startTCombo;

    @FXML
    private ComboBox<LocalTime> scheduler_endTCombo;

    @FXML
    private ComboBox<String> scheduler_typeCombo;

    @FXML
    private ComboBox<Record> scheduler_customerCombo;

    @FXML
    private ComboBox<User> scheduler_userCombo;

    @FXML
    private DatePicker scheduler_datePicker;

    @FXML
    private TextField scheduler_appointmentIDTF;

    @FXML
    private TextField scheduler_descTF;

    @FXML
    private TextField scheduler_locationTF;

    @FXML
    private TextField scheduler_titleTF;

    @FXML
    private RadioButton scheduler_monthRadio;

    @FXML
    private RadioButton scheduler_weekRadio;

    @FXML
    private RadioButton scheduler_allAppointmentsRadio;


    /**
     *  Adds appointment to database
     * @throws NullPointerException  handles if time and date fields are empty or if there is an overlap time appointments
     */
    @FXML
    void AddSchedule() {
        // check if appointment is valid
        if (isValidAppointment()) {

            String addErrorMessage = "";

            try {
                if (scheduler_startTCombo.getSelectionModel().isEmpty()) {
                    addErrorMessage += "Please select a Start Time. \n";
                    throw new NullPointerException();
                }

                if (scheduler_endTCombo.getSelectionModel().isEmpty()) {
                    addErrorMessage += "Please select an End Time. \n";
                    throw new NullPointerException();
                }
                if (scheduler_datePicker.getValue() == null) {
                    addErrorMessage += "Please select a Date";
                    throw new NullPointerException();
                }

            } catch (NullPointerException ne) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText(addErrorMessage);
                alert.showAndWait();
            }

            String query = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID, Create_Date, Created_By, Last_Update, Last_Updated_By) VALUES (?,?,?,?,?,?,?,?,?,current_timestamp(),'root',current_timestamp(),'root')";
            LocalDate date = scheduler_datePicker.getValue();
            // Get local time dates and times
            LocalTime startTime = scheduler_startTCombo.getValue();
            LocalTime endTime = scheduler_endTCombo.getValue();

            // Create a local DateTime by combining the local date and time
            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

            try {
                // get data from database and set the values of the ?'s from the query as the text fields
                PreparedStatement statement = JDBC.conn.prepareStatement(query);
                statement.setString(1, scheduler_titleTF.getText());
                statement.setString(2, scheduler_descTF.getText());
                statement.setString(3, scheduler_locationTF.getText());
                statement.setString(4, scheduler_typeCombo.getValue());
                statement.setTimestamp(5, Timestamp.valueOf(startDateTime));
                statement.setTimestamp(6, Timestamp.valueOf(endDateTime));
                statement.setInt(7, scheduler_customerCombo.getSelectionModel().getSelectedItem().getId());
                statement.setInt(8, scheduler_userCombo.getSelectionModel().getSelectedItem().getId());
                statement.setInt(9, scheduler_contactCombo.getSelectionModel().getSelectedItem().getId());

                // check for Overlap and if start and end times are valid
                try {
                    if (endDateTime.isEqual(startDateTime) || endDateTime.isBefore(startDateTime)) {
                        addErrorMessage += "End Time Cannot be on or before Start Time. \n";
                        throw new NullPointerException();
                    } else {
                        if (isOverlap(startDateTime, endDateTime, -1)) {
                            addErrorMessage += "There an appointment Overlap! Select new Times. \n";
                            throw new NullPointerException();
                        }
                    }

                    statement.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment Added");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        clearAllFields();
                        Refresh();
                    }

                } catch (NullPointerException ne) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Invalid Appointment");
                    alert.setContentText(addErrorMessage);
                    alert.showAndWait();
                }

            } catch (SQLException se) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
                alert.show();
            } catch (NullPointerException ne) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText(addErrorMessage);
                alert.showAndWait();
            }
        }
    }

    /**
     *  clears text fields when button is pressed
     */
    @FXML
    void ClearFields() {
        clearAllFields();
    }

    /**
     *  Deletes appointment from database
     * @throws NullPointerException  handles if there is no appointment selected to delete
     */
    @FXML
    void DeleteSchedule() {
        Appointment selected = scheduler_tableView.getSelectionModel().getSelectedItem();
        int id = selected.getId();
        String query = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = " + id;
        String type = scheduler_typeCombo.getValue();

        try {
            PreparedStatement statement = JDBC.conn.prepareStatement(query);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel this appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                statement.executeUpdate();
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Appointment Successfully Cancelled! \n\nAppointment ID: " + id + "\nType: '" + type + "'");
                alert1.show();
                clearAllFields();
                Refresh();
            }

        } catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        } catch (NullPointerException ne) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Must select appointment to delete.");
            alert.show();
        }

    }

    /**
     *  Updates Appointment in database
     * @throws NullPointerException if appointment is invalid or if the fields are empty
     * throws SQLException if there are database issues
     */
    @FXML
    void UpdateSchedule() {
        // checks for valid appointment upon updating
        if (isValidAppointment()) {

            String updateErrorMessage = "";
            // make sure the dates and times fields are filled
            try {
                if (scheduler_startTCombo.getSelectionModel().isEmpty()) {
                    updateErrorMessage += "Please select a Start Time. \n";
                    throw new NullPointerException();
                }

                if (scheduler_endTCombo.getSelectionModel().isEmpty()) {
                    updateErrorMessage += "Please select an End Time. \n";
                    throw new NullPointerException();
                }
                if (scheduler_datePicker.getValue() == null) {
                    updateErrorMessage += "Please select a Date";
                    throw new NullPointerException();
                }

            } catch (NullPointerException ne) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText(updateErrorMessage);
                alert.showAndWait();
            }

            // Get local dates and times
            LocalDate date = scheduler_datePicker.getValue();
            LocalTime startTime = scheduler_startTCombo.getSelectionModel().getSelectedItem();
            LocalTime endTime = scheduler_endTCombo.getSelectionModel().getSelectedItem();

            // Create Local DateTime from combined date and time
            LocalDateTime startDT = LocalDateTime.of(date, startTime);
            LocalDateTime endDT = LocalDateTime.of(date, endTime);

            // convert to timestamp for database
            Timestamp sqlStart = Timestamp.valueOf(startDT);
            Timestamp sqlEnd = Timestamp.valueOf(endDT);

            // Query
            String query = "UPDATE APPOINTMENTS "
                    + "SET Title = ?, Description = ?, Location = ?, Type = ?, "
                    + "Start = ?, End = ?, Customer_ID = ?, User_ID = ?, "
                    + "Contact_ID = ?, Last_Update = current_timestamp(), Last_Updated_By = 'root' "
                    + "WHERE Appointment_ID = " + scheduler_appointmentIDTF.getText();

            // Get fields and update database using those fields
            try {
                PreparedStatement statement = JDBC.conn.prepareStatement(query);
                statement.setString(1, scheduler_titleTF.getText());
                statement.setString(2, scheduler_descTF.getText());
                statement.setString(3, scheduler_locationTF.getText());
                statement.setString(4, scheduler_typeCombo.getValue());
                statement.setTimestamp(5, sqlStart);
                statement.setTimestamp(6, sqlEnd);
                statement.setInt(7, scheduler_customerCombo.getSelectionModel().getSelectedItem().getId());
                statement.setInt(8, scheduler_userCombo.getSelectionModel().getSelectedItem().getId());
                statement.setInt(9, scheduler_contactCombo.getSelectionModel().getSelectedItem().getId());

                try {
                    if (endDT.isEqual(startDT) || endDT.isBefore(startDT)) {
                        updateErrorMessage += "End Time Cannot be on or before Start Time. \n";
                        throw new NullPointerException();
                    } else {
                        if (isOverlap(startDT, endDT, Integer.parseInt(scheduler_appointmentIDTF.getText()))) {
                            updateErrorMessage += "There an appointment Overlap! Select new Times. \n";
                            throw new NullPointerException();
                        }
                    }

                    statement.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment Successfully Updated");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        clearAllFields();
                        Refresh();
                    }

                } catch (NullPointerException ne) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Invalid Appointment");
                    alert.setContentText(updateErrorMessage);
                    alert.showAndWait();
                }

            } catch (SQLException se) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
                alert.show();
            } catch (NullPointerException ne) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText(updateErrorMessage);
                alert.showAndWait();
            }
        }
    }

    /**
     *  Shows all appointments
     */
    @FXML
    void selectedAllAppts() {
        if (scheduler_allAppointmentsRadio.isSelected()) {
            Refresh();
        }
    }

    /**
     *  shows filtered appointments by Week
     */
    @FXML
    void selectedByWeek() {
        Refresh();
    }

    /**
     * shows filtered appointments by month
     */
    @FXML
    void selectedByMonth() {
        Refresh();
    }

    /**
     * Populates Text fields when table view item is selected
     */
    @FXML
    void onSelectedTableView() {
        Appointment selected = scheduler_tableView.getSelectionModel().getSelectedItem();

        if (selected != null) {
            // Convert selected timestamp to localDatetime
            LocalDateTime startDateTime = selected.getStart().toLocalDateTime();
            LocalDateTime endDateTime = selected.getEnd().toLocalDateTime();
            // Separate the date from datetime
            LocalDate startDate = startDateTime.toLocalDate();
            // Separate the time from the date time
            LocalTime startTime = startDateTime.toLocalTime();
            LocalTime endTime = endDateTime.toLocalTime();

            // Populate the fields
            scheduler_appointmentIDTF.setText(String.valueOf(selected.getId()));
            scheduler_titleTF.setText(selected.getTitle());
            scheduler_descTF.setText(selected.getDescription());
            scheduler_locationTF.setText(selected.getLocation());
            scheduler_typeCombo.setValue(selected.getType());
            scheduler_datePicker.setValue(startDate);

            for (Record customer : customers) {
                if (customer.getId() != selected.getCustomer_id()) continue;
                scheduler_customerCombo.setValue(customer);
            }

            for (User user : users) {
                if (user.getId() != selected.getUser_id()) continue;
                scheduler_userCombo.setValue(user);
            }

            for (Contact contact : contacts) {
                if (contact.getId() != selected.getContact_id()) continue;
                scheduler_contactCombo.setValue(contact);
            }
            for (LocalTime time : scheduler_startTCombo.getItems()) {
                if (startTime.equals(time)){
                    scheduler_startTCombo.setValue(startTime);
                    break;
                }
                else {
                    scheduler_startTCombo.setPromptText("Time not available");
                }
            }
            for (LocalTime time : scheduler_endTCombo.getItems()) {
                if (endTime.equals(time)){
                    scheduler_endTCombo.setValue(endTime);
                    break;
                }
                else {
                    scheduler_endTCombo.setPromptText("Time not available");
                }
            }
        }

    }

    /**
     *  Exit app
     * @param event exits the app when exit button is pressed
     */
    @FXML
    void exitApp(ActionEvent event) {
        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     *  To Records page
     * @param event goes to records screen
     * @throws IOException handles scene errors
     */
    @FXML
    void toRecords(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("records.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1031, 615);
        stage.setTitle("MyOffice");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *  To Reports page
     * @param event goes to reports screen
     * @throws IOException handles scene errors
     */
    @FXML
    void toReports(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("reports.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1030, 543);
        stage.setTitle("MyOffice");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Called to initialize a controller after its root element has been completely processed.
     *
     * @param url location - The location used to resolve relative paths for the root object,
     *                or null if the location is not known. *Java8 docs*
     * @param resourceBundle resources - The resources used to localize the root object,
     *                        or null if the root object was not localized. *Java8 docs*
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Open: 8 am EST
        LocalTime startEST = LocalTime.of(8, 0);
        // Convert to ZonedDT for conversion to Local
        ZonedDateTime easternStartZDT = ZonedDateTime.of(LocalDate.now(), startEST, easternZone);
        // Convert to Local ZDT
        ZonedDateTime easternStartToLocal = easternStartZDT.withZoneSameInstant(localZone);
        // Convert local ZDT to local LocalTime
        LocalTime startLocal = easternStartToLocal.toLocalTime();

        // Close: 10 pm EST
        LocalTime endEST = LocalTime.of(22, 0);
        // Convert to ZDT for conversion to local
        ZonedDateTime easternEndZDT = ZonedDateTime.of(LocalDate.now(), endEST, easternZone);
        // Convert to local ZDT
        ZonedDateTime easternEndToLocal = easternEndZDT.withZoneSameInstant(localZone);
        // Convert local ZDT to local LocalTime
        LocalTime endLocal = easternEndToLocal.toLocalTime();

        scheduler_timeZoneLbl.setText(TimeZone.getDefault().getDisplayName());
        Refresh();
        scheduler_contactCombo.setItems(contacts);
        scheduler_typeCombo.setItems(types);
        scheduler_customerCombo.setItems(customers);
        onCustomerSelect();
        scheduler_userCombo.setItems(users);
        onUserSelect();

        while (startLocal.isBefore(endLocal.plusSeconds(1))) {
            scheduler_startTCombo.getItems().add(startLocal);
            scheduler_endTCombo.getItems().add(startLocal);
            startLocal = startLocal.plusMinutes(15);
        }

        // Disable the days to select an appointment on the picker before today's date
        scheduler_datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.isBefore(today));
            }
        });

    }

    /**
     *  When the Customer ID combo box is selected, the Cell view shows the Customer ID
     *  and the Customer name
     * <p>
     *  The button view shows just the customers ID
     */
    public void onCustomerSelect() {
        scheduler_customerCombo.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Record> call(ListView<Record> recordListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Record item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText("[" + item.getId() + "] " + item.getName());
                        }
                    }
                };
            }
        });

        scheduler_customerCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Record item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(String.valueOf(item.getId()));
                } else {
                    setText(null);
                }
            }
        });
    }
    /**
     *  When the User ID combo box is selected, the Cell view shows the User ID
     *  and the Username
     * <p>
     *  The button view shows just the user ID
     */
    public void onUserSelect() {
        scheduler_userCombo.setCellFactory(new Callback<>() {
            @Override
            public ListCell<User> call(ListView<User> userListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText("[" + item.getId() + "] " + item.getUsername());
                        }
                    }
                };
            }
        });

        scheduler_userCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(String.valueOf(item.getId()));
                } else {
                    setText(null);
                }
            }
        });
    }

    /**
     *
     * Refreshes the table view for live adds, updates and delete changes to the database
     * LAMBDA EXPRESSIONS -- 2 Lambda expressions used for code efficiency to filter appointments. One filters based on
     * the dates within the month of the current month. The other filters for the week based on the current day.
     *
     */
    public void Refresh() {
        ObservableList<Appointment> allAppointments = Appointment.getAllAppointments();
        FilteredList<Appointment> filtered = new FilteredList<>(allAppointments);

        scheduler_AppointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        scheduler_titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        scheduler_descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        scheduler_locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        scheduler_contactCol.setCellValueFactory(new PropertyValueFactory<>("contact_name"));
        scheduler_typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        scheduler_startDTCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        scheduler_endDTCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        scheduler_customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        scheduler_UserIDCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        // filter for selected month radio button
        if (scheduler_monthRadio.isSelected()) {
            LocalDate now = LocalDate.now();
            LocalDate thisMonth = now.plusMonths(1);

            // lambda expression used for code efficiency
            filtered.setPredicate(appt -> {
                LocalDateTime toLocalDateTime = appt.getStart().toLocalDateTime();
                LocalDate getDate = toLocalDateTime.toLocalDate();

                return getDate.isAfter(now.minusDays(1)) && getDate.isBefore(thisMonth);
            });

            scheduler_tableView.setItems(filtered);
            if (scheduler_tableView == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No Appointments This Month!");
                alert.showAndWait();
            }

        }
        // filter for selected week radio button
        else if (scheduler_weekRadio.isSelected()) {
            LocalDate now = LocalDate.now();
            LocalDate thisWeek = now.plusWeeks(1);

            // lambda expression used for code efficiency
            filtered.setPredicate(appt -> {
                LocalDateTime toLocalDateTime = appt.getStart().toLocalDateTime();
                LocalDate getDate = toLocalDateTime.toLocalDate();

                return getDate.isAfter(now.minusDays(1)) && getDate.isBefore(thisWeek);
            });

            scheduler_tableView.setItems(filtered);

            if (scheduler_tableView == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No Appointments This Week!");
                alert.showAndWait();
            }
        }
        else {
            scheduler_tableView.setItems(allAppointments);
        }

    }

    /**
     *  Clears all text fields and combo boxes
     *
     */
    public void clearAllFields() {
        scheduler_appointmentIDTF.clear();
        scheduler_titleTF.clear();
        scheduler_descTF.clear();
        scheduler_locationTF.clear();
        scheduler_contactCombo.getSelectionModel().clearSelection();
        scheduler_typeCombo.getSelectionModel().clearSelection();
        scheduler_startTCombo.getSelectionModel().clearSelection();
        scheduler_endTCombo.getSelectionModel().clearSelection();
        scheduler_datePicker.getEditor().clear();
        scheduler_customerCombo.getSelectionModel().clearSelection();
        scheduler_userCombo.getSelectionModel().clearSelection();

    }

    /**
     *  Validates if appointment text fields are empty
     * @return true if all text fields have values in them and false if any of them are empty;
     *  Adds an error message and sends an Alert to the UI if any of the fields are empty
     */
    public boolean isValidAppointment() {

        String title = scheduler_titleTF.getText();
        String desc = scheduler_descTF.getText();
        String loc = scheduler_locationTF.getText();
        Contact contact = scheduler_contactCombo.getValue();
        String type = scheduler_typeCombo.getValue();
        Record customer = scheduler_customerCombo.getValue();
        User user = scheduler_userCombo.getValue();

        String errorMessage = "";

        if (title == null || title.isEmpty()) {
            errorMessage += "Please enter a Title. \n";
        }

        if (desc == null || desc.isEmpty()) {
            errorMessage += "Please enter a Description. \n";
        }

        if (loc == null || loc.isEmpty()) {
            errorMessage += "Please enter a Location. \n";
        }

        if (contact == null) {
            errorMessage += "Please select a Contact. \n";
        }

        if (type == null || type.isEmpty()) {
            errorMessage += "Please select a Type. \n";
        }

        if (customer == null) {
            errorMessage += "Please select a Customer. \n";
        }

        if (user == null) {
            errorMessage += "Please select a User. \n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Appointment");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }

    /**
     *  Checks if there is appointment overlap
     * @param start takes the appointment start time
     * @param end takes the appointment end time
     * @param Appointment_ID takes the appointment id
     * @return true if the start and end times overlap with a time in the database of the same date.
     *          false if there is no overlap in times and dates
     * throws SQLException handles database errors
     * throws Exception catches any other errors other than database
     */
    public boolean isOverlap(LocalDateTime start, LocalDateTime end, int Appointment_ID) {
        String query = "SELECT * FROM APPOINTMENTS " +
                "WHERE (? >= Start AND ? < End OR ? > Start AND ? <= End OR ? <= Start AND ? >= End) " +
                "AND Appointment_ID != ?";

        try {
            PreparedStatement statement = JDBC.conn.prepareStatement(query);
            statement.setTimestamp(1, Timestamp.valueOf(start));
            statement.setTimestamp(2, Timestamp.valueOf(start));
            statement.setTimestamp(3, Timestamp.valueOf(end));
            statement.setTimestamp(4, Timestamp.valueOf(end));
            statement.setTimestamp(5, Timestamp.valueOf(start));
            statement.setTimestamp(6, Timestamp.valueOf(end));
            statement.setInt(7, Appointment_ID);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "SQL Error: " + se.getMessage());
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
            alert.show();
        }
        return false;
    }

}