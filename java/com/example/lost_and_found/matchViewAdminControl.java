package com.example.lost_and_found;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class matchViewAdminControl implements Initializable {

    @FXML
    private TextField foundItem;

    @FXML
    private TextField foundLocation;

    @FXML
    private TextField lostLocation;

    @FXML
    private TextField lostItem;

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField lostContact;

    @FXML
    private TextField foundContact;


    @FXML
    private ChoiceBox<String> statusBox;


    matchModel currentModel;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusBox.getItems().addAll("Pending", "Verified" , "Claimed" , "Rejected");
//        statusBox.setValue("Pending");
    }

    @FXML
    void cancelChanges(ActionEvent event) {
        setFields(currentModel);
    }

    @FXML
    void closeView(ActionEvent event) {
        stage.close();
    }

    @FXML
    void onClickChanges(ActionEvent event) throws Exception {

        String seletedStatus = statusBox.getValue().toLowerCase();

        queriesAdmin queries = new queriesAdmin();

        int value = queries.statusUpdate(currentModel.getMatchId(), seletedStatus);
        if (value > 0) {
            currentModel.setStatus(seletedStatus);  // model update karo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Item Status updated successfully.");
            alert.setX(520);
            alert.setY(300);
            alert.showAndWait();
            stage.close();  // popup band karo
//            adminMatchControl control = new adminMatchControl();
//            control.displayMatches();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update. Try again.");
            alert.showAndWait();
        }
    }

    public void seeItem(matchModel model , Parent load)throws Exception{
        statusBox.setValue(model.getStatus());
        currentModel = model;
        pane.getStyleClass().add("matchPane");
        setFields(model);
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lost_and_found/viewMatch.fxml"));
//        Parent load = loader.load();

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

    private void setFields(matchModel model) {
        lostItem.setText(model.getLostItemName());
        lostLocation.setText(model.getLostLocation());
        lostContact.setText(model.getLostContact());
        foundContact.setText(model.getFoundContact());
        foundLocation.setText(model.getFoundLocation());
        foundItem.setText(model.getFoundItemName());
    }
}
