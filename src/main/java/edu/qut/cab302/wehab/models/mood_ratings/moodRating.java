package edu.qut.cab302.wehab.models.mood_ratings;


import edu.qut.cab302.wehab.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Represents mood ratings for users, allowing the creation, retrieval, and management of mood data.
 */
public class moodRating
{
    private int moodRating;
    private LocalDate ratingDate;

    /**
     * Constructor for creating a moodRating object.
     *
     * @param moodRating the rating of the mood (e.g., from 1 to 10)
     * @param ratingDate the date the mood rating was recorded
     */
    public moodRating(int moodRating, LocalDate ratingDate)
    {
        this.moodRating = moodRating;
        this.ratingDate = ratingDate;
    }

    /**
     * @return the mood rating
     */
    public int getMoodRating() { return moodRating; }

    /**
     * @return the date of the mood rating
     */
    public LocalDate getRatingDate() { return ratingDate; }

    /**
     * Creates the dailyMoodRatings table in the database if it does not exist.
     */
    public static void createMoodTable()
    {
        String createMoodTable =
                "CREATE TABLE IF NOT EXISTS dailyMoodRatings (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username VARCHAR(255), " +
                        "moodRating INTEGER NOT NULL, " +
                        "ratingDate DATE NOT NULL, " +
                        "FOREIGN KEY (username) REFERENCES userAccounts(username) ON DELETE CASCADE" +
                ")";

        try (Statement createTable = DatabaseConnection.getInstance().createStatement())
        {
            createTable.execute(createMoodTable);
            System.out.println("table 'dailyMoodRatings' created successfully");
        }
        catch (SQLException error)
        {
            System.err.println("error creating daily table " + error.getMessage());
        }
    }

    /**
     * Inserts a new mood rating into the database for a specific user.
     *
     * @param moodRating the mood rating to insert (must be between 1 and 10)
     * @param username the username of the user providing the rating
     */
    public static void insertMoodRating(int moodRating, String username)
    {
        if (moodRating < 1 || moodRating > 10)
        {
            throw new IllegalArgumentException("Mood Rating must be between 1 and 10");
        }

        String insertSQL = "INSERT INTO dailyMoodRatings (username, moodRating, ratingDate) VALUES (?, ?, date('now'))";

        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(insertSQL))
        {
            statement.setString(1, username);
            statement.setInt(2, moodRating);
            statement.executeUpdate();
        } catch (SQLException error) { System.err.println("Error inserting mood rating: " + error.getMessage()); }
    }

    /**
     * Retrieves the mood ratings for the last 7 days for a specific user.
     *
     * @param username the username of the user whose ratings are to be retrieved
     * @return a list of moodRating objects for the last 7 days
     */
    public static List<moodRating> getLast7Days(String username)
    {
        String query = "SELECT moodRating, ratingDate FROM dailyMoodRatings WHERE username = ? AND ratingDate >= date('now', '-7 days')";
        List<moodRating> ratings = new ArrayList<>();

        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(query))
        {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            while (result.next())
            {
                int moodRating = result.getInt("moodRating");
                LocalDate ratingDate = LocalDate.parse(result.getString("ratingDate"));
                ratings.add(new moodRating(moodRating, ratingDate));
            }
        } catch (SQLException error) { System.err.println(error); }
        return ratings;
    }

    /**
     * Checks if the user has rated their mood today.
     *
     * @param username the username of the user to check
     * @return true if the user has rated today, false otherwise
     */
    public static boolean hasRatedToday(String username)
    {
        String query = "SELECT COUNT(*) FROM dailyMoodRatings WHERE username = ? and ratingDate = date('now')";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(query))
        {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            if (result.next())
            {
                return result.getInt(1) > 0;
            }
        } catch (SQLException error) { System.err.println(error); }
        return false;
    }

}
