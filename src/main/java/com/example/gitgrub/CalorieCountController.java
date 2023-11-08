package com.example.gitgrub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import java.net.URL;
import java.util.ResourceBundle;

public class CalorieCountController implements Initializable {

    @FXML
    private TextField mealNameField;
    @FXML
    private TextField foodItemField;
    @FXML
    private TextField portionSizeField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField weightField;
    @FXML
    private ListView<Meal> itemsListView;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button calculateButton;
    @FXML
    private Label caloriesResLabel;

    private ObservableList<Meal> listItems = FXCollections.observableArrayList();

    private void addButtonAction() {
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                // Check if all fields are filled
                if (mealNameField.getText().isEmpty() || foodItemField.getText().isEmpty() ||
                        portionSizeField.getText().isEmpty() || ageField.getText().isEmpty() ||
                        heightField.getText().isEmpty() || weightField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are mandatory!");
                    return;
                }

                // Parse input values
                String mealName = mealNameField.getText().trim();
                String foodName = foodItemField.getText().trim();
                int size = Integer.parseInt(portionSizeField.getText().trim());
                int age = Integer.parseInt(ageField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());
                double weight = Double.parseDouble(weightField.getText().trim());

                // Create a new Meal object and add it to the list
                listItems.add(new Meal(mealName, foodName, size));

                // Clear input fields
                mealNameField.clear();
                foodItemField.clear();
                portionSizeField.clear();
                ageField.clear();
                heightField.clear();
                weightField.clear();

                itemsListView.setItems(listItems);
            }
        });
    }

    private void removeButtonAction() {
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (itemsListView.getSelectionModel().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select at least 1 item from the list!");
                    return;
                }
                itemsListView.getItems().removeAll(itemsListView.getSelectionModel().getSelectedItems());
            }
        });
    }

    private void calculateButtonAction() {
        calculateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (itemsListView.getItems().isEmpty() || ageField.getText().isEmpty() ||
                        heightField.getText().isEmpty() || weightField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are mandatory!");
                    return;
                }

                // Parse age, height, and weight
                int age = Integer.parseInt(ageField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());
                double weight = Double.parseDouble(weightField.getText().trim());

                // Calculate BMR using the Mifflin-St Jeor equation
                double bmr = 10 * weight + 6.25 * height - 5 * age + 5;

                // Calculate total daily calories based on activity factor (assuming sedentary activity)
                double activityFactor = 1.2;
                int totalCalories = (int) (bmr * activityFactor);

                // Update the calories label
                caloriesResLabel.setText("Calories: " + totalCalories);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        itemsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        addButtonAction();
        removeButtonAction();
        calculateButtonAction();
    }
}
