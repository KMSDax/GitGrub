package com.example.gitgrub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;


//<TextField fx:id="userField" layoutX="25.0" layoutY="129.0" prefHeight="35.0" prefWidth="250.0" promptText="Username" />
//<TextField fx:id="userEmail" layoutX="25.0" layoutY="184.0" prefHeight="35.0" prefWidth="250.0" promptText="E-mail" />
//<PasswordField fx:id="passwordResetField" layoutX="26.0" layoutY="239.0" prefHeight="35.0" prefWidth="250.0" promptText="Enter New Password" />
//<PasswordField fx:id="passwordResetConfirmField" layoutX="26.0" layoutY="294.0" prefHeight="35.0" prefWidth="250.0" promptText="Re-enter New Password" />
//
//<Button fx:id="resetButton" layoutX="84.0" layoutY="348.0" mnemonicParsing="false" onAction="#updatePassword" prefHeight="35.0" prefWidth="132.0" text="Update Password" />

public class ForgotPasswordController {
    @FXML
    private TextField userField, userEmail;
    @FXML
    private PasswordField passwordResetField, passwordResetConfirmField;
    @FXML
    private Button resetButton;
    @FXML
    public void goToLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Log-in-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("GitGrub - Log in");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
    @FXML
    public void updatePassword(ActionEvent actionEvent) {
        // Get the new password from the password field
        String newPassword = passwordResetField.getText();

        // Get the username from the UI (assuming you have a TextField named usernameField)
        Label usernameField = new Label();
        String username = usernameField.getText(); // Assuming usernameField is a TextField for username input

        // Assuming you have a User object representing the current user
        User currentUser = getCurrentUser(username); // Pass the username to the method

        // Update the user's password
        if (currentUser != null) {
            currentUser.setPassword(newPassword);
            System.out.println("Password updated successfully!");
        } else {
            System.out.println("User not found!");
        }
    }

    private User getCurrentUser(String username) {
        // Implement this method to retrieve the current user based on the provided username
        // Search the users list, database, or any other data source to find the user
        User[] users = new User[0];
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }


}
