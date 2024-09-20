package edu.qut.cab302.wehab.medication;

import org.json.JSONArray;
import org.json.JSONObject;

public class MedicationParser {

    private String resultsMessage;
    public String getResultsMessage() { return resultsMessage; }

    public Medication[] parseMedications(String jsonResponse) {

        if(jsonResponse == null) {
            resultsMessage = "No results found.";
            return null;
        }

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
