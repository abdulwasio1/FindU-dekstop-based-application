package com.example.lost_and_found;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class allItemDisplayControl extends userDashboardControl implements Initializable  {


    @FXML
    private Button all;

    @FXML
    private Button docs;

    @FXML
    private Button electronics;

    @FXML
    private Button accessories;


    @FXML
    private Button keys;


    @FXML
    private Button allItems;

    @FXML
    private VBox displayBox;

    @FXML
    private Button exit;

    @FXML
    private Button home;

    @FXML
    private Button matches;

    @FXML
    private Button profile;

    @FXML
    private VBox recordBox;

    @FXML
    private Button reportItem;

    @FXML
    private Label user_name;

    @FXML
    Label emptyDisplay;

    @FXML
    private Label usernameLabel;

    @FXML
    public ChoiceBox<String> displayChoice;

    private Button[] Homebuttons;
    private Button[] catgButtons;

    @FXML
    private Button newToFirst;

    ObservableList<itemModel> list = FXCollections.observableArrayList();;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Homebuttons = new Button[]{ home, allItems , matches , profile , exit ,reportItem};
        catgButtons = new Button[]{ all , electronics , accessories ,docs ,keys, };
        selectionUIHelper.setSelected(allItems, Homebuttons);
        selectionUIHelper.setSelected(all ,catgButtons);

        displayChoice.getItems().addAll("Latest First" , "Oldest First");
        displayChoice.setValue("Oldest First");
        displayChoice.setOnAction(this::filter);

        try {
            view();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        usernameLabel.setText(Session.getUserName());




    }

    @FXML
    public void clickHome(ActionEvent event) throws Exception{
        switchScene(event , "userDashboard.fxml");
    }

    @FXML
    public void clickMatch(ActionEvent event) throws Exception{
        switchScene(event , "matches.fxml");
    }

    @FXML
    public void clickProfile(ActionEvent event) throws Exception{
        switchScene(event , "profile.fxml");
    }

    @FXML
    public void clickReport(ActionEvent event) throws Exception{
        switchScene(event , "report.fxml");
    }
    @FXML
    public void clickAll(ActionEvent event) throws Exception{
        switchScene(event , "allItems.fxml");
    }
    @FXML
    public void signOut(ActionEvent event) throws Exception{
        super.signOut(event);
    }

    public void setList(String itemValue) throws Exception {
        displayBox.getChildren().clear();

        loginQueries queries = new loginQueries();
        list = queries.showValues(Session.getUserId());
        ObservableList<itemModel> searchedValues = FXCollections.observableArrayList();

        for (itemModel model : list) {
            String modelValue = model.getItem().toLowerCase();
            if (modelValue.contains(itemValue.toLowerCase())) {
                searchedValues.add(model);
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



    @Override
    public void view()throws Exception {
        displayBox.getChildren().clear();
        loginQueries queries = new loginQueries();
        list = queries.showValues(Session.getUserId());
        if (list.isEmpty()){
            emptyDisplay.setVisible(true);
        }else {
            emptyDisplay.setVisible(false);
        }
        if (displayChoice.getValue().equals("Latest First")){
           newToFirstDisplay();
        }else {
            for (itemModel model : list){
                displayBox.getChildren().add(createRow(model));
            }
        }
    }





    public void newToFirstDisplay(){
        displayBox.getChildren().clear();
        for (int i = list.size() - 1; i >= 0; i--) {
            displayBox.getChildren().add(createRow(list.get(i)));
        }
    }
    public void filter(Event event) {
        if (displayChoice.getValue().equals("Latest First")) {
            newToFirstDisplay();
        } else {
            displayBox.getChildren().clear();
            for (itemModel model : list) {
                displayBox.getChildren().add(createRow(model));
            }
        }
    }
    public void clickCategory(ActionEvent event) throws Exception {
        displayBox.getChildren().clear();
        ObservableList<itemModel> category  = FXCollections.observableArrayList();
        Button catgButton = (Button) event.getSource();
        selectionUIHelper.setSelected(catgButton , catgButtons);
        if (catgButton.getText().equals("All")) {
            this.view();
            return;
        }
        if (catgButton.getText().equals("Electronics")) {
            for (itemModel model : list){
                if (model.getCategory().equals("Electronics")){
                    category.add(model);
                }
            }
        } else if (catgButton.getText().equals("Accessories")) {
            for (itemModel model : list){
                if (model.getCategory().equals("Bags & Accessories")){
                    category.add(model);
                }
            }
        }else if (catgButton.getText().equals("Keys")) {
            for (itemModel model : list){
                if (model.getCategory().equals("Vehicles & Keys")){
                    category.add(model);
                }
            }
        }else if (catgButton.getText().equals("Documents")) {
            for (itemModel model : list){
                if (model.getCategory().equals("Documents & IDs")){
                    category.add(model);
                }
            }
        }else {
            this.view();
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
}
