package com.example.gitgrub;

import java.util.ArrayList;

public class MealPlanner {
    private ArrayList<Meal> meals = new ArrayList<>();

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void displayMeals() {
        System.out.println("Your Meals:");
        for (Meal meal : meals) {
            System.out.println(meal);
        }
        System.out.println();
    }

    public void calculateTotalNutrition() {
        int totalCalories = 0;
        double totalProtein = 0.0;
        double totalCarbs = 0.0;
        double totalFat = 0.0;

        for (Meal meal : meals) {
            totalCalories += meal.getCalories();
            totalProtein += meal.getProtein();
            totalCarbs += meal.getCarbs();
            totalFat += meal.getFat();
        }

        System.out.println("Total Nutrition for the Day:");
        System.out.println("Total Calories: " + totalCalories);
        System.out.println("Total Protein: " + totalProtein + " grams");
        System.out.println("Total Carbs: " + totalCarbs + " grams");
        System.out.println("Total Fat: " + totalFat + " grams\n");
    }

    public String getTotalCalories() {
        return null;
    }

    public String getTotalProtein() {
        return null;
    }

    public String getTotalCarbs() {
        return null;
    }

    public String getTotalFat() {
        return null;
    }
}
