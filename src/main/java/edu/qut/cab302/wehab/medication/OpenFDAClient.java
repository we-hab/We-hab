package edu.qut.cab302.wehab.medication;

import org.json.JSONArray;

import java.net.SocketTimeoutException;

public class OpenFDAClient {

    private FDAApiService apiService;
    private MedicationParser medicationParser;

    private String resultsMessageForView;
    public String getResultsMessageForView() { return resultsMessageForView; }

    public OpenFDAClient() {
        apiService = new FDAApiService();
        medicationParser = new MedicationParser();
    }

    public Medication[] searchForMedications(String query) {

        String apiResultsMessage;
        String jsonParserResultsMessage;

        try {
            String jsonResponse = apiService.queryAPI(query);
            apiResultsMessage = apiService.getResultsMessage();

            if(apiResultsMessage != null) {
                resultsMessageForView = apiResultsMessage;
            }

            Medication[] searchResults = medicationParser.parseMedications(jsonResponse);
            jsonParserResultsMessage = medicationParser.getResultsMessage();

            if(resultsMessageForView != null) {
                resultsMessageForView += "\n" + jsonParserResultsMessage;
            } else {
                resultsMessageForView = jsonParserResultsMessage;
            }

            System.out.println(resultsMessageForView);

            return searchResults;

        } catch (SocketTimeoutException e) {
            resultsMessageForView = e.getMessage();
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
