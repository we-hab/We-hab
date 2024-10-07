package edu.qut.cab302.wehab.medication;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import edu.qut.cab302.wehab.WrappingText;

import java.io.IOException;

public class MedicationInfoPage {

    String medicationId;
    String medicationJson;
    JSONObject medicationJsonObject;
    Scene scene;


    public MedicationInfoPage(String medicationId) {
        this.medicationId = medicationId;
        try {
            medicationJson = queryAPI(medicationId);
        } catch (IOException e) {
            medicationJson = null;
        }
        medicationJsonObject = new JSONObject(medicationJson);

    }

    private int sceneWidth = 1200;
    private int textWrappingWidth = sceneWidth - 50;

    public Scene getMedicationInfoPage() {

        VBox content = new VBox();
        ScrollPane scrollPane = new ScrollPane(content);
        Scene scene = new Scene(scrollPane, sceneWidth, 800);

        for (String key : medicationJsonObject.keySet()) {
            WrappingText heading = new WrappingText(capitalizeHeading(key), textWrappingWidth);
            heading.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
            content.getChildren().add(heading);

            Object value = medicationJsonObject.get(key);
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
                            content.getChildren().add(nestedHeading);

                            Object nestedValue = nestedObject.get(nestedKey);
                            if (nestedValue instanceof String) {
                                WrappingText nestedValueWrappingText = new WrappingText((String) nestedValue, textWrappingWidth);
                                content.getChildren().add(nestedValueWrappingText);
                            } else if (nestedValue instanceof JSONArray) {
                                JSONArray deepNestedArray = (JSONArray) nestedValue;
                                for (int j = 0; j < deepNestedArray.length(); j++) {
                                    WrappingText deepNestedValueWrappingText = new WrappingText(deepNestedArray.getString(j), textWrappingWidth);
                                    content.getChildren().add(deepNestedValueWrappingText);
                                }
                            } else if (nestedValue instanceof JSONObject) {
                                handleNestedJSONObject((JSONObject) nestedValue, content);
                            }
                        }
                    }
                }
            } else if (value instanceof JSONObject) {
                handleNestedJSONObject((JSONObject) value, content);
            } else if (value instanceof String) {
                WrappingText valueWrappingText = new WrappingText((String) value, textWrappingWidth);
                content.getChildren().add(valueWrappingText);
            }

        }

        return scene;
    }

    private String queryAPI(String medicationID) throws IOException {
        FDAApiService fdaApiService = new FDAApiService();
        return fdaApiService.queryAPIById(medicationID);
    }

    private String capitalizeHeading(String key) {
        return key.replace("_", " ").substring(0, 1).toUpperCase() + key.replace("_", " ").substring(1);
    }

    private void handleNestedJSONObject(JSONObject jsonObject, VBox content) {
        for (String key : jsonObject.keySet()) {
            WrappingText nestedHeading = new WrappingText(capitalizeHeading(key), textWrappingWidth);
            nestedHeading.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            content.getChildren().add(nestedHeading);

            Object value = jsonObject.get(key);
            if (value instanceof String) {
                WrappingText valueWrappingText = new WrappingText((String) value, textWrappingWidth);
                content.getChildren().add(valueWrappingText);
            } else if (value instanceof JSONObject) {
                handleNestedJSONObject((JSONObject) value, content);
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.length(); i++) {
                    if (array.get(i) instanceof String) {
                        WrappingText valueWrappingText = new WrappingText(array.getString(i), textWrappingWidth);
                        content.getChildren().add(valueWrappingText);
                    } else if (array.get(i) instanceof JSONObject) {
                        handleNestedJSONObject(array.getJSONObject(i), content);
                    }
                }
            }
        }
    }

}
