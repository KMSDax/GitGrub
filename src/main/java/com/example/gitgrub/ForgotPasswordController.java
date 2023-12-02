package com.example.gitgrub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//<TextField fx:id="userField" layoutX="25.0" layoutY="129.0" prefHeight="35.0" prefWidth="250.0" promptText="Username" />
//<TextField fx:id="userEmail" layoutX="25.0" layoutY="184.0" prefHeight="35.0" prefWidth="250.0" promptText="E-mail" />
//<PasswordField fx:id="passwordResetField" layoutX="26.0" layoutY="239.0" prefHeight="35.0" prefWidth="250.0" promptText="Enter New Password" />
//<PasswordField fx:id="passwordResetConfirmField" layoutX="26.0" layoutY="294.0" prefHeight="35.0" prefWidth="250.0" promptText="Re-enter New Password" />
//
//<Button fx:id="resetButton" layoutX="84.0" layoutY="348.0" mnemonicParsing="false" onAction="#updatePassword" prefHeight="35.0" prefWidth="132.0" text="Update Password" />

public class ForgotPasswordController {
    @FXML
    private TextField emailTF, userNameTF;
    @FXML
    private PasswordField newPasswordTF, confirmNewPasswordTF;

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
        String username = userNameTF.getText();
        String email = emailTF.getText();
        if(newPasswordTF.getText().equals(confirmNewPasswordTF.getText())) {
            String newPassword = newPasswordTF.getText();
            try {
                Connection connection = DBConn.connectDB();
                String checkUserSql = "SELECT uid FROM users WHERE user_id = ? AND user_email = ?";
                PreparedStatement checkUserStatement = connection.prepareStatement(checkUserSql);

                checkUserStatement.setString(1, username);
                checkUserStatement.setString(2, email);

                ResultSet checkUserResult = checkUserStatement.executeQuery();

                if (checkUserResult.next()) {
                    // User exists, proceed with updating the password
                    String updatePasswordSql = "UPDATE users SET user_password = ? WHERE user_id = ?";
                    PreparedStatement updatePasswordStatement = connection.prepareStatement(updatePasswordSql);

                    updatePasswordStatement.setString(1, newPassword);
                    updatePasswordStatement.setString(2, username);

                    // Execute the update
                    int rowsAffected = updatePasswordStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Password updated successfully!");
                    } else {
                        System.out.println("Failed to update password.");
                    }

                    // Close resources
                    updatePasswordStatement.close();
                } else {
                    System.out.println("User not found.");
                }

                // Close resources
                checkUserResult.close();
                checkUserStatement.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace(); // Handle SQL exceptions appropriately
            }
        } else {
            System.out.println("New passwords do not match.");
        }
    }
}
