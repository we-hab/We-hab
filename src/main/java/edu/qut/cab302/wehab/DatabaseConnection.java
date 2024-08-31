package edu.qut.cab302.wehab;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection
{
    private static Connection instance = null;

    private DatabaseConnection()
    {
        String url = "jdbc:sqlite:db/database.db"; // <-- CHANGED THIS <3
        try
        {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) { System.err.println(sqlEx); }
    }

    public static Connection getInstance()
    {
        if (instance == null) { new DatabaseConnection(); } return instance;
    }


}