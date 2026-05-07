package com.example.lost_and_found;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class matchControl extends userDashboardControl implements Initializable {
    @FXML
    private Button allItems;

    @FXML
    private Button exit;

    @FXML
    private Button home;

    @FXML
    private Button matches;

    @FXML
    private Button profile;

    @FXML
    private Button reportItem;

    @FXML
    private VBox matchBox;

    @FXML
    private Label emptyDisplay;

    @FXML
    private Label usernameLabel;

    Button[] buttons;

    ObservableList<matchModel> list;

    Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttons = new Button[]{home, reportItem, matches,profile ,exit , allItems};
        selectionUIHelper.setSelected(matches , buttons);
        usernameLabel.setText(Session.getUserName());
        try {
            displayMatchRecords();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    public void displayMatchRecords()throws  Exception{
        if (Session.getItemCount()<=0){
            emptyDisplay.setVisible(true);
        }
        matchBox.getChildren().clear();
        loginQueries queries = new loginQueries();
        list = queries.displayMatches(Session.getUserId());
        if (list.isEmpty()){
            emptyDisplay.setVisible(true);
        }else {
            emptyDisplay.setVisible(false);
        }
        for (matchModel model : list){
            matchBox.getChildren().add(createRow(model));
        }

    }

    private HBox createRow(matchModel model) throws IOException {

        // ── Lost Item Image ──
        ImageView lostImg = new ImageView();
        lostImg.setFitHeight(42);
        lostImg.setFitWidth(42);
        lostImg.setPreserveRatio(false);

        String lostImgPath = model.getLostImagePath();
        if (lostImgPath != null && !lostImgPath.isEmpty() && !lostImgPath.equals("null")) {
            try {
                File imgFile = new File(System.getProperty("user.home") + "/FindU/uploads/" + lostImgPath);
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

        // ── Found Item Image ──
        ImageView foundImg = new ImageView();
        foundImg.setFitHeight(42);
        foundImg.setFitWidth(42);
        foundImg.setPreserveRatio(false);

        String foundImgPath = model.getFoundImagePath();
        if (foundImgPath != null && !foundImgPath.isEmpty() && !foundImgPath.equals("null")) {
            try {
                File imgFile = new File(System.getProperty("user.home") + "/FindU/uploads/" + foundImgPath);
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

        // ── Status + Score ── (same as before)
        Label status = new Label(model.getStatus() != null ? model.getStatus() : "pending");
        status.getStyleClass().add(
                model.getStatus().equals("pending") ? "badge-lost" : "badge-found"
        );

        ProgressBar bar = new ProgressBar();
        Label scoreLabel = new Label(model.getScore() + "%");
        scoreLabel.getStyleClass().add("item-date");
        bar.setProgress(model.getScore() / 100.0);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lost_and_found/viewMatch.fxml"));
                Parent load = loader.load();
                matchViewControl control = loader.getController();
                control.seeItem(model, load);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        HBox box3 = new HBox(20, status, bar, scoreLabel, eyeButton);
        box3.setAlignment(Pos.CENTER_RIGHT);

        box1.setPrefWidth(300);
        box2.setPrefWidth(300);
        box3.setPrefWidth(300);

        HBox box = new HBox(40, box1, box2, box3);
        box.getStyleClass().add("row-set");
        box.setPadding(new Insets(14));
        box.setMaxWidth(Double.MAX_VALUE);

        return box;
    }

    private void loadPlaceholder(ImageView img) {
        try {
            img.setImage(new Image(getClass().getResourceAsStream(
                    "/com/example/lost_and_found/icons/profile.png")));
        } catch (Exception e) {
            // ignore
        }
    }

    public void clickProfile(ActionEvent event) throws Exception {
        switchScene(event, "profile.fxml");
    }
    public void clickHome(ActionEvent event)throws Exception{
        switchScene(event, "userDashboard.fxml");
    }

    public void clickReport(ActionEvent event) throws Exception {
        switchScene(event, "report.fxml");
    }
    public void clickMatch(ActionEvent event)throws Exception{
        switchScene(event , "matches.fxml");
    }
    public void clickAllItems(ActionEvent event)throws Exception{
        switchScene(event , "allItems.fxml");
    }
    public void clickSignOut(ActionEvent event)throws Exception{
        super.signOut(event);
    }




}
