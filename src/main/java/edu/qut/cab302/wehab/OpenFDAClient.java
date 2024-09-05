package edu.qut.cab302.wehab;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class OpenFDAClient {

    public static String getMedicationInfo(String medicationName, boolean isGenericName) {

        try {

            String encodedMedicationName = URLEncoder.encode(medicationName, StandardCharsets.UTF_8.toString());
            String apiUrl;


            if(isGenericName) {
                apiUrl = "https://api.fda.gov/drug/ndc.json?search=generic_name:%22" + encodedMedicationName + "%22" + "&limit=1";

            } else {
                apiUrl = "https://api.fda.gov/drug/ndc.json?search=brand_name:%22" + encodedMedicationName + "%22" + "&limit=1";

            }
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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