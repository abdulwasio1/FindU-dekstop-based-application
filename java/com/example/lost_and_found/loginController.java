package com.example.lost_and_found;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController {
    @FXML
   private AnchorPane pane;

    @FXML
    private TextField passwordVisible;

    @FXML
    private ToggleButton passwordToggle;

    @FXML
    private AnchorPane header;

    @FXML
    private Button homeButton;

    @FXML
    private Button listingButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button reportButton;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button signButton;

    @FXML
    private TextField emailField;


    @FXML
    private PasswordField passwordField;

    private String initialPassword;

    private Stage stage ;



    public void loginClick(ActionEvent event) throws Exception {

        String email = emailField.getText();

        // ✅ jo field visible hai usse password lo
        String password = passwordToggle.isSelected()
                ? passwordVisible.getText()
                : passwordField.getText();
        usersModel model = new usersModel(email , password);

        loginQueries queries = new loginQueries();
        boolean value = queries.loginCheck(model);
        if (value){
            FXMLLoader loader;
            if (Session.getRole().equalsIgnoreCase("user") && !Session.getRole().isEmpty()){
                 loader = new FXMLLoader(getClass().getResource("/com/example/lost_and_found/userDashboard.fxml"));
            }else{
                 loader = new FXMLLoader(getClass().getResource("/com/example/lost_and_found/adminDash.fxml"));
            }
           Parent root = loader.load();
           stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
           stage.setScene(new Scene(root));
           stage.setResizable(false);
           stage.centerOnScreen();
           stage.show();
        }else {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Error!");
           alert.setHeaderText(null);
           alert.setContentText("Invalid Email or Password, Please Try Again");
           alert.showAndWait();
        }
    }
    public void signUpUpperClick(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene currentScene = ((Node) event.getSource()).getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lost_and_found/register.fxml"));
        Parent root = loader.load();
        regController control = loader.getController();
        control.getPreviousLoginScene(currentScene);
        stage.setScene(new Scene(root));
        stage.show();


    }

    public void hidePassword(ActionEvent event) {
        if (passwordToggle.isSelected()) {

            //CODE FROM GPT
            passwordVisible.setText(passwordField.getText());
            passwordVisible.setOpacity(0.51);
            passwordVisible.setMouseTransparent(false);

            passwordField.setOpacity(0);
            passwordField.setMouseTransparent(true);
        } else {
            passwordField.setText(passwordVisible.getText());
            passwordField.setOpacity(0.51);
            passwordField.setMouseTransparent(false);

            passwordVisible.setOpacity(0);
            passwordVisible.setMouseTransparent(true);
        }

    }


    }
