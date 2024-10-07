package edu.qut.cab302.wehab.medication;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;


import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;

/**
 * This class is responsible for directly querying the openFDA API to fetch drug label information.
 * It facilitates searching for a medication by its brand or generic name and handles HTTP requests and responses.
 */
public class FDAApiService {

    /**
     * Message containing the result of the HTTP request, used for UI output and error handling.
     */
    private String resultsMessage;

    /**
     * @return string containing the result message or error details.
     */
    public String getResultsMessage() {
        return resultsMessage;
    }

    /**
     * Opens an HTTP connection to the given URL.
     *
     * @param url The URL to which the connection is opened.
     * @return An HttpURLConnection object for the specified URL.
     * @throws IOException If an input or output exception occurs while opening the connection.
     */
    protected HttpURLConnection openConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    /**
     * Creates a URL object from a string representation of the URL.
     *
     * @param urlString The string representation of the URL.
     * @return A URL object created from the given string.
     * @throws MalformedURLException If the string is not of a URL structure or cannot be parsed.
     */
    protected URL createUrlObject(String urlString) throws MalformedURLException {
        return new URL(urlString);
    }

    /**
     * Queries the API for information about a medication by brand or generic name.
     * First attempts to search by brand name, and if no results are found, searches by generic name.
     *
     * @param medicationName The name of the medication to search for (brand or generic).
     * @return A String containing the API response, which is typically JSON data.
     * null returned if an error occurs.
     * @throws SocketTimeoutException If a connection timeout occurs during the query.
     */
    public String queryAPI(String medicationName) throws SocketTimeoutException {

        resultsMessage = null;

        try {

            String encodedMedicationName = URLEncoder.encode(medicationName, StandardCharsets.UTF_8.toString());
            String apiUrlString;

            // Define a cutoff date of 3 years before the current date to limit search results.
            LocalDate cutoffDate = LocalDate.now().minusYears(3);
            String cutoffTime = cutoffDate.toString();

            apiUrlString = "https://api.fda.gov/drug/label.json?search=openfda.brand_name:%22" + encodedMedicationName + "%22+AND+effective_time:[" + cutoffTime + "+TO+*]&limit=30";

            URL url = createUrlObject(apiUrlString);
            HttpURLConnection connection = openConnection(url);
            connection.setRequestMethod("GET");

            int status = 0;
            try {
                status = connection.getResponseCode();
            } catch (UnknownHostException e) {
                return null;
            }
            InputStream inputStream;

            if (status == 200) {
                System.out.println("Brand name search successful.");
                inputStream = connection.getInputStream();

            } else if (status == 404) {
                System.out.println("Brand name not found. Trying generic search...");
                apiUrlString = "https://api.fda.gov/drug/label.json?search=openfda.generic_name:%22" + encodedMedicationName + "%22+AND+effective_time:[" + cutoffTime + "+TO+*]&limit=30";
                url = new URL(apiUrlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                status = connection.getResponseCode();

                if (status == 200) {
                    System.out.println("Generic search successful.");
                    inputStream = connection.getInputStream();
                } else if (status == 404) {
                    System.out.println("Generic not found.");
                    inputStream = connection.getErrorStream();
                } else if (status == 400) {
                    resultsMessage = "Bad request: " + medicationName;
                    System.out.println("HTTP error response code: " + status);
                    System.out.println(resultsMessage);
                    inputStream = connection.getErrorStream();
                } else {
                    resultsMessage = "HTTP error response code: " + status;
                    System.out.println(resultsMessage);
                    inputStream = connection.getErrorStream();
                }

            } else if (status == 400) {
                resultsMessage = "Bad request: " + medicationName;
                System.out.println(resultsMessage);
                inputStream = connection.getErrorStream();

            } else {
                resultsMessage = "HTTP error response code: " + status;
                System.out.println(resultsMessage);
                inputStream = connection.getErrorStream();
            }

            // Read the API response from the InputStream.
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder content = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                connection.disconnect();

                return content.toString();
            } else {
                return null;
            }

        } catch (SocketTimeoutException e) {
            resultsMessage = e.getMessage();
            return null;
        } catch (Exception e) {
            resultsMessage = e.getMessage();
            e.printStackTrace();
            return null;
        }
    }

    public String queryAPIById(String id) throws IOException {

        String results;

        String encodedMedicationId = URLEncoder.encode(id, StandardCharsets.UTF_8.toString());
        String apiUrlString = "https://api.fda.gov/drug/label.json?search=openfda.id=%22" + encodedMedicationId + "%22&limit=1";

        System.out.println("Querying API with ID: " + encodedMedicationId + "\nUsing URL: " + (apiUrlString));

        URL url = createUrlObject(apiUrlString);
        HttpURLConnection connection = openConnection(url);
        connection.setRequestMethod("GET");

        int status = 0;
        try {
            status = connection.getResponseCode();
        } catch (UnknownHostException e) {
            return null;
        }
        InputStream inputStream;

        if (status == 200) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }

        // Read the API response from the InputStream.
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
            connection.disconnect();

            return content.toString();
        } else {
            return null;
        }
    }
}