package com.example.gitgrub;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.web.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Imported Dax's infoBox and printSQLException methods from the LogInController
import static com.example.gitgrub.LogInController.infoBox;
import static com.example.gitgrub.LogInController.printSQLException;
import static com.example.gitgrub.Spoonacular.fetchNutritionLabel;
import static com.example.gitgrub.Spoonacular.fetchRecipeInformation;

public class LandingPageController extends MainApplication implements Initializable {
    public Label nameLabel;
    public Label emailLabel;
    public Label dobLabel;
    public Label phoneLabel;
    public Label addressLabel;
    public Button editButton;
    public Button confirmChangesButton;
    public Pane viewProfilePane;
    public Button addMembersButton;
    public Pane addMembersPane;
    public Pane nutrtionLabelPane;

    @FXML
    private Button news;
    @FXML
    private ImageView profilePic;
    @FXML
    private Pane newsPane, cookbookPane, page1, page2, editProfilePane;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField firstNameTextField, lastNameTextField, emailTextField, dobTextField, phoneTextField, streetTextField, cityTextField, stateTextField, zipTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image picture = new Image(User.getInstance().getUser_profile());
        profilePic.setImage(picture);

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
        usernameLabel.setText(username);

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


    public void openCookbook() {
        cookbookPane.setVisible(true);
        newsPane.setVisible(false);

        // Create layouts for page1 and page2
        VBox recipeLayoutPage1 = new VBox(20);
        VBox recipeLayoutPage2 = new VBox(20);

        // Fetch and display recipes
        for (int i = 0; i < 4; i++) {
            int recipeId = i + 1; // Replace with the appropriate recipe IDs
            JSONObject recipeInfo = fetchRecipeInformation(recipeId);

            if (recipeInfo != null) {
                String title = recipeInfo.getString("title");
                String description = recipeInfo.optString("summary", "Description Unavailable: Please visit source link");
                String sourceUrl = recipeInfo.getString("sourceUrl");
                String imageUrl = recipeInfo.optString("image", "null");

                // Create nodes to display recipe information
                Label titleLabel = new Label("Title: " + title);
                Label descriptionLabel = new Label("Description: " + description);
                Hyperlink sourceLink = new Hyperlink("Source URL");
                sourceLink.setOnAction(e -> getHostServices().showDocument(sourceUrl));

                Image recipeImage = new Image(imageUrl);
                ImageView imageView = new ImageView(recipeImage);
                imageView.setFitHeight(75);
                imageView.setFitWidth(150);

                // Create a container for each recipe
                VBox recipeContainer = new VBox(10);
                recipeContainer.getChildren().addAll(titleLabel, descriptionLabel, sourceLink, imageView);

                // Add the recipe to the appropriate layout (page1 or page2)
                if (i % 2 == 0) {
                    recipeLayoutPage1.getChildren().add(recipeContainer);
                } else {
                    recipeLayoutPage2.getChildren().add(recipeContainer);
                }
            }
        }

        // Clear previous content and add the recipe layouts to page1 and page2
        page1.getChildren().clear();
        page1.getChildren().add(recipeLayoutPage1);

        page2.getChildren().clear();
        page2.getChildren().add(recipeLayoutPage2);
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
        viewProfilePane.setVisible(true);
        addMembersPane.setVisible(false);
        editProfilePane.setVisible(false);
    }
    public void openEditProfile() {
        editProfilePane.setVisible(true);
        viewProfilePane.setVisible(false);
        addMembersPane.setVisible(false);
    }

    public void openMembersPane(){
        addMembersPane.setVisible(true);
        viewProfilePane.setVisible(false);
        editProfilePane.setVisible(false);
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


}
