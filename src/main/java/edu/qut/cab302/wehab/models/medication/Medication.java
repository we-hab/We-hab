package edu.qut.cab302.wehab.models.medication;

import org.json.JSONObject;
import org.json.JSONArray;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The Medication class represents a medication with various properties
 * such as FDA ID, brand name, generic name, manufacturer, active ingredients,
 * and pharmacologic classes. This class parses a JSON object from the
 * OpenFDA API and stores the relevant details about the medication.
 */
public class Medication {

    private String id;
    private String displayName;
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

    /**
     * @return The FDA ID of the medication.
     */
    public String getID() { return id; }


    /**
     * @return The display name (brand name if not null else generic name) of the medication.
     */
    public String getDisplayName() { return displayName; }

    /**
     * @return The last updated date for the medication information.
     */
    public LocalDate getLastUpdated() { return lastUpdated; }

    /**
     * @return The brand name of the medication.
     */
    public String getBrandName() { return brandName; }

    /**
     * @return The generic name of the medication.
     */
    public String getGenericName() { return genericName; }

    /**
     * @return The manufacturer of the medication.
     */
    public String getManufacturer() { return manufacturer; }

    /**
     * @return An array of active ingredient names, or null if unspecified.
     */
    public String[] getActiveIngredients() { return activeIngredientNames; }

    /**
     * @return The administration route of the medication (e.g., oral, topical),
     * or null if unspecified.
     */
    public String getAdministrationRoute() { return administrationRoute; }

    /**
     * @return An array of the established pharmacologic classes of the medication,
     * or null if unspecified.
     */
    public String[] getMedicationTypes() { return establishedPharmacologicClassNames; }

    /**
     * @return The description or purpose of the medication, or null if unspecified.
     */
    public String getDescription() { return description; }

    /**
     * Checks whether the medication has a brand name.
     *
     * @return true if a brand name exists, otherwise false.
     */
    public boolean hasBrandName() { return brandName != null; }

    /**
     * Constructs a Medication object by parsing a JSONObject containing the
     * medication information from the openFDA API.
     *
     * @param jsonMedicationObject The JSONObject representing the medication data.
     */
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

        JSONObject openfda = jsonMedicationObject.optJSONObject("openfda");

        manufacturer = openfda.optJSONArray("manufacturer_name").optString(0);

        brandName = openfda.optJSONArray("brand_name").optString(0) + " (" + manufacturer + ")";

        genericName = openfda.optJSONArray("generic_name").optString(0);

        displayName = (brandName != null) ? brandName : genericName;

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

    /**
     * *For debugging purposes*
     * Prints all relevant information about the medication to the console, including its
     * generic name, brand name, active ingredients, administration route,
     * pharmacologic classes, and description.
     */
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