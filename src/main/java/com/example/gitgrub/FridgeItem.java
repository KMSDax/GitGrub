package com.example.gitgrub;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

class FridgeItem {
    private final StringProperty name;
    private final StringProperty expirationDate;
    private int fridgeContentsID;

    FridgeItem(String name, LocalDate expirationDate) {
        this.fridgeContentsID = -1;
        this.name = new SimpleStringProperty(name);
        this.expirationDate = new SimpleStringProperty(expirationDate.toString());
    }
    FridgeItem(String name, LocalDate expirationDate, int fridgeContentsID) {
        this.fridgeContentsID = fridgeContentsID;
        this.name = new SimpleStringProperty(name);
        this.expirationDate = new SimpleStringProperty(expirationDate.toString());
    }

    public int getFridgeContentsID() {
        return fridgeContentsID;
    }

    public void setFridgeContentsID(int fridgeContentsID) {
        this.fridgeContentsID = fridgeContentsID;
    }

    String getName() {
        return name.get();
    }

    String getExpirationDate() {
        return expirationDate.get();
    }

    StringProperty nameProperty() {
        return name;
    }

    StringProperty expirationDateProperty() {
        return expirationDate;
    }

    @Override
    public String toString() {
        return getName()+" "+getExpirationDate();
    }
}
