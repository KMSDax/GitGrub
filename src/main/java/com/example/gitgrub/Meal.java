package com.example.gitgrub;

public class Meal {
    private String name;
    private int calories;
    private double protein;
    private double carbs;
    private double fat;

    public Meal(String name, int calories, double protein, double carbs, double fat) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFat() {
        return fat;
    }

    @Override
    public String toString() {
        return "Meal: " + name + ", Calories: " + calories +
                ", Protein: " + protein + "g, Carbs: " + carbs + "g, Fat: " + fat + "g";
    }
}
