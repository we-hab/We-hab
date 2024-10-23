package edu.qut.cab302.wehab.models.medication;

import org.json.JSONObject;

public class MedicationFactory {

    public static Medication createFromJsonObject(JSONObject jsonObject) {
        return new Medication(jsonObject);
    }

}
