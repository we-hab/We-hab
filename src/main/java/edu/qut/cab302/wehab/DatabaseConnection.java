package edu.qut.cab302.wehab;
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

    private DatabaseConnection()
    {
        String url = "jdbc:sqlite:db/database.db";
        try
        {
            instance = DriverManager.getConnection(url);
            createTable();
        } catch (SQLException sqlEx) { System.err.println(sqlEx); }
    }

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

