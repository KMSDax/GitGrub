package com.example.gitgrub;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class FridgeContents extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a FridgeManager for the user
        FridgeManager fridgeManager = new FridgeManager();

        // Create a JavaFX interface
        VBox root = new VBox();
        root.setSpacing(10);

        Label titleLabel = new Label("Fridge Contents");
        ListView<FridgeItem> listView = new ListView<>();
        listView.setPrefSize(300, 200);

        Button addButton = new Button("Add Item");
        addButton.setOnAction(e -> {
            // Show a dialog to add an item to the fridge
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Item");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter item name:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(itemName -> {
                // Add the item to the fridge with today's date as expiration
                fridgeManager.addItemToFridge(itemName, LocalDate.now());
                updateListView(listView, fridgeManager.getFridgeContents());
            });
        });

        Button removeButton = new Button("Remove Item");
        removeButton.setOnAction(e -> {
            // Remove the selected item from the fridge
            FridgeItem selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                fridgeManager.removeItemFromFridge(selectedItem.getName());
                updateListView(listView, fridgeManager.getFridgeContents());
            }
        });

        Button checkButton = new Button("Check Expiration");
        checkButton.setOnAction(e -> {
            // Check and notify about items close to expiration
            fridgeManager.checkFridgeContents();
        });

        root.getChildren().addAll(titleLabel, listView, addButton, removeButton, checkButton);

        // Set up the JavaFX scene
        Scene scene = new Scene(root, 320, 350);
        primaryStage.setTitle("Fridge Contents");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateListView(ListView<FridgeItem> listView, Map<String, LocalDate> fridgeContents) {
        listView.getItems().clear();
        fridgeContents.forEach((item, date) -> listView.getItems().add(new FridgeItem(item, date)));
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
