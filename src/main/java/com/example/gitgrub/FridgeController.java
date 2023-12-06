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
    public static ListView<FridgeItem> fridgeContentsListView;

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

        // Set up the JavaFX scene
        Scene scene = new Scene(root, 320, 350);
        primaryStage.setTitle("Fridge Contents");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void removeItemAction(ActionEvent actionEvent) {
        fridgeManager.removeItemFromFridge(fridgeContentsListView.getSelectionModel().getSelectedItem());
        fridgeContentsListView.setItems(FXCollections.observableArrayList(fridgeManager.getFridgeContents()));

    }


    public void addItemAction(ActionEvent actionEvent) {
        fridgeManager.addItemToFridge(searchListView.getSelectionModel().getSelectedItem(), expirationDatePicker.getValue());
        fridgeContentsListView.setItems(FXCollections.observableArrayList(fridgeManager.getFridgeContents()));

    }


}