package com.example.softwareii_dt.controller;

import com.example.softwareii_dt.Appointment;
import com.example.softwareii_dt.MainApplication;
import com.example.softwareii_dt.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 *  Reports Controller for reports.fxml
 *  Tabbed Reports Views
 *  @author dariustaylor
 */
public class ReportsController implements Initializable {

    public FXMLLoader fxmlLoader;
    public Scene scene;
    public Stage stage;

    @FXML
    private TableView<Appointment> reports_totalContactsView;

    @FXML
    private TableView<Report> reports_totalCustomersView;

    @FXML
    private TableView<Appointment> reports_totalUserView;

    @FXML
    private TableColumn<Appointment, Integer> totalContact_AppointmentIDCol;

    @FXML
    private TableColumn<Appointment, String> totalContact_contactCol;

    @FXML
    private TableColumn<Appointment, Integer> totalContact_customerIDCol;

    @FXML
    private TableColumn<Appointment, String> totalContact_descCol;

    @FXML
    private TableColumn<Appointment, Timestamp> totalContact_endCol;

    @FXML
    private TableColumn<Appointment, Timestamp> totalContact_startCol;

    @FXML
    private TableColumn<Appointment, String> totalContact_titleCol;

    @FXML
    private TableColumn<Appointment, String> totalContact_typeCol;

    @FXML
    private TableColumn<Report, Timestamp> totalCustomer_monthCol;

    @FXML
    private TableColumn<Report, Integer> totalCustomer_totalCol;

    @FXML
    private TableColumn<Report, String> totalCustomer_typeCol;

    @FXML
    private TableColumn<Appointment, Integer> totalUser_appointmentIDCol;

    @FXML
    private TableColumn<Appointment, Integer> totalUser_customerIDCol;

    @FXML
    private TableColumn<Appointment, String> totalUser_descCol;

    @FXML
    private TableColumn<Appointment, Timestamp> totalUser_endCol;

    @FXML
    private TableColumn<Appointment, Timestamp> totalUser_startCol;

    @FXML
    private TableColumn<Appointment, String> totalUser_titleCol;

    @FXML
    private TableColumn<Appointment, String> totalUser_typeCol;

    @FXML
    private TableColumn<Appointment, String> totalUser_userCol;

    /**
     *  Exits App
     * @param event exits app when button is pressed
     */
    @FXML
    void exitApp(ActionEvent event) {
        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     *  To Records Page
     * @param event goes to records screen when button pressed
     * @throws IOException handles scene errors
     */
    @FXML
    void toRecords(ActionEvent event) throws IOException {
        fxmlLoader = new FXMLLoader(MainApplication.class.getResource("records.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(), 1031, 615);
        stage.setTitle("MyOffice");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *  To Scheduler Page
     * @param event goes to scheduler screen when button pressed
     * @throws IOException handles scene errors
     */
    @FXML
    void toScheduler(ActionEvent event) throws IOException {
        fxmlLoader = new FXMLLoader(MainApplication.class.getResource("scheduler.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(), 1522, 677);
        stage.setTitle("MyOffice");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Called to initialize a controller after its root element has been completely processed.
     *      *  Shows the full report views based on the tab selected
     *
     * @param url location - The location used to resolve relative paths for the root object,
     *                or null if the location is not known. *Java8 docs*
     * @param resourceBundle resources - The resources used to localize the root object,
     *                        or null if the root object was not localized. *Java8 docs*
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Total Appointments By Type/Month
        totalCustomer_monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        totalCustomer_typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalCustomer_totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

        reports_totalCustomersView.setItems(Report.getAllTotalByType_Month());

        // All Appointments By Contact
        totalContact_contactCol.setCellValueFactory(new PropertyValueFactory<>("contact_name"));
        totalContact_AppointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalContact_titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        totalContact_typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalContact_descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        totalContact_startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        totalContact_endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        totalContact_customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));

        reports_totalContactsView.setItems(Appointment.getAllByContact());

        // All Appointments By User
        totalUser_userCol.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        totalUser_appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalUser_titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        totalUser_typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalUser_descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        totalUser_startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        totalUser_endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        totalUser_customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));

        reports_totalUserView.setItems(Appointment.getAllByUser());
    }
}
