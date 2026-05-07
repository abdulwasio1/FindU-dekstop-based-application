package com.example.lost_and_found;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class adminDashboardControl implements Initializable {
    @FXML
    private VBox displayBox;
    private Stage stage;

    @FXML
    private Label emptyDisplay;
//
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
//
//    @FXML
//    private AnchorPane pane1;
//
    @FXML
    private Button profile;

    @FXML
    private TextField searchField;

    @FXML
    private Label totalItemLabel;

    @FXML
    private Label totalMatchLabel;

    @FXML
    private Label totalPendingLabel;

    @FXML
    private Label totalUserLabel;

    private Button[] homeButtons;

    @FXML
    private Label user_name;
//
    @FXML
    private Label usernameLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeButtons = new Button[]{home,manageItems,manageUsers,manageMatches,profile};
        selectionUIHelper.setSelected(home,homeButtons);
        usernameLabel.setText(adminSession.getAdminName());
        user_name.setText(adminSession.getAdminName());

        try {
            loadRecentItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        totalItemLabel.setText(Integer.toString(adminSession.getTotalItems()));
        totalUserLabel.setText(Integer.toString(adminSession.getTotalUsers()));
        totalMatchLabel.setText(Integer.toString(adminSession.getTotalMatches()));
        totalPendingLabel.setText(Integer.toString(adminSession.getPendingItems()));
    }
    public void loadRecentItems()throws Exception{
        displayBox.getChildren().clear();
        queriesAdmin queries = new queriesAdmin();
        ObservableList<itemModel> items = queries.getItems();
        if (items.isEmpty()){
            emptyDisplay.setVisible(true);
        }else {
            emptyDisplay.setVisible(false);
        }
        int start = items.size() - 1;
        int end = Math.max(items.size() - 4, 0);
        for (int i = start; i >= end; i--) {
            displayBox.getChildren().add(createRow(items.get(i)));
        }

    }
    public HBox createRow(itemModel model){
        ImageView img = new ImageView();
        img.setFitHeight(52);
        img.setFitWidth(52);
        img.setPreserveRatio(false);

        String itemImg = model.getImage_path();
        if (itemImg != null && !itemImg.isEmpty() && !itemImg.equals("null")) {
            try {
                java.io.File imgFile = new java.io.File(
                        System.getProperty("user.home") + "/FindU/uploads/" + itemImg);
                if (imgFile.exists()) {
                    img.setImage(new Image(imgFile.toURI().toString()));
                } else {
                    loadPlaceholder(img, "item");
                }
            } catch (Exception e) {
                loadPlaceholder(img, "item");
            }
        } else {
            loadPlaceholder(img, "item");
        }

        Label itemName = new Label(model.getItem());
        itemName.getStyleClass().add("item-name");

        Label catTag = new Label(model.getCategory());
        Label locTag = new Label(model.getLocation());
        catTag.getStyleClass().add("item-tag");
        locTag.getStyleClass().add("item-tag");

        HBox tags = new HBox(12 , catTag , locTag);
        VBox info = new VBox(5, itemName , tags);

        Label status = new Label(model.getStatus());
        status.getStyleClass().add(
                model.getType().equals("pending") ? "badge-lost" : "badge-found"
        );

        Label type = new Label(model.getType());
        type.getStyleClass().add(
                model.getType().equals("lost") ? "badge-lost" : "badge-found"
        );
        Label date = new Label(model.getDate_lost_found());
        date.getStyleClass().add("item-date");

        HBox.setHgrow(info, Priority.ALWAYS);  //Yeh line info VBox ko bolta hai ke baaki saari khali jagah le lo

        Button deleteButton = new Button();
        Image icon = new Image(getClass().getResourceAsStream("/com/example/lost_and_found/icons/trash.png"));
        ImageView imgView = new ImageView(icon);
        imgView.setFitWidth(15);
        imgView.setFitHeight(15);

        deleteButton.setGraphic(imgView);
        deleteButton.getStyleClass().add("edit-button");
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    clickDeleteItem(model);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });



        HBox row = new HBox(14 , img ,info , status , type , date , deleteButton);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 14, 14, 14));
        row.setMaxWidth(Double.MAX_VALUE);
        row.getStyleClass().add("row-set");
        return  row;
    }
    private void loadPlaceholder(ImageView img, String type) {
        try {
            String icon = type.equals("profile") ? "profile.png" : "placeholder.png";
            img.setImage(new Image(getClass().getResourceAsStream(
                    "/com/example/lost_and_found/icons/" + icon)));
        } catch (Exception e) {
            // ignore
        }
    }

    private void clickDeleteItem(itemModel model) throws Exception{
        queriesAdmin queries = new queriesAdmin();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conformation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete the item?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            int value = queries.deleteItemByAdmin(model);
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
        loadRecentItems();
    }

    @FXML
    void clickHome(ActionEvent event) throws Exception {
        switchScene(event , "adminDash.fxml");
    }

    @FXML
    void clickManageItems(ActionEvent event) throws Exception {
        switchScene(event , "adminItems.fxml");
    }

    @FXML
    void clickManageMatches(ActionEvent event) throws Exception {
        switchScene(event , "adminMatch.fxml");
    }

    @FXML
    void clickManageUsers(ActionEvent event) throws Exception {
        switchScene(event ,"usersAdmin.fxml");
    }

    @FXML
    void clickProfile(ActionEvent event) throws Exception{
        switchScene(event , "adminProfile.fxml");
    }
    @FXML
    public void signOut(ActionEvent event) throws Exception {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");
        alert.setHeaderText("Sign Out");
        alert.setContentText("Are you sure to Sign Out");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            adminSession.clear();
            switchScene(event, "login.fxml");
            stage.centerOnScreen();
        } else {
            System.out.println("Action cancelled");
        }
    }

    @FXML
    void onClickSearch(ActionEvent event)throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lost_and_found/adminItems.fxml"));
        Parent root = loader.load();

        adminDisplayItems control = loader.getController();
        control.setList(searchField.getText()); // ✅ pass ONLY text

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    public void  switchScene(ActionEvent event, String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/lost_and_found/" + fxmlFile)
        );

        Parent root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }
}
