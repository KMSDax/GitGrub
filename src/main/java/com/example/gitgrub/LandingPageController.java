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

        // Create a layout to display recipes
        VBox recipeLayout = new VBox(20); // Adjust spacing as needed

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

                recipeLayout.getChildren().add(recipeContainer);
            }
        }

        // Clear previous content and add the recipe layout to cookbookPane
        cookbookPane.getChildren().clear();
        cookbookPane.getChildren().add(recipeLayout);
    }
}

