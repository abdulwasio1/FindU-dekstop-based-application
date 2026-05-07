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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class manageUsersAdmin implements Initializable {
    @FXML
    VBox displayBox;

    @FXML
    Label emptyDisplay;

    @FXML
    private Label usernameLabel;

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
    private TextField searchField;

    private Button homeButtons[];
    adminDashboardControl dashboard = new adminDashboardControl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeButtons = new Button[]{home,manageUsers,manageMatches,manageItems,profile};
        selectionUIHelper.setSelected(manageUsers ,homeButtons);
        usernameLabel.setText(adminSession.getAdminName());
        try {
            displayUsers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    void displayUsers()throws Exception{
        displayBox.getChildren().clear();
        queriesAdmin query = new queriesAdmin();
        ObservableList<usersModel> users = query.getUsers();
        if (users.isEmpty()){
            emptyDisplay.setVisible(true);
        }else {
            emptyDisplay.setVisible(false);
        }
        for (usersModel user : users){
            if (user.getName().equals(adminSession.getAdminName())) continue;
            displayBox.getChildren().add(createRow(user));
        }
    }
    public HBox createRow(usersModel model){

        ImageView img = new ImageView();
        img.setFitHeight(52);
        img.setFitWidth(52);
        img.setPreserveRatio(false);

        String profileImg = model.getProfile_image();
        if (profileImg != null && !profileImg.isEmpty() && !profileImg.equals("null")) {
            try {
                java.io.File imgFile = new java.io.File(
                        System.getProperty("user.home") + "/FindU/profiles/" + profileImg);
                if (imgFile.exists()) {
                    img.setImage(new Image(imgFile.toURI().toString()));
                } else {
                    loadPlaceholder(img, "profile");
                }
            } catch (Exception e) {
                loadPlaceholder(img, "profile");
            }
        } else {
            loadPlaceholder(img, "profile");
        }

        Label itemName = new Label(model.getName());
        itemName.getStyleClass().add("item-name");

        Label catTag = new Label(model.getEmail());
        Label locTag = new Label(model.getS_id());
        catTag.getStyleClass().add("item-tag");
        locTag.getStyleClass().add("item-tag");

        HBox tags = new HBox(12 , catTag , locTag);
        VBox info = new VBox(5, itemName , tags);

//        Label status = new Label(model.getStatus());
//        status.getStyleClass().add(
//                model.getType().equals("pending") ? "badge-lost" : "badge-found"
//        );

        Label role = new Label(model.getRole());
        role.getStyleClass().add(
                model.getRole().equals("user") ? "badge-lost" : "badge-found"
        );
//        Label date = new Label(model.getDate_lost_found());
//        date.getStyleClass().add("item-date");

        HBox.setHgrow(info, Priority.ALWAYS);  //Yeh line info VBox ko bolta hai ke baaki saari khali jagah le lo

        Button delete = new Button();
        Image iconDelete = new Image(getClass().getResourceAsStream("/com/example/lost_and_found/icons/trash.png"));
        ImageView imgViewDelete = new ImageView(iconDelete);
        imgViewDelete.setFitWidth(15);
        imgViewDelete.setFitHeight(15);

        Button editButton = new Button();
        Image iconEdit = new Image(getClass().getResourceAsStream("/com/example/lost_and_found/icons/edit.png"));
        ImageView imgViewEdit = new ImageView(iconEdit);
        imgViewEdit.setFitWidth(19);
        imgViewEdit.setFitHeight(19);

        editButton.setGraphic(imgViewEdit);
        editButton.getStyleClass().add("edit-button");
        editButton.setOnAction(e -> {
            try {
                clickEditUser(model);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        delete.setGraphic(imgViewDelete);
        delete.getStyleClass().add("edit-button");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    System.out.println("Delete User Clicked");
                    clickDeleteUser(model);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

//        Use same with lambda expression



        HBox row = new HBox(10 , img ,info , role , editButton , delete);
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

    private void clickDeleteUser(usersModel model) throws Exception{
            queriesAdmin queries = new queriesAdmin();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conformation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to delete the User?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                int value = queries.deleteUser(model);
                if (value>0){
                    Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                    confirm.setTitle("Success");
                    confirm.setHeaderText(null);
                    confirm.setContentText("User Deleted Successfully");
                    confirm.showAndWait();
                }else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to delete user, Try Again!!");
                    errorAlert.showAndWait();
                }

            } else {
                System.out.println("Action cancelled");
            }
            displayUsers();

    }

    public void clickEditUser(usersModel user) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lost_and_found/editUser.fxml"));
        Parent load = loader.load();
        userViewControl control = loader.getController();
        control.seeUser(user, load);
        displayUsers();
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
    public void signOut(ActionEvent event) throws Exception{
        dashboard.signOut(event);
    }

    public void onClickSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase().trim();

        displayBox.getChildren().clear();

        try {
            if (searchText.isEmpty()) {
                displayUsers();
                return;
            }

            queriesAdmin query = new queriesAdmin();
            ObservableList<usersModel> users = query.getUsers();

            for (usersModel user : users) {
                if (user.getName().equals(adminSession.getAdminName())) continue;

                if (
                        user.getName().toLowerCase().contains(searchText) ||
                                user.getEmail().toLowerCase().contains(searchText) ||
                                user.getRole().toLowerCase().contains(searchText)
                ) {
                    displayBox.getChildren().add(createRow(user));
                }else {
                    emptyDisplay.setVisible(true);
                }
            }

            // ✅ THIS is the correct logic
            emptyDisplay.setVisible(displayBox.getChildren().isEmpty());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
