package com.example.gitgrub;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FridgeController extends MainApplication {

    public DatePicker expirationDatePicker;
    public ListView searchlistView;
    public TextField searchField;

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

    public void removeItemAction(ActionEvent actionEvent) {
    }

    public void checkExpirationAction(ActionEvent actionEvent) {
    }

    public void addItemAction(ActionEvent actionEvent) {
        FridgeManager fm=new FridgeManager();
        fm.addItemToFridge(String.valueOf(searchField), expirationDatePicker.getValue());

    }
}