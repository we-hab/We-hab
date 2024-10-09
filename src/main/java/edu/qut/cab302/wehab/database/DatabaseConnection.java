package edu.qut.cab302.wehab.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages the connection to the application's database.
 * Includes methods to open, close, and handle queries.
 */

public class DatabaseConnection
{
    private static Connection instance = null;

    /**
     * Private constructor to establish the database connection and create necessary tables.
     * Uses SQLite to connect to a local database file.
     */
    private DatabaseConnection()
    {
        String url = "jdbc:sqlite:db/database.db";
        try
        {
            instance = DriverManager.getConnection(url);
            createTable();
        } catch (SQLException sqlEx) { System.err.println(sqlEx); }
    }

    /**
     * Creates the "userAccounts" table in the database if it doesn't already exist.
     * The table includes columns for username, first name, last name, email, and password.
     */
    public static void createTable()
    {
        String createTableSQL =
                "CREATE TABLE IF NOT EXISTS userAccounts (" +
                        "username VARCHAR(255) PRIMARY KEY, " +
                        "firstName VARCHAR(255) NOT NULL, " +
                        "lastName VARCHAR(255) NOT NULL, " +
                        "email VARCHAR(255) NOT NULL," +
                        "password VARCHAR(255) NOT NULL" +
                        ")";

        try (Statement createTable = instance.createStatement()) { createTable.execute(createTableSQL); } catch (SQLException error) { System.err.println(error); }
    }

    /**
     * Retrieves the singleton instance of the database connection.
     * If the connection is closed or hasn't been initialized, it creates a new connection.
     *
     * @return the active connection to the database
     */
    public static Connection getInstance()
    {
        try {
            if (instance == null || instance.isClosed()) {
                new DatabaseConnection();  // Recreate the instance if it's closed or null
            }
        } catch (SQLException sqlEx) {
            System.err.println("Error checking database connection: " + sqlEx.getMessage());
        }
        return instance;
    }
}

