package com.example.gitgrub;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;

import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.web.*;

import java.io.IOException;

// Imported Dax's infoBox and printSQLException methods from the LogInController
import static com.example.gitgrub.LogInController.infoBox;
import static com.example.gitgrub.LogInController.printSQLException;
import static com.example.gitgrub.Spoonacular.fetchNutritionLabel;
import static com.example.gitgrub.Spoonacular.fetchRecipeInformation;

public class LandingPageController extends MainApplication implements Initializable {
    @FXML
    public Label usernameLabel,nameLabel,emailLabel,dobLabel,phoneLabel,addressLabel;
    @FXML
    public Button editButton,confirmChangesButton,addMembersButton,openFitnessButton,openAllergiesButton;
    @FXML
    public Pane viewProfilePane,addMembersPane,nutrtionLabelPane,viewMembersInfoPane,fitnessPane,allergiesPane;

    @FXML
    private ImageView profilePic;
    @FXML
    private Pane newsPane, cookbookPane, page1, page2, editProfilePane;
    @FXML

    private TextField firstNameTextField, lastNameTextField, emailTextField, dobTextField, phoneTextField, streetTextField, cityTextField, stateTextField, zipTextField, height, weight, age;

    @FXML
    private CheckBox dairy, peanuts, shellfish, egg, gluten, grain, seafood, sesame, soy, sulfite, treenuts, wheat;
    @FXML
    private ChoiceBox umdietChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image picture = new Image(User.getInstance().getUser_profile());
        profilePic.setImage(picture);
        usernameLabel.setText(User.getInstance().getUser_id());
        umdietChoiceBox.setItems(FXCollections.observableArrayList("Keto","Vegetarian","Gluten Free"));

        // Initializes  user's general information for the Profile Pane Display
        String firstName = User.getInstance().getUser_firstname();
        String lastName = User.getInstance().getUser_lastname();
        String email = User.getInstance().getUser_email();
        String dob = User.getInstance().getUser_dob();
        String phoneNum = User.getInstance().getUser_phonenumber();
        String username = User.getInstance().getUser_id();
        firstNameTextField.setText(firstName);
        lastNameTextField.setText(lastName);
        emailTextField.setText(email);
        dobTextField.setText(dob);
        phoneTextField.setText(phoneNum);

        nameLabel.setText(firstName + " " + lastName);
        emailLabel.setText(email);
        dobLabel.setText(dob);
        phoneLabel.setText(phoneNum);
        // Initializes  user's Location information for the Profile Pane Display
        String street = User.getInstance().getUser_street();
        String city = User.getInstance().getUser_city();
        String state = User.getInstance().getState_id();
        int zip = User.getInstance().getUser_zipcode();
        streetTextField.setText(street);
        cityTextField.setText(city);
        stateTextField.setText(state);
        zipTextField.setText(String.valueOf(zip));

        addressLabel.setText(street + ", " + city + ", \n" + state + " " + zip);
    }

    public void openNews() {
        newsPane.setVisible(true);
        cookbookPane.setVisible(false);
    }


    public void openCookbook(ActionEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("cookbook-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setTitle("GitGrub - Cookbook");
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();

    }

    // Testing fetchNutritionLabel through the bookmarks label
    public void showRecipeNutritionLabel() {
        WebView webView = new WebView();

        String nutritionLabelContent = fetchNutritionLabel(1);
        if (nutritionLabelContent != null) {
            webView.getEngine().loadContent(nutritionLabelContent);

            webView.setPrefSize(nutrtionLabelPane.getWidth()-10, nutrtionLabelPane.getHeight()-10);
            webView.setMaxSize(nutrtionLabelPane.getWidth(), nutrtionLabelPane.getHeight());
            webView.setTranslateX(10);
            webView.setTranslateY(10);
            nutrtionLabelPane.getChildren().add(webView);
        }
    }
    // Shows the profile page on ProfilePicture click and allows for profile editing
    public void openProfile() {
        if(viewProfilePane.isVisible()) {
            viewProfilePane.setVisible(false);
        }else{
            viewProfilePane.setVisible(true);
        }

            editProfilePane.setVisible(false);
            addMembersPane.setVisible(false);
        viewMembersInfoPane.setVisible(false);
    }

    public void openEditProfile() {
        editProfilePane.setVisible(true);
        viewProfilePane.setVisible(false);
        addMembersPane.setVisible(false);
        viewMembersInfoPane.setVisible(false);
    }

    public void openMembersPane(){
        addMembersPane.setVisible(true);
        viewProfilePane.setVisible(false);
        editProfilePane.setVisible(false);
        viewMembersInfoPane.setVisible(false);
    }
    public void openViewMembersPane(){
        addMembersPane.setVisible(false);
        viewProfilePane.setVisible(false);
        editProfilePane.setVisible(false);
        viewMembersInfoPane.setVisible(true);
    }

    // Confirm Profile Changes button on landing Page
    public void confirmProfileChanges(){
        User currentUser = User.getInstance();
        if(currentUser != null){
            String newFirstName = firstNameTextField.getText();
            String newLastName = lastNameTextField.getText();
            String newEmail = emailTextField.getText();
            String newDOB = dobTextField.getText();
            String newPhoneNum = phoneTextField.getText();
            String newStreet = streetTextField.getText();
            String newCity = cityTextField.getText();
            // String newState = stateTextField.getText();
            int newZip = Integer.parseInt(zipTextField.getText());

            // Update the user's first name and last name in the instance
            currentUser.setUser_firstname(newFirstName);
            currentUser.setUser_lastname(newLastName);
            currentUser.setUser_email(newEmail);
            currentUser.setUser_dob(newDOB);
            currentUser.setUser_phonenumber(newPhoneNum);
            currentUser.setUser_street(newStreet);
            currentUser.setUser_city(newCity);
            // currentUser.setState_id(newState);
            currentUser.setUser_zipcode(newZip);

            // Calls updateUserInDatabase method to update the user's information in the database
            if (updateUserInDatabase(currentUser)) {
                infoBox("Profile changes saved!", null, "Success");
            } else {
                infoBox("Failed to save profile changes.", null, "Failed");
            }
        } else {
            infoBox("User instance not found. Please log in.", null, "Failed");
        }
        editProfilePane.setVisible(!editProfilePane.isVisible());
        viewProfilePane.setVisible(!viewProfilePane.isVisible());
    }



    // Updates the Database with new TextField Information on Profile Pane
    // Works in conjunction with ConfirmProfileChanges
    private boolean updateUserInDatabase(User user) {
        final String UPDATE_QUERY = "UPDATE users SET user_firstname = ?, user_lastname = ?, user_email = ?, " +
                "user_dob = ?, user_phonenum = ?, user_street = ?, user_city = ?, user_zipcode = ? WHERE user_id = ?";

        try (Connection connection = DBConn.connectDB()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
                preparedStatement.setString(1, user.getUser_firstname());
                preparedStatement.setString(2, user.getUser_lastname());
                preparedStatement.setString(3, user.getUser_email());
                preparedStatement.setString(4, user.getUser_dob());
                preparedStatement.setString(5, user.getUser_phonenumber());
                preparedStatement.setString(6, user.getUser_street());
                preparedStatement.setString(7, user.getUser_city());
                preparedStatement.setString(8, String.valueOf(user.getUser_zipcode()));
                preparedStatement.setString(9, user.getUser_id());

                int rowsUpdated = preparedStatement.executeUpdate();
                return rowsUpdated > 0;
            }
        }  catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }
public void getintolerence(){
    ArrayList<String> allergies=new ArrayList<>();
    if(dairy.isSelected()){
        allergies.add("dairy");
    }
    if(peanuts.isSelected()){
        allergies.add("peanuts");
    }
    if(shellfish.isSelected()){
        allergies.add("shellfish");
    }
    if(egg.isSelected()){
        allergies.add("egg");
    }
    if(gluten.isSelected()){
        allergies.add("gluten");
    }
    if(grain.isSelected()){
        allergies.add("grain");
    }
    if(seafood.isSelected()){
        allergies.add("seafood");
    }
    if(soy.isSelected()){
        allergies.add("soy");
    }
    if(sulfite.isSelected()){
        allergies.add("sulfite");
    }
    if(treenuts.isSelected()){
        allergies.add("treenuts");
    }
    if(wheat.isSelected()){
        allergies.add("wheat");
    }
    if(sesame.isSelected()){
        allergies.add("sesame");
    }
    System.out.println(allergies);

}

    public void addNewMember(ActionEvent actionEvent) {
    }

    public void updateAllergies(ActionEvent actionEvent) {
    }

    public void openAllergies() {
        if(allergiesPane.isVisible()){
            allergiesPane.setVisible(false);
        }else{
            allergiesPane.setVisible(true);
        }
        fitnessPane.setVisible(false);
    }

    public void openFitness() {
        if(fitnessPane.isVisible()){
            fitnessPane.setVisible(false);
        }else{
            fitnessPane.setVisible(true);
        }
        allergiesPane.setVisible(false);
    }

    public void calcFitness(ActionEvent actionEvent) {
        double w = Double.parseDouble(weight.getText());
        double h = Double.parseDouble(height.getText());
        int a = Integer.parseInt(age.getText());
        System.out.println("Weight: " + w);
        System.out.println("Height: " + h);
        System.out.println("age: " + a);
        double bmi = (w / (h * h )) * 703;
        System.out.println("bmi: " + bmi);
        double BFP = (1.20 * bmi) + (0.23 * a) - 16.2;
        System.out.println("bfp: " + BFP);
        double MM = BFP - 100;
        System.out.println("MM: " + MM);
    }

    public void editMember(ActionEvent actionEvent) {
    }

    public void openRefrigeratorPane(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fridge-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("GitGrub - Sign up");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
}
