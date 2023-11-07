
package com.example.gitgrub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import java.util.ArrayList;
import java.util.List;

public class PasswordResetController {

    private final List<User> users; // Assume this list holds your user data

    @FXML
    private PasswordField passwordResetField; // Password field in the UI

    // Constructor (or initialize the users list in another way)
    public PasswordResetController() {
        users = new ArrayList<>();
        // Populate the users list with sample users (for demonstration purposes)
        users.add(new User("username1", "password1"));
        users.add(new User("username2", "password2"));
        // Add more users if necessary
    }

    @FXML
    public void updatePassword(ActionEvent actionEvent) {
        // Get the new password from the password field
        String newPassword = passwordResetField.getText();

        // Assuming you have a User object representing the current user
        User currentUser = getCurrentUser(); // Implement this method to get the current user

        // Update the user's password
        if (currentUser != null) {
            currentUser.setPassword(newPassword);
            System.out.println("Password updated successfully!");
        } else {
            System.out.println("User not found!");
        }
    }

    private User getCurrentUser() {
        // Implement this method to retrieve the current user based on your application's logic
        // For example, you could search the users list by username
        String username = "username1"; // Replace this with the actual username or obtain it from the UI
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    // User class for demonstration purposes
    private class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String newPassword) {
            this.password = newPassword;
        }
    }
}