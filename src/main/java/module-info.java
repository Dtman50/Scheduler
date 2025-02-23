module com.example.softwareii_dt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.softwareii_dt to javafx.fxml;
    exports com.example.softwareii_dt;
    exports com.example.softwareii_dt.controller;
    opens com.example.softwareii_dt.controller to javafx.fxml;
}