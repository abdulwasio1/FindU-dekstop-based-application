package com.example.lost_and_found;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class regController {
    @FXML
    private TextField emailField;

    @FXML
    private AnchorPane header;

    @FXML
    private Button homeButton;

    @FXML
    private Button listingButton;

    @FXML
    private Button loginButton;

    @FXML
    private TextField nameField;

    @FXML
    private AnchorPane pane;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private Button reportButton;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button signButton;

    @FXML
    private Button signUp;

    @FXML
    private TextField studentId;

    private Scene previousScene;

    private Stage stage;

    public void getPreviousLoginScene(Scene scene){
        this.previousScene = scene;
    }

    public void loginUpClick(ActionEvent event) throws Exception{
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        displayLoginScreen(stage);

    }
//    public void onSignUpBotton(ActionEvent event) throws Exception{
//        if (!nameField.getText().isEmpty() && !emailField.getText().isEmpty() && !passwordField.getText().isEmpty() && !studentId.getText().isEmpty()){
//            usersModel model = new usersModel(nameField.getText() ,studentId.getText(), phoneField.getText() , emailField.getText() , passwordField.getText());
//
//            loginQueries queries = new loginQueries();
//            int value = queries.addUser(model);
//            Alert alert;
//            if (value>0){
//                System.out.println("AddedUser");
//                alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Registration Successful!");
//                alert.setHeaderText("Your account has been created. Please login.");
//                alert.showAndWait();
//                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//                displayLoginScreen(stage);
//
//            }else {
//                System.out.println("Error");
//                alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Error!");
//                alert.setHeaderText(null);
//                alert.setContentText("Registration failed. Please try again");
//            }
//
//        }else {
//            System.out.println("Fields are incomplete");
//            Alert alert  = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error!");
//            alert.setHeaderText("All Fields must be filled");
//            alert.showAndWait();
//        }
//
//    }

    public void onSignUpBotton(ActionEvent event) throws Exception {

        // 🔹 Step 1: Get and trim input
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String student = studentId.getText().trim();
        String phone = phoneField.getText().trim();

        // 🔹 Step 2: Empty check
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || student.isEmpty() || phone.isEmpty() ) {
            showError("All required fields must be filled");
            return;
        }



        // 🔹 Step 4: Phone validation (only digits, optional field)
        if (!phone.isEmpty()) {
            for (char c : phone.toCharArray()) {
                if (!Character.isDigit(c)) {
                    showError("Phone must contain only digits");
                    return;
                }
            }

            if (phone.length() < 10 || phone.length() > 15) {
                showError("Phone must be between 10–15 digits");
                return;
            }
        }

        // 🔹 Step 5: Student ID validation (format: 023-25-0007)
        if (!student.matches("\\d{3}-\\d{2}-\\d{4}")) {
            showError("Invalid Student ID format (e.g., 023-25-0000)");
            return;
        }

        // 🔹 Step 6: Create model
        usersModel model = new usersModel(name, student, phone, email, password);

        // 🔹 Step 7: Database call
        loginQueries queries = new loginQueries();
        int value = queries.addUser(model);

        // 🔹 Step 8: Handle result
        if (value > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your account has been created. Please login.");
            alert.showAndWait();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            displayLoginScreen(stage);

        } else if (value == -1) {  // assuming duplicate case
            showError("Email or Student ID already exists");
        } else {
            showError("Registration failed. Try again.");
        }
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void displayLoginScreen(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lost_and_found/login.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

