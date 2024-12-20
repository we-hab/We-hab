package edu.qut.cab302.wehab.models.medication;

import java.net.SocketTimeoutException;

/**
 * The OpenFDAClient class provides functionality to search for medications using the openFDA API and parse the results.
 * It uses the {@link FDAApiService} to query the openFDA API and the {@link MedicationParser} to parse the
 * JSON responses into Medication objects.
 */
public class OpenFDAClient {

    private final FDAApiService apiService;
    private final MedicationParser medicationParser;

    /**
     * A message intended for the view, providing information about the status of the API query and parsing of results.
     */
    private String resultsMessageForView;

    /**
     * @return the results message to be shown in the view.
     */
    public String getResultsMessageForView() { return resultsMessageForView; }

    /**
     * Constructs an OpenFDAClient object by initialising the API service and the medication parser.
     */
    public OpenFDAClient() {
        apiService = new FDAApiService();
        medicationParser = new MedicationParser();
    }

    /**
     * Searches for medications using a given query string by calling the queryAPI method of {@link FDAApiService} and passing the results to {@link MedicationParser} for parsing.
     * @param query the search query to send to the API.
     * @return an array of {@link Medication} objects matching the search query, or null if an error occurred.
     */
    public Medication[] searchForMedications(String query) {

        String jsonParserResultsMessage;
        String jsonResponse;

        try {
            // Query the API with the given search string
            jsonResponse = apiService.queryAPI(query);

        } catch (SocketTimeoutException e) {
            resultsMessageForView = e.getMessage();
            return null;
        }

        resultsMessageForView = apiService.getResultsMessage();

        // Parse the JSON response into Medication objects
        Medication[] searchResults = medicationParser.parseMedications(jsonResponse);
        jsonParserResultsMessage = medicationParser.getResultsMessage();

        if(resultsMessageForView != null) {
            resultsMessageForView += "\n" + jsonParserResultsMessage;
        } else {
            resultsMessageForView = jsonParserResultsMessage;
        }

        // Print the results message for debugging
        System.out.println(resultsMessageForView);

        return searchResults;
    }
}
