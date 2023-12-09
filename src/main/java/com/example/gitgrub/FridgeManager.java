package com.example.gitgrub;

import javafx.event.ActionEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FridgeManager {
    private ArrayList<FridgeItem>fridgeContents;

    public FridgeManager() {
        fridgeContents = new ArrayList<>();
    }

    public void addItemToFridge(FridgeItem fridgeItem) {
        fridgeContents.add(fridgeItem);
    }

    public void setFridgeContents(ArrayList<FridgeItem> fridgeContents) {
        this.fridgeContents = fridgeContents;
    }

    public void removeItemFromFridge(FridgeItem item) {
        fridgeContents.remove(item);
    }

    public void checkFridgeContents() {
        LocalDate today = LocalDate.now();
        for (FridgeItem entry : fridgeContents)
        {
            LocalDate expirationDate = LocalDate.parse(entry.getExpirationDate());
            long daysUntilExpiration = today.until(expirationDate).getDays();

            if (daysUntilExpiration <= 3) {
                // Notify the user about the item close to expiration
                sendNotification(entry.getName(), daysUntilExpiration);
            }
        }
    }

    private void sendNotification(String item, long daysUntilExpiration) {

        System.out.println("Item '" + item + "' is close to expiration in " + daysUntilExpiration + " days.");
    }

    public ArrayList<FridgeItem>getFridgeContents() {
        return fridgeContents;
    }
}
