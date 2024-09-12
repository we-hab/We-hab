package edu.qut.cab302.wehab.medication;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


public class OpenFDAClient {

    public static ArrayList<Medication> searchForMedications(String query) {

        JSONArray results = getMedicationsFromResults(getJSONObjectFromRawString(queryAPI(query)));

        if(results != null) {

            ArrayList<Medication> medications = new ArrayList<Medication>();

            for(int i = 0; i < results.length(); i++) {
                JSONObject openfda = results.getJSONObject(i).getJSONObject("openfda");
                if(openfda.has("product_type")) {
                    medications.add(new Medication(results.getJSONObject(i)));
                }
            }
            return medications;
        }

        System.out.println("No results found.");
        return null;
    }

    private static JSONArray getMedicationsFromResults(JSONObject apiResult) {

        JSONArray results = apiResult.optJSONArray("results");
        return results;

    }

    public static JSONObject getJSONObjectFromRawString(String rawJSONString) {

        return new JSONObject(rawJSONString);

    }

    public static String queryAPI(String medicationName) {

        try {

            String encodedMedicationName = URLEncoder.encode(medicationName, StandardCharsets.UTF_8.toString());
            String apiUrl;

            apiUrl = "https://api.fda.gov/drug/label.json?search=active_ingredient:%22" + encodedMedicationName + "%22+OR+openfda.brand_name:%22" + encodedMedicationName + "%22&limit=30";

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = connection.getResponseCode();
            InputStream inputStream;

            if(status == 200) {
                inputStream = connection.getInputStream();
            } else {
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