package com.example.gitgrub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

public class MealRatingController {

    @FXML
    private ListView<String> mealListView;

    @FXML
    private ImageView star1;

    @FXML
    private ImageView star2;

    @FXML
    private ImageView star3;

    @FXML
    private ImageView star4;

    @FXML
    private ImageView star5;

    @FXML
    private Label averageRatingLabel;

    private int userRating = 0;

    @FXML
    private void rateMealAction(ActionEvent event) {
        // Implement your rating logic here
        userRating++;
        updateStarRating();
    }

    @FXML
    private void handleStarClick(ActionEvent event) {
        // Implement star click logic here
        userRating = Integer.parseInt(((ImageView) (event.getSource())).getId().substring(4));
        updateStarRating();
    }

    private void updateStarRating() {
        star1.setStyle(userRating >= 1 ? "-fx-fill: gold;" : "");
        star2.setStyle(userRating >= 2 ? "-fx-fill: gold;" : "");
        star3.setStyle(userRating >= 3 ? "-fx-fill: gold;" : "");
        star4.setStyle(userRating >= 4 ? "-fx-fill: gold;" : "");
        star5.setStyle(userRating >= 5 ? "-fx-fill: gold;" : "");

        calculateAverageRating();
    }

    private void calculateAverageRating() {
        // Implement logic to calculate average rating
        double averageRating = userRating / 5.0; // Assuming a maximum of 5 stars
        averageRatingLabel.setText(String.format("Average Rating: %.1f", averageRating));
    }
}
