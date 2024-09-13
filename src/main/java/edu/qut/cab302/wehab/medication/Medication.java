package edu.qut.cab302.wehab.medication;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Medication {

    private LocalDate lastUpdated;
    private String genericName;
    private String brandName;
    private JSONArray activeIngredients;
    private String administrationRoute;
    private JSONArray establishedPharmacologicClasses;
    private String description;


    // Methods
    Medication(JSONObject jsonMedicationObject) {

        String effectiveTime = jsonMedicationObject.optString("effective_time");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        lastUpdated = LocalDate.parse(effectiveTime, formatter);

        if(jsonMedicationObject.has("description")) {
            description = jsonMedicationObject.optJSONArray("description").optString(0);
        } else if(jsonMedicationObject.has("purpose")) {
            description = jsonMedicationObject.optJSONArray("purpose").optString(0);
        }

        JSONObject openfda = jsonMedicationObject.getJSONObject("openfda");

        genericName = openfda.optJSONArray("generic_name").optString(0);
        brandName = openfda.optJSONArray("brand_name").optString(0);

        if(openfda.has("substance_name")) {
            activeIngredients = openfda.optJSONArray("substance_name");
        } else if(openfda.has("active_ingredients")) {
            activeIngredients = openfda.optJSONArray("active_ingredients");
        }

        if (openfda.has("route")) {
            administrationRoute = openfda.optJSONArray("route").optString(0);
        }
        establishedPharmacologicClasses = openfda.optJSONArray("pharm_class_epc");



        printInfo();
    }

    public void printInfo() {
        System.out.println("Last Updated: " + lastUpdated);
        System.out.println("Generic Name: " + genericName);
        System.out.println("Brand Name: " + brandName);
        System.out.print("Active Ingredients:");
        if (activeIngredients != null) {
            for(int i = 0; i < activeIngredients.length(); i++) {
                System.out.print("\n\t- " + activeIngredients.getString(i));
            }
            System.out.println();
        } else {
            System.out.println(" Unspecified");
        }
        System.out.println("Administration Route: " + ((administrationRoute != null) ? administrationRoute : "Unspecified"));
        System.out.print("Medication Type:");

        if (establishedPharmacologicClasses != null) {
            for (int i = 0; i < establishedPharmacologicClasses.length(); i++) {
                System.out.print("\n\t- " + establishedPharmacologicClasses.getString(i));
            }
            System.out.println();
        } else {
            System.out.println(" Unspecified");
        }

        System.out.println("Description: " + description);
        System.out.println();
    }


}