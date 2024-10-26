package edu.qut.cab302.wehab.controllers.medication;

import edu.qut.cab302.wehab.dao.MedicationDAO;
import edu.qut.cab302.wehab.models.medication.FDAApiService;
import edu.qut.cab302.wehab.models.medication.Medication;
import edu.qut.cab302.wehab.models.medication.MedicationParser;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import edu.qut.cab302.wehab.util.WrappingText;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for displaying detailed information about a specific medication
 * by rendering JSON data loaded from the openFDA API using an FDAApiService object.
 */
public class MedicationInfoPageController {

    private static String activeStyleSheet = "/edu/qut/cab302/wehab/css/MainStyleSheet.css";  // Default stylesheet
    private static String activeTextSizeSheet = "/edu/qut/cab302/wehab/css/MainStyleSheet.css";  // Default text size

    // Medication identifier
    private String medicationId;

    // Raw JSON string of the medication information
    private String medicationJson;

    // Parsed JSON object containing medication data
    private final JSONObject medicationJsonObject;

    private Medication medication;

    public MedicationInfoPageController(String medicationId) {
        this.medicationId = medicationId;
        try {
            medicationJson = queryAPI(this.medicationId);
        } catch (IOException e) {
            medicationJson = null;
        }
        MedicationParser medicationParser = new MedicationParser();
        medication = medicationParser.parseMedications(medicationJson)[0];

        medicationJsonObject = new JSONObject(medicationJson);
    }

    // UI layout constants
    private final int sceneWidth = 1200;
    private final int sceneHeight = 900;
    private final int textWrappingWidth = sceneWidth - 50;

    /**
     * Generates a Scene to display the medication information.
     * This method processes the JSON data and builds the UI components dynamically.
     *
     * @return A Scene object representing the medication information page.
     */
    public Scene getMedicationInfoPage() {

        // Initialise the VBox to hold all UI components
        // Container to hold the UI components of the medication page
        VBox content = new VBox();
        content.setMinSize(sceneWidth, sceneHeight);
        content.setMaxWidth(sceneWidth);
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setMinSize(sceneWidth, sceneHeight);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Scene scene = new Scene(scrollPane, sceneWidth, sceneHeight);

        Label displayName = new Label(medication.getDisplayName());
        displayName.setWrapText(true);
        displayName.setStyle("-fx-font-weight: bold; -fx-font-size: 24;");
        content.getChildren().add(displayName);

        // Iterate through each key in the JSON object to dynamically generate UI components
        for (String key : medicationJsonObject.keySet()) {
            WrappingText heading = new WrappingText(capitalizeHeading(key), textWrappingWidth);
            heading.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
            content.getChildren().add(heading);

            Object value = medicationJsonObject.get(key);
            // Handle different types of JSON values (String, JSONObject, JSONArray)
            if (value instanceof JSONArray) {
                JSONArray valuesArray = (JSONArray) value;
                for (int i = 0; i < valuesArray.length(); i++) {
                    Object arrayValue = valuesArray.get(i);
                    if (arrayValue instanceof String) {
                        WrappingText valueWrappingText = new WrappingText((String) arrayValue, textWrappingWidth);
                        content.getChildren().add(valueWrappingText);
                    } else if (arrayValue instanceof JSONObject) {
                        JSONObject nestedObject = (JSONObject) arrayValue;
                        for (String nestedKey : nestedObject.keySet()) {
                            WrappingText nestedHeading = new WrappingText(capitalizeHeading(nestedKey), textWrappingWidth);
                            nestedHeading.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
                            addContentToView(nestedHeading, content);

                            Object nestedValue = nestedObject.get(nestedKey);
                            if (nestedValue instanceof String) {
                                WrappingText nestedValueWrappingText = new WrappingText((String) nestedValue, textWrappingWidth);
                                addContentToView(nestedValueWrappingText, content);
                            } else if (nestedValue instanceof JSONArray) {
                                JSONArray deepNestedArray = (JSONArray) nestedValue;
                                for (int j = 0; j < deepNestedArray.length(); j++) {
                                    WrappingText deepNestedValueWrappingText = new WrappingText(deepNestedArray.getString(j), textWrappingWidth);
                                    addContentToView(deepNestedValueWrappingText, content);
                                }
                            } else if (nestedValue instanceof JSONObject) {
                                // Recursively handle deeply nested JSON objects
                                handleNestedJSONObject((JSONObject) nestedValue, content);
                            }
                        }
                    }
                }
            } else if (value instanceof JSONObject) {
                handleNestedJSONObject((JSONObject) value, content);
            } else if (value instanceof String) {
                WrappingText valueWrappingText = new WrappingText((String) value, textWrappingWidth);
                addContentToView(valueWrappingText, content);
            }
        }

        scene.getStylesheets().add(getClass().getResource(activeStyleSheet).toExternalForm());
        scene.getStylesheets().add(getClass().getResource(activeTextSizeSheet).toExternalForm());
        content.getStyleClass().add("modal-container");

        return scene;
    }

    /**
     * Queries the FDA API for medication data based on the ID.
     *
     * @param medicationID The ID of the medication to query.
     * @return A JSON string containing the medication data.
     * @throws IOException If an error occurs while querying the API.
     */
    private String queryAPI(String medicationID) throws IOException {
        FDAApiService fdaApiService = new FDAApiService();
        return fdaApiService.queryAPIById(medicationID);
    }

    /**
     * Capitalises the first letter of a JSON key and replaces underscores with spaces.
     * This is used for formatting JSON keys into readable headings.
     *
     * @param key The JSON key to format.
     * @return The formatted heading string.
     */
    private String capitalizeHeading(String key) {
        return key.replace("_", " ").substring(0, 1).toUpperCase() + key.replace("_", " ").substring(1);
    }

    /**
     * Handles nested JSON objects by recursively adding their content to the VBox.
     *
     * @param jsonObject The nested JSON object to process.
     * @param pageContents The VBox to add the content to.
     */
    private void handleNestedJSONObject(JSONObject jsonObject, VBox pageContents) {
        for (String key : jsonObject.keySet()) {
            WrappingText nestedHeading = new WrappingText(capitalizeHeading(key), textWrappingWidth);
            nestedHeading.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            addContentToView(nestedHeading, pageContents);

            Object value = jsonObject.get(key);
            if (value instanceof String) {
                WrappingText valueWrappingText = new WrappingText((String) value, textWrappingWidth);
                addContentToView(valueWrappingText, pageContents);
            } else if (value instanceof JSONObject) {
                handleNestedJSONObject((JSONObject) value, pageContents);
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.length(); i++) {
                    if (array.get(i) instanceof String) {
                        WrappingText valueWrappingText = new WrappingText(array.getString(i), textWrappingWidth);
                        addContentToView(valueWrappingText, pageContents);
                    } else if (array.get(i) instanceof JSONObject) {
                        handleNestedJSONObject(array.getJSONObject(i), pageContents);
                    }
                }
            }
        }
    }

    /**
     * Adds a WrappingText node to the provided VBox view.
     * If the text contains HTML, it renders the content in a WebView.
     *
     * @param text The WrappingText object to add.
     * @param view The VBox container to which the content is added.
     */
    private void addContentToView(WrappingText text, VBox view) {

        String plainText = text.getText();

        if(isHtml(plainText)) {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.loadContent(plainText);
            webView.setFocusTraversable(false);
            view.getChildren().add(webView);
        } else {
            view.getChildren().add(text);
        }
    }

    /**
     * Determines if a string contains HTML content.
     *
     * @param text The string to check.
     * @return True if the text contains HTML tags, false otherwise.
     */
    private boolean isHtml(String text) {
        return text != null && text.trim().matches(".*<[^>]+>.*"); // Regex to check for HTML tags
    }
}