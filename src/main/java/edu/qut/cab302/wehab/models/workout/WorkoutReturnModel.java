package edu.qut.cab302.wehab.models.workout;
import edu.qut.cab302.wehab.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.time.*;


public class WorkoutReturnModel {

    /**
     * The database connection instance to be used for executing SQL queries.
     */
    private static Connection getConnection() throws SQLException {
        Connection connection = DatabaseConnection.getInstance();
        if (connection.isClosed()) {
            connection = DatabaseConnection.getInstance(); // Reconnect if closed
        }
        return connection;
    }


    /**
     * Create the Workout table in database using the username as the primary key.
     * This table stores workout records detailing workout type, date, duration, and effort.
     */
    public static void createWorkoutTable() {

        // PK = ID, FK = username
        String createWorkoutTableSQL = "CREATE TABLE IF NOT EXISTS workouts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username VARCHAR(255) NOT NULL, " +
                "workoutType VARCHAR(255) NOT NULL, " +
                "date DATE NOT NULL, " +
                "duration INTEGER NOT NULL, " +
                "effort INTEGER NOT NULL, " +
                "FOREIGN KEY (username) REFERENCES userAccounts(username) ON DELETE CASCADE" +
                ")";
        try (Statement createWorkoutTable = getConnection().createStatement()) {
            createWorkoutTable.execute(createWorkoutTableSQL);
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        }
    }

    /** Method to add workout to database.
     * Takes workout object and username.
     * */
    public static void addWorkout(Workout workout, String username) {

        String insertSQL = "INSERT INTO workouts (username, workoutType, date, duration, effort) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement insertWorkoutTable = getConnection().prepareStatement(insertSQL)) {
            insertWorkoutTable.setString(1, username);
            insertWorkoutTable.setString(2, workout.getWorkoutType());
            insertWorkoutTable.setString(3, workout.getDate().toString());
            insertWorkoutTable.setInt(4, workout.getDuration());
            insertWorkoutTable.setInt(5, workout.getEffort());
            insertWorkoutTable.executeUpdate();
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        }
    }

    /** Method to retrieve all workout data for a user from the database to display in the UI.
     * */
    public static List<Workout> getWorkouts(String username) {
        List<Workout> workouts = new ArrayList<>();

        String querySQL = "SELECT workoutType, date, duration, effort FROM workouts WHERE username = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(querySQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String workoutType = resultSet.getString("workoutType");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                int duration = resultSet.getInt("duration");
                int effort = resultSet.getInt("effort");
                Workout workout = new Workout(workoutType, date, duration, effort);
                workouts.add(workout);
            }
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        }

        return workouts;
    }

    /** Method to retrieve aggregated workout minutes from the database for a given user to display in the heatmap.
     * */
    public static TreeMap<LocalDate, Integer> getMonthlyMinutes(String username) {
        // Initialise auto sorting storage.
        TreeMap<LocalDate, Integer> monthlyMinutes = new TreeMap<>();

        // Talk to the DB and handle errors.
        String querySQL = "SELECT date, duration FROM workouts WHERE username = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(querySQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            // Extract and add the total minutes for the current month.
            while (resultSet.next()) {
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                int duration = resultSet.getInt("duration");
                if (date.getMonth() == LocalDate.now().getMonth()) {
                    monthlyMinutes.put(date, monthlyMinutes.getOrDefault(date, 0) + duration);
                }
            }
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        }

        return monthlyMinutes;
    }
}
