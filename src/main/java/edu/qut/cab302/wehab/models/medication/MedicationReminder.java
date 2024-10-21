package edu.qut.cab302.wehab.models.medication;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class represents a medication reminder. It includes details such as the reminder ID, username,
 * medication ID, dosage amount, dosage unit, date, time, and status of the reminder.
 */
public class MedicationReminder {

    private String reminderID;  // Unique identifier for the medication reminder
    private String username;  // Username associated with the reminder
    private String medicationID;  // ID of the medication being reminded about
    private String displayName;  // Name to display for the medication
    private double dosageAmount;  // Dosage amount for the medication
    private String dosageUnit;  // Unit of the dosage (e.g., mg, ml)
    private LocalDate dosageDate;  // Date when the medication should be taken
    private LocalTime dosageTime;  // Time when the medication should be taken
    private String status;  // Status of the reminder (e.g., taken, missed)

    /**
     * Constructor for MedicationReminder.
     *
     * @param reminderID Unique identifier for the reminder.
     * @param username Username of the person who the reminder is for.
     * @param medicationID ID of the medication.
     * @param displayName Display name for the medication.
     * @param dosageAmount Dosage amount of the medication.
     * @param dosageUnit Unit of the dosage (e.g., mg, ml).
     * @param dosageDate Date for the medication to be taken.
     * @param dosageTime Time for the medication to be taken.
     * @param status Status of the reminder (e.g., taken, missed).
     */
    public MedicationReminder(String reminderID, String username, String medicationID, String displayName,
                              double dosageAmount, String dosageUnit, LocalDate dosageDate,
                              LocalTime dosageTime, String status) {
        this.reminderID = reminderID;
        this.username = username;
        this.medicationID = medicationID;
        this.displayName = displayName;
        this.dosageAmount = dosageAmount;
        this.dosageUnit = dosageUnit;
        this.dosageDate = dosageDate;
        this.dosageTime = dosageTime;
        if (status != null) {
            this.status = status;
        }
    }

    /**
     * Gets the unique identifier for the reminder.
     *
     * @return The reminder ID.
     */
    public String getReminderID() {
        return reminderID;
    }

    /**
     * Gets the username associated with the reminder.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the reminder.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the ID of the medication associated with the reminder.
     *
     * @return The medication ID.
     */
    public String getMedicationID() {
        return medicationID;
    }

    /**
     * Sets the medication ID for the reminder.
     *
     * @param medicationID The medication ID to set.
     */
    public void setMedicationID(String medicationID) {
        this.medicationID = medicationID;
    }

    /**
     * Gets the display name of the medication.
     *
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name of the medication.
     *
     * @param displayName The display name to set.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the dosage amount for the medication.
     *
     * @return The dosage amount.
     */
    public double getDosageAmount() {
        return dosageAmount;
    }

    /**
     * Sets the dosage amount for the medication.
     *
     * @param dosageAmount The dosage amount to set.
     */
    public void setDosageAmount(double dosageAmount) {
        this.dosageAmount = dosageAmount;
    }

    /**
     * Gets the unit of the dosage (e.g., mg, ml).
     *
     * @return The dosage unit.
     */
    public String getDosageUnit() {
        return dosageUnit;
    }

    /**
     * Sets the unit of the dosage (e.g., mg, ml).
     *
     * @param dosageUnit The dosage unit to set.
     */
    public void setDosageUnit(String dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    /**
     * Returns the dosage information as a string combining the amount and unit.
     *
     * @return A string containing the dosage amount and unit.
     */
    public String getDosageString() {
        return dosageAmount + " " + dosageUnit;
    }

    /**
     * Gets the date for when the medication should be taken.
     *
     * @return The dosage date.
     */
    public LocalDate getDosageDate() {
        return dosageDate;
    }

    /**
     * Sets the date for when the medication should be taken.
     *
     * @param dosageDate The dosage date to set.
     */
    public void setDosageDate(LocalDate dosageDate) {
        this.dosageDate = dosageDate;
    }

    /**
     * Gets the time for when the medication should be taken.
     *
     * @return The dosage time.
     */
    public LocalTime getDosageTime() {
        return dosageTime;
    }

    /**
     * Sets the time for when the medication should be taken.
     *
     * @param dosageTime The dosage time to set.
     */
    public void setDosageTime(LocalTime dosageTime) {
        this.dosageTime = dosageTime;
    }

    /**
     * Gets the status of the reminder (e.g., taken, missed).
     *
     * @return The status of the reminder.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the reminder (e.g., taken, missed).
     *
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Prints all the details of the medication reminder to the console.
     */
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
