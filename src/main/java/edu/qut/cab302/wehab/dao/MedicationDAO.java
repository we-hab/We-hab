package edu.qut.cab302.wehab.dao;

import edu.qut.cab302.wehab.database.DatabaseConnection;

import java.security.GeneralSecurityException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static edu.qut.cab302.wehab.util.EncryptionUtility.encrypt;
import static edu.qut.cab302.wehab.util.EncryptionUtility.decrypt;

import edu.qut.cab302.wehab.models.medication.MedicationReminderFactory;

import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.models.medication.Medication;
import edu.qut.cab302.wehab.models.medication.MedicationReminder;

/**
 * This class provides methods for interacting with the medication-related tables in the database.
 * It includes functionality for performing CRUD operations on the medications and medication reminder tables.
 */
public class MedicationDAO {

    /**
     * The database connection instance to be used for executing SQL queries.
     */

    private static final String username = Session.getInstance().getLoggedInUser().getUsername();

    private static Connection getConnection() throws SQLException {
        Connection connection = DatabaseConnection.getInstance();
        if (connection.isClosed()) {
            connection = DatabaseConnection.getInstance(); // Reconnect if closed
        }
        return connection;
    }

    /**
     * Creates the medications table in the database if it does not already exist.
     * The medications table contains a primary key field for storing medication IDs.
     *
     * @throws SQLException if an error occurs while executing the SQL statement.
     */
    private static void createMedicationsTable() throws SQLException {
        Connection connection = getConnection();
        Statement createMedicationsTable;

        String createTableSQL =
                "CREATE TABLE IF NOT EXISTS medications (" +
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
        Connection connection = getConnection();
        String encryptedMedicationID;

        try {
            encryptedMedicationID = encrypt(medication.getID());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException();
        }

        String checkMedicationSql = "SELECT COUNT(*) FROM medications WHERE medicationID = ? AND username = ?";
        PreparedStatement checkMedication = connection.prepareStatement(checkMedicationSql);
        checkMedication.setString(1, encryptedMedicationID);
        checkMedication.setString(2, username);

        ResultSet resultSet = checkMedication.executeQuery();
        resultSet.next();

        int count = resultSet.getInt(1);

        if (count > 0) {
            throw new SQLException("You have already added this medication.");
        }

        String encryptedDisplayName;
        try {
            encryptedDisplayName = encrypt(medication.getDisplayName());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        String addMedicationSql = "INSERT INTO medications (medicationID, username, displayName, addedDateTime) VALUES (?, ?, ?, ?)";
        PreparedStatement addMedication = connection.prepareStatement(addMedicationSql);
        addMedication.setString(1, encryptedMedicationID);
        addMedication.setString(2, username);
        addMedication.setString(3, encryptedDisplayName);
        addMedication.setString(4, String.valueOf(LocalDateTime.now()));
        System.out.println(addMedication.executeUpdate());
    }

    public static HashMap<String, String> getUserSavedMedicationNames() throws SQLException {
        ResultSet resultSet = queryUserSavedMedications();
        HashMap<String, String> userSavedMedications = new HashMap<>();
        while (resultSet.next()) {
            try {
                userSavedMedications.put(decrypt(resultSet.getString("displayName")), decrypt(resultSet.getString("medicationID")));
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        }
        return userSavedMedications;
    }

    public static void deleteUserSavedMedication(String medicationID) throws SQLException {
        Connection connection = getConnection();
        String deleteMedicationSql = "DELETE FROM medications WHERE username = ? AND medicationID = ?";
        PreparedStatement deleteMedication = connection.prepareStatement(deleteMedicationSql);
        deleteMedication.setString(1, username);
        deleteMedication.setString(2, medicationID);
        deleteMedication.executeUpdate();
    }

    private static ResultSet queryUserSavedMedications() throws SQLException {
        Connection connection = getConnection();

        String sqlCommand =
                "SELECT * " +
                "FROM medications " +
                "WHERE username = ? " +
                "ORDER BY addedDateTime ASC";
        PreparedStatement sqlStatement = connection.prepareStatement(sqlCommand);

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
    public static void createMedicationRemindersTable() throws SQLException {
        Connection connection = getConnection();
        Statement createMedicationRemindersTable;

        String createJunctionTableSQL = "CREATE TABLE IF NOT EXISTS medicationReminders (" +
                "reminderID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "username TEXT NOT NULL," +
                "medicationID TEXT NOT NULL," +
                "dosageAmount REAL NOT NULL, -- E.g., 500 (for mg)\n" +
                "dosageUnit TEXT NOT NULL, -- E.g., 'mg', 'mL', `capsules'\n" +
                "dosageTime TEXT NOT NULL," +
                "dosageDate TEXT NOT NULL, -- YYYY-MM-DD format\n" +
                "status TEXT DEFAULT NULL," +
                "FOREIGN KEY (username) REFERENCES userAccounts(username)," +
                "FOREIGN KEY (medicationID) REFERENCES medications(medicationID)" +
                ")";

        createMedicationRemindersTable = connection.createStatement();

        createMedicationRemindersTable.execute(createJunctionTableSQL);
    }

    public static void markMedicationAsTaken(String reminderID) throws SQLException {
        Connection connection = getConnection();
        String sql =
            "UPDATE medicationReminders " +
            "SET status = 'Taken'" +
            "WHERE reminderID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, reminderID);
        preparedStatement.executeUpdate();
    }

    public static void markMedicationAsMissed(String reminderID) throws SQLException {
        Connection connection = getConnection();
        String sql =
                "UPDATE medicationReminders " +
                "SET status = 'Missed' " +
                "WHERE reminderID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, reminderID);
        preparedStatement.executeUpdate();
    }

    public static void deleteMedicationReminder(String reminderID) throws SQLException {
        Connection connection = getConnection();
        String sql = "DELETE FROM medicationReminders WHERE reminderID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, reminderID);
        preparedStatement.executeUpdate();
    }

    public static void addReminder(String medicationID, String dosageAmount, String dosageUnit, String dosageTime, String dosageDate) throws SQLException {
        Connection connection = getConnection();
        String sql =
                "INSERT INTO medicationReminders(username, medicationID, dosageAmount," +
                "dosageUnit, dosageDate, dosageTime) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        String encryptedMedicationID;

        try {
            encryptedMedicationID = encrypt(medicationID);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        PreparedStatement addReminderStmt = connection.prepareStatement(sql);
        addReminderStmt.setString(1, username);
        addReminderStmt.setString(2, encryptedMedicationID);
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

    public static ArrayList<MedicationReminder> getDailyReminders() throws SQLException {

        ResultSet currentDayMedicationsStream = queryCurrentDayReminders();

        ArrayList<MedicationReminder> reminders = new ArrayList<>();

        if (currentDayMedicationsStream != null) {
            while (currentDayMedicationsStream.next()) {
                reminders.add(MedicationReminderFactory.createFromResultSet(currentDayMedicationsStream));
            }
        }

        return reminders;
    }

    public static ArrayList<MedicationReminder> getReminderLog(LocalDate startDate, LocalDate endDate) throws SQLException {

        ArrayList<MedicationReminder> loggedReminders = new ArrayList<>();

        ResultSet reminderLogStream = queryLoggedReminders(startDate.toString(), endDate.toString());
        if (reminderLogStream != null) {
            while (reminderLogStream.next()) {
                loggedReminders.add(MedicationReminderFactory.createFromResultSet(reminderLogStream));
            }
        }
        return loggedReminders;
    }

    private static ResultSet queryLoggedReminders(String startDate, String endDate) throws SQLException {
        Connection connection = getConnection();

        String sqlQuery =
                "SELECT *" +
                "FROM medicationReminders " +
                "WHERE STATUS IS NOT NULL " +
                "AND dosageDate BETWEEN ? AND ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1, startDate.toString());
        preparedStatement.setString(2, endDate.toString());

        System.out.println("Querying medication reminders from " + startDate + " to " + endDate);
        ResultSet results = preparedStatement.executeQuery();

        if (!results.isBeforeFirst()) {
            System.out.println("No medication reminders found for this period.");
            return null;
        }

        return results;
    }

    public static ArrayList<MedicationReminder> getAllReminders() throws SQLException {

        ResultSet currentDayMedicationsStream = queryActiveUsersReminders();

        ArrayList<MedicationReminder> reminders = new ArrayList<>();

        if (currentDayMedicationsStream != null) {
            while (currentDayMedicationsStream.next()) {
                reminders.add(MedicationReminderFactory.createFromResultSet(currentDayMedicationsStream));
            }
        }

        return reminders;
    }

    private static ResultSet queryCurrentDayReminders() throws SQLException {
        Connection connection = getConnection();

        String sqlCommand =
                "SELECT *" +
                "FROM medicationReminders " +
                "WHERE username = ? " +
                "AND dosageDate = ? " +
                "AND status IS NULL " +
                "ORDER BY dosageTime ASC";

        PreparedStatement queryTodaysMedications = connection.prepareStatement(sqlCommand);
        queryTodaysMedications.setString(1, Session.getInstance().getLoggedInUser().getUsername());
        queryTodaysMedications.setString(2, LocalDate.now().toString());

        System.out.println("Querying medication reminders for " + LocalDate.now().toString());
        System.out.println("User: " + Session.getInstance().getLoggedInUser().getUsername());
        ResultSet results = queryTodaysMedications.executeQuery();

        if (!results.isBeforeFirst()) {
            System.out.println("No medication reminders found for today.");
            return null;
        }

        return results;
    }

    private static ResultSet queryActiveUsersReminders() throws SQLException {
        Connection connection = getConnection();

        String sqlCommand =
                "SELECT *" +
                "FROM medicationReminders " +
                "WHERE username = ? " +
                "AND status IS NULL " +
                "ORDER BY dosageTime ASC";

        PreparedStatement queryTodaysMedications = connection.prepareStatement(sqlCommand);
        queryTodaysMedications.setString(1, Session.getInstance().getLoggedInUser().getUsername());

        System.out.println("Querying medication reminders for " + LocalDate.now().toString());
        System.out.println("User: " + Session.getInstance().getLoggedInUser().getUsername());
        ResultSet results = queryTodaysMedications.executeQuery();

        if (!results.isBeforeFirst()) {
            System.out.println("No medication reminders found for today.");
            return null;
        }

        return results;
    }

    public static String getDisplayNameById(String id) throws SQLException {
        Connection connection = getConnection();

        String sqlCommand =
                "SELECT displayName " +
                "FROM medications " +
                "WHERE medicationId = ?";

        String encryptedMedicationID;

        try {
            encryptedMedicationID = encrypt(id);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        PreparedStatement getDisplayNameById = connection.prepareStatement(sqlCommand);
        getDisplayNameById.setString(1, encryptedMedicationID);

        ResultSet results = getDisplayNameById.executeQuery();
        String displayName = results.getString("displayName");

        try {
            return decrypt(displayName);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes the medications table from the database if it exists.
     *
     * @throws SQLException if an error occurs while executing the SQL statement.
     */
    private static void deleteMedicationsTable() throws SQLException {
        Connection connection = getConnection();

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
        Connection connection = getConnection();
        Statement deleteJunctionTable;
        String deleteJunctionTableSQL = "DROP TABLE IF EXISTS medicationReminders";
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
        createMedicationRemindersTable();
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

    public static void updateReminder(MedicationReminder reminder) throws SQLException {
        Connection connection = getConnection();

        String sql =
                "UPDATE medicationReminders " +
                "SET dosageAmount = ?, dosageUnit = ?, dosageTime = ?, dosageDate = ?  " +
                "WHERE reminderID = ? ";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        String reminderID = reminder.getReminderID();
        String dosageAmount = String.valueOf(reminder.getDosageAmount());
        String dosageUnit = reminder.getDosageUnit();
        String dosageTime = reminder.getDosageTime().toString();
        String dosageDate = reminder.getDosageDate().toString();

        preparedStatement.setString(1, dosageAmount);
        preparedStatement.setString(2, dosageUnit);
        preparedStatement.setString(3, dosageTime);
        preparedStatement.setString(4, dosageDate);
        preparedStatement.setString(5, reminderID);

        preparedStatement.executeUpdate();
    }
}
