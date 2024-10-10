package edu.qut.cab302.wehab.models.medication;

import edu.qut.cab302.wehab.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import edu.qut.cab302.wehab.database.Session;

/**
 * This class provides methods for interacting with the medication-related tables in the database.
 * It includes functionality for creating and deleting the medication and user-medication junction tables,
 * as well as saving medications and resetting the tables.
 */
public class MedicationSearchModel {

    /**
     * The database connection instance to be used for executing SQL queries.
     */
    private static Connection connection = DatabaseConnection.getInstance();

    private static String username = Session.getInstance().getLoggedInUser().getUsername();

    /**
     * Creates the medications table in the database if it does not already exist.
     * The medications table contains a primary key field for storing medication IDs.
     *
     * @throws SQLException if an error occurs while executing the SQL statement.
     */
    private static void createMedicationsTable() throws SQLException {

        Statement createMedicationsTable;

        String createTableSQL = "CREATE TABLE IF NOT EXISTS medications (" +
                "medicationID TEXT NOT NULL," +
                "username TEXT NOT NULL," +
                "displayName TEXT NOT NULL," +
                "addedDateTime TEXT NOT NULL," +
                "PRIMARY KEY (medicationID, username)," +
                "FOREIGN KEY (username) REFERENCES userAccounts(username) ON DELETE CASCADE" +
                ")";

        createMedicationsTable = connection.createStatement();
        createMedicationsTable.execute(createTableSQL);
    }

    public static void addMedicationToUserList(Medication medication) throws SQLException {

        String checkMedicationSql = "SELECT COUNT(*) FROM medications WHERE medicationID = ? AND username = ?";
        PreparedStatement checkMedication = connection.prepareStatement(checkMedicationSql);
        checkMedication.setString(1, medication.getID());
        checkMedication.setString(2, username);

        ResultSet resultSet = checkMedication.executeQuery();
        resultSet.next();

        int count = resultSet.getInt(1);

        if (count > 0) {
            throw new SQLException("You have already added this medication.");
        }

        String addMedicationSql = "INSERT INTO medications (medicationID, username, displayName, addedDateTime) VALUES (?, ?, ?, ?)";
        PreparedStatement addMedication = connection.prepareStatement(addMedicationSql);
        addMedication.setString(1, medication.getID());
        addMedication.setString(2, username);
        addMedication.setString(3, medication.getDisplayName());
        addMedication.setString(4, String.valueOf(LocalDateTime.now()));
        System.out.println(addMedication.executeUpdate());
    }

    public static HashMap<String, String> getUserSavedMedicationNames() throws SQLException {
        ResultSet resultSet = queryUserSavedMedications();
        HashMap<String, String> userSavedMedications = new HashMap<>();
        while (resultSet.next()) {
            userSavedMedications.put(resultSet.getString("displayName"), resultSet.getString("medicationID"));
        }
        return userSavedMedications;
    }

    public static void deleteUserSavedMedication(String medicationID) throws SQLException {
        String deleteMedicationSql = "DELETE FROM medications WHERE username = ? AND medicationID = ?";
        PreparedStatement deleteMedication = connection.prepareStatement(deleteMedicationSql);
        deleteMedication.setString(1, username);
        deleteMedication.setString(2, medicationID);
        deleteMedication.executeUpdate();
    }

    private static ResultSet queryUserSavedMedications() throws SQLException {

        String sqlCommand = "SELECT * FROM medications " +
                "WHERE username = ? " +
                "ORDER BY addedDateTime ASC";
        PreparedStatement sqlStatement = connection.prepareStatement("SELECT * FROM medications WHERE username = ?");

        sqlStatement.setString(1, username);
        return sqlStatement.executeQuery();
    }

    /**
     * Creates the user-medication junction table in the database if it does not already exist.
     * This table establishes a many-to-many relationship between users and medications,
     * with a composite primary key consisting of the username and medicationID.
     *
     * @throws SQLException if an error occurs while executing the SQL statement.
     */
    private static void createUserMedicationRemindersTable() throws SQLException {
        Statement createUserMedicationRemindersTable;

        String createJunctionTableSQL = "CREATE TABLE IF NOT EXISTS userMedicationReminders (" +
                "reminderID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "username TEXT NOT NULL," +
                "medicationID TEXT NOT NULL," +
                "dosageAmount REAL NOT NULL, -- E.g., 500 (for mg)\n" +
                "dosageUnit TEXT NOT NULL, -- E.g., 'mg', 'mL', `capsules'\n" +
                "dosageTime TEXT NOT NULL," +
                "dosageDate TEXT NOT NULL, -- YYYY-MM-DD format\n" +
                "FOREIGN KEY (username) REFERENCES userAccounts(username)," +
                "FOREIGN KEY (medicationID) REFERENCES medications(medicationID)" +
                ")";

        createUserMedicationRemindersTable = connection.createStatement();

        createUserMedicationRemindersTable.execute(createJunctionTableSQL);
    }

    public static void addReminder(String medicationID, String dosageAmount, String dosageUnit, String dosageTime, String dosageDate) throws SQLException {
        String sql = "INSERT INTO userMedicationReminders(username, medicationID, dosageAmount, dosageUnit, dosageTime, dosageDate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement addReminderStmt = connection.prepareStatement(sql);
        addReminderStmt.setString(1, username);
        addReminderStmt.setString(2, medicationID);
        addReminderStmt.setString(3, dosageAmount);
        addReminderStmt.setString(4, dosageUnit);
        addReminderStmt.setString(5, dosageTime);
        addReminderStmt.setString(6, dosageDate);
        System.out.println("Adding reminder with the following fields:");
        System.out.println("ID: " + medicationID);
        System.out.println("Dosage Amount: " + dosageAmount);
        System.out.println("Dosage Unit: " + dosageUnit);
        System.out.println("Dosage Time: " + dosageTime);
        System.out.println("Dosage Date: " + dosageDate);
        System.out.println(addReminderStmt.executeUpdate());
        System.out.println("Reminder added.");
    }

    public static ArrayList<PrescribedMedicationDose> getCurrentDayMedications() throws SQLException {

        ResultSet currentDayMedications = queryCurrentDayMedications();

        ArrayList<PrescribedMedicationDose> doses = new ArrayList<>();

        while (currentDayMedications.next()) {
            doses.add(parseMedicationDoseFromResultSet(currentDayMedications));
        }

        return doses;
    }

    private static PrescribedMedicationDose parseMedicationDoseFromResultSet(ResultSet rs) throws SQLException {

        String username = rs.getString("username");
        String medicationID = rs.getString("medicationID");
        String displayName = getDisplayNameById(medicationID).getString("displayName");
        double dosageAmount = rs.getDouble("dosageAmount");
        String dosageUnit = rs.getString("dosageUnit");
        LocalDate dosageDate = LocalDate.parse(rs.getString("dosageDate"));
        LocalTime dosageTime = LocalTime.parse(rs.getString("dosageTime"));

        return new PrescribedMedicationDose(username, medicationID, displayName, dosageAmount, dosageUnit, dosageDate, dosageTime);
    }

    private static ResultSet queryCurrentDayMedications() throws SQLException {

        String sqlCommand = "SELECT * FROM userMedicationReminders " +
                "WHERE username = ? " +
                "AND dosageDate = ? " +
                "ORDER BY dosageTime ASC";

        PreparedStatement queryTodaysMedications = connection.prepareStatement(sqlCommand);
        queryTodaysMedications.setString(1, Session.getInstance().getLoggedInUser().getUsername());
        queryTodaysMedications.setString(2, LocalDate.now().toString());

        ResultSet results = queryTodaysMedications.executeQuery();

        return results;
    }

    private static ResultSet getDisplayNameById(String id) throws SQLException {

        String sqlCommand = "SELECT displayName FROM medications " +
                "WHERE medicationId = ?";

        PreparedStatement getDisplayNameById = connection.prepareStatement(sqlCommand);
        getDisplayNameById.setString(1, id);

        ResultSet results = getDisplayNameById.executeQuery();

        return results;
    }

    /**
     * Deletes the medications table from the database if it exists.
     *
     * @throws SQLException if an error occurs while executing the SQL statement.
     */
    private static void deleteMedicationsTable() throws SQLException {

        Statement deleteMedicationsTable;
        String deleteMedicationsTableSQL = "DROP TABLE IF EXISTS medications";
        deleteMedicationsTable = connection.createStatement();
        deleteMedicationsTable.execute(deleteMedicationsTableSQL);
    }

    /**
     * Deletes the user-medication junction table from the database if it exists.
     *
     * @throws SQLException if an error occurs while executing the SQL statement.
     */
    private static void deleteJunctionTable() throws SQLException {
        Statement deleteJunctionTable;
        String deleteJunctionTableSQL = "DROP TABLE IF EXISTS userMedicationReminders";
        deleteJunctionTable = connection.createStatement();
        deleteJunctionTable.execute(deleteJunctionTableSQL);
    }

    /**
     * Creates both the medications and user-medications junction tables in the database.
     *
     * @throws SQLException if an error occurs while creating either of the tables.
     */
    public static void createMedicationTables() throws SQLException {
        createMedicationsTable();
        createUserMedicationRemindersTable();
    }

    /**
     * Deletes both the medications and user-medications junction tables from the database.
     *
     * @throws SQLException if an error occurs while deleting either of the tables.
     */
    private static void deleteTables() throws SQLException {
        deleteMedicationsTable();
        deleteJunctionTable();
    }

    /**
     * Resets the database by deleting both the medications and user-medications junction tables
     * and then recreating them.
     *
     * @throws SQLException if an error occurs while resetting the tables.
     */
    private static void resetTables() throws SQLException {
        deleteTables();
        createMedicationTables();
    }


    /**
     * Saves a medicationID into the medications table if it does not already exist.
     * The method uses an "INSERT OR IGNORE" SQL statement to ensure no duplicate
     * medicationID entries are inserted.
     *
     * @param medicationID The ID of the medication to be saved.
     * @throws SQLException if an error occurs while executing the SQL insert statement.
     */
    protected static void saveMedication(String medicationID, String displayName) throws SQLException {
        PreparedStatement insertMedication;
        String insertMedicationSQL = "INSERT OR IGNORE INTO medications (medicationID, displayName) VALUES (?, ?)";

        insertMedication = connection.prepareStatement(insertMedicationSQL);
        insertMedication.setString(1, medicationID);
        insertMedication.executeUpdate();
    }
}
