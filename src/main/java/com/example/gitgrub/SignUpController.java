package com.example.gitgrub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class SignUpController implements Initializable{

    public TextField usernameTextField;
    public DatePicker DOBDatePicker;
    @FXML
    private TextField emailIdTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField zipCodeTextField;
    @FXML
    private Button signUp;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private ChoiceBox stateChoice;
    private int stateId;
    @FXML
    private CheckBox dairy;
    @FXML
    private CheckBox nuts;
    @FXML
    private CheckBox shellfish;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final String SELECT_QUERY = "SELECT * FROM states"; // Include stateid in the query
        String[] stateCodes = new String[50];
        Map<String, Integer> stateIdMap = new HashMap<>(); // To store the mapping of state code to state id

        try (Connection connection = DBConn.connectDB()) {
            assert connection != null;


            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                int i = 0;
                while (resultSet.next()) {
                    int stateId = resultSet.getInt("state_id");
                    String stateCode = resultSet.getString("state_code");
                    stateCodes[i] = stateCode;
                    i++;
                    stateIdMap.put(stateCode, stateId);
                }

                // Create the ChoiceBox and set the default value to the first option
                ObservableList<String> stateList = FXCollections.observableArrayList(stateCodes);
                stateChoice.setItems(stateList);
                stateChoice.getSelectionModel().selectFirst();

                // Add an event listener to handle user selection
                stateChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        int selectedStateId = stateIdMap.get(newValue); // Get the stateid based on the selected state code
                        System.out.println("Selected State Code: " + newValue);
                        System.out.println("Corresponding State ID: " + selectedStateId);
                        stateId = selectedStateId;
                    }
                });

            }
        } catch (SQLException e) {
            // Handle the SQL exception
            printSQLException(e);
        }
    }

    public void BackToLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("log-in-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("GitGrub-Sign up");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
    public void signUp() {

        Window owner = signUp.getScene().getWindow();

        //Validation Check
        if (usernameTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your username");
            return;
        }
        if (emailIdTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your email");
            return;
        }
        if (phoneNumberTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your phone number");
            return;
        }
        // Password validation
        if (firstNameTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your first name");
            return;
        }
        if (lastNameTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your last name");
            return;
        }
        if (DOBDatePicker.getValue()==null) {
            showAlert(owner,
                    "Please enter your Date of Birth");
            return;
        }
        if (addressTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your Street Address");
            return;
        }
        if (cityTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter the City");
            return;
        }
        if (zipCodeTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your Zip Code");
            return;
        }
        if (zipCodeTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your Zip Code");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your Password");
            return;
        }
        if (confirmPassword.getText().isEmpty()) {
            showAlert(owner,
                    "Please confirm your password");
            return;
        }
        if(!passwordField.getText().equals(confirmPassword.getText())){
            showAlert(owner,
                    "Password do not match");
            return;
        }

        // Save user input from text fields
        String userid = usernameTextField.getText();
        String email = emailIdTextField.getText();
        String password = passwordField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        LocalDate DOB = DOBDatePicker.getValue();
        String streetAddress = addressTextField.getText();
        String city = cityTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String zipCode = zipCodeTextField.getText();
        ArrayList<String> allergies=new ArrayList<>();
        if(dairy.isSelected()){
            allergies.add("dairy");
        }
        if(nuts.isSelected()){
            allergies.add("nuts");
        }
        if(shellfish.isSelected()){
            allergies.add("shellfish");
        }
        // Check if an account with the entered email already exists
        boolean flag = checkForUsername(userid);
        System.out.println("Flag: "+flag);

        if (flag) {
            // Don't create account
            infoBox("An account with this username already exists.", null, "Failed");
        } else {
            // Create account
            addUser(1,userid, password, firstName, lastName, DOB, phoneNumber, email, streetAddress, city, stateId, zipCode, "https://www.google.com/url?sa=i&url=https%3A%2F%2Fstock.adobe.com%2Fsearch%3Fk%3D%2522default%2Bprofile%2Bpicture%2522&psig=AOvVaw1tPyPjFlmrNqXAf6yLmQY4&ust=1696734540913000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCOio6e764oEDFQAAAAAdAAAAABAE", allergies);
            infoBox("Registration Successful", null, "Success");

        }

    }

    public static boolean checkForUsername(String userid) {

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        final String SELECT_QUERY = "SELECT * FROM users WHERE user_id = ?";
        try (Connection connection = DBConn.connectDB()) {
            assert connection != null;
            try (// Step 2:Create a statement using connection object
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
                preparedStatement.setString(1, userid);

                System.out.println(preparedStatement);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("An account with this username already exists.");
                    return true;
                }


            }
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    } // End of checkForEmail

    public static void addUser(int roleID, String userId, String userPassword, String userFirstname, String userLastname, LocalDate userDob, String userPhonenumber, String userEmail, String userStreet, String userCity, int stateId, String userZipcode, String userProfilePic, ArrayList<String> allergies) {

        final String INSERT_QUERY = "INSERT INTO users (role_id, user_id,user_password,user_firstname,user_lastname,user_DOB,user_phonenum,user_email,user_street,user_city,state_id,user_zipcode,user_pic) " +
                "VALUES (?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        System.out.println(roleID + " " +
                userId+ " " +
                userPassword+ " " +
                userFirstname+ " " +
                userLastname+ " " +
                userDob+ " " +
                userPhonenumber+ " " +
                userEmail+ " " +
                userStreet+ " " +
                userCity+ " " +
                stateId+ " " +
                userZipcode + " " +(allergies));
        try (Connection connection = DBConn.connectDB()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
                preparedStatement.setInt(1, roleID);
                preparedStatement.setString(2, userId);
                preparedStatement.setString(3, userPassword);
                preparedStatement.setString(4, userFirstname);
                preparedStatement.setString(5, userLastname);
                preparedStatement.setString(6, String.valueOf(userDob));
                preparedStatement.setString(7, userPhonenumber);
                preparedStatement.setString(8, userEmail);
                preparedStatement.setString(9, userStreet);
                preparedStatement.setString(10, userCity);
                preparedStatement.setInt(11, stateId);
                preparedStatement.setString(12, userZipcode);
                preparedStatement.setString(13, userProfilePic);
                preparedStatement.executeUpdate();

                // Check if any rows were inserted (1 row should be inserted for success)

            }
        } catch (SQLException e) {
            // Handle the SQL exception
            printSQLException(e);
        }
    }
    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    } // End of infoBox

    private static void showAlert(Window owner, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Form Error!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    } // End of showAlert

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
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
