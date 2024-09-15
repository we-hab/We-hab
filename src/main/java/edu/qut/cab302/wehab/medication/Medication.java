package edu.qut.cab302.wehab.medication;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Medication {

    private String id;
    private LocalDate lastUpdated;
    private String brandName;
    private String genericName;
    private String manufacturer;
    private JSONArray activeIngredients;
    private String administrationRoute;
    private JSONArray establishedPharmacologicClasses;
    private String description;

    private String[] activeIngredientNames;
    private String[] establishedPharmacologicClassNames;

    public String getID() { return id; }
    public LocalDate getLastUpdated() { return lastUpdated; }
    public String getBrandName() { return brandName; }
    public String getGenericName() { return genericName; }
//    public String get
    public String[] getActiveIngredients() { return activeIngredientNames; }
    public String getAdministrationRoute() { return administrationRoute; }
    public String[] getMedicationTypes() { return establishedPharmacologicClassNames; }
    public String getDescription() { return description; }

    public boolean hasBrandName() { return brandName != null; }

    // Methods
    Medication(JSONObject jsonMedicationObject) {

        id = jsonMedicationObject.getString("id");

        String effectiveTime = jsonMedicationObject.optString("effective_time");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        lastUpdated = LocalDate.parse(effectiveTime, formatter);

        if(jsonMedicationObject.has("description")) {
            description = jsonMedicationObject.optJSONArray("description").optString(0);
        } else if(jsonMedicationObject.has("purpose")) {
            description = jsonMedicationObject.optJSONArray("purpose").optString(0);
        }

        JSONObject openfda = jsonMedicationObject.getJSONObject("openfda");

        manufacturer = openfda.optJSONArray("manufacturer_name").optString(0);

        brandName = openfda.optJSONArray("brand_name").optString(0) + " (" + manufacturer + ")";

        genericName = openfda.optJSONArray("generic_name").optString(0);

        if(openfda.has("substance_name")) {
            activeIngredients = openfda.optJSONArray("substance_name");
            activeIngredientNames = new String[activeIngredients.length()];
            for(int i = 0; i < activeIngredients.length(); i++) {
                activeIngredientNames[i] = activeIngredients.optString(i);
            }
        } else if(openfda.has("active_ingredients")) {
            activeIngredients = openfda.optJSONArray("active_ingredients");
            activeIngredientNames = new String[activeIngredients.length()];
            for(int i = 0; i < activeIngredients.length(); i++) {
                activeIngredientNames[i] = activeIngredients.optString(i);
            }
        }

        if (openfda.has("route")) {
            administrationRoute = openfda.optJSONArray("route").optString(0);
        }

        if(openfda.has("pharm_class_epc")) {
            establishedPharmacologicClasses = openfda.optJSONArray("pharm_class_epc");
            establishedPharmacologicClassNames = new String[establishedPharmacologicClasses.length()];
            for(int i = 0; i < establishedPharmacologicClasses.length(); i++) {
                establishedPharmacologicClassNames[i] = establishedPharmacologicClasses.optString(i);
            }
        }
    }

    public void printInfo() {
        System.out.println("Last Updated: " + lastUpdated);
        System.out.println("Generic Name: " + genericName);
        System.out.println("Brand Name: " + brandName);
        System.out.print("Active Ingredients:");
        if (activeIngredientNames != null) {
            for(String ingredient : activeIngredientNames) {
                System.out.print("\n\t- " + ingredient);
            }
            System.out.println();
        } else {
            System.out.println(" Unspecified");
        }

        System.out.println("Administration Route: " + ((administrationRoute != null) ? administrationRoute : "Unspecified"));
        System.out.print("Medication Type:");

        if(establishedPharmacologicClassNames != null) {
            for(String pharmaClass : establishedPharmacologicClassNames) {
                System.out.print("\n\t- " + pharmaClass);
            }
            System.out.println();
        } else {
            System.out.println(" Unspecified");
        }

        System.out.println("Description: " + description);
        System.out.println();
    }


}