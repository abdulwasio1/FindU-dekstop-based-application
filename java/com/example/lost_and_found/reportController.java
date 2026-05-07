package com.example.lost_and_found;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.text.DateFormatter;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class reportController implements Initializable {
    @FXML
    private ChoiceBox<String> categoryChoice;

    @FXML
    DatePicker reportDate;


    @FXML
    private TextField contactField;

    @FXML
    private TextArea descField;


    @FXML
    private TextField locationField;

    @FXML
    private TextField nameField;

    @FXML
    private Label dateLabel;

    @FXML
    private Label imgLabel;

    @FXML
    private Button found;

    @FXML
    private Button lost;

    private Button[] itemButton;

    String buttonType;
    @FXML Label usernameLabel;

    private Button[] Homebuttons;

    String statusByDefault = "pending";

    userDashboardControl dashboard = new userDashboardControl();
    @FXML private Button home;
    @FXML private Button allItems;
    @FXML private Button matches;
    @FXML private Button profile;
    @FXML private Button exit;
    @FXML private Button reportItem;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryChoice.getItems().addAll(
                "Electronics",
                "Bags & Accessories",
                "Documents & IDs",
                "Books & Stationery",
                "Clothing",
                "Jewellery & Valuables",
                "Pets & Animals",
                "Vehicles & Keys",
                "Food & Bottles",
                "Sports & Music",
                "Other"
        );
        categoryChoice.setValue("Electronics");
        categoryChoice.setOnAction(this::clickChoice);
        buttonType =  "lost";
        Homebuttons = new Button[]{ home, allItems , matches , profile , exit ,reportItem};
        itemButton = new Button[]{lost , found};
        for (Button btn : itemButton) {
            btn.getStyleClass().add("itemTypeButtons");
        }
        lost.getStyleClass().add("clickLostAndFound");

        selectionUIHelper.setSelected(reportItem , Homebuttons);
        usernameLabel.setText(Session.getUserName());

    }

    public void clickChoice(ActionEvent event) {
        categoryChoice.setValue(categoryChoice.getValue());
    }

    private void clearFields(){
        nameField.clear();
        contactField.clear();
        locationField.clear();
        reportDate.setValue(null);
        categoryChoice.setValue("Electronics");
        descField.clear();
        previewImage.setImage(null);
        imageItem.clear();
    }
    public void setButtonTypeLost(ActionEvent event){
        for (Button button : itemButton){
            button.getStyleClass().remove("clickLostAndFound");
        }
        lost.getStyleClass().add("clickLostAndFound");
        dateLabel.setText("Date Lost");
        buttonType =  "lost";
        imgLabel.setText("Image Upload (if have)");
        clearFields();
    }


    public void setButtonTypeFound(ActionEvent event){
        for (Button button : itemButton){
            button.getStyleClass().remove("clickLostAndFound");
        }
        found.getStyleClass().add("clickLostAndFound");
        dateLabel.setText("Date Found");
        buttonType =  "found";
        imgLabel.setText("Image Upload");
        clearFields();
    }

    public void submit(ActionEvent event) throws Exception {
        try {
            LocalDate date = reportDate.getValue();
            String format_date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if (buttonType.equals("found") && (selectedImagePath == null || selectedImagePath.isEmpty())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Image Required");
                alert.setHeaderText(null);
                alert.setContentText("Must Upload an image before submit");
                alert.showAndWait();
                return;
            }

            String imageName = null;
            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                imageName = saveImage(new File(selectedImagePath));
            }

            itemModel item = new itemModel(
                    nameField.getText(),
                    categoryChoice.getValue(),
                    locationField.getText(),
                    buttonType,
                    statusByDefault,
                    imageName,
                    format_date,
                    contactField.getText(),
                    descField.getText()
            );

            if (nameField.getText().isEmpty()) item.setItem(null);
            if (locationField.getText().isEmpty()) item.setLocation(null);
            if (contactField.getText().isEmpty()) item.setContact(null);

            loginQueries queries = new loginQueries();
            int value = queries.insertItem(item, Session.getUserId());

            Alert alert;
            if (value > 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Item report has been submitted successfully!");
                alert.showAndWait();
                handleMatches(item);
                clearFields();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Item report submission failed. Please try again.");
                alert.showAndWait();
            }

        } catch (DateTimeParseException | NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Date");
            alert.setHeaderText(null);
            alert.setContentText("Please select a valid date.");
            alert.showAndWait();
        }
    }
    public void clickHome(ActionEvent event)throws Exception{
        dashboard.switchScene(event, "userDashboard.fxml");
    }
    public void clickAllItems(ActionEvent event) throws Exception {
        dashboard.switchScene(event, "allItems.fxml");
    }

    public void clickProfile(ActionEvent event)throws Exception{
        dashboard.switchScene(event, "profile.fxml");
    }
    public void onClickSignOut(ActionEvent event) throws Exception {
        dashboard.signOut(event);
    }
    public void clickMatches(ActionEvent event) throws  Exception{
        dashboard.switchScene(event, "matches.fxml");
    }

    public void handleMatches(itemModel currentItem)throws Exception{
        String remainingItemtype;
        loginQueries queries = new loginQueries();
        ArrayList<itemModel> checkingLists;
        if ("lost".equals(currentItem.getType())){
            remainingItemtype = "found";
        }else {
            remainingItemtype = "lost";
        }
        checkingLists = queries.getRemainingItems(remainingItemtype);

        for (itemModel toCheck : checkingLists){
            int score = handleScore(currentItem , toCheck);
            if (score>=50){
                queries.addMatchItem(score , currentItem , toCheck , remainingItemtype);
            }
        }

    }
    public int handleScore(itemModel currentItem, itemModel toCheckItem) {

        int score = 0;

        String name1 = currentItem.getItem().toLowerCase().trim();
        String name2 = toCheckItem.getItem().toLowerCase().trim();

        if (name1.equals(name2)) {
            score += 40;
        } else if (name1.contains(name2) || name2.contains(name1)) {
            score += 25;
        } else {
            score += commonWords(name1, name2) * 10;
        }

        if (currentItem.getCategory().equalsIgnoreCase(toCheckItem.getCategory())) {
            score += 25;
        }

        String loc1 = (currentItem.getLocation() != null) ? currentItem.getLocation().toLowerCase().trim() : "";
        String loc2 = (toCheckItem.getLocation() != null) ? toCheckItem.getLocation().toLowerCase().trim() : "";

        if (loc1.equals(loc2)) {
            score += 20;
        } else if (loc1.contains(loc2) || loc2.contains(loc1)) {
            score += 10;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(currentItem.getDate_lost_found() , formatter);
        LocalDate date2 = LocalDate.parse(toCheckItem.getDate_lost_found() , formatter);
        long diff = Math.abs(ChronoUnit.DAYS.between(date1, date2));

        if (diff == 0) {
            score += 10;
        } else if (diff <= 2) {
            score += 5;
        }

        if (currentItem.getDescription() != null && toCheckItem.getDescription() != null) {
            int common = commonWords(
                    currentItem.getDescription().toLowerCase(),
                    toCheckItem.getDescription().toLowerCase()
            );
            score += common * 5;
        }

        if (score > 100) {
            score = 100;
        }

        return score;
    }

    private int commonWords(String text1, String text2) {
        if (text1 == null || text2 == null) return 0;

        String[] stopWords = {"the", "a", "an", "is", "it", "in", "on", "at", "of", "and", "or",
                "my", "with", "near", "found", "lost", "item", "was", "have",
                "black", "white", "red", "blue", "i", "to", "by", "this", "that"};

        String t1 = text1.toLowerCase().trim();
        String t2 = text2.toLowerCase().trim();

        String[] words1 = t1.split("\\s+");

        int count = 0;

        for (String word : words1) {

            boolean isStop = false;
            for (String stop : stopWords) {
                if (word.equals(stop)) { isStop = true; break; }
            }
            if (isStop) continue;

            if (t2.contains(word)) {
                count++;
            }
        }

        return count;
    }


    @FXML
    private ImageView previewImage;

    @FXML
    private TextField imageItem;

    private String selectedImagePath;

    @FXML
    public void browseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectedImagePath = file.getAbsolutePath();
            imageItem.setText(file.getName());

            Image image = new Image(file.toURI().toString());
            previewImage.setImage(image);
        }
    }

    private String saveImage(File file) throws Exception {
        String destDir = System.getProperty("user.home") + "/FindU/uploads/";

        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getName();
        File dest = new File(destDir + fileName);

        Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }








}
