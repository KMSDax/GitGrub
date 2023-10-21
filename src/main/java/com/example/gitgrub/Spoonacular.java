package com.example.gitgrub;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents Spoonacular recipes retrieved from an external API.
 */
public class Spoonacular {
    private final String title;
    private final String description;
    private String sourceUrl, image;
    private int readyInMinutes;
    private int servings;
    private static final List<Spoonacular> recipeList = new ArrayList<>();
    public Spoonacular(String title, String description,String sourceUrl, String image, int readyInMinutes, int servings) {
        this.title = title;
        this.description = description;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.sourceUrl = sourceUrl;
        this.image = image;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }
    public static void getRandRecipe() {
        // Define the API URL for Spoonacular recipes
        String apiUrl = "https://api.spoonacular.com/recipes/random?number=1&tags=vegetarian,dessert&apiKey=109600fd90884071b83282171199d792";

        try {
            // Create a URL object and open a connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and parse the JSON response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());

                JSONArray results = jsonResponse.getJSONArray("recipes");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject recipe = results.getJSONObject(i);
                    String title = recipe.getString("title");
                    String description = "Description Unavailable: Please visit source link.";

                    if (recipe.has("summary") && !recipe.isNull("summary")) {
                        description = recipe.getString("summary");
                    }

                    String sourceUrl = recipe.getString("sourceUrl");
                    String image = "null";

                    if (recipe.has("image") && !recipe.isNull("image")) {
                        image = recipe.getString("image");
                    }

                    int readyInMinutes = recipe.getInt("readyInMinutes");
                    int servings = recipe.getInt("servings");

                    // Now you have various details in separate variables
                    System.out.println("Title: " + title);
                    System.out.println("Description: " + description);
                    System.out.println("Source URL: " + sourceUrl);
                    System.out.println("Image URL: " + image);
                    System.out.println("Ready In Minutes: " + readyInMinutes);
                    System.out.println("Servings: " + servings);

                    // Extract and print analyzedInstructions
                    JSONArray analyzedInstructions = recipe.getJSONArray("analyzedInstructions");
                    for (int j = 0; j < analyzedInstructions.length(); j++) {
                        JSONObject instruction = analyzedInstructions.getJSONObject(j);
                        String name = instruction.getString("name");
                        System.out.println("Instruction Name: " + name);
                        JSONArray steps = instruction.getJSONArray("steps");
                        for (int k = 0; k < steps.length(); k++) {
                            JSONObject step = steps.getJSONObject(k);
                            int stepNumber = step.getInt("number");
                            String stepDescription = step.getString("step");
                            System.out.println("Step " + stepNumber + ": " + stepDescription);
                        }
                    }

                    // Create a Spoonacular object and add it to the list
                    recipeList.add(new Spoonacular(title, description, sourceUrl, image, readyInMinutes, servings));
                }
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static JSONObject fetchRecipeInformation(int recipeId) {
        String apiKey = "109600fd90884071b83282171199d792";
        String apiUrl = "https://api.spoonacular.com/recipes/" + recipeId + "/information?apiKey=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return new JSONObject(response.toString());
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    public static void recipeSearch(){
//        String apiURL = "https://api.spoonacular.com/food/products/search&apiKey=109600fd90884071b83282171199d792";
//    }

}
