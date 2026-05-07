package com.example.lost_and_found;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class matchViewControl implements Initializable {

    @FXML
    private TextField foundContact;

    @FXML
    private TextField foundItem;

    @FXML
    private TextField foundLocation;

    @FXML
    private TextField lostContact;

    @FXML
    private TextField lostItem;

    @FXML
    private TextField lostLocation;

    @FXML
    AnchorPane pane;

    private Stage stage;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane.getStyleClass().add("matchPane");

    }

    public void seeItem(matchModel model , Parent load)throws Exception{
        pane.getStyleClass().add("matchPane");
        setFields(model);

        stage = new Stage();
        Scene scene = new Scene(load);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setX(415.5);
        stage.setY(200.5);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    public void closeView(ActionEvent event){
        stage.close();
    }
    public void setFields(matchModel model){
        lostItem.setText(model.getLostItemName());
        lostLocation.setText(model.getLostLocation());
        lostContact.setText(model.getLostContact());

        foundItem.setText(model.getFoundItemName());
        foundLocation.setText(model.getFoundLocation());
        foundContact.setText(model.getFoundContact());
    }
}
