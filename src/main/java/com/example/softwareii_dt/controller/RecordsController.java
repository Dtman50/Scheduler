package com.example.softwareii_dt.controller;

import com.example.softwareii_dt.*;
import com.example.softwareii_dt.JDBC_helper.JDBC;
import com.example.softwareii_dt.Record;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *  Record Controller for records.fxml
 *  Holds Customer Records
 *  @author dariustaylor
 */
public class RecordsController implements Initializable {

    public Stage stage;
    public Scene scene;
    public FXMLLoader fxmlLoader;
    private final Connection conn = JDBC.conn;
    public PreparedStatement statement;
    // List of all countries
    public final ObservableList<Countries> countries = Countries.getAllCountries();
    // List of all States and Divisions
    public final ObservableList<FirstLevelDivisions> divisions = FirstLevelDivisions.getAllDivisions();
    // List of Statuses
    public final ObservableList<String> statuses = FXCollections.observableArrayList("Bronze", "Silver", "Gold", "Diamond", "Platinum");

    @FXML
    private RadioButton basicCustomerRadio;

    @FXML
    private RadioButton premiumCustomerRadio;

    @FXML
    private ToggleGroup classificationGroup;

    @FXML
    private TableView<Record> records_tableView;

    @FXML
    private TableColumn<Record, String> recordsView_classCol;

    @FXML
    private TableColumn<Record, Integer> recordsView_IDcol;

    @FXML
    private TableColumn<Record, String> recordsView_addressCol;

    @FXML
    private TableColumn<Countries, String> recordsView_countryCol;

    @FXML
    private TableColumn<Record, String> recordsView_nameCol;

    @FXML
    private TableColumn<Record, Long> recordsView_phoneCol;

    @FXML
    private TableColumn<Record, Integer> recordsView_postalCol;

    @FXML
    private TableColumn<FirstLevelDivisions, String> recordsView_stateCol;

    @FXML
    private ComboBox<Countries> records_countryCombo;

    @FXML
    private ComboBox<FirstLevelDivisions> records_stateCombo;

    @FXML
    private ComboBox<String> records_statusCombo;

    @FXML
    private TextField records_search;

    @FXML
    private TextField records_ID_TF;

    @FXML
    private TextField records_address_TF;

    @FXML
    private TextField records_name_TF;

    @FXML
    private TextField records_phoneNum_TF;

    @FXML
    private TextField records_postal_TF;

    /**
     * When country combo box is pressed, the Cell view shows the country and the Button View shows the country
     *              Based on which country is selected, the state/divisions combo box is populated.
     * @throws NullPointerException if no country is selected
     */
    @FXML
    void onCountrySelect() throws NullPointerException {
        ObservableList<FirstLevelDivisions> filtered = FXCollections.observableArrayList();

        try {
            int selected = records_countryCombo.getSelectionModel().getSelectedItem().getId();

            records_countryCombo.setCellFactory(new Callback<>() {
                @Override
                public ListCell<Countries> call(ListView<Countries> listView) {
                    return new ListCell<>() {
                        @Override
                        protected void updateItem(Countries item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setText(null);
                            } else {
                                setText(item.getCountryName());
                            }
                        }
                    };
                }
            });

            records_countryCombo.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Countries item, boolean empty) {
                    super.updateItem(item, empty);

                    if(item != null) {
                        setText(item.getCountryName());
                    } else {
                        setText(null);
                    }
                }
            });

            for (FirstLevelDivisions divs : divisions) {
                if (divs.getCountry_id() == selected) {
                    filtered.add(divs);
                }
            }

            records_stateCombo.setItems(filtered);

        } catch (NullPointerException ne) {
            //Do Nothing
        }

    }

    /**
     *
     * When the state/division combo box is selected, the Cell view shows the state/division
     *              and the Button View shows the state/Division.
     */
    @FXML
    void onStateSelect() {

        records_stateCombo.setCellFactory(new Callback<>() {
            @Override
            public ListCell<FirstLevelDivisions> call(ListView<FirstLevelDivisions> firstLevelDivisionsListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(FirstLevelDivisions item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getDivisionName());
                        }
                    }
                };
            }
        });
        records_stateCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(FirstLevelDivisions item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(item.getDivisionName());
                } else {
                    setText(null);
                }
            }
        });
    }

    /**
     *  on Basic Radio Button Selected
     *  Disables the status ComboBox.
     */
    @FXML
    void onBasicSelected() {
        records_statusCombo.setDisable(true);
    }

    /**
     *  on Premium Radio Button Selected
     *  Enables the status ComboBox.
     */
    @FXML
    void onPremiumSelected() {
        records_statusCombo.setDisable(false);
    }

    /**
     *  on Enter key pressed, searches view by id or name based on what was entered
     *     and displays result.
     * <p>
     *  RUNTIME ERROR : When entering letters into the search box that do not match any of the
     *  results, you get a EXCEPTION error and that was corrected by
     *  surrounding the code block in a try/catch statement and catching the error
     *  but just ignoring it. So then you don't receive the error in the debugger.
     * </p>
     */
    @FXML
    void onSearchRecord() {
        try {
            ObservableList<Record> searched = Clients.lookupRecord(records_search.getText());

            if (searched.size() == 0) {
                int id = Integer.parseInt(records_search.getText());
                Record record = Clients.lookupRecord(id);
                if (record != null) {
                    searched.add(record);
                }
                if (searched.size() == 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "No Results. Try again.");
                    alert.showAndWait();
                }
            }
            records_tableView.setItems(searched);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No Results. Try again.");
            alert.show();
        }
    }

    /**
     *  To scheduler page
     * @param event to scheduler if the button is pressed
     * @throws IOException handles errors with the loading scene
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
     *  To reports Page
     * @param event to reports page when button is pressed
     * @throws IOException handles errors with loading the scene
     */
    @FXML
    void toReports(ActionEvent event) throws IOException {
        fxmlLoader = new FXMLLoader(MainApplication.class.getResource("reports.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(), 1030, 543);
        stage.setTitle("MyOffice");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     *  Clears all fields when button is pressed
     */
    @FXML
    void clearAllFields() {
        clearFields();
    }

    /**
     *  Populates text fields when customer is selected.
     *  Gets the selected Customer from the table view and populates the data into the fields of the form
     */
    @FXML
    void onSelected() {
        Record selected = records_tableView.getSelectionModel().getSelectedItem();
        System.out.println(selected.getClass().getName());
        if (selected.getClassification().equals("Basic")) {
            basicCustomerRadio.setSelected(true);
        } else if (selected.getClassification().equals("Premium")) {
            premiumCustomerRadio.setSelected(true);
        }
        if (selected.getClass().getName().equals("com.example.softwareii_dt.PremiumRecord")) {
            PremiumRecord premiumSelected = (PremiumRecord) selected;
            records_ID_TF.setText(String.valueOf(premiumSelected.getId()));
            records_name_TF.setText(premiumSelected.getName());
            records_address_TF.setText(premiumSelected.getAddress());
            records_postal_TF.setText(String.valueOf(premiumSelected.getPostal()));
            records_phoneNum_TF.setText(String.valueOf(premiumSelected.getPhone()));
            records_statusCombo.setValue(premiumSelected.getStatus());
            records_statusCombo.setDisable(false);
        } else if (selected.getClass().getName().equals("com.example.softwareii_dt.BasicRecord")) {
            BasicRecord basicSelected = (BasicRecord) selected;
            records_ID_TF.setText(String.valueOf(basicSelected.getId()));
            records_name_TF.setText(basicSelected.getName());
            records_address_TF.setText(basicSelected.getAddress());
            records_postal_TF.setText(String.valueOf(basicSelected.getPostal()));
            records_phoneNum_TF.setText(String.valueOf(basicSelected.getPhone()));
            records_statusCombo.setDisable(true);
        }

        for (FirstLevelDivisions divs : divisions) {
            if (divs.getId() != selected.getDivision_ID()) continue;
            records_stateCombo.setValue(divs);

            for (Countries country : countries) {
                if (country.getId() != divs.getCountry_id()) continue;
                records_countryCombo.setValue(country);
            }
        }

    }

    /**
     *
     *  Adds a record to the database when the add button is pressed and send an alert to the UI
     *              when the customer is added.
     * throws SQLException if there are errors with the database.
     */
    @FXML
    void addRecord() {
        String query = "INSERT INTO CUSTOMERS(Customer_Name, Address, Postal_Code, Phone, Division_ID, Classification, Status) VALUES (?,?,?,?,?,?,?)";

        try {
            statement = conn.prepareStatement(query);
            statement.setString(1, records_name_TF.getText());
            statement.setString(2, records_address_TF.getText());
            statement.setString(3, records_postal_TF.getText());
            statement.setString(4, records_phoneNum_TF.getText());
            statement.setInt(5, records_stateCombo.getSelectionModel().getSelectedItem().getId());
            if (basicCustomerRadio.isSelected()) {
                statement.setString(6, "Basic");
                statement.setString(7, null);
            } else if (premiumCustomerRadio.isSelected()) {
                statement.setString(6, "Premium");
                statement.setString(7, records_statusCombo.getValue());
            }

            statement.executeUpdate();


            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer Added!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                clearAllFields();
                Refresh();
            }

        } catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        }

    }

    /**
     *  Updates a record in the database with the populated fields on the form when the update button is pressed and send an alert to the UI
     *              when the customer is updated.
     * throws SQLException if there are errors with the database.
     */
    @FXML
    void updateRecord() {
        String classification = "";
        String clientStatus = records_statusCombo.getValue();
        if (basicCustomerRadio.isSelected()) {
            classification = "Basic";
            clientStatus = null;
        } else if (premiumCustomerRadio.isSelected()){
            classification = "Premium";
            clientStatus = records_statusCombo.getValue();
        }
        String query = "UPDATE CUSTOMERS " +
                "SET" +
                " Customer_Name = '" + records_name_TF.getText() +
                "', Address = '" + records_address_TF.getText() +
                "', Postal_Code = '" + records_postal_TF.getText() +
                "', Phone = '" + records_phoneNum_TF.getText() +
                "', Division_ID = '" + records_stateCombo.getSelectionModel().getSelectedItem().getId() +
                "', Classification = '" + classification +
                "', Status = '" + clientStatus +
                "' WHERE Customer_ID = " + records_ID_TF.getText();

        try {
            statement = JDBC.conn.prepareStatement(query);
            statement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer Updated!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                clearAllFields();
                Refresh();
            }

        } catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + se.getMessage());
            alert.show();
        }

    }

    /**
     * Deletes a record from the database when the delete button is pressed. Sends a confirmation alert to confirm
     *             the delete and send an alert to the UI when the record is deleted.
     * throws SQLException if there are errors with the database.
     */
    @FXML
    void deleteRecord() {
        Record selected = records_tableView.getSelectionModel().getSelectedItem();
        int id = selected.getId();
        String query = "DELETE FROM CUSTOMERS WHERE Customer_ID = " + id;
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You sure you want to Delete?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                statement = JDBC.conn.prepareStatement(query);
                statement.executeUpdate();
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Customer id: " + id + " Deleted!");
                alert1.show();

                clearAllFields();
                Refresh();

            }

        }catch (SQLException se) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "This customer has associated appointments. You must delete the appointments before you can delete the customer");
            alert.show();
        }

    }

    /**
     *  Exits App
     * @param event exits the app when the button is pressed
     */
    @FXML
    void exitApp(ActionEvent event) {
        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Refreshes the table view for live adds, updates and delete changes to the database
     *
     */
    public void Refresh(){
        recordsView_IDcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        recordsView_addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        recordsView_nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        recordsView_phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        recordsView_postalCol.setCellValueFactory(new PropertyValueFactory<>("postal"));
        recordsView_countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        recordsView_stateCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        recordsView_classCol.setCellValueFactory(new PropertyValueFactory<>("classification"));

        ObservableList<Record> records = Clients.getAllRecords();
        records_tableView.setItems(records);

    }

    /**
     *  Clears all fields and combo boxes in the form
     */
    public void clearFields() {
        records_ID_TF.clear();
        records_name_TF.clear();
        records_address_TF.clear();
        records_postal_TF.clear();
        records_phoneNum_TF.clear();
        records_countryCombo.getSelectionModel().clearSelection();
        records_stateCombo.getSelectionModel().clearSelection();
        records_statusCombo.getSelectionModel().clearSelection();
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
        Refresh();
        records_countryCombo.setItems(countries);
        records_statusCombo.setItems(statuses);

    }

}