package com.example.gitgrub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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
    static String apiKey = "apiKey=109600fd90884071b83282171199d792";
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
    public static JSONObject fetchRecipeInformation(int recipeId) {
        String apiUrl = "https://api.spoonacular.com/recipes/" + recipeId + "/information?" + apiKey;

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

    public static String fetchNutritionLabel(int recipeId) {
        String apiUrl = "https://api.spoonacular.com/recipes/" + recipeId + "/nutritionLabel?" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Set the Accept header to request HTML content
            connection.setRequestProperty("Accept", "text/html");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
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
public static JSONObject getRandRecipe() {
    // Define the API URL for Spoonacular recipes
    String apiUrl = "https://api.spoonacular.com/recipes/random?number=1&tags=vegetarian,dessert&" + apiKey;

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

            if (!results.isEmpty()) {
                return results.getJSONObject(0);
            } else {
                // Handle the case when there are no recipes
                System.out.println("No recipes found.");
            }
        } else {
            System.out.println("Request failed with response code: " + responseCode);
        }
    } catch (IOException | JSONException e) {
        e.printStackTrace();
    }

    return null; // Return null in case of an error or no recipes
}
}
