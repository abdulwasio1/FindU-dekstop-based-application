package com.example.lost_and_found;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class userViewControl implements Initializable {

    private Stage stage;

    @FXML
    private AnchorPane pane;

    @FXML
    private ChoiceBox<String> roleChoice;

    @FXML
    private TextField userEmail;

    @FXML
    private TextField userId;

    @FXML
    private TextField userName;

    @FXML
    private TextField userPassword;

    @FXML
    private TextField userPhone;

    private usersModel currentUser;

    private manageUsersAdmin users = new manageUsersAdmin();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleChoice.getItems().addAll("User" , "Admin");
        roleChoice.setValue("User");
    }

    @FXML
    void closeView(ActionEvent event) {
        stage.close();
    }

    @FXML
    void cancelChanges(ActionEvent event) {
        setFields(currentUser);  // original values restore
    }

    @FXML
    void onClickChanges(ActionEvent event) throws Exception {
        String selectedRole = roleChoice.getValue().toLowerCase();

        queriesAdmin queries = new queriesAdmin();
        int value = queries.updateUserRole(currentUser.getUser_id(), selectedRole);

        if (value > 0) {
            currentUser.setRole(selectedRole);  // model update karo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User role updated successfully.");
            alert.setX(520);
            alert.setY(300);
            alert.showAndWait();
            stage.close();  // popup band karo
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update. Try again.");
            alert.showAndWait();
        }
    }
    public void seeUser(usersModel model , Parent load)throws Exception{
        roleChoice.setValue(model.getRole());
        currentUser = model;
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
    public void setFields(usersModel model){
        userName.setText(model.getName());
        userId.setText(model.getS_id());
        userPassword.setText(model.getPassword());

        userPhone.setText(model.getPhone());
        userEmail.setText(model.getEmail());
//        u.setText(model.getFoundContact());
    }

}
