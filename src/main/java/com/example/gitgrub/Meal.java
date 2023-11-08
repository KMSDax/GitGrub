package com.example.gitgrub;

public class Meal {
    private final String name;
    private final String food;
    private final int size;

    public Meal()
    {
        this.food = this.name = "";
        this.size = 0;
    }

    public Meal(String name, String food, int size) {
        this.name = name;
        this.food = food;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getFood() {
        return food;
    }

    public int getSize() {
        return size;
    }

    public int getCalorie() {
        return switch (food) {
            case "Bagel" -> 140;
            case "Cornflakes" -> 130;
            case "Pasta" -> 330;
            case "Rice" -> 420;
            case "Apple" -> 44;
            case "Chicken" -> 220;
            case "Pork" -> 320;
            case "Carrot" -> 16;
            case "Yoghurt" -> 90;
            case "Chocolate" -> 200;
            default -> 0;
        };
    }

    public int getTotalCalorie()
    {
        return(getCalorie() * getSize());
    }

    @Override
    public String toString()
    {
        return(getName() + " " + getFood() + ": " + getSize());
    }
}

