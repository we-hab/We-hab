package edu.qut.cab302.wehab.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest
{
    @BeforeEach
    public void setUp()
    {
        DatabaseConnection.getInstance(); // Initialize database connection
    }

    @AfterEach
    public void close() throws SQLException
    {
        Connection connection = DatabaseConnection.getInstance();
        if (connection != null && !connection.isClosed()) { connection.close(); }
    }

    @Test
    public void testGetInstance_ShouldReturnSingleConnection() throws SQLException
    {
        Connection connection1 = DatabaseConnection.getInstance();
        Connection connection2 = DatabaseConnection.getInstance();
        assertNotNull(connection1);
        assertNotNull(connection2);
        assertSame(connection1, connection2, "Database Connection should return the same instance.");
    }

    @Test
    public void testCreateTable_ShouldCreateUserAccountsTable() throws SQLException
    {
        Connection connection = DatabaseConnection.getInstance();
        Statement statement = connection.createStatement();
        var ResultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='userAccounts';");
        assertTrue(ResultSet.next(), "Table 'userAccounts' should exist after DatabaseConnection is initialized.");
    }

    @Test
    public void testConnectionClosed_ShouldReopenConnection() throws SQLException
    {
        Connection connection1 = DatabaseConnection.getInstance();
        connection1.close();

        Connection connection2 = DatabaseConnection.getInstance();
        assertNotNull(connection2);
        assertFalse(connection2.isClosed(), "DatabaseConnection should reopen connection if it was closed." );
    }

    @Test
    public void testPrivateConstructor_ShouldNotBeCalledDirectly()
    {
        try
        {
            DatabaseConnection.class.getDeclaredConstructor().newInstance();
            fail("Expected exception to be thrown");
        } catch (Exception error) { assertTrue(error instanceof IllegalAccessException, "Private constructor should be accessible."); }
    }
}
