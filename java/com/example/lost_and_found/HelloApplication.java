package com.example.lost_and_found;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class    HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1290, 690);
        scene.getStylesheets().add(HelloApplication.class.getResource("styleLogin.css").toExternalForm());
        stage.getIcons().add(
                new javafx.scene.image.Image(
                        HelloApplication.class.getResourceAsStream("icons/app.png")
                )
        );
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle("FindU");
        stage.setScene(scene);

        stage.show();
    }
}
