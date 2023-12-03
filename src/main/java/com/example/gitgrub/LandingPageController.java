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
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.layout.RowConstraints;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.gitgrub.LogInController.infoBox;
import static com.example.gitgrub.LogInController.printSQLException;
import static com.example.gitgrub.Spoonacular.fetchNutritionLabel;
import static com.example.gitgrub.Spoonacular.getRandRecipe;

public class LandingPageController extends MainApplication implements Initializable {
    @FXML
    public ListView<String> userMembersListView,viewCurrentMembersInfoPane;
    @FXML
    public Label umdietLabel,umfullnameLabel,umdobLabel,umheightLabel;
    @FXML
    private DatePicker umDOBDatePicker;
    @FXML
    private TextField umfirstnameTextfield,umlastnameTextfield,umHeightTextField;
    @FXML
    private ChoiceBox<String> umdietChoiceBox;
    @FXML
    public Label usernameLabel,nameLabel,emailLabel,dobLabel,phoneLabel,addressLabel,MMCalcLabel,BFPCalcLabel,BMICalcLabel, randTitleLabel, randReadyInMinutesLabel, randServingsLabel, stepsLabel;
    @FXML
    public Button editButton,confirmChangesButton,addMembersButton,openFitnessButton,openAllergiesButton, adminPanelBtn;
    @FXML
    public Pane viewProfilePane,addMembersPane,nutrtionLabelPane,viewMembersInfoPane,fitnessPane,allergiesPane,umuserPane, adminPanelPane;
    @FXML
    private ImageView profilePic, randImageView;
    @FXML
    private Pane newsPane, editProfilePane;
    @FXML
    private TextField firstNameTextField, lastNameTextField, emailTextField, dobTextField, phoneTextField, streetTextField, cityTextField, stateTextField, zipTextField, height, weight, age;
    @FXML
    private CheckBox dairy, peanuts, shellfish, egg, gluten, grain, seafood, sesame, soy, sulfite, treenuts, wheat;
    @FXML
    public WebView randDescriptionPane;
    @FXML
    private GridPane userGridPane;
    @FXML
    private ScrollPane adminScrollPane;

    private int selectedUserHeight = 0;
    private String selectedUserDOB = "";

    private String selectedUserFirstname = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image picture = new Image(User.getInstance().getUser_profile());
        profilePic.setImage(picture);
        usernameLabel.setText(User.getInstance().getUser_id());
        umdietChoiceBox.setItems(FXCollections.observableArrayList("Keto","Vegetarian","Gluten Free"));

        loadRandRecipe();

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

        userMembersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Fetch the details of the selected user member and update the labels
                updateLabelsForSelectedUserMember(newValue);
                umuserPane.setVisible(true); // Make the Pane visible when an item is selected
            } else {
                // If nothing is selected, hide the Pane
                umuserPane.setVisible(false);
            }
        });

        String adminValue = User.getInstance().adminProperty().get();
        if ("Yes".equals(adminValue)) {
            adminPanelBtn.setVisible(true);
        }
    }

    public void showAdminPanel(){
        newsPane.setVisible(false);
        adminPanelPane.setVisible(true);

        populateUserGridPane();
    }

    public void openNews() {
        newsPane.setVisible(true);
    }

    public void loadRandRecipe(){
        JSONObject randRecipe = getRandRecipe();
        if(randRecipe == null) {
            return;
        }
        String title = randRecipe.getString("title");
        String description = "Description Unavailable: Please visit source link.";
        int readyInMinutes = randRecipe.getInt("readyInMinutes");
        int servings = randRecipe.getInt("servings");
        String sourceUrl = randRecipe.getString("sourceUrl");
        String image = "null";

        if (randRecipe.has("summary") && !randRecipe.isNull("summary")) {
            description = randRecipe.getString("summary");
        }
        if (randRecipe.has("image") && !randRecipe.isNull("image")) {
            image = randRecipe.getString("image");
        }

        randTitleLabel.setText(title);
        randDescriptionPane.getEngine().loadContent(description);
        System.out.println("Source URL: " + sourceUrl);
        randReadyInMinutesLabel.setText("Ready In Minutes: " + readyInMinutes);
        randServingsLabel.setText("Servings: " + servings);

        Image recipeImage = new Image(image);
        randImageView.setImage(recipeImage);

        // Extract and print analyzedInstructions
        JSONArray analyzedInstructions = randRecipe.getJSONArray("analyzedInstructions");
        StringBuilder stepsText = new StringBuilder("Instructions:\n");

        for (int j = 0; j < analyzedInstructions.length(); j++) {
            JSONObject instruction = analyzedInstructions.getJSONObject(j);
            JSONArray steps = instruction.getJSONArray("steps");
            for (int k = 0; k < steps.length(); k++) {
                JSONObject step = steps.getJSONObject(k);
                int stepNumber = step.getInt("number");
                String stepDescription = step.getString("step");
                stepsText.append("Step ").append(stepNumber).append(": ").append(stepDescription).append("\n");
            }
        }
        stepsLabel.setText(stepsText.toString());
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

        User.getUser().setAllergies(allergies, selectedUserFirstname);
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

    public void updateAllergies() {
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
    @FXML
    public void editMember() {
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
    @FXML
    public void openMealPlanner(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MealPlanner.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("GitGrub - Sign up");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
    @FXML
    public void populateListView() {
        int UID = Integer.parseInt(User.getUser_Uid());

        ObservableList<String> userMembers = FXCollections.observableArrayList();

        try {
            Connection connection = DBConn.connectDB();
            String sql = "SELECT user_member_firstname, user_member_lastname FROM user_member_specs WHERE UID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, UID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String firstName = resultSet.getString("user_member_firstname");

                userMembers.add(firstName);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

            // Set the populated list of user members to the ListView
            userMembersListView.setItems(userMembers);
            System.out.println(userMembers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateLabelsForSelectedUserMember(String selectedUserMember) {
        String UID = User.getUser_Uid(); // Assuming this gets the active user's ID
        selectedUserFirstname = selectedUserMember;
        try {
            Connection connection = DBConn.connectDB(); // Assuming connectDB() establishes and returns a Connection
            String sql = "SELECT user_member_firstname, user_member_lastname, user_member_height, user_member_DOB, dietValue FROM user_member_specs WHERE UID = ? AND user_member_firstname = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, UID);
            preparedStatement.setString(2, selectedUserMember);
            System.out.println(selectedUserMember);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String fullName = resultSet.getString("user_member_firstname") + " " + resultSet.getString("user_member_lastname");
                String height = resultSet.getString("user_member_height");

                // Save the height to an instance variable for later use in calcFitness
                selectedUserHeight = Integer.parseInt(height);

                String dob = resultSet.getString("user_member_DOB");

                // Save the date of birth to an instance variable for later use in calcFitness
                selectedUserDOB = dob;


                System.out.println(selectedUserHeight + " " + selectedUserDOB);
                //Height Conversion
                int heightInInches = Integer.parseInt(height);
                int feet = heightInInches / 12;
                int remainingInches = heightInInches % 12;

                String convertedHeight = feet + " feet " + remainingInches + " inches";
                String diet = resultSet.getString("dietValue");

                umfullnameLabel.setText(fullName);
                umheightLabel.setText(convertedHeight);
                umdobLabel.setText(String.valueOf(calculateAge(dob)));
                umdietLabel.setText(diet);

                allergiesPane.getChildren().get(0);
            }

            sql = "SELECT intolerance_list FROM user_member_allergy WHERE user_member_id = ? AND user_member_firstname = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, User.getUser_Uid());
            stmt.setString(2, selectedUserMember);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // Retrieve the UID from the database
                // Close resources
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("intolerance_list"));

                    String list = resultSet.getString("intolerance_list");

                    dairy.setSelected(list.contains("dairy"));
                    peanuts.setSelected(list.contains("peanuts"));
                    shellfish.setSelected(list.contains("shellfish"));
                    egg.setSelected(list.contains("egg"));
                    gluten.setSelected(list.contains("gluten"));
                    grain.setSelected(list.contains("grain"));
                    seafood.setSelected(list.contains("seafood"));
                    soy.setSelected(list.contains("soy"));
                    sulfite.setSelected(list.contains("sulfite"));
                    treenuts.setSelected(list.contains("treenuts"));
                    wheat.setSelected(list.contains("wheat"));
                    sesame.setSelected(list.contains("sesame"));
                }
            } else {
                dairy.setSelected(false);
                peanuts.setSelected(false);
                shellfish.setSelected(false);
                egg.setSelected(false);
                gluten.setSelected(false);
                grain.setSelected(false);
                seafood.setSelected(false);
                soy.setSelected(false);
                sulfite.setSelected(false);
                treenuts.setSelected(false);
                wheat.setSelected(false);
                sesame.setSelected(false);

            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void calcFitness() {
        double w = Double.parseDouble(weight.getText());
        double h = selectedUserHeight; // Use the fetched height value

        // Calculate age using the selected user's date of birth
        int a = calculateAge(selectedUserDOB);

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
    public void submitLogout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("log-in-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("GitGrub");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    private int calculateAge(String dobString) {
        LocalDate dob = LocalDate.parse(dobString); // Assuming the date is in ISO format (yyyy-MM-dd)
        LocalDate currentDate = LocalDate.now();

        return Period.between(dob, currentDate).getYears();
    }

    // Methods for AdminPanelGrid and Ban/Promote methods
    private void populateUserGridPane() {
        Label nameHeaderLabel = new Label("User's Full Name");
        Label userPhonenumHeaderLabel = new Label("Phone #");
        Label editHeaderLabel = new Label("Edit");
        Label deleteHeaderLabel = new Label("Ban");
        Label viewHeaderLabel = new Label("Promote");

        userGridPane.add(nameHeaderLabel, 0, 0);
        userGridPane.add(userPhonenumHeaderLabel, 1, 0);
        userGridPane.add(editHeaderLabel, 2, 0);
        userGridPane.add(deleteHeaderLabel, 3, 0);
        userGridPane.add(viewHeaderLabel, 4, 0);

        userGridPane.setHgap(10);
        userGridPane.setVgap(10);

        try {
            Connection connection = DBConn.connectDB();
            String sql = "SELECT user_id, user_firstName, user_lastName, user_phonenum FROM users";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();



            int row = 1;

            while (resultSet.next()) {
                String userId = resultSet.getString("user_id");
                String firstName = resultSet.getString("user_firstName");
                String lastName = resultSet.getString("user_lastName");
                String phonenum = resultSet.getString("user_phonenum");

                Label nameLabel = new Label(firstName + " " + lastName);
                Label phonenumLabel = new Label(phonenum);

                // Create CRUD buttons
                Button editButton = new Button("Edit");
                Button banButton = new Button("Ban");
                Button PromoteButton = new Button("Promote");

                // Set actions for CRUD buttons (you can modify these actions accordingly)
                editButton.setOnAction(e -> handleEditUser(userId));
                banButton.setOnAction(e -> handleBanUser(userId));
                PromoteButton.setOnAction(e -> handlePromoteUser(userId));

                userGridPane.add(nameLabel, 0, row);
                userGridPane.add(phonenumLabel, 1, row);
                userGridPane.add(editButton, 2, row);
                userGridPane.add(banButton, 3, row);
                userGridPane.add(PromoteButton, 4, row);

                // Increment row for the next user
                row++;
            }

            adminScrollPane.setContent(userGridPane);

            // Set up the ScrollPane
            adminScrollPane.setFitToWidth(true);
            adminScrollPane.setFitToHeight(true);

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleEditUser(String userId) {
        // Handle edit user action
        System.out.println("Edit user with ID: " + userId);
    }

    private void handleBanUser(String userId) {
        try {
            // Connect to the database
            Connection connection = DBConn.connectDB();

            // Prepare the SQL statement to delete the user
            String sql = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);

            // Execute the deletion
            int rowsDeleted = preparedStatement.executeUpdate();

            // Check if the deletion was successful
            if (rowsDeleted > 0) {
                System.out.println("User banned and deleted successfully.");
            } else {
                System.out.println("Failed to ban and delete user.");
            }

            // Close resources
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handlePromoteUser(String userId) {
        try {
            // Connect to the database
            Connection connection = DBConn.connectDB();

            // Prepare the SQL statement to update the role_id
            String sql = "UPDATE users SET role_id = 2 WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);

            // Execute the update
            int rowsUpdated = preparedStatement.executeUpdate();

            // Check if the update was successful
            if (rowsUpdated > 0) {
                System.out.println("User promoted successfully.");
            } else {
                System.out.println("Failed to promote user.");
            }

            // Close resources
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}