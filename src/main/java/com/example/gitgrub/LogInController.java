package com.example.gitgrub;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInController {
    @FXML
    private Button loginButton;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void login(ActionEvent event) throws IOException {
       // Window owner = loginButton.getScene().getWindow();

        if (userField.getText().isEmpty()) {
            showAlert(null, "Please enter your username");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(null, "Please enter a password");
            return;
        }

        String userId = userField.getText();
        String password = passwordField.getText();

        boolean flag = validate(userId, password);

        if (flag) {
            infoBox("Login Successful!", null, "Success");
            // Load the main application window
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("landing-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setTitle("GitGrub");
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();

        } else {
            infoBox("Please enter correct Username and Password", null, "Failed");
        }
    }
    @FXML
    public void goToSignUp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("sign-up-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 535, 400);
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("GitGrub - Sign up");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
    @FXML
    public void goToForgotPassword(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("forgot-my-password-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("GitGrub - Forgot Password");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
    @FXML
    private Button adminlogin;
    public static void infoBox(String infoMessage, String headerText, String title) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(infoMessage);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.showAndWait();
        });
    }

    private static void showAlert(Window owner, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Form Error!");
        alert.setHeaderText(null);
        alert.setContentText(message);
      //  alert.initOwner(owner);
        alert.show();
    }
    public static boolean validate(String userId, String password){
        final String SELECT_QUERY = "SELECT * FROM users WHERE user_id = ? and user_password = ?";

        try (Connection connection = DBConn.connectDB()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
                preparedStatement.setString(1, userId);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM states WHERE state_id = ?");
                    preparedStatement2.setInt(1, Integer.parseInt(resultSet.getString(12)));
                    ResultSet stateResultSet = preparedStatement2.executeQuery();

                    if (stateResultSet.next()) {
                        User.initializeUser(Integer.parseInt(resultSet.getString(2)),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5),
                                resultSet.getString(6),
                                resultSet.getString(7),
                                resultSet.getString(8),
                                resultSet.getString(9),
                                resultSet.getString(10),
                                resultSet.getString(11),
                                stateResultSet.getString(2),
                                Integer.parseInt(resultSet.getString(13)),
                                resultSet.getString(14));
                        System.out.println("Logged in!");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }
    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

}

