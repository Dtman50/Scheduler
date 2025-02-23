package com.example.softwareii_dt;

import com.example.softwareii_dt.JDBC_helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main Application
 * Starts Application
 * @author dariustaylor
 */
public class MainApplication extends Application {
    /**
     * Primary stage for this application
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException handles errors with the scene Loader
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("MyOffice");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method for the whole application.
     * @param args Starts a connection with the database and then
     *             launches the app upon running the app and closes the connection upon closing the app.
     */
    public static void main(String[] args){
        JDBC.makeConnection();
        launch();
        JDBC.closeConnection();
    }
}
























