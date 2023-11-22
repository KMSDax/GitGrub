package com.example.gitgrub;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class CalorieCountController {

    @FXML
    private TextField mealNameField;

    @FXML
    private TextField caloriesField;

    @FXML
    private VBox mealSummaryBox;

    @FXML
    private Label totalCaloriesLabel;

    public void addMeal() {
        String mealName = mealNameField.getText();
        String caloriesText = caloriesField.getText();

        if (!mealName.isEmpty() && !caloriesText.isEmpty()) {
            try {
                int calories = Integer.parseInt(caloriesText);

                // Update the meal summary
                Label mealLabel = new Label(mealName + ": " + calories + " calories");
                mealSummaryBox.getChildren().add(mealLabel);

                // Update the total calories label
                updateTotalCalories(calories);
            } catch (NumberFormatException e) {
                // Handle invalid calorie input
                System.out.println("Invalid calories input");
            }
        }
    }

    private void updateTotalCalories(int calories) {
        String totalCaloriesText = totalCaloriesLabel.getText();
        int currentTotalCalories = Integer.parseInt(totalCaloriesText.split(":")[1].trim());
        int newTotalCalories = currentTotalCalories + calories;
        totalCaloriesLabel.setText("Total Calories: " + newTotalCalories);
    }
}
