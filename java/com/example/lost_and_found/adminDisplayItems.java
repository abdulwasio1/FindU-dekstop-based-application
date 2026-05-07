package com.example.lost_and_found;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class adminDisplayItems extends adminDashboardControl implements Initializable {
    @FXML
    Label emptyDisplay;

    @FXML
    private Button accessories;

    @FXML
    private Button all;

    @FXML
    private VBox displayBox;

    @FXML
    private ChoiceBox<String> displayChoice;

    @FXML
    private Button docs;

    @FXML
    private Button electronics;

    @FXML
    private Button exit;

    @FXML
    private Button home;

    @FXML
    private Button keys;

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
    private Label usernameLabel;

    Button[] homeButtons;
    Button[] catgButtons;
    adminDashboardControl dashboard = new adminDashboardControl();
    ObservableList<itemModel> items;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeButtons = new Button[]{home,manageItems,manageMatches,manageUsers,profile};
        catgButtons = new Button[]{all,accessories,keys,docs,electronics};
        selectionUIHelper.setSelected(manageItems , homeButtons);
        usernameLabel.setText(adminSession.getAdminName());

        displayChoice.getItems().addAll("Latest First" , "Oldest First");
        displayChoice.setValue("Oldest First");
        displayChoice.setOnAction(this::filter);

        try {
            loadRecentItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public void filter(Event event) {
        if (displayChoice.getValue().equals("Latest First")) {
            newToFirstDisplay();
        } else {
            displayBox.getChildren().clear();
            for (itemModel model : items) {
                displayBox.getChildren().add(createRow(model));  // ← oldest first = normal order
            }
        }
    }
    public void newToFirstDisplay(){
        displayBox.getChildren().clear();
        for (int i = items.size() - 1; i >= 0; i--) {
            displayBox.getChildren().add(createRow(items.get(i)));
        }
    }

    @Override
    public void loadRecentItems() throws Exception {
        displayBox.getChildren().clear();
        queriesAdmin queries = new queriesAdmin();
         items = queries.getItems();
        if (items.isEmpty()){
            emptyDisplay.setVisible(true);
        }else {
            emptyDisplay.setVisible(false);
        }
        if (displayChoice.getValue().equals("Latest First")){
            newToFirstDisplay();
        }else {
            for (itemModel model : items){
                displayBox.getChildren().add(createRow(model));
            }
        }

    }
    //    public HBox createRow(itemModel model) {
//        ImageView img = new ImageView();
//        img.setFitHeight(52);
//        img.setFitWidth(52);
//
//        Label itemName = new Label(model.getItem());
//        itemName.getStyleClass().add("item-name");
//
//        Label catTag = new Label(model.getCategory());
//        Label locTag = new Label(model.getLocation());
//        catTag.getStyleClass().add("item-tag");
//        locTag.getStyleClass().add("item-tag");
//
//        HBox tags = new HBox(12, catTag, locTag);
//        VBox info = new VBox(5, itemName, tags);
//
//        Label status = new Label(model.getStatus());
//        status.getStyleClass().add(
//                model.getType().equals("pending") ? "badge-lost" : "badge-found"
//        );
//
//        Label type = new Label(model.getType());
//        type.getStyleClass().add(
//                model.getType().equals("lost") ? "badge-lost" : "badge-found"
//        );
//        Label date = new Label(model.getDate_lost_found());
//        date.getStyleClass().add("item-date");
//
//        HBox.setHgrow(info, Priority.ALWAYS);  //Yeh line info VBox ko bolta hai ke baaki saari khali jagah le lo
//
//        Button editButton = new Button();
//        Image icon = new Image(getClass().getResourceAsStream("/com/example/lost_and_found/icons/trash.png"));
//        ImageView imgView = new ImageView(icon);
//        imgView.setFitWidth(15);
//        imgView.setFitHeight(15);
//
//        editButton.setGraphic(imgView);
//        editButton.getStyleClass().add("edit-button");
//        editButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                try {
//                    clickDeleteItem(model);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        HBox row = new HBox(14 , img ,info , status , type , date , editButton);
//        row.setAlignment(Pos.CENTER_LEFT);
//        row.setPadding(new Insets(14, 14, 14, 14));
//        row.setMaxWidth(Double.MAX_VALUE);
//        row.getStyleClass().add("row-set");
//        return  row;
//    }
//        public void clickDeleteItem(itemModel model) throws Exception {
//            queriesAdmin queries = new queriesAdmin();
//            int value = queries.deleteItemByAdmin(model);
//            if (value>0){
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                alert.setTitle("Conformation");
//                alert.setHeaderText(null);
//                alert.setContentText("Are you sure to delete the item?");
//
//                Optional<ButtonType> result = alert.showAndWait();
//
//                if (result.isPresent() && result.get() == ButtonType.OK) {
//                    displayItems();
//                } else {
//                    System.out.println("Action cancelled");
//                }
//            }else {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Error");
//                alert.setHeaderText(null);
//                alert.setContentText("Failed to delete item, Try Again!!");
//                alert.showAndWait();
//            }
//        }

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

    @FXML
    void signOutItems(ActionEvent event) throws Exception {
        dashboard.signOut(event);
    }
    public void clickCategory(ActionEvent event) throws Exception {
        displayBox.getChildren().clear();
        ObservableList<itemModel> category  = FXCollections.observableArrayList();
        Button catgButton = (Button) event.getSource();
        selectionUIHelper.setSelected(catgButton , catgButtons);
        if (catgButton.getText().equals("All")) {
            this.loadRecentItems();  // ← All button ke liye seedha view() call
            return;       // ← return karo warna neeche ka code bhi chalega
        }
        if (catgButton.getText().equals("Electronics")) {
            for (itemModel model : items){
                if (model.getCategory().equals("Electronics")){
                    category.add(model);
                }
            }
        } else if (catgButton.getText().equals("Accessories")) {
            for (itemModel model : items){
                if (model.getCategory().equals("Bags & Accessories")){
                    category.add(model);
                }
            }
        }else if (catgButton.getText().equals("Keys")) {
            for (itemModel model : items){
                if (model.getCategory().equals("Vehicles & Keys")){
                    category.add(model);
                }
            }
        }else if (catgButton.getText().equals("Documents")) {
            for (itemModel model : items){
                if (model.getCategory().equals("Documents & IDs")){
                    category.add(model);
                }
            }
        }else {
            this.loadRecentItems();
        }
        if (!category.isEmpty()){
            emptyDisplay.setVisible(false);
            for (itemModel model : category){
                displayBox.getChildren().add(createRow(model));
            }
        }else {
            emptyDisplay.setVisible(true);
        }
    }
    public void setList(String text) throws Exception {
        displayBox.getChildren().clear();
        String search = text.toLowerCase();
        queriesAdmin queries = new queriesAdmin();
        items = queries.getItems();
        ObservableList<itemModel> searchedValues = FXCollections.observableArrayList();

        for (itemModel model : items) {
            if (
                    model.getItem().toLowerCase().contains(search) ||
                            model.getLocation().toLowerCase().contains(search)

            ) {
               searchedValues.add(model);
            }else {
                emptyDisplay.setVisible(true);
            }
        }

        if (searchedValues.isEmpty()) {
            emptyDisplay.setVisible(true);
        } else {
            emptyDisplay.setVisible(false);
            for (itemModel model : searchedValues) {
                displayBox.getChildren().add(createRow(model));
            }
        }
    }

}
