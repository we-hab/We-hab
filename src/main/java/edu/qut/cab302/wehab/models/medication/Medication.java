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

    private final String id;
    private final String displayName;
    private final LocalDate lastUpdated;
    private final String brandName;
    private final String genericName;
    private final String manufacturer;
    private final String administrationRoute;
    private final String description;
    private final String[] activeIngredientNames;
    private final String[] establishedPharmacologicClassNames;

    private Medication(MedicationBuilder builder) {

        this.id = builder.id;
        this.lastUpdated = builder.lastUpdated;
        this.brandName = builder.brandName;
        this.genericName = builder.genericName;
        this.manufacturer = builder.manufacturer;

        this.displayName = ((builder.brandName != null) ? builder.brandName : builder.genericName) + " (" + builder.manufacturer + ")";

        this.administrationRoute = builder.administrationRoute;
        this.description = builder.description;
        this.activeIngredientNames = builder.parsedActiveIngredients;
        this.establishedPharmacologicClassNames = builder.parsedEstablishedPharmClasses;


    }

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

    public static Medication createFromJsonObject(JSONObject jsonMedicationObject) {

        MedicationBuilder builder = new MedicationBuilder();

        builder.withID(jsonMedicationObject.getString("id"));

        String effectiveTime = jsonMedicationObject.optString("effective_time");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        builder.withLastUpdated(LocalDate.parse(effectiveTime, formatter));

        if(jsonMedicationObject.has("description")) {
            builder.withDescription(jsonMedicationObject.optJSONArray("description").optString(0));
        } else if(jsonMedicationObject.has("purpose")) {
            builder.withDescription(jsonMedicationObject.optJSONArray("purpose").optString(0));
        }

        JSONObject openfda = jsonMedicationObject.optJSONObject("openfda");

        builder.withManufacturer(openfda.optJSONArray("manufacturer_name").optString(0));

        builder.withBrandName(openfda.optJSONArray("brand_name").optString(0));

        builder.withGenericName(openfda.optJSONArray("generic_name").optString(0));

        if(openfda.has("substance_name")) {
            JSONArray activeIngredientsTemp = openfda.optJSONArray("substance_name");
            String[] activeIngredientNames = new String[activeIngredientsTemp.length()];
            for(int i = 0; i < activeIngredientsTemp.length(); i++) {
                activeIngredientNames[i] = activeIngredientsTemp.optString(i);
            }
            builder.withActiveIngredients(activeIngredientNames);
        } else if(openfda.has("active_ingredients")) {
            JSONArray activeIngredientsTemp = openfda.optJSONArray("active_ingredients");
            String[] activeIngredientNames = new String[activeIngredientsTemp.length()];
            for(int i = 0; i < activeIngredientsTemp.length(); i++) {
                activeIngredientNames[i] = activeIngredientsTemp.optString(i);
            }
            builder.withActiveIngredients(activeIngredientNames);
        }

        if (openfda.has("route")) {
            builder.withAdministrationRoute(openfda.optJSONArray("route").optString(0));
        }

        if(openfda.has("pharm_class_epc")) {
            JSONArray establishedPharmacologicClassesTemp = openfda.optJSONArray("pharm_class_epc");
            String[] establishedPharmacologicClassNames = new String[establishedPharmacologicClassesTemp.length()];
            for(int i = 0; i < establishedPharmacologicClassesTemp.length(); i++) {
                establishedPharmacologicClassNames[i] = establishedPharmacologicClassesTemp.optString(i);
            }
            builder.withEstablishedPharmClasses(establishedPharmacologicClassNames);
        }
        return builder.build();
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

    public static class MedicationBuilder {

        private String id;
        private String displayName;
        private LocalDate lastUpdated;
        private String brandName;
        private String genericName;
        private String manufacturer;
        private String administrationRoute;
        private String description;
        private String[] parsedActiveIngredients;
        private String[] parsedEstablishedPharmClasses;

        public MedicationBuilder withID(String id) {
            this.id = id;
            return this;
        }

        public MedicationBuilder withLastUpdated(LocalDate lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public MedicationBuilder withBrandName(String brandName) {
            this.brandName = brandName;
            return this;
        }

        public MedicationBuilder withGenericName(String genericName) {
            this.genericName = genericName;
            return this;
        }

        public MedicationBuilder withManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public MedicationBuilder withAdministrationRoute(String administrationRoute) {
            this.administrationRoute = administrationRoute;
            return this;
        }

        public MedicationBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public MedicationBuilder withActiveIngredients(String[] activeIngredientNames) {
            this.parsedActiveIngredients = activeIngredientNames;
            return this;
        }

        public MedicationBuilder withEstablishedPharmClasses(String[] establishedPharmacologicClasses) {
            this.parsedEstablishedPharmClasses = establishedPharmacologicClasses;
            return this;
        }

        public Medication build() {
            return new Medication(this);
        }
    }
}