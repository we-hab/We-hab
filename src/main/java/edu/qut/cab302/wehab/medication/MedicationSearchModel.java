package edu.qut.cab302.wehab.medication;

import edu.qut.cab302.wehab.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MedicationSearchModel {

    protected static Connection connection = DatabaseConnection.getInstance();

    protected static void createMedicationsTable() throws SQLException {

        Statement createMedicationsTable;

        String createTableSQL = "CREATE TABLE IF NOT EXISTS medications (" +
                "medicationID VARCHAR(255) PRIMARY KEY" +
                ")";

        createMedicationsTable = connection.createStatement();

        createMedicationsTable.execute(createTableSQL);

    }

    protected static void createJunctionTable() throws SQLException {
        Statement createJunctionTable;

        String createJunctionTableSQL = "CREATE TABLE IF NOT EXISTS userMedications (" +
                "username VARCHAR(255) NOT NULL," +
                "medicationID VARCHAR(255) NOT NULL," +
                "PRIMARY KEY (username, medicationID)" +
                ")";

        createJunctionTable = connection.createStatement();

        createJunctionTable.execute(createJunctionTableSQL);
    }

    private void deleteMedicationsTable() throws SQLException {

        Statement deleteMedicationsTable;
        String deleteMedicationsTableSQL = "DROP TABLE IF EXISTS medications";
        deleteMedicationsTable = connection.createStatement();
        deleteMedicationsTable.execute(deleteMedicationsTableSQL);
    }

    private void deleteJunctionTable() throws SQLException {
        Statement deleteJunctionTable;
        String deleteJunctionTableSQL = "DROP TABLE IF EXISTS userMedications";
        deleteJunctionTable = connection.createStatement();
        deleteJunctionTable.execute(deleteJunctionTableSQL);
    }

    private void createTables() throws SQLException {
        createMedicationsTable();
        createJunctionTable();
    }

    private void deleteTables() throws SQLException {
        deleteMedicationsTable();
        deleteJunctionTable();
    }

    protected void resetTables() throws SQLException {
        deleteTables();
        createTables();
    }

    protected static void saveMedication(String medicationID) throws SQLException {
        PreparedStatement insertMedication;
        String insertMedicationSQL = "INSERT OR IGNORE INTO medications (medicationID) VALUES (?)";

        insertMedication = connection.prepareStatement(insertMedicationSQL);
        insertMedication.setString(1, medicationID);
    }
}
