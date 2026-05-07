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

public class adminProfileControl implements Initializable {

    @FXML private TextField  fullNameField;
    @FXML private TextField  emailField;
    @FXML private TextField  phoneField;
    @FXML private TextField  roleField;

    @FXML private PasswordField passwordField;
    @FXML private TextField     passwordVisible;
    @FXML private ToggleButton  passwordToggle;

    @FXML private Label userNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label typeLabel;

    @FXML private Button home;
    @FXML private Button manageItems;
    @FXML private Button manageMatches;
    @FXML private Button manageUsers;
    @FXML private Button profile;

    private String initialUsername;
    private String initialEmail;
    private String initialPhone;
    private String initialPassword;

    private final userDashboardControl dashboard = new userDashboardControl();

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label usernameLabel;

    private String selectedProfileImagePath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Button[] navButtons = { home, manageItems, manageMatches, profile, manageUsers };
        selectionUIHelper.setSelected(profile, navButtons);
        usernameLabel.setText(adminSession.getAdminName());
        loadFields();
        if (adminSession.getProfileImagePath() != null) {

            File file = new File(
                    System.getProperty("user.home") +
                            "/FindU/profiles/" +
                            adminSession.getProfileImagePath()
            );

            if (file.exists()) {
                profileImageView.setImage(new Image(file.toURI().toString()));
            }
        }
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

            try {
                String destDir = System.getProperty("user.home") + "/FindU/profiles/";
                File dir = new File(destDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + file.getName();

                File destFile = new File(destDir + fileName);

                Files.copy(file.toPath(), destFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                adminSession.setProfileImagePath(fileName);

                profileImageView.setImage(new Image(destFile.toURI().toString()));

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Image Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to save profile image.");
                alert.showAndWait();
            }
        }
    }

    private void loadFields() {
        fullNameField.setText(adminSession.getAdminName());
        emailField.setText(adminSession.getEmail());
        roleField.setText(adminSession.getRole());

        String phone = adminSession.getPhone() != null ? adminSession.getPhone() : "";
        phoneField.setText(phone);
        phoneLabel.setText(phone);

        String password = adminSession.getPassword() != null ? adminSession.getPassword() : "";
        passwordField.setText(password);
        passwordVisible.setVisible(false);
        passwordVisible.setManaged(false);
        passwordToggle.setSelected(false);

        userNameLabel.setText(adminSession.getAdminName());
        emailLabel.setText(adminSession.getEmail());
        typeLabel.setText(adminSession.getRole());

        initialUsername = fullNameField.getText();
        initialEmail    = emailField.getText();
        initialPhone    = phoneField.getText();
        initialPassword = password;
    }

    @FXML
    public void onClickChanges(ActionEvent event) throws Exception {
        String name  = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String pass  = passwordToggle.isSelected()
                ? passwordVisible.getText().trim()
                : passwordField.getText().trim();


        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Form",
                    "Please fill in all required fields before saving.");
            return;
        }

        if (!phone.matches("[0-9]+")) {
            showAlert(Alert.AlertType.WARNING, "Invalid Phone Number",
                    "Phone number must contain digits only.");
            return;
        }
        if (phone.length() < 10 || phone.length() > 15) {
            showAlert(Alert.AlertType.WARNING, "Invalid Phone Length",
                    "Phone number must be between 10 and 15 digits.");
            return;
        }

        if (!email.endsWith("@iba-suk.edu.pk")) {
            showAlert(Alert.AlertType.WARNING, "Invalid Email",
                    "Email must be in the format: user@iba-suk.edu.pk");
            return;
        }

        adminSession.setAdmin(adminSession.getAdminId(), name, email, pass, phone, typeLabel.getText());
        queriesAdmin queries = new queriesAdmin();
        int result = queries.saveChanges();

        if (result > 0) {
            loadFields();
            showAlert(Alert.AlertType.INFORMATION, "Profile Updated",
                    "Your profile has been updated successfully.");
        } else if (result == -1) {
            cancelChanges(event);
            showAlert(Alert.AlertType.WARNING, "Already Exists",
                    "This email is already linked to another account. Please use a different email.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Save Failed",
                    "Something went wrong while saving. Please try again.");
        }
    }

    @FXML
    public void deleteAccount(ActionEvent event) throws Exception {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Account");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete your account? This cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            loginQueries queries = new loginQueries();
            boolean deleted = queries.deleteAccount(adminSession.getAdminId());

            if (deleted) {
                showAlert(Alert.AlertType.INFORMATION, "Account Deleted",
                        "Your account has been successfully deleted.");
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(
                        getClass().getResource("/com/example/lost_and_found/login.fxml"));
                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                stage.show();
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Failed",
                        "Account could not be deleted. Please try again.");
            }
        }
    }

    @FXML
    public void hidePassword(ActionEvent event) {
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

    @FXML
    public void cancelChanges(ActionEvent event) {
        fullNameField.setText(initialUsername);
        emailField.setText(initialEmail);
        phoneField.setText(initialPhone);

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

    @FXML void clickHome(ActionEvent event)         throws Exception { dashboard.switchScene(event, "adminDash.fxml");    }
    @FXML void clickManageItems(ActionEvent event)  throws Exception { dashboard.switchScene(event, "adminItems.fxml");   }
    @FXML void clickManageMatches(ActionEvent event)throws Exception { dashboard.switchScene(event, "adminMatch.fxml");   }
    @FXML void clickManageUsers(ActionEvent event)  throws Exception { dashboard.switchScene(event, "usersAdmin.fxml");   }
    @FXML void clickProfile(ActionEvent event)      throws Exception { dashboard.switchScene(event, "adminProfile.fxml"); }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void signOut(ActionEvent event) throws Exception{
        dashboard.signOut(event);
    }
}