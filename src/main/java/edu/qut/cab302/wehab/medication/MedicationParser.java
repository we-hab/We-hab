package edu.qut.cab302.wehab.medication;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The MedicationParser class provides methods to parse medication data returned by API queries.
 * This class is responsible for converting JSON data into an array of Medication objects.
 * It also provides feedback through a results message, indicating the success or failure of the parsing.
 */
public class MedicationParser {

    /**
     * Message indicating the result of the parsing process. It can either hold a success message
     * indicating the number of results found or a failure message indicating that no results were found.
     */
    private String resultsMessage;

    /**
     * @return the results message indicating the outcome of the last parsing operation.
     */
    public String getResultsMessage() { return resultsMessage; }

    /**
     * Parses a JSON response containing medication data and converts it into an array of Medication objects.
     * If the JSON response contains valid data, the method creates and returns an array of Medication objects.
     * If the JSON response is null or does not contain a "results" array, it returns null.
     *
     * @param jsonResponse the JSON response string to be parsed, which is expected to contain an array of medications under the "results" key.
     * @return an array of Medication objects parsed from the JSON response, or null if no valid results were found.
     */
    public Medication[] parseMedications(String jsonResponse) {

        if(jsonResponse == null) {
            resultsMessage = "No results found.";
            return null;
        }

        // Clear the previous resultsMessage.
        resultsMessage = null;

        JSONObject responseJSONObject = new JSONObject(jsonResponse);
        JSONArray results = responseJSONObject.optJSONArray("results");

        if (results != null) {

            resultsMessage = ("Results: " + results.length());

            int resultsCount = results.length();
            Medication[] medications = new Medication[resultsCount];

            for(int i = 0; i < results.length(); i++) {
                medications[i] = new Medication(results.getJSONObject(i));
            }
            return medications;

        } else {
            {
                resultsMessage = "No results found.";
            }
        }
        return null;
    }
}
