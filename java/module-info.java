module com.example.lost_and_found {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jfr;
    requires java.desktop;
    requires mysql.connector.java;


    opens com.example.lost_and_found to javafx.fxml;
    exports com.example.lost_and_found;
}