package edu.qut.cab302.wehab.models.medication;

import java.time.LocalDate;
import java.time.LocalTime;

public class PrescribedMedicationDose {

    private String username;
    private String medicationID;
    private String displayName;
    private double dosageAmount;
    private String dosageUnit;
    private LocalDate dosageDate;
    private LocalTime dosageTime;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMedicationID() { return medicationID; }
    public void setMedicationID(String medicationID) { this.medicationID = medicationID; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public double getDosageAmount() { return dosageAmount; }
    public void setDosageAmount(double dosageAmount) { this.dosageAmount = dosageAmount; }

    public String getDosageUnit() { return dosageUnit; }
    public void setDosageUnit(String dosageUnit) { this.dosageUnit = dosageUnit; }

    public LocalDate getDosageDate() { return dosageDate; }
    public void setDosageDate(LocalDate dosageDate) { this.dosageDate = dosageDate; }

    public LocalTime getDosageTime() { return dosageTime; }
    public void setDosageTime(LocalTime dosageTime) { this.dosageTime = dosageTime; }


    public PrescribedMedicationDose(String username, String medicationID, String displayName, double dosageAmount, String dosageUnit, LocalDate dosageDate, LocalTime dosageTime) {
        this.username = username;
        this.medicationID = medicationID;
        this.displayName = displayName;
        this.dosageAmount = dosageAmount;
        this.dosageUnit = dosageUnit;
        this.dosageDate = dosageDate;
        this.dosageTime = dosageTime;
    }

}