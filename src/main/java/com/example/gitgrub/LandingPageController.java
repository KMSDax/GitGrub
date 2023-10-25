package com.example.gitgrub;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.JSONObject;

import java.net.URL;
import java.sql.SQLOutput;
import java.util.ResourceBundle;

import static com.example.gitgrub.Spoonacular.fetchRecipeInformation;

public class LandingPageController extends MainApplication implements Initializable {
    public Label usernameLabel;
    @FXML
    public ImageView imageView1,imageView2,imageView3,imageView4;
    @FXML
    public Label titleLabel1,titleLabel2,titleLabel3,titleLabel4;

    @FXML
    public Hyperlink sourceLink1,sourceLink2,sourceLink3,sourceLink4;
    public WebView descriptionPane1;


    @FXML
    private ImageView profilePic;
    @FXML
    private Pane newsPane, cookbookPane, page1, page2;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image picture = new Image(User.getInstance().getUser_profile());
        profilePic.setImage(picture);
        usernameLabel.setText(User.getInstance().getUser_id());

    }

    public void openNews() {
        newsPane.setVisible(true);
        cookbookPane.setVisible(false);
    }


    public void openCookbook() {
        cookbookPane.setVisible(true);
        newsPane.setVisible(false);

        for (int i = 0; i <= 2; i++) {
            int recipeId = i + 1; // Replace with the appropriate recipe IDs
            JSONObject recipeInfo = fetchRecipeInformation(recipeId);

            if (recipeInfo != null) {
                String title = recipeInfo.getString("title");
                String description = recipeInfo.optString("summary", "Description Unavailable: Please visit source link");
                String sourceUrl = recipeInfo.getString("sourceUrl");
                String imageUrl = recipeInfo.optString("image", "null");

                // Get references to the JavaFX components in page1
                Label titleLabel = (Label) page1.lookup("#titleLabel" + (i + 1));
                ImageView imageView = (ImageView) page1.lookup("#imageView" + (i + 1));
                Hyperlink sourceLink = (Hyperlink) page1.lookup("#sourceLink" + (i + 1));
                WebView descriptionPane = (WebView) page1.lookup("#descriptionPane" + (i + 1));

                // Set the recipe information
                titleLabel.setText(title);
                sourceLink.setText("Source URL");
                sourceLink.setOnAction(e -> getHostServices().showDocument(sourceUrl));
                // Set image
                Image recipeImage = new Image(imageUrl);
                //imageView.setImage(recipeImage);

                // Create a container for each recipe
                Label descriptionLabel = new Label(description);
                Text styledText = new Text(description);
                WebEngine webEngine = descriptionPane.getEngine();
                webEngine.loadContent(description);
            }

        }

    }
    public void Mom(){
        System.out.println("mom");
    }
}

