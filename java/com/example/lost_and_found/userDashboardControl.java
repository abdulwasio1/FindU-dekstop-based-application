package com.example.lost_and_found;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class userDashboardControl implements Initializable {

    @FXML Label usernameLabel;
    @FXML Label user_name;

    @FXML private Label claimedItemLabel;
    @FXML private Label foundItemsLabel;
    @FXML private Label lostItemsLabel;
    @FXML private Label totalItemLabel;


    @FXML private TextField searchField;


    @FXML private VBox recordBox;
    @FXML private Button reportItem;
    @FXML private Button exit;
    @FXML private Button home;
    @FXML private Button matches;
    @FXML private Button profile;
    @FXML private Button allItems;
    @FXML private Label emptyDisplay;



    private Stage stage;

    private String searchButtonType;

    private Button[] Homebuttons;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ImageView sidebarImg = new ImageView();
        String imgPath = Session.getProfileImage();
        if (imgPath != null && !imgPath.isEmpty() && !imgPath.equals("null")) {
            File f = new File(System.getProperty("user.home") + "/FindU/profiles/" + imgPath);
            if (f.exists()) sidebarImg.setImage(new Image(f.toURI().toString()));
        }

        usernameLabel.setText(Session.getUserName());
        user_name.setText(Session.getUserName() + "!!");
        Homebuttons = new Button[]{ home, allItems , matches , profile , exit ,reportItem};
        selectionUIHelper.setSelected(home , Homebuttons);


        try {
            view();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (Session.getItemCount()>0){
            emptyDisplay.setVisible(false);
        }else {
            emptyDisplay.setVisible(true);
        }
    }

    public void view() throws Exception {
        recordBox.getChildren().clear();
        loginQueries query = new loginQueries();
        ObservableList<itemModel> list = query.showValues(Session.getUserId());
        int start = list.size() - 1;
        int end = Math.max(list.size() - 4, 0);
        for (int i = start; i >= end; i--) {
            recordBox.getChildren().add(createRow(list.get(i)));
        }

        if (list.isEmpty()) {
            emptyDisplay.setVisible(true);
        } else {
            emptyDisplay.setVisible(false);
        }

        totalItemLabel.setText(Integer.toString(Session.getItemCount()));
        lostItemsLabel.setText(Integer.toString(Session.getLostCount()));
        foundItemsLabel.setText(Integer.toString(Session.getFoundCount()));
        claimedItemLabel.setText(Integer.toString(Session.getTotalClaim()));
    }
    public HBox createRow(itemModel model) {
        ImageView img = new ImageView();
        img.setFitHeight(52);
        img.setFitWidth(52);
        img.setPreserveRatio(false);

        if (model.getImage_path() != null && !model.getImage_path().isEmpty()) {
            try {
                String imagePath = System.getProperty("user.home")
                        + "/FindU/uploads/"
                        + model.getImage_path();
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    img.setImage(new Image(imgFile.toURI().toString()));
                } else {
                    loadPlaceholder(img);
                }
            } catch (Exception e) {
                loadPlaceholder(img);
            }
        } else {
            loadPlaceholder(img);
        }

        Label itemName = new Label(model.getItem());
        itemName.getStyleClass().add("item-name");

        Label catTag = new Label(model.getCategory());
        Label locTag = new Label(model.getLocation());
        catTag.getStyleClass().add("item-tag");
        locTag.getStyleClass().add("item-tag");

        HBox tags = new HBox(12, catTag, locTag);
        VBox info = new VBox(5, itemName, tags);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label status = new Label(model.getStatus());
        status.getStyleClass().add(
                model.getStatus().equals("pending") ? "badge-lost" : "badge-found"
        );

        Label type = new Label(model.getType());
        type.getStyleClass().add(
                model.getType().equals("lost") ? "badge-lost" : "badge-found"
        );

        Label date = new Label(model.getDate_lost_found());
        date.getStyleClass().add("item-date");

        Button deleteButton = new Button();
        Image icon = new Image(getClass().getResourceAsStream(
                "/com/example/lost_and_found/icons/trash.png"));
        ImageView imgView = new ImageView(icon);
        imgView.setFitWidth(15);
        imgView.setFitHeight(15);
        deleteButton.setGraphic(imgView);
        deleteButton.getStyleClass().add("edit-button");
        deleteButton.setOnAction(e -> {
            try {
                clickDeleteItem(model);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        HBox row = new HBox(14, img, info, status, type, date, deleteButton);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 14, 14, 14));
        row.setMaxWidth(Double.MAX_VALUE);
        row.getStyleClass().add("row-set");
        return row;
    }

    private void loadPlaceholder(ImageView img) {
        try {
            img.setImage(new Image(getClass().getResourceAsStream(
                    "/com/example/lost_and_found/icons/placeholder.png")));
        } catch (Exception e) {
        }
    }

    public void clickDeleteItem( itemModel model) throws Exception {
        loginQueries queries = new loginQueries();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conformation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete the item?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            int value = queries.deleteItem(model);
            if (value>0){
                Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                confirm.setTitle("Success");
                confirm.setHeaderText(null);
                confirm.setContentText("Item Deleted Successfully");
                confirm.showAndWait();
            }else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to delete item, Try Again!!");
                errorAlert.showAndWait();
            }

        } else {
            System.out.println("Action cancelled");
        }
        view();
    }




    public void switchScene(ActionEvent event, String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/lost_and_found/" + fxmlFile)
        );

        Parent root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    public void onClickHome(ActionEvent event) throws Exception {
        switchScene(event, "userDashboard.fxml");
    }

    public void signOut(ActionEvent event) throws Exception {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");
        alert.setHeaderText("Sign Out");
        alert.setContentText("Are you sure to Sign Out");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Session.clear();
            switchScene(event, "login.fxml");
            stage.centerOnScreen();
        } else {
            System.out.println("Action cancelled");
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
    public void clickLocation(ActionEvent event){
        searchButtonType = "location";
        searchField.setText("");
    }

    public void onClickSearch(ActionEvent event)throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lost_and_found/allItems.fxml"));
        Parent root = loader.load();

        allItemDisplayControl control = loader.getController();
        control.setList(searchField.getText());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    void setSelected(Button selected, Button[] buttons) {
        for (Button b : buttons) {
            b.getStyleClass().remove("categoryBarSelect");
        }
            selected.getStyleClass().add("categoryBarSelect");
    }
    public void clickCategory(ActionEvent event) throws Exception {

    }




}