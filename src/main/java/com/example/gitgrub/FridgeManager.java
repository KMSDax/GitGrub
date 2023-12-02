package com.example.gitgrub;

import javafx.event.ActionEvent;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FridgeManager {
    private Map<String, LocalDate> fridgeContents;

    public FridgeManager() {
        fridgeContents = new HashMap<>();
    }

    public void addItemToFridge(String item, LocalDate expirationDate) {
        fridgeContents.put(item, expirationDate);
    }

    public void removeItemFromFridge(String item) {
        fridgeContents.remove(item);
    }

    public void checkFridgeContents() {
        LocalDate today = LocalDate.now();
        for (Map.Entry<String, LocalDate> entry : fridgeContents.entrySet()) {
            String item = entry.getKey();
            LocalDate expirationDate = entry.getValue();
            long daysUntilExpiration = today.until(expirationDate).getDays();

            if (daysUntilExpiration <= 3) {
                // Notify the user about the item close to expiration
                sendNotification(item, daysUntilExpiration);
            }
        }
    }

    private void sendNotification(String item, long daysUntilExpiration) {

        System.out.println("Item '" + item + "' is close to expiration in " + daysUntilExpiration + " days.");
    }

    public Map<String, LocalDate> getFridgeContents() {
        return fridgeContents;
    }
}
