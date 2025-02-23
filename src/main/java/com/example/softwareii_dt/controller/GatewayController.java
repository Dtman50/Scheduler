package com.example.softwareii_dt.controller;

import com.example.softwareii_dt.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *  Gateway Controller for gateway.fxml
 *  Gateway station after successful login
 *  @author dariustaylor
 */
public class GatewayController {

    // Stage variable for page switch
    public Stage stage;
    // Scene variable for page switch
    public Scene scene;
    // FXML Loader variable for page switch
    public FXMLLoader fxmlLoader;

    /**
     *  Exits App
     * @param event closes App when exit button is pressed
     */
    @FXML
    void exit(ActionEvent event) {
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
     *  To Reports Page
     * @param event goes to reports screen when button pressed
     * @throws IOException handles scene errors
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

}
