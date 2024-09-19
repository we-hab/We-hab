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


import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;


public class FDAApiService {

    private String resultsMessage;
    public String getResultsMessage() { return resultsMessage; }

    protected HttpURLConnection openConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    public String queryAPI(String medicationName) {

        resultsMessage = null;

        try {

            String encodedMedicationName = URLEncoder.encode(medicationName, StandardCharsets.UTF_8.toString());
            String apiUrlString;

            LocalDate cutoffDate = LocalDate.now().minusYears(3);
            String cutoffTime = cutoffDate.toString();

            apiUrlString = "https://api.fda.gov/drug/label.json?search=openfda.brand_name:%22" + encodedMedicationName + "%22+AND+effective_time:[" + cutoffTime + "+TO+*]&limit=30";

            URL url = new URL(apiUrlString);
            HttpURLConnection connection = openConnection(url);
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
                apiUrlString = "https://api.fda.gov/drug/label.json?search=openfda.generic_name:%22" + encodedMedicationName + "%22+AND+effective_time:[" + cutoffTime + "+TO+*]&limit=30";
                url = new URL(apiUrlString);
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