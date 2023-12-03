package com.example.gitgrub;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

class FridgeItem {
    private final StringProperty name;
    private final StringProperty expirationDate;

    FridgeItem(String name, LocalDate expirationDate) {
        this.name = new SimpleStringProperty(name);
        this.expirationDate = new SimpleStringProperty(expirationDate.toString());
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
}
