package com.example.gitgrub;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import com.example.gitgrub.FridgeItem;

public class FridgeController extends MainApplication implements Initializable {


    public DatePicker expirationDatePicker;
    public ListView<String> searchListView;
    public TextField searchField;
    public static FridgeManager fridgeManager = new FridgeManager();
    public ListView<FridgeItem> fridgeContentsListView;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String>itemNames= FXCollections.observableArrayList("Milk", "Cheese", "Bacon", "Eggs", "Tuna", "Anchovies");
        searchListView.setItems(itemNames);

    }

    @Override
    public void start(Stage primaryStage) {

        // Create a JavaFX interface
        VBox root = new VBox();
        root.setSpacing(10);

        Label titleLabel = new Label("Fridge Contents");
        //ListView<FridgeItem> listView = new ListView<>();
       // listView.setPrefSize(300, 200);

//        Button addButton = new Button("Add Item");
//        addButton.setOnAction(e -> {
//            // Show a dialog to add an item to the fridge
//            TextInputDialog dialog = new TextInputDialog();
//            dialog.setTitle("Add Item");
//            dialog.setHeaderText(null);
//            dialog.setContentText("Enter item name:");
//            Optional<String> result = dialog.showAndWait();
//            result.ifPresent(itemName -> {
//                // Add the item to the fridge with today's date as expiration
//                fridgeManager.addItemToFridge(itemName, LocalDate.now());
//                updateListView(fridgeContentsListView, fridgeManager.getFridgeContents());
//            });
     //   });

//        Button removeButton = new Button("Remove Item");
//        removeButton.setOnAction(e -> {
//            // Remove the selected item from the fridge
//            FridgeItem selectedItem = fridgeContentsListView.getSelectionModel().getSelectedItem();
//            if (selectedItem != null) {
//                fridgeManager.removeItemFromFridge(selectedItem);
//                updateListView(fridgeContentsListView, fridgeManager.getFridgeContents());
//            }
//        });

//        Button checkButton = new Button("Check Expiration");
//        checkButton.setOnAction(e -> {
//            // Check and notify about items close to expiration
//            fridgeManager.checkFridgeContents();
//        });

      //  root.getChildren().addAll(titleLabel, fridgeContentsListView, checkButton);

        // Set up the JavaFX scene
        Scene scene = new Scene(root, 320, 350);
        primaryStage.setTitle("Fridge Contents");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//    private void updateListView(ListView<FridgeItem> listView, ArrayList<FridgeItem> fridgeContents) {
//        listView.getItems().clear();
//        fridgeContents.forEach((item) -> listView.getItems().add(item));
//    }

    public void removeItemAction(ActionEvent actionEvent) {
        fridgeManager.removeItemFromFridge(fridgeContentsListView.getSelectionModel().getSelectedItem());
        fridgeContentsListView.setItems(FXCollections.observableArrayList(fridgeManager.getFridgeContents()));

    }


    public void addItemAction(ActionEvent actionEvent) {
        fridgeManager.addItemToFridge(searchListView.getSelectionModel().getSelectedItem(), expirationDatePicker.getValue());
        fridgeContentsListView.setItems(FXCollections.observableArrayList(fridgeManager.getFridgeContents()));

    }


}