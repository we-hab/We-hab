package edu.qut.cab302.wehab.models.medication;

import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import static edu.qut.cab302.wehab.dao.MedicationDAO.getDisplayNameById;
import static edu.qut.cab302.wehab.util.EncryptionUtility.decrypt;

public class MedicationReminderFactory {

    public static MedicationReminder createFromResultSet(ResultSet rs) throws SQLException {

        String reminderID = rs.getString("reminderID");
        String username = rs.getString("username");
        String ciphertextMedicationID = rs.getString("medicationID");
        String plaintextMedicationID;
        try {
            plaintextMedicationID = decrypt(ciphertextMedicationID);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        String displayName;

        displayName = getDisplayNameById(plaintextMedicationID);
        double dosageAmount = rs.getDouble("dosageAmount");
        String dosageUnit = rs.getString("dosageUnit");
        LocalDate dosageDate = LocalDate.parse(rs.getString("dosageDate"));
        LocalTime dosageTime = LocalTime.parse(rs.getString("dosageTime"));

//        System.out.println("Created object for reminder ID: " + reminderID);
//        System.out.println("\tUsername: " + username);
//        System.out.println("\tCiphertextMedicationID: " + ciphertextMedicationID);
//        System.out.println("\tPlaintextMedicationID: " + plaintextMedicationID);

        String status = rs.getString("status") != null ? rs.getString("status") : null;

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
