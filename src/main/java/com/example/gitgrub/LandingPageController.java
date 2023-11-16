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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.gitgrub.LogInController.infoBox;
import static com.example.gitgrub.LogInController.printSQLException;
import static com.example.gitgrub.Spoonacular.fetchNutritionLabel;

public class LandingPageController extends MainApplication implements Initializable {
    @FXML
    public ListView<String> userMembersListView,viewCurrentMembersInfoPane;
    @FXML
    private DatePicker umDOBDatePicker;

    @FXML
    private TextField umfirstnameTextfield,umlastnameTextfield,umHeightTextField;

    @FXML
    private ChoiceBox<String> umdietChoiceBox;

    @FXML
    private Button addNewMembersButton;

    @FXML
    private Button cancel;
    @FXML
    public Label usernameLabel,nameLabel,emailLabel,dobLabel,phoneLabel,addressLabel,MMCalcLabel,BFPCalcLabel,BMICalcLabel;
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

        populateListView();
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

    @FXML
    private void addNewMember() {
        String firstName = umfirstnameTextfield.getText();
        String lastName = umlastnameTextfield.getText();
        String diet = umdietChoiceBox.getValue();
        String heightStr = umHeightTextField.getText();
        LocalDate dob = umDOBDatePicker.getValue();

        // Validate input fields...

        if (firstName.isEmpty() || lastName.isEmpty() || diet == null || heightStr.isEmpty() || dob == null) {
            // Handle empty fields or show an alert to the user
            return;
        }

        int height = Integer.parseInt(heightStr);
        int UID = Integer.parseInt(Objects.requireNonNull(User.getUser_Uid())); // Fetch the current user's ID from your User class

        try {
            Connection connection = DBConn.connectDB();

            String sql = "INSERT INTO user_member_specs (UID, user_member_firstname, user_member_lastname, dietValue, user_member_height, user_member_DOB) VALUES (?, ?, ?, ?, ?, ?)";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, UID);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, diet);
            preparedStatement.setInt(5, height);
            preparedStatement.setDate(6, java.sql.Date.valueOf(dob));

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            // Clear fields or show success message
            umfirstnameTextfield.clear();
            umlastnameTextfield.clear();
            umHeightTextField.clear();
            umDOBDatePicker.getEditor().clear();

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void updateAllergies(ActionEvent actionEvent) {
    }
    @FXML
    public void openAllergies() {
        if(allergiesPane.isVisible()){
            allergiesPane.setVisible(false);
        }else{
            allergiesPane.setVisible(true);
        }
        fitnessPane.setVisible(false);
    }
    @FXML
    public void openFitness() {
        if(fitnessPane.isVisible()){
            fitnessPane.setVisible(false);
        }else{
            fitnessPane.setVisible(true);
        }
        allergiesPane.setVisible(false);
    }

    //Calculates the fitness of the individual using the fitness pane
    @FXML
    public void calcFitness(ActionEvent actionEvent) {
        double w = Double.parseDouble(weight.getText());
        double h = Double.parseDouble(height.getText());
        int a = Integer.parseInt(age.getText());

        // Calculate BMI
        double bmi = (w / (h * h)) * 703;
        DecimalFormat df = new DecimalFormat("#.##");
        String roundedBMI = df.format(bmi);
        BMICalcLabel.setText(roundedBMI);

        // Calculate BFP
        double BFP = (1.20 * bmi) + (0.23 * a) - 16.2;
        String roundedBFP = df.format(BFP);
        BFPCalcLabel.setText(roundedBFP);

        // Calculate MM
        double MM = BFP - 100;
        String roundedMM = df.format(MM);
        MMCalcLabel.setText(roundedMM);
    }
    @FXML
    public void editMember(ActionEvent actionEvent) {
    }
    @FXML
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

    public void populateListView() {
        int UID = Integer.parseInt(User.getUser_Uid()); // Assuming this gets the active user's ID

        // Query the database to get user members for the active user
        ObservableList<String> userMembers = FXCollections.observableArrayList();

        try {
            Connection connection = DBConn.connectDB(); // Assuming connectDB() establishes and returns a Connection
            String sql = "SELECT user_member_firstname, user_member_lastname FROM user_member_specs WHERE UID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, UID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String firstName = resultSet.getString("user_member_firstname");
                String lastName = resultSet.getString("user_member_lastname");
                String fullName = firstName + " " + lastName;

                userMembers.add(fullName);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

            // Set the populated list of user members to the ListView
            userMembersListView.setItems(userMembers);
            System.out.println(userMembers);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions appropriately
        }
    }
}
