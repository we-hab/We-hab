package edu.qut.cab302.wehab.medication;

import java.util.HashMap;

import org.json.JSONObject;

public class Medication {

    private String genericName; // Paracetamol, Lisdexamfetamine, etc.
    private String brandName; // Panadol, Vyvanse, etc.
    private String medicationClass; // Antibiotic, SSRI, etc.
    private String manufacturer; // Square Pharmaceuticals, etc.
    private String description; // "30 tablets in bottle", etc.
    private HashMap<String, String> activeIngredients = new HashMap<String, String>(); // Key: Ingredient, Value: Strength
    private String form; // Tablet, injection etc.

    Medication(JSONObject jsonMedicationData) {


    }

}
