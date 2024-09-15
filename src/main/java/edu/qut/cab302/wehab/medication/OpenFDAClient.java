package edu.qut.cab302.wehab.medication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class OpenFDAClient {

    private static String resultsMessage;
    public static String getResultsMessage() { return resultsMessage; }

    public static Medication[] searchForMedications(String query) {

        resultsMessage = "";

        JSONArray results = getMedicationsFromResults(getJSONObjectFromRawString(queryAPI(query)));

        if(results != null) {

            int resultsCount = results.length();

            Medication[] medications = new Medication[resultsCount];

            for(int i = 0; i < results.length(); i++) {
                medications[i] = new Medication(results.getJSONObject(i));
            }
            return medications;
        }

        return null;
    }

    private static JSONArray getMedicationsFromResults(JSONObject apiResult) {

        JSONArray results = apiResult.optJSONArray("results");

        if (results != null) {
            resultsMessage = ("Results: " + apiResult.optJSONObject("meta").optJSONObject("results").optInt("total"));
            System.out.println(resultsMessage);
            System.out.println();
        } else if(resultsMessage == "") {
            {
                resultsMessage = "No results found.";
            }
        }

        return results;

    }

    public static JSONObject getJSONObjectFromRawString(String rawJSONString) {

        return new JSONObject(rawJSONString);

    }

    public static String queryAPI(String medicationName) {

        try {

            String encodedMedicationName = URLEncoder.encode(medicationName, StandardCharsets.UTF_8.toString());
            String apiUrl;

            LocalDate cutoffDate = LocalDate.now().minusYears(3);
            String cutoffTime = cutoffDate.toString();

            apiUrl = "https://api.fda.gov/drug/label.json?search=openfda.brand_name:%22" + encodedMedicationName + "%22+AND+effective_time:[" + cutoffTime + "+TO+*]&limit=30";

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = 0;
            try {
                status = connection.getResponseCode();
            } catch (UnknownHostException e) {
                return null;
            }
            InputStream inputStream;

            if(status == 200) {
                System.out.println("Brand name search successful.");
                inputStream = connection.getInputStream();

            } else if (status == 404) {
                System.out.println("Brand name not found. Trying generic search...");
                apiUrl = "https://api.fda.gov/drug/label.json?search=openfda.generic_name:%22" + encodedMedicationName + "%22+AND+effective_time:[" + cutoffTime + "+TO+*]&limit=30";
                url = new URL(apiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                status = connection.getResponseCode();

                if(status == 200) {
                    System.out.println("Generic search successful.");
                    inputStream = connection.getInputStream();
                } else if (status == 404) {
                    System.out.println("Generic not found.");
                    inputStream = connection.getErrorStream();
                } else {
                    resultsMessage = "Error searching for medication: " + medicationName;
                    System.out.println("HTTP error response code: " + status);
                    System.out.println(resultsMessage);
                    inputStream = connection.getErrorStream();
                }

            } else {
                resultsMessage += "Error searching for medication: " + medicationName;
                System.out.println("HTTP error response code: " + status);
                System.out.println(resultsMessage);
                inputStream = connection.getErrorStream();
            }


            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
            connection.disconnect();

            return content.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}