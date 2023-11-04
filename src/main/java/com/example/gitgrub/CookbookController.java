package com.example.gitgrub;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.gitgrub.Spoonacular.fetchRecipeInformation;

public class CookbookController extends MainApplication implements Initializable {
    @FXML
    public Label titleLabel1, titleLabel2, titleLabel3, titleLabel4;
    @FXML
    public Hyperlink sourceLink1, sourceLink2, sourceLink3, sourceLink4;
    @FXML
    public WebView descriptionPane1, descriptionPane2, descriptionPane3, descriptionPane4;
    @FXML
    public ImageView imageView1, imageView2, imageView3, imageView4;
    @FXML
    public Button back,next;
    private int currentIndex = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateRecipes();
        next.setOnAction(event -> showNextRecipes());
        back.setOnAction(event -> showPreviousRecipes());
    }
    public void updateRecipes() {

        for (int i = 0; i < 4; i++) {
            int recipeId = currentIndex + i + 1; // Replace with the appropriate recipe IDs
            JSONObject recipeInfo = fetchRecipeInformation(recipeId);

            if (recipeInfo != null) {
                String title = recipeInfo.getString("title");
                String description = recipeInfo.optString("summary", "Description Unavailable: Please visit source link");
                String sourceUrl = recipeInfo.getString("sourceUrl");
                String imageUrl = recipeInfo.optString("image", "null");

                // Get references to the JavaFX components
                Label titleLabel = getRecipeLabel(i + 1);
                Hyperlink sourceLink = getRecipeSourceLink(i + 1);
                WebView descriptionPane = getRecipeDescriptionPane(i + 1);
                ImageView imageView = getRecipeImage(i + 1);
                // Set the recipe information
                titleLabel.setText(title);

                sourceLink.setOnAction(e -> getHostServices().showDocument(sourceUrl));
                // Convert imageURl from String to Image Object, set Image to recipeImage
                Image recipeImage = new Image(imageUrl);
                imageView.setImage(recipeImage);
                // Create webEngine Object to load description content
                WebEngine webEngine = descriptionPane.getEngine();
                Document doc = Jsoup.parse(description);
                doc.select("a").remove();

                webEngine.loadContent(String.valueOf(doc));
            }
        }
    }
    private Label getRecipeLabel(int index) {
        switch (index) {
            case 1:
                return titleLabel1;
            case 2:
                return titleLabel2;
            case 3:
                return titleLabel3;
            case 4:
                return titleLabel4;
            default:
                return null;
        }
    }

    private Hyperlink getRecipeSourceLink(int index) {
        switch (index) {
            case 1:
                return sourceLink1;
            case 2:
                return sourceLink2;
            case 3:
                return sourceLink3;
            case 4:
                return sourceLink4;
            default:
                return null;
        }
    }

    private WebView getRecipeDescriptionPane(int index) {
        switch (index) {
            case 1:
                return descriptionPane1;
            case 2:
                return descriptionPane2;
            case 3:
                return descriptionPane3;
            case 4:
                return descriptionPane4;
            default:
                return null;
        }
    }
    private ImageView getRecipeImage(int index) {
        switch (index) {
            case 1:
                return imageView1;
            case 2:
                return imageView2;
            case 3:
                return imageView3;
            case 4:
                return imageView4;
            default:
                return null;
        }
    }
    private void showNextRecipes() {
        if (currentIndex + 4 < 99999) { // Assuming a total of 20 recipes
            currentIndex += 4;
            updateRecipes();
        }
    }

    private void showPreviousRecipes() {
        if (currentIndex > 0) {
            currentIndex -= 4;
            updateRecipes();
        }
    }
}

