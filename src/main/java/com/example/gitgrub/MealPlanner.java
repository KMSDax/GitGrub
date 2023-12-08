package com.example.gitgrub;

import java.util.ArrayList;

public class MealPlanner {
    private ArrayList<Meal> meals = new ArrayList<>();
    private int totalCalories = 0;
    private double totalProtein = 0.0;
    private double totalCarbs = 0.0;
    private double totalFat = 0.0;

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void calculateTotalNutrition() {
        totalCalories = 0;
        totalProtein = 0.0;
        totalCarbs = 0.0;
        totalFat = 0.0;

        for (Meal meal : meals) {
            totalCalories += meal.getCalories();
            totalProtein += meal.getProtein();
            totalCarbs += meal.getCarbs();
            totalFat += meal.getFat();
        }
    }

    public String getTotalCalories() {
        return String.valueOf(totalCalories);
    }

    public String getTotalProtein() {
        return String.valueOf(totalProtein);
    }

    public String getTotalCarbs() {
        return String.valueOf(totalCarbs);
    }

    public String getTotalFat() {
        return String.valueOf(totalFat);
    }
}
