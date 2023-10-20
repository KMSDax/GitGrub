package com.example.gitgrub;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.gitgrub.Spoonacular.fetchRecipeInformation;

public class LandingPageController extends MainApplication implements Initializable {
    @FXML
    private Button news;
    @FXML
    private ImageView profilePic;
    @FXML
    private Pane newsPane, cookbookPane, page1, page2;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image picture = new Image(User.getInstance().getUser_profile());
        profilePic.setImage(picture);

    }

    public void openNews() {
        newsPane.setVisible(true);
        cookbookPane.setVisible(false);
    }


    public void openCookbook() {
        cookbookPane.setVisible(true);
        newsPane.setVisible(false);

        // Create layouts for page1 and page2
        VBox recipeLayoutPage1 = new VBox(20);
        VBox recipeLayoutPage2 = new VBox(20);

        // Fetch and display recipes
        for (int i = 0; i < 4; i++) {
            int recipeId = i + 1; // Replace with the appropriate recipe IDs
            JSONObject recipeInfo = fetchRecipeInformation(recipeId);

            if (recipeInfo != null) {
                String title = recipeInfo.getString("title");
                String description = recipeInfo.optString("summary", "Description Unavailable: Please visit source link");
                String sourceUrl = recipeInfo.getString("sourceUrl");
                String imageUrl = recipeInfo.optString("image", "null");

                // Create nodes to display recipe information
                Label titleLabel = new Label("Title: " + title);
                Label descriptionLabel = new Label("Description: " + description);
                Hyperlink sourceLink = new Hyperlink("Source URL");
                sourceLink.setOnAction(e -> getHostServices().showDocument(sourceUrl));

                Image recipeImage = new Image(imageUrl);
                ImageView imageView = new ImageView(recipeImage);
                imageView.setFitHeight(100);
                imageView.setFitWidth(200);

                // Create a container for each recipe
                VBox recipeContainer = new VBox(10);
                recipeContainer.getChildren().addAll(titleLabel, descriptionLabel, sourceLink, imageView);

                // Add the recipe to the appropriate layout (page1 or page2)
                if (i % 2 == 0) {
                    recipeLayoutPage1.getChildren().add(recipeContainer);
                } else {
                    recipeLayoutPage2.getChildren().add(recipeContainer);
                }
            }
        }

        // Clear previous content and add the recipe layouts to page1 and page2
        page1.getChildren().clear();
        page1.getChildren().add(recipeLayoutPage1);

        page2.getChildren().clear();
        page2.getChildren().add(recipeLayoutPage2);
    }
}

