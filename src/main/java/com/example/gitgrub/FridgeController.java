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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        ObservableList<String>itemNames= FXCollections.observableArrayList("Milk", "Cheese", "Bacon", "Eggs", "Tuna", "Anchovies", "Ice Cream", "Bananas", "Iced Tea", "Lemonade", "Butter", "Cream Cheese");
        searchListView.setItems(itemNames);
        ObservableList<FridgeItem> fridgeItems = getFridgeContentsFromDB();
        fridgeContentsListView.setItems(fridgeItems);
        ArrayList<FridgeItem> fridgeItems1 = new ArrayList<>();
        for (FridgeItem item:fridgeItems
             ) {fridgeItems1.add(item);

        }
        fridgeManager.setFridgeContents(fridgeItems1);

    }

    @Override
    public void start(Stage primaryStage) {

        // Create a JavaFX interface
        VBox root = new VBox();
        root.setSpacing(10);


        // Set up the JavaFX scene
        Scene scene = new Scene(root, 320, 350);
        primaryStage.setTitle("Fridge Contents");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void removeItemAction(ActionEvent actionEvent) throws SQLException {
        try {
            FridgeItem fridgeItem = removeFridgeItemToDB(fridgeContentsListView.getSelectionModel().getSelectedItem());
            fridgeManager.removeItemFromFridge(fridgeItem);
            fridgeContentsListView.setItems(FXCollections.observableArrayList(fridgeManager.getFridgeContents()));
        }
        catch(SQLException e) {
            e.printStackTrace();
        }


    }


    public void addItemAction(ActionEvent actionEvent) {
        try {
            FridgeItem fridgeItem = addFridgeItemToDB(new FridgeItem(searchListView.getSelectionModel().getSelectedItem(), expirationDatePicker.getValue()));
            fridgeManager.addItemToFridge(fridgeItem);
            fridgeContentsListView.setItems(FXCollections.observableArrayList(fridgeManager.getFridgeContents()));
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

    }

    public ObservableList<FridgeItem>getFridgeContentsFromDB() {
        int UID=Integer.parseInt(User.getUser_Uid());
        ObservableList<FridgeItem> fridgeItems = FXCollections.observableArrayList();

        try {
            Connection connection = DBConn.connectDB();
            String sql = "SELECT * FROM fridge_contents WHERE UID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,UID);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
               String name = resultSet.getString("ingredientName");
               String expirationDate = resultSet.getString("expirationDate");
                fridgeItems.add(new FridgeItem(name, LocalDate.parse(expirationDate)));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fridgeItems;
    }
    public FridgeItem addFridgeItemToDB(FridgeItem fridgeItem) throws SQLException {
        int UID=Integer.parseInt(User.getUser_Uid());
        String sql = "INSERT INTO fridge_contents (uid,ingredientName,expirationDate) VALUES (?,?,?)";
        String sql2 = "SELECT * FROM fridge_contents ORDER BY fridge_contents_id LIMIT 1";
        Connection connection = DBConn.connectDB();
        Connection connection2 = DBConn.connectDB();
        FridgeItem newFridgeItem = fridgeItem;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            PreparedStatement ps2 = connection2.prepareStatement(sql2);
            preparedStatement.setInt(1,UID);
            preparedStatement.setString(2,fridgeItem.getName());
            preparedStatement.setString(3, fridgeItem.getExpirationDate());
            preparedStatement.executeUpdate();
            ResultSet rs = ps2.executeQuery();
            rs.next();
            int fridgeContentsID = rs.getInt("fridge_contents_id");
            newFridgeItem.setFridgeContentsID(fridgeContentsID);
            preparedStatement.close();

        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            connection.close();
            connection2.close();
        }
            return newFridgeItem;
        }
    public FridgeItem removeFridgeItemToDB(FridgeItem fridgeItem) throws SQLException {
        int UID=fridgeItem.getFridgeContentsID();
       // String sql = "DELETE FROM fridge_contents";
        String sql = "DELETE FROM fridge_contents WHERE fridge_contents_id = ?";
        Connection connection = DBConn.connectDB();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,UID);
            preparedStatement.executeUpdate();
            preparedStatement.close();

        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            connection.close();
        }
        return fridgeItem;
    }


}