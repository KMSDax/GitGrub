package com.example.gitgrub;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FridgeController extends MainApplication {

    @FXML
    private ListView<FridgeItem> searchlistView;
    private FridgeManager fridgeManager = new FridgeManager();

    public void initialize() {
        // Initialize the UI components and bindings
        updateListView();
    }

    @FXML
    public void addItem() {
        // Add the item to the fridge with today's date as expiration
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Item");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter item name:");
        dialog.showAndWait().ifPresent(itemName -> {
            fridgeManager.addItemToFridge(itemName, LocalDate.now());
            updateListView();
        });
    }

    @FXML
    public void removeItem() {
        // Remove the selected item from the fridge
        FridgeItem selectedItem = searchlistView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            fridgeManager.removeItemFromFridge(selectedItem.getName());
            updateListView();
        }
    }

    @FXML
    public void checkExpiration() {
        // Check and notify about items close to expiration
        fridgeManager.checkFridgeContents();
    }

    private void updateListView() {
        searchlistView.getItems().clear();
        fridgeManager.getFridgeContents().forEach((name, date) -> searchlistView.getItems().add(new FridgeItem(name, date)));
    }
}
