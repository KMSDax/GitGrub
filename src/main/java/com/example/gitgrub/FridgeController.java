package com.example.gitgrub;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.*;

public class FridgeController extends MainApplication {

    @FXML
    private ListView<FridgeItem> searchlistView;
    private FridgeManager fridgeManager = new FridgeManager();

    public void initialize() {
        // Initialize the UI components and bindings
        updateListView();
    }

    public void addItem() {
        // Show a dialog to add an item to the fridge
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Item");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter item name:");
        dialog.showAndWait().ifPresent(itemName -> {
            // Add the item to the fridge with today's date as expiration
            fridgeManager.addItemToFridge(itemName, LocalDate.now());
            updateListView();
        });
    }

    public void removeItem() {
        // Remove the selected item from the fridge
        FridgeItem selectedItem = searchlistView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            fridgeManager.removeItemFromFridge(selectedItem.getName());
            updateListView();
        }
    }

    public void checkExpiration() {
        // Check and notify about items close to expiration
        fridgeManager.checkFridgeContents();
    }

    private void updateListView() {
        searchlistView.getItems().clear();
        System.out.println(fridgeManager.getFridgeContents().values());
    }
}

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

class FridgeManager {
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
        // Implement your notification logic here.
        System.out.println("Item '" + item + "' is close to expiration in " + daysUntilExpiration + " days.");
    }

    public Map<String, LocalDate> getFridgeContents() {
        return fridgeContents;
    }
}
