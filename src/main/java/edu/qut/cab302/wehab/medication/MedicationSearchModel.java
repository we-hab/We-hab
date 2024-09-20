package edu.qut.cab302.wehab.medication;

import edu.qut.cab302.wehab.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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

    /**
     * Creates the medications table in the database if it does not already exist.
     * The medications table contains a primary key field for storing medication IDs.
     *
     * @throws SQLException if an error occurs while executing the SQL statement.
     */
    private static void createMedicationsTable() throws SQLException {

        Statement createMedicationsTable;

        String createTableSQL = "CREATE TABLE IF NOT EXISTS medications (" +
                "medicationID VARCHAR(255) PRIMARY KEY" +
                ")";

        createMedicationsTable = connection.createStatement();

        createMedicationsTable.execute(createTableSQL);
    }

    /**
     * Creates the user-medication junction table in the database if it does not already exist.
     * This table establishes a many-to-many relationship between users and medications,
     * with a composite primary key consisting of the username and medicationID.
     *
     * @throws SQLException if an error occurs while executing the SQL statement.
     */
    private static void createJunctionTable() throws SQLException {
        Statement createJunctionTable;

        String createJunctionTableSQL = "CREATE TABLE IF NOT EXISTS userMedications (" +
                "username VARCHAR(255) NOT NULL," +
                "medicationID VARCHAR(255) NOT NULL," +
                "PRIMARY KEY (username, medicationID)" +
                ")";

        createJunctionTable = connection.createStatement();

        createJunctionTable.execute(createJunctionTableSQL);
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
        String deleteJunctionTableSQL = "DROP TABLE IF EXISTS userMedications";
        deleteJunctionTable = connection.createStatement();
        deleteJunctionTable.execute(deleteJunctionTableSQL);
    }

    /**
     * Creates both the medications and user-medications junction tables in the database.
     *
     * @throws SQLException if an error occurs while creating either of the tables.
     */
    protected static void createMedicationTables() throws SQLException {
        createMedicationsTable();
        createJunctionTable();
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
    protected static void saveMedication(String medicationID) throws SQLException {
        PreparedStatement insertMedication;
        String insertMedicationSQL = "INSERT OR IGNORE INTO medications (medicationID) VALUES (?)";

        insertMedication = connection.prepareStatement(insertMedicationSQL);
        insertMedication.setString(1, medicationID);
        insertMedication.executeUpdate();
    }
}
