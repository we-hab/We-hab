package edu.qut.cab302.wehab.moodRatings;
import edu.qut.cab302.wehab.database.DatabaseConnection;
import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.mood_ratings.moodRating;
import edu.qut.cab302.wehab.user_account.UserAccount;
import edu.qut.cab302.wehab.workout.Workout;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class moodRatingsTest
{
    private UserAccount testUser;

    @BeforeEach
    public void SetUp() throws Exception
    {
        testUser = new UserAccount("testUserr", "TestName","TestLastName","Test@gmail.com","TestPassword123");
    }



    @Test
    public void testCreateMoodTable_ShouldExecuteCreateTableStatement() throws SQLException
    {
        Connection connection = DatabaseConnection.getInstance();
        Statement statement = connection.createStatement();
        var ResultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='dailyMoodRatings';");
        assertTrue(ResultSet.next(), "Table 'dailyMoodRatings' should exist after DatabaseConnection is initialized.");
    }

    @Test
    public void testInsertMoodRating_ShouldInsertMoodRatingIntoDatabase() throws SQLException
    {
        moodRating moodRating = new moodRating(2,LocalDate.now());
        assertEquals(2,moodRating.getMoodRating());
        assertEquals(LocalDate.now(), LocalDate.now());
    }

    @Test
    public void testGetLast7Days_ShouldReturnMoodRatingsForTheLast7Days() throws SQLException
    {
        Connection connection = DatabaseConnection.getInstance();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO dailyMoodRatings (username, moodRating, ratingDate) VALUES (?, ?, ?)");

        for (int i = 0; i < 7; i++)
        {
            statement.setString(1,"Garry");
            statement.setInt(2, i + 1);
            statement.setString(3, LocalDate.now().minusDays(i).toString());
            statement.executeUpdate();
        }
        List<moodRating> ratings = moodRating.getLast7Days("Garry");
        assertEquals(7, ratings.size(), "Should return mood ratings for the last 7 days");

    }

    @Test
    public void testHasRatedToday_SHouldReturnTrueWhenMoodIsRatedToday() throws SQLException
    {
        moodRating.insertMoodRating(5, testUser.getUsername());

        boolean hasRatedToday = moodRating.hasRatedToday(testUser.getUsername());
        assertTrue(hasRatedToday, "User should have rated their mood today.");
    }

    @Test
    public void testInsertMoodRating_ShouldThrowSQLExceptionOnInvalidInput()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            moodRating.insertMoodRating(80, testUser.getUsername());
        }, "Expected IllegalArgumentException to be thrown for invalid mood rating above 10");

        assertThrows(IllegalArgumentException.class, () -> {
            moodRating.insertMoodRating(0, testUser.getUsername());
        }, "Expected IllegalArgumentException to be thrown for invalid mood rating below 1");
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
    public void testGetLast7Days_ShouldReturnEmptyListWhenNoRatings()
    {
        List<moodRating> ratings = moodRating.getLast7Days("te");

        assertTrue(ratings.isEmpty(), "Should return an empty list when no ratings exist for the user");
    }
}
