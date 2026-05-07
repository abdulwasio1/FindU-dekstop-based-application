package com.example.lost_and_found;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.ResourceBundle;

public class profileController implements Initializable {
    @FXML
    private TextField emailField;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField fullNameField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private TextField phoneField;

    @FXML
    private TextField passwordVisible;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label userNameLabel;


    @FXML
    Label studId;

    @FXML
    Label usernameLabelSide;

    @FXML
    ToggleButton passwordToggle;

    @FXML
    TextField studentIdField;

    @FXML
    TextField roleField;

    private Stage stage;

    private String initialUsername , initialEmail , initialPhone , initialPassword , initialStudentId;

    userDashboardControl dashboard = new userDashboardControl();
    private Button[] Homebuttons;
    @FXML private Button home;
    @FXML private Button allItems;
    @FXML private Button matches;
    @FXML private Button profile;
    @FXML private Button exit;
    @FXML private Button reportItem;
    @FXML private ImageView profileImageView;

    private String selectedProfileImagePath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Homebuttons = new Button[]{ home, allItems , matches , profile , exit ,reportItem};
        selectionUIHelper.setSelected(profile, Homebuttons);
        loadFields();
    }

    @FXML
    public void browseProfileImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectedProfileImagePath = file.getAbsolutePath();
            profileImageView.setImage(new Image(file.toURI().toString()));
        }
    }

    private String saveProfileImage(File file) throws Exception {
        String destDir = System.getProperty("user.home") + "/FindU/profiles/";
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getName();
        Files.copy(file.toPath(), new File(destDir + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    private void loadProfileImage() {
        String imgPath = Session.getProfileImage();
        if (imgPath != null && !imgPath.isEmpty() && !imgPath.equals("null")) {
            try {
                File imgFile = new File(System.getProperty("user.home")
                        + "/FindU/profiles/" + imgPath);
                if (imgFile.exists()) {
                    profileImageView.setImage(new Image(imgFile.toURI().toString()));
                } else {
                    loadPlaceholder();
                }
            } catch (Exception e) {
                loadPlaceholder();
            }
        } else {
            loadPlaceholder();
        }
    }

    private void loadPlaceholder() {
        profileImageView.setImage(new Image(getClass().getResourceAsStream(
                "/com/example/lost_and_found/icons/profile.png")));
    }



    public void loadFields(){


        fullNameField.setText(Session.getUserName());
        phoneField.setText(Session.getPhone());
        emailField.setText(Session.getEmail());
        studentIdField.setText(Session.getS_id());
        roleField.setText(Session.getRole());
        if (passwordToggle.isSelected()) {
            passwordVisible.setText(Session.getPassword());
        } else {
            passwordField.setText(Session.getPassword());
        }
        userNameLabel.setText(Session.getUserName());
        emailLabel.setText(Session.getEmail());
        phoneLabel.setText(Session.getPhone());

        phoneField.setText(Session.getPhone() != null ? Session.getPhone() : "");
        phoneLabel.setText(Session.getPhone() != null ? Session.getPhone() : "");

        typeLabel.setText(Session.getRole());
        studId.setText(Session.getS_id());
        usernameLabelSide.setText(Session.getUserName());

        initialUsername = fullNameField.getText();
        initialEmail = emailField.getText();
        initialPhone = phoneField.getText();
        initialStudentId = studentIdField.getText();
        initialPassword = Session.getPassword();
        loadProfileImage();


    }
    public void onClickChanges(ActionEvent event) throws Exception{
        String pass = passwordToggle.isSelected() ? passwordVisible.getText() : passwordField.getText();
        if (!phoneField.getText().isEmpty() && !phoneField.getText().matches("[0-9]+") ) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Student ID");
            alert.setHeaderText(null);
            alert.setContentText("Phone Number only contain digits");
            alert.showAndWait();
            return;
        }
        if ((phoneField.getText().length()<10 ||  phoneField.getText().length()>15)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Student ID");
            alert.setHeaderText(null);
            alert.setContentText("Phone Number only contain digits between 10 - 15");
            alert.showAndWait();
            return;
        }
        if (!studentIdField.getText().isEmpty() && !studentIdField.getText().matches("[0-9]{3}-[0-9]{2}-[0-9]{4}")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Student ID");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Student ID format (e.g., 023-25-0000)");
            alert.showAndWait();
            return;
        }
        if (!emailField.getText().isEmpty() && !emailField.getText().endsWith("@iba-suk.edu.pk")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Student ID");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Email format (e.g., user@iba-suk.edu.pk)");
            alert.showAndWait();
            return;
        }


        if (!fullNameField.getText().isEmpty() && !emailField.getText().isEmpty() && !pass.isEmpty() && !studentIdField.getText().isEmpty() && !phoneField.getText().isEmpty()){
            String profileImgName = Session.getProfileImage();
            if (selectedProfileImagePath != null && !selectedProfileImagePath.isEmpty()) {
                try {
                    profileImgName = saveProfileImage(new File(selectedProfileImagePath));
                    loginQueries imgQuery = new loginQueries();
                    imgQuery.saveProfileImage(profileImgName, Session.getUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Session.setUser(Session.getUserId(), studentIdField.getText(),
                    fullNameField.getText(), emailField.getText(),
                    pass, phoneField.getText(), typeLabel.getText(),
                    profileImgName);

            loginQueries queries = new loginQueries();
            int value = queries.saveChanges();
            Alert alert;
            if (value>0){
                Session.setUser(Session.getUserId() , studentIdField.getText() , fullNameField.getText() , emailField.getText() , pass , phoneField.getText() ,typeLabel.getText() , Session.getProfileImage());
                loadFields();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Profile Updated");
                alert.setHeaderText(null);
                alert.setContentText("Your profile has been updated successfully.");
                alert.showAndWait();
            }else if (value!=-1){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Save Failed");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong while saving. Please try again.");
                alert.showAndWait();
            } else if (value==-1) {
                cancelChanges(event);
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Already Exists");
                alert.setHeaderText(null);
                alert.setContentText("This email address or Student ID is already linked to another account. Please use a different email or Student ID.");
                alert.showAndWait();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Form");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all required fields before saving.");
            alert.showAndWait();
        }



    }
    public void deleteAccount(ActionEvent event) throws Exception{
        Alert alert  = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete your Account");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            loginQueries queries = new loginQueries();
            boolean check = queries.deleteAccount(Session.getUserId());
            if (check){
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Account Deleted");
                success.setHeaderText(null);
                success.setContentText("Your account has been successfully deleted. Thank you for using our service.");
                success.showAndWait();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lost_and_found/login.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Parent root = loader.load();
                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                stage.show();

            }else {
                Alert failed = new Alert(Alert.AlertType.ERROR);
                failed.setTitle("Delete Failed");
                failed.setHeaderText(null);
                failed.setContentText("Account could not be deleted. Please try again.");
                failed.showAndWait();
            }

        }
    }
    public void hidePassword(ActionEvent event){
        if (passwordToggle.isSelected()) {
            passwordVisible.setText(passwordField.getText());
            passwordVisible.setVisible(true);
            passwordVisible.setManaged(true);
            passwordField.setText("");
            passwordField.setManaged(false);
        } else {
            passwordField.setText(passwordVisible.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordVisible.setVisible(false);
            passwordVisible.setManaged(false);
        }

    }
    public void cancelChanges(ActionEvent event){
        fullNameField.setText(initialUsername);
        emailField.setText(initialEmail);
        phoneField.setText(initialPhone);
        studentIdField.setText(initialStudentId);
        if (passwordToggle.isSelected()) {
            passwordVisible.setText(initialPassword);
            passwordVisible.setVisible(true);
            passwordVisible.setManaged(true);
            passwordField.setText("");
            passwordField.setManaged(false);
        } else {
            passwordField.setText(initialPassword);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordVisible.setText("");
            passwordVisible.setVisible(false);
            passwordVisible.setManaged(false);
        }
    }

    public void signOut(ActionEvent event) throws Exception{
        dashboard.signOut(event);
    }
    public void clickHome(ActionEvent event)throws Exception{
        dashboard.switchScene(event , "userDashboard.fxml");
    }
    public void clickReportItem(ActionEvent event)throws Exception{
        dashboard.switchScene(event , "report.fxml");
    }
    public void clickMatches(ActionEvent event)throws Exception{
        dashboard.switchScene(event , "matches.fxml");
    }
    public void clickAllItems(ActionEvent event) throws Exception {
        dashboard.switchScene(event , "allItems.fxml");
    }
    public void clickProfile(ActionEvent event) throws Exception {
        dashboard.switchScene(event , "profile.fxml");
    }


}
