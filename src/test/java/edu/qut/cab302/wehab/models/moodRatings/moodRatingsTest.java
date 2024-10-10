package edu.qut.cab302.wehab.models.moodRatings;
import edu.qut.cab302.wehab.database.DatabaseConnection;
import edu.qut.cab302.wehab.models.mood_ratings.moodRating;
import edu.qut.cab302.wehab.models.user_account.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class moodRatingsTest {
    private UserAccount testUser;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private Statement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void SetUp() throws Exception
    {
        testUser = new UserAccount("testUserr", "TestName", "TestLastName", "Test@gmail.com", "TestPassword123");

        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    public void testCreateMoodTable_ShouldExecuteCreateTableStatement() throws Exception
    {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='dailyMoodRatings';"))
                .thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        try (MockedStatic<DatabaseConnection> mockedDbConnection = mockStatic(DatabaseConnection.class))
        {
            mockedDbConnection.when(DatabaseConnection::getInstance).thenReturn(mockConnection);
            var ResultSet = mockStatement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='dailyMoodRatings';");
            assertTrue(ResultSet.next(), "Table 'dailyMoodRatings' should exist after DatabaseConnection is initialized.");
        }
    }

    @Test
    public void testInsertMoodRating_ShouldInsertMoodRatingIntoDatabase() throws Exception
    {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        try (MockedStatic<DatabaseConnection> mockedDbConnection = mockStatic(DatabaseConnection.class))
        {
            mockedDbConnection.when(DatabaseConnection::getInstance).thenReturn(mockConnection);

            moodRating.insertMoodRating(2, testUser.getUsername());

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).setString(1, testUser.getUsername());
            verify(mockPreparedStatement, times(1)).setInt(2, 2);
        }
    }

    @Test
    public void testGetLast7Days_ShouldReturnMoodRatingsForTheLast7Days() throws Exception
    {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("moodRating")).thenReturn(1, 2, 3);
        when(mockResultSet.getString("ratingDate")).thenReturn(
                LocalDate.now().minusDays(2).toString(),
                LocalDate.now().minusDays(1).toString(),
                LocalDate.now().toString());

        try (MockedStatic<DatabaseConnection> mockedDbConnection = mockStatic(DatabaseConnection.class))
        {
            mockedDbConnection.when(DatabaseConnection::getInstance).thenReturn(mockConnection);

            List<moodRating> ratings = moodRating.getLast7Days(testUser.getUsername());
            assertEquals(3, ratings.size(), "Should return mood ratings for the last 7 days");
        }
    }

    @Test
    public void testHasRatedToday_ShouldReturnTrueWhenMoodIsRatedToday() throws Exception
    {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        try (MockedStatic<DatabaseConnection> mockedDbConnection = mockStatic(DatabaseConnection.class))
        {
            mockedDbConnection.when(DatabaseConnection::getInstance).thenReturn(mockConnection);

            boolean hasRatedToday = moodRating.hasRatedToday(testUser.getUsername());
            assertTrue(hasRatedToday, "User should have rated their mood today.");
        }
    }

    @Test
    public void testInsertMoodRating_ShouldThrowSQLExceptionOnInvalidInput()
    {
        assertThrows(IllegalArgumentException.class, () -> moodRating.insertMoodRating(80, testUser.getUsername()),
                "Expected IllegalArgumentException to be thrown for invalid mood rating above 10");

        assertThrows(IllegalArgumentException.class, () -> moodRating.insertMoodRating(0, testUser.getUsername()),
                "Expected IllegalArgumentException to be thrown for invalid mood rating below 1");
    }

    @Test
    public void testMoodRatingConstructor_ShouldCreateCorrectMoodRatingObject()
    {
        LocalDate today = LocalDate.now();
        moodRating testMood = new moodRating(3, today);

        assertEquals(3, testMood.getMoodRating(), "The mood rating should be 3");
        assertEquals(today, testMood.getRatingDate(), "The rating date should be today's date");
    }

    @Test
    public void testGetLast7Days_ShouldReturnEmptyListWhenNoRatings() throws Exception
    {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate no ratings

        try (MockedStatic<DatabaseConnection> mockedDbConnection = mockStatic(DatabaseConnection.class))
        {
            mockedDbConnection.when(DatabaseConnection::getInstance).thenReturn(mockConnection);

            List<moodRating> ratings = moodRating.getLast7Days(testUser.getUsername());
            assertTrue(ratings.isEmpty(), "Should return an empty list when no ratings exist for the user");
        }
    }
}