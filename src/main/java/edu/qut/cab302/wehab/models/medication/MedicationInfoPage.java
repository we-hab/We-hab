package edu.qut.cab302.wehab.models.medication;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;
import edu.qut.cab302.wehab.util.WrappingText;

import java.io.IOException;

/**
 * This class is responsible for generating the detailed information page for a specific medication
 * by fetching the data from an API and displaying it in a formatted view using JavaFX components.
 */
public class MedicationInfoPage {

    private String medicationId;  // ID of the medication
    private String medicationJson;  // JSON data of the medication retrieved from API
    private JSONObject medicationJsonObject;  // JSON object parsed from medicationJson
    private Scene scene;  // Scene that displays the medication information

    /**
     * Constructor for MedicationInfoPage.
     * Initializes the page with the specified medication ID and retrieves the data from the API.
     *
     * @param medicationId The ID of the medication to retrieve.
     */
    public MedicationInfoPage(String medicationId) {
        this.medicationId = medicationId;
        try {
            medicationJson = queryAPI(medicationId);  // Query the API for the medication data
        } catch (IOException e) {
            medicationJson = null;
        }
        medicationJsonObject = new JSONObject(medicationJson);  // Convert JSON string to JSONObject
    }

    private int sceneWidth = 1200;  // Width of the scene for displaying content
    private int textWrappingWidth = sceneWidth - 50;  // Text wrapping width for layout

    /**
     * Returns the JavaFX Scene containing the medication information page.
     * It dynamically constructs the page by iterating through the JSON object data and formatting the output.
     *
     * @return The constructed Scene displaying the medication details.
     */
    public Scene getMedicationInfoPage() {

        VBox content = new VBox();  // VBox layout to hold the medication content
        ScrollPane scrollPane = new ScrollPane(content);  // ScrollPane to allow scrolling
        Scene scene = new Scene(scrollPane, sceneWidth, 800);  // Create the Scene with fixed width and height

        // Iterate over the keys of the medication JSON object and display each section
        for (String key : medicationJsonObject.keySet()) {
            WrappingText heading = new WrappingText(capitalizeHeading(key), textWrappingWidth);  // Display each key as a heading
            heading.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");  // Style the heading
            content.getChildren().add(heading);  // Add heading to the content

            Object value = medicationJsonObject.get(key);  // Get the value associated with the key
            if (value instanceof JSONArray) {
                JSONArray valuesArray = (JSONArray) value;
                // Iterate over the array if the value is a JSONArray
                for (int i = 0; i < valuesArray.length(); i++) {
                    Object arrayValue = valuesArray.get(i);
                    if (arrayValue instanceof String) {
                        WrappingText valueWrappingText = new WrappingText((String) arrayValue, textWrappingWidth);  // Display as text if string
                        content.getChildren().add(valueWrappingText);
                    } else if (arrayValue instanceof JSONObject) {
                        JSONObject nestedObject = (JSONObject) arrayValue;
                        // Iterate over the nested JSONObject
                        for (String nestedKey : nestedObject.keySet()) {
                            WrappingText nestedHeading = new WrappingText(capitalizeHeading(nestedKey), textWrappingWidth);  // Display nested keys as headings
                            nestedHeading.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
                            content.getChildren().add(nestedHeading);

                            Object nestedValue = nestedObject.get(nestedKey);
                            if (nestedValue instanceof String) {
                                WrappingText nestedValueWrappingText = new WrappingText((String) nestedValue, textWrappingWidth);  // Display nested values as text
                                content.getChildren().add(nestedValueWrappingText);
                            } else if (nestedValue instanceof JSONArray) {
                                // Iterate over the JSONArray in the nested object
                                JSONArray deepNestedArray = (JSONArray) nestedValue;
                                for (int j = 0; j < deepNestedArray.length(); j++) {
                                    WrappingText deepNestedValueWrappingText = new WrappingText(deepNestedArray.getString(j), textWrappingWidth);
                                    content.getChildren().add(deepNestedValueWrappingText);
                                }
                            } else if (nestedValue instanceof JSONObject) {
                                handleNestedJSONObject((JSONObject) nestedValue, content);  // Recursively handle deeply nested JSON objects
                            }
                        }
                    }
                }
            } else if (value instanceof JSONObject) {
                handleNestedJSONObject((JSONObject) value, content);  // Handle JSON objects
            } else if (value instanceof String) {
                WrappingText valueWrappingText = new WrappingText((String) value, textWrappingWidth);  // Display string values
                content.getChildren().add(valueWrappingText);
            }
        }
        return scene;
    }

    /**
     * Queries the API using the medication ID and retrieves the corresponding medication data in JSON format.
     *
     * @param medicationID The ID of the medication to query.
     * @return The JSON response from the API as a string.
     * @throws IOException If an error occurs while querying the API.
     */
    private String queryAPI(String medicationID) throws IOException {
        FDAApiService fdaApiService = new FDAApiService();
        return fdaApiService.queryAPIById(medicationID);  // Query the API using the FDAApiService
    }

    /**
     * Capitalizes and formats the JSON key as a heading, replacing underscores with spaces.
     *
     * @param key The key to format.
     * @return The formatted heading string.
     */
    private String capitalizeHeading(String key) {
        return key.replace("_", " ").substring(0, 1).toUpperCase() + key.replace("_", " ").substring(1);  // Capitalize the first letter
    }

    /**
     * Handles nested JSON objects and displays them in the content VBox.
     * Recursively processes nested structures to ensure all data is displayed.
     *
     * @param jsonObject The nested JSON object to process.
     * @param content The VBox where the content is displayed.
     */
    private void handleNestedJSONObject(JSONObject jsonObject, VBox content) {
        // Iterate over the keys of the nested JSON object
        for (String key : jsonObject.keySet()) {
            WrappingText nestedHeading = new WrappingText(capitalizeHeading(key), textWrappingWidth);  // Display nested keys as headings
            nestedHeading.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            content.getChildren().add(nestedHeading);

            Object value = jsonObject.get(key);  // Get the value associated with the nested key
            if (value instanceof String) {
                WrappingText valueWrappingText = new WrappingText((String) value, textWrappingWidth);  // Display value as text
                content.getChildren().add(valueWrappingText);
            } else if (value instanceof JSONObject) {
                handleNestedJSONObject((JSONObject) value, content);  // Recursively handle nested JSON objects
            } else if (value instanceof JSONArray) {
                // Iterate over the array in the nested object
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.length(); i++) {
                    if (array.get(i) instanceof String) {
                        WrappingText valueWrappingText = new WrappingText(array.getString(i), textWrappingWidth);  // Display array elements as text
                        content.getChildren().add(valueWrappingText);
                    } else if (array.get(i) instanceof JSONObject) {
                        handleNestedJSONObject(array.getJSONObject(i), content);  // Recursively handle nested objects in array
                    }
                }
            }
        }
    }
}
