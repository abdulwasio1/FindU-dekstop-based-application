package com.example.lost_and_found;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class adminMatchControl implements Initializable {
    @FXML
    public VBox displayBox;

    @FXML
    private Button exit;

    @FXML
    private Button home;

    @FXML
    private Button manageItems;

    @FXML
    private Button manageMatches;

    @FXML
    private Button manageUsers;

    @FXML
    private Button profile;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchField;

    @FXML
    private Label usernameLabel;

    @FXML
    Label emptyDisplay;

    private Button[] homeButtons;

    adminDashboardControl dashboard = new adminDashboardControl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeButtons = new Button[]{home,manageItems,manageMatches,manageUsers,profile};
        selectionUIHelper.setSelected(manageMatches , homeButtons);
        usernameLabel.setText(adminSession.getAdminName());
        try {
            displayMatches();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void displayMatches() throws Exception{
        displayBox.getChildren().clear();
        queriesAdmin queries = new queriesAdmin();
        ObservableList<matchModel> matches = queries.displayMatchesByAdmin();
        if (matches.isEmpty()){
            emptyDisplay.setVisible(true);
        }else {
            emptyDisplay.setVisible(false);
        }
         for (matchModel model : matches) {
                displayBox.getChildren().add(createRow(model));
         }

    }
    private HBox createRow(matchModel model) throws IOException {

        ImageView lostImg = new ImageView();
        lostImg.setFitHeight(42);
        lostImg.setFitWidth(42);
        lostImg.setPreserveRatio(false);

        String lostImgPath = model.getLostImagePath();
        if (lostImgPath != null && !lostImgPath.isEmpty() && !lostImgPath.equals("null")) {
            try {
                java.io.File imgFile = new java.io.File(
                        System.getProperty("user.home") + "/FindU/uploads/" + lostImgPath);
                if (imgFile.exists()) {
                    lostImg.setImage(new Image(imgFile.toURI().toString()));
                } else {
                    loadPlaceholder(lostImg);
                }
            } catch (Exception e) {
                loadPlaceholder(lostImg);
            }
        } else {
            loadPlaceholder(lostImg);
        }

        Label lostItem = new Label(model.getLostItemName());
        lostItem.getStyleClass().add("item-name");

        Label lostContact = new Label(model.getLostContact());
        lostContact.getStyleClass().add("item-tag");

        VBox lostInfo = new VBox(3, lostItem, lostContact);
        lostInfo.setAlignment(Pos.CENTER_LEFT);
        HBox box1 = new HBox(12, lostImg, lostInfo);
        box1.setAlignment(Pos.CENTER_LEFT);

        // ── Found Item ──
        ImageView foundImg = new ImageView();
        foundImg.setFitHeight(42);
        foundImg.setFitWidth(42);
        foundImg.setPreserveRatio(false);

        String foundImgPath = model.getFoundImagePath();
        if (foundImgPath != null && !foundImgPath.isEmpty() && !foundImgPath.equals("null")) {
            try {
                java.io.File imgFile = new java.io.File(
                        System.getProperty("user.home") + "/FindU/uploads/" + foundImgPath);
                if (imgFile.exists()) {
                    foundImg.setImage(new Image(imgFile.toURI().toString()));
                } else {
                    loadPlaceholder(foundImg);
                }
            } catch (Exception e) {
                loadPlaceholder(foundImg);
            }
        } else {
            loadPlaceholder(foundImg);
        }

        Label foundItem = new Label(model.getFoundItemName());
        foundItem.getStyleClass().add("item-name");

        Label foundContact = new Label(model.getFoundContact());
        foundContact.getStyleClass().add("item-tag");

        VBox foundInfo = new VBox(3, foundItem, foundContact);
        foundInfo.setAlignment(Pos.CENTER_LEFT);
        HBox box2 = new HBox(12, foundImg, foundInfo);
        box2.setAlignment(Pos.CENTER_LEFT);

        Label status = new Label(model.getStatus() != null ? model.getStatus() : "pending");
        status.getStyleClass().add(
                model.getStatus().equals("pending") ? "badge-lost" : "badge-found"
        );

        ProgressBar bar = new ProgressBar();
        Label scoreLabel = new Label( model.getScore() + "%");

        scoreLabel.getStyleClass().add("item-date");
        bar.setProgress(model.getScore()/100.0);

        bar.getStyleClass().add("progress-bar");
        bar.setPrefWidth(100);
        bar.setPrefHeight(8);

        Button eyeButton = new Button();
        Image icon = new Image(getClass().getResourceAsStream("/com/example/lost_and_found/icons/eye.png"));
        ImageView imgView = new ImageView(icon);
        imgView.setFitWidth(15);
        imgView.setFitHeight(15);

        eyeButton.setGraphic(imgView);
        eyeButton.getStyleClass().add("eyeButton");


        eyeButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lost_and_found/editMatchAdmin.fxml"));
                Parent load = loader.load();
                matchViewAdminControl control = loader.getController();
                control.seeItem(model, load);
                displayMatches();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        Button deleteButton = new Button();
        Image deteteIcon = new Image(getClass().getResourceAsStream("/com/example/lost_and_found/icons/trash.png"));
        ImageView deleteImage = new ImageView(deteteIcon);
        deleteImage.setFitWidth(15);
        deleteImage.setFitHeight(15);
        deleteButton.setGraphic(deleteImage);
        deleteButton.getStyleClass().add("deleteButton");

        deleteButton.setOnAction(e -> {
            try {
                deleteMatch(model);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        HBox box3 = new HBox(9, status, bar , scoreLabel , eyeButton , deleteButton);
        box3.setAlignment(Pos.CENTER_RIGHT);

        box1.setPrefWidth(300);
        box2.setPrefWidth(280);
        box3.setPrefWidth(300);

        // ── Main Row ──
        HBox box = new HBox(40, box1, box2, box3);
        box.getStyleClass().add("row-set");
        box.setPadding(new Insets(14));
        box.setMaxWidth(Double.MAX_VALUE);

        return box;
    }
    private void loadPlaceholder(ImageView img) {
        try {
            img.setImage(new Image(getClass().getResourceAsStream(
                    "/com/example/lost_and_found/icons/placeholder.png")));
        } catch (Exception e) {
            // ignore
        }
    }

    private void deleteMatch(matchModel model) throws Exception {
        queriesAdmin queries = new queriesAdmin();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conformation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete the Match?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            int value = queries.deleteMatchAdmin(model);
            if (value>0){
                Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                confirm.setTitle("Success");
                confirm.setHeaderText(null);
                confirm.setContentText("Match Deleted Successfully");
                confirm.showAndWait();
                displayMatches();
            }else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to delete Match, Try Again!!");
                errorAlert.showAndWait();
            }

        } else {
            System.out.println("Action cancelled");
        }

    }


    public void editMatch(matchModel model) throws Exception{

    }

    @FXML
    void clickHome(ActionEvent event) throws Exception {
        dashboard.switchScene(event , "adminDash.fxml");
    }

    @FXML
    void clickManageItems(ActionEvent event) throws Exception {
        dashboard.switchScene(event , "adminItems.fxml");
    }

    @FXML
    void clickManageMatches(ActionEvent event) throws Exception {
        dashboard.switchScene(event , "adminMatch.fxml");
    }

    @FXML
    void clickManageUsers(ActionEvent event) throws Exception {
        dashboard.switchScene(event ,"usersAdmin.fxml");
    }

    @FXML
    void clickProfile(ActionEvent event) throws Exception{
        dashboard.switchScene(event , "adminProfile.fxml");
    }

    public void onClickSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase().trim();

        displayBox.getChildren().clear();

        try {
            if (searchText.isEmpty()) {
                displayMatches();
                return;
            }

            queriesAdmin query = new queriesAdmin();
            ObservableList<matchModel> matches = query.displayMatchesByAdmin();

            for (matchModel match : matches) {

                if (
                        match.getLostItemName().toLowerCase().contains(searchText) ||
                                match.getFoundItemName().toLowerCase().contains(searchText) ||
                                match.getLostLocation().toLowerCase().contains(searchText) ||
                                match.getFoundLocation().toLowerCase().contains(searchText)
                ) {
                    displayBox.getChildren().add(createRow(match));
                }else {
                    emptyDisplay.setVisible(true);
                }
            }

            emptyDisplay.setVisible(displayBox.getChildren().isEmpty());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void signOut(ActionEvent event) throws Exception{
        dashboard.signOut(event);
    }
}
