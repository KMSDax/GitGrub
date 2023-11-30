package com.example.gitgrub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.gitgrub.Spoonacular.fetchNutritionLabel;
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
    public Button nutritionButton1,nutritionButton2,nutritionButton3,nutritionButton4;
    @FXML
    public Button back,next;
    @FXML
    public TextField search;
    @FXML
    public Pane nutrtionLabelPane;
    public Button descriptionButton1,descriptionButton2,descriptionButton3,descriptionButton4;
    public Button instructionButton1,instructionButton2,instructionButton3,instructionButton4;
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
                Button nutritionButton = getNutritionButton(i + 1);
                Button descriptionButton = getDescriptionButton(i + 1); // assuming you have a method to get the Description buttons
                Button instructionsButton = getInstructionsButton(i + 1); // Get the instructions button

                // Store the original description content for each recipe
                String originalDescription = description;
                descriptionPane.getProperties().put("originalDescription", originalDescription);

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

                // Fetch and display nutrition label content when nutrition button is clicked
                nutritionButton.setOnAction(event -> getRecipeNutrition(recipeId, descriptionPane));


                // Reset to original description when description button is clicked
                descriptionButton.setOnAction(event -> resetToDescription(descriptionPane));

                instructionsButton.setOnAction(event -> getRecipeInstructions(recipeId, descriptionPane));

                System.out.println(recipeId);
            }
        }
    }

    public void getRecipeInstructions(int recipeId, WebView descriptionPane) {
        // Fetch recipe information including instructions, ingredients, appliances, etc.
        JSONObject recipeInfo = fetchRecipeInformation(recipeId);

        if (recipeInfo != null) {
            // Extract relevant information
            JSONArray analyzedInstructions = recipeInfo.optJSONArray("analyzedInstructions");

            StringBuilder recipeDetails = new StringBuilder();

            // Display ingredients, appliances, prep time, and servings
            JSONArray ingredients = recipeInfo.optJSONArray("extendedIngredients");
            JSONArray equipment = recipeInfo.optJSONArray("extendedEquipment");
            int prepTime = recipeInfo.optInt("readyInMinutes", 0);
            int servings = recipeInfo.optInt("servings", 0);

            recipeDetails.append("<h2>Ingredients:</h2><ul>");
            if (ingredients != null) {
                for (int i = 0; i < ingredients.length(); i++) {
                    JSONObject ingredientObj = ingredients.getJSONObject(i);
                    String ingredientName = ingredientObj.getString("original");
                    recipeDetails.append("<li>").append(ingredientName).append("</li>");
                }
            }
            recipeDetails.append("</ul>");

            recipeDetails.append("<h2>Equipment:</h2><ul>");
            if (equipment != null) {
                for (int i = 0; i < equipment.length(); i++) {
                    JSONObject equipmentObj = equipment.getJSONObject(i);
                    String equipmentName = equipmentObj.getString("name");
                    recipeDetails.append("<li>").append(equipmentName).append("</li>");
                }
            }
            recipeDetails.append("</ul>");

            recipeDetails.append("<h2>Prep Time:</h2>");
            recipeDetails.append("<p>").append(prepTime).append(" minutes</p>");
            recipeDetails.append("<h2>Servings:</h2>");
            recipeDetails.append("<p>").append(servings).append(" servings</p>");

            // Display instructions
            if (analyzedInstructions != null && analyzedInstructions.length() > 0) {
                for (int j = 0; j < analyzedInstructions.length(); j++) {
                    JSONObject instruction = analyzedInstructions.getJSONObject(j);
                    String name = instruction.optString("name");
                    recipeDetails.append("<h2>").append(name).append("</h2>");

                    JSONArray steps = instruction.optJSONArray("steps");
                    if (steps != null) {
                        recipeDetails.append("<ol>");
                        for (int k = 0; k < steps.length(); k++) {
                            JSONObject step = steps.getJSONObject(k);
                            int stepNumber = step.optInt("number");
                            String stepDescription = step.optString("step");
                            recipeDetails.append("<li>").append(stepDescription).append("</li>");
                        }
                        recipeDetails.append("</ol>");
                    }
                }
            } else {
                // If analyzedInstructions are not available, use the regular instructions
                String instructions = recipeInfo.optString("instructions", "Instructions Unavailable");
                recipeDetails.append("<h2>Instructions:</h2>");
                recipeDetails.append("<p>").append(instructions).append("</p>");
            }

            // Load content into the WebView
            descriptionPane.getEngine().loadContent(recipeDetails.toString());
        }
    }
    public void getRecipeNutrition(int recipeId, WebView descriptionPane) {
        // Fetch and display nutrition label content for the given recipe ID
        String nutritionLabelContent = fetchNutritionLabel(recipeId);

        if (nutritionLabelContent != null) {
            // Apply CSS to style the nutrition label content
            String styledContent = "<style>" +
                    "body { font-family: 'Arial', sans-serif; font-size: 14px; line-height: 1.6; }" +
                    ".nutrition-label { padding: 10px; border: 1px solid #ccc; border-radius: 5px; }" +
                    "</style>" + nutritionLabelContent;

            descriptionPane.getEngine().loadContent(styledContent);
            // Additional adjustments if needed
        }
    }
    private void resetToDescription(WebView descriptionPane) {
        String originalDescription = (String) descriptionPane.getProperties().get("originalDescription");
        if (originalDescription != null) {
            descriptionPane.getEngine().loadContent(originalDescription);
        }
    }

    private Button getDescriptionButton(int index) {
        switch (index) {
            case 1:
                return descriptionButton1;
            case 2:
                return descriptionButton2;
            case 3:
                return descriptionButton3;
            case 4:
                return descriptionButton4;
            default:
                return null;
        }
    }
    private Button getInstructionsButton(int index) {
        switch (index) {
            case 1:
                return instructionButton1;
            case 2:
                return instructionButton2;
            case 3:
                return instructionButton3;
            case 4:
                return instructionButton4;
            default:
                return null;
        }
    }
    private Button getNutritionButton(int index) {
        switch (index) {
            case 1:
                return nutritionButton1;
            case 2:
                return nutritionButton2;
            case 3:
                return nutritionButton3;
            case 4:
                return nutritionButton4;
            default:
                return null;
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
        if (currentIndex + 4 < 13) { // cuz we're poor college students and can't afford more then 150 api calls a day!
            currentIndex += 5;
            updateRecipes();
        }
    }

    private void showPreviousRecipes() {
        if (currentIndex > 0) {
            currentIndex -= 5;
            updateRecipes();
        }
    }
}

