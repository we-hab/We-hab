package edu.qut.cab302.wehab;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class moodRating
{
    private int moodRating;
    private LocalDate ratingDate;

    public moodRating(int moodRating, LocalDate ratingDate)
    {
        this.moodRating = moodRating;
        this.ratingDate = ratingDate;
    }

    public int getMoodRating() { return moodRating; }
    public LocalDate getRatingDate() { return ratingDate; }
    public void setMoodRating(int moodRating) { this.moodRating = moodRating; }
    public void setRatingDate(LocalDate ratingDate) { this.ratingDate = ratingDate; }

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

    // Method to insert a new mood rating
    public static void insertMoodRating(int moodRating, String username)
    {
        String insertSQL = "INSERT INTO dailyMoodRatings (username, moodRating, ratingDate) VALUES (?, ?, date('now'))";

        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(insertSQL))
        {
            statement.setString(1, username);
            statement.setInt(2, moodRating);
            statement.executeUpdate();
        } catch (SQLException error) { System.err.println(error); }
    }

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
