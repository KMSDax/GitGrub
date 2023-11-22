package com.example.gitgrub;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MealPlannerController {

    @FXML
    private TextField mealNameField;

    @FXML
    private TextField caloriesField;

    @FXML
    private TextField proteinField;

    @FXML
    private TextField carbsField;

    @FXML
    private TextField fatField;

    @FXML
    private ListView<String> mealListView;

    @FXML
    private Label totalNutritionLabel;

    private MealPlanner mealPlanner;

    public MealPlannerController() {
        this.mealPlanner = new MealPlanner();
    }

    @FXML
    public void addMeal() {
        String name = mealNameField.getText();
        int calories = Integer.parseInt(caloriesField.getText());
        double protein = Double.parseDouble(proteinField.getText());
        double carbs = Double.parseDouble(carbsField.getText());
        double fat = Double.parseDouble(fatField.getText());

        Meal meal = new Meal(name, calories, protein, carbs, fat);
        mealPlanner.addMeal(meal);

        updateMealListView();
        calculateTotalNutrition();
        clearInputFields();
    }

    @FXML
    public void displayMeals() {
        mealPlanner.displayMeals();
        updateMealListView();
    }

    @FXML
    public void calculateTotalNutrition() {
        mealPlanner.calculateTotalNutrition();
        updateTotalNutritionLabel();
    }

    private void updateMealListView() {
        mealListView.getItems().clear();
        for (Meal meal : mealPlanner.getMeals()) {
            mealListView.getItems().add(meal.toString());
        }
    }

    private void updateTotalNutritionLabel() {
        totalNutritionLabel.setText("Total Nutrition for the Day:\n" +
                "Total Calories: " + mealPlanner.getTotalCalories() + "\n" +
                "Total Protein: " + mealPlanner.getTotalProtein() + " grams\n" +
                "Total Carbs: " + mealPlanner.getTotalCarbs() + " grams\n" +
                "Total Fat: " + mealPlanner.getTotalFat() + " grams");
    }

    private void clearInputFields() {
        mealNameField.clear();
        caloriesField.clear();
        proteinField.clear();
        carbsField.clear();
        fatField.clear();
    }
}
