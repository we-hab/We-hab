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
        String createUserAccountsTableSQL =
                "CREATE TABLE IF NOT EXISTS userAccounts (" +
                        "username VARCHAR(255) PRIMARY KEY, " +
                        "firstName VARCHAR(255) NOT NULL, " +
                        "lastName VARCHAR(255) NOT NULL, " +
                        "email VARCHAR(255) NOT NULL," +
                        "password VARCHAR(255) NOT NULL" +
                        ");";
        String createMoodRatingTable =
                "CREATE TABLE IF NOT EXISTS moodRatings (" +
                        "username VARCHAR(255)," +
                        "ratingDate DATE," +
                        "moodRating INT, " +
                        "PRIMARY KEY (username, ratingDate)," +
                        "FOREIGN KEY (username) REFERENCES userAccounts(username)" +
                        ");";

        try (Statement createTable = instance.createStatement())
        {
            createTable.execute(createUserAccountsTableSQL);
            createTable.execute(createMoodRatingTable);
        } catch (SQLException error) { System.err.println(error); }
    }

    public static Connection getInstance()
    {
        if (instance == null) { new DatabaseConnection(); } return instance;
    }
}

