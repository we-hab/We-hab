package edu.qut.cab302.wehab.models.medication;

import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import static edu.qut.cab302.wehab.dao.MedicationDAO.getDisplayNameById;
import static edu.qut.cab302.wehab.util.EncryptionUtility.decrypt;

/**
 * Factory class responsible for creating instances of {@link MedicationReminder}
 * from a {@link ResultSet}.
 */
public class MedicationReminderFactory {

    /**
     * Creates a {@link MedicationReminder} object from a {@link ResultSet}.
     *
     * @param rs the {@link ResultSet} containing the data to construct a {@link MedicationReminder}
     * @return a new {@link MedicationReminder} object populated with data from the {@link ResultSet}
     * @throws SQLException if an SQL exception occurs while accessing the {@link ResultSet}
     */
    public static MedicationReminder createFromResultSet(ResultSet rs) throws SQLException {

        String reminderID = rs.getString("reminderID");
        String username = rs.getString("username");
        String ciphertextMedicationID = rs.getString("medicationID");
        String plaintextMedicationID;

        // Decrypt the medication ID to get the plaintext value
        try {
            plaintextMedicationID = decrypt(ciphertextMedicationID);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        // Retrieve the display name of the medication using the decrypted ID
        String displayName = getDisplayNameById(plaintextMedicationID);

        // Retrieve other dosage-related information
        double dosageAmount = rs.getDouble("dosageAmount");
        String dosageUnit = rs.getString("dosageUnit");
        LocalDate dosageDate = LocalDate.parse(rs.getString("dosageDate"));
        LocalTime dosageTime = LocalTime.parse(rs.getString("dosageTime"));

        // Retrieve the reminder status if available, or set it to null
        String status = rs.getString("status") != null ? rs.getString("status") : null;

        // Build and return the MedicationReminder object
        return new MedicationReminder.MedicationReminderBuilder()
                .withReminderID(reminderID)
                .withUsername(username)
                .withMedicationID(plaintextMedicationID)
                .withDisplayName(displayName)
                .withDosageAmount(dosageAmount)
                .withDosageUnit(dosageUnit)
                .withDosageDate(dosageDate)
                .withDosageTime(dosageTime)
                .withStatus(status)
                .build();
    }
}
