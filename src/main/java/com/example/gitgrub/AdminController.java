package com.example.gitgrub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    private static String loggedInUsername;  // Store the logged-in user's username

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Button loginButton;

    public static void login(String username, String password) {
        // Perform authentication here, e.g., check against a database
        if ("admin".equals(username) && "password".equals(password)) {
            loggedInUsername = username;
            // Load the admin panel
            loadAdminPanel();
        } else {
            // Show an error message for invalid credentials
            showErrorMessage("Invalid username or password");
        }
    }

    private static void loadAdminPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(AdminController.class.getResource("admin-panel.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = loginUsername.getText();
        String password = loginPassword.getText();
        login(username, password);
    }

    private static void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
