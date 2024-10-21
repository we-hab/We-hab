package edu.qut.cab302.wehab.models.medication;

import java.time.LocalDate;
import java.time.LocalTime;

public class MedicationReminder {

    private String reminderID;
    private String username;
    private String medicationID;
    private String displayName;
    private double dosageAmount;
    private String dosageUnit;
    private LocalDate dosageDate;
    private LocalTime dosageTime;
    private String status;

    public String getReminderID() { return reminderID; }

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

    public String getDosageString() { return dosageAmount + " " + dosageUnit;}

    public LocalDate getDosageDate() { return dosageDate; }
    public void setDosageDate(LocalDate dosageDate) { this.dosageDate = dosageDate; }

    public LocalTime getDosageTime() { return dosageTime; }
    public void setDosageTime(LocalTime dosageTime) { this.dosageTime = dosageTime; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    public MedicationReminder(String reminderID, String username, String medicationID, String displayName, double dosageAmount, String dosageUnit, LocalDate dosageDate, LocalTime dosageTime, String status) {
        this.reminderID = reminderID;
        this.username = username;
        this.medicationID = medicationID;
        this.displayName = displayName;
        this.dosageAmount = dosageAmount;
        this.dosageUnit = dosageUnit;
        this.dosageDate = dosageDate;
        this.dosageTime = dosageTime;

        if(status != null) {
            this.status = status;
        }
    }

    public void printAllDetails() {
        System.out.println("-------------------------------");
        System.out.println("Details of Medication Reminder:");
        System.out.println("Reminder ID: " + reminderID);
        System.out.println("Username: " + username);
        System.out.println("Medication ID: " + medicationID);
        System.out.println("Display Name: " + displayName);
        System.out.println("Dosage Amount: " + dosageAmount);
        System.out.println("Dosage Unit: " + dosageUnit);
        System.out.println("Dosage Date: " + dosageDate);
        System.out.println("Dosage Time: " + dosageTime);
        System.out.println("Status: " + status);
        System.out.println("-------------------------------");
    }

}