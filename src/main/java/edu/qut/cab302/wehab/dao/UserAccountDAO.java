package edu.qut.cab302.wehab.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import edu.qut.cab302.wehab.database.DatabaseConnection;
import edu.qut.cab302.wehab.models.user_account.UserAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for interacting with UserAccount entities in the database.
 */
public class UserAccountDAO
{
    // Create a connection with the database, so we can edit it.
    private Connection connection;

    /**
     * Constructs a UserAccountDAO object and initializes the database connection.
     */
    public UserAccountDAO() { connection = DatabaseConnection.getInstance(); }

    /**
     * Registers a new user account in the database.
     *
     * @param userAccount The UserAccount object containing user information to be registered.
     */
    public void registerAccount(UserAccount userAccount) {
        try {
            // Convert username and email to lowercase before saving
            String lowerCaseUsername = userAccount.getUsername().toLowerCase();
            String lowerCaseEmail = userAccount.getEmail().toLowerCase();

            // Prepare SQL statement with lowercased values
            PreparedStatement tryRegister = connection.prepareStatement(
                    "INSERT INTO userAccounts (username, firstName, lastName, email, password) VALUES (?, ?, ?, ?, ?)"
            );
            tryRegister.setString(1, lowerCaseUsername);
            tryRegister.setString(2, userAccount.getFirstName());
            tryRegister.setString(3, userAccount.getLastName());
            tryRegister.setString(4, lowerCaseEmail);
            tryRegister.setString(5, userAccount.getHashedPassword());

            // Execute the statement
            tryRegister.execute();

        } catch (SQLException error) {
            System.err.println(error);
        }
    }


    /**
     * A class to log in an existing user, will compare against the database for the entered username and password, and will return true or false if all correct.
     * @param enteredUsername The username created by the user
     * @param enteredPassword The password created of the user
     * @return True if the username and password match the stored values, false otherwise.
     */
    public boolean LoginToAccount(String enteredUsername, String enteredPassword)
    {
        try
        {
            PreparedStatement tryLogin = connection.prepareStatement("SELECT username, password FROM userAccounts WHERE username = ?");
            tryLogin.setString(1, enteredUsername);
            ResultSet resultSet = tryLogin.executeQuery();

            if (resultSet.next())
            {
                String storedHashedPassword = resultSet.getString("password");

                BCrypt.Result result = BCrypt.verifyer().verify(enteredPassword.toCharArray(), storedHashedPassword);

                if (result.verified)
                {
                    // Both username and password were found, return true.
                    return true;
                }
                else
                {
                    // The password was not correct, return false.
                    return false;
                }
            }
            else
            {
                // The username was not found, return false.
                return false;
            }

        } catch (SQLException error) { System.err.println(error); return false; }
    }

    /**
     * Deletes a user account from the database.
     *
     * @param username The username of the account to be deleted.
     * @return True if the account was successfully deleted, false otherwise.
     */
    public boolean deleteAccount(String username) {
        String deleteQuery = "DELETE FROM UserAccounts WHERE username = ?";
        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setString(1, username);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;  // If rowsDeleted > 0, account was successfully deleted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves a list of all usernames from the database.
     *
     * @return A list of all usernames as strings.
     */
    public List<String> getAllusernames()
    {
        List<String> usernames = new ArrayList<>();
        try
        {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT username FROM userAccounts");

            while (result.next())
            {
                usernames.add(result.getString("username"));
            }
        } catch (SQLException error) { System.err.println(error); }
        return usernames;
    }

    /**
     * Retrieves a list of all emails from the database.
     *
     * @return A list of all emails as strings.
     */
    public List<String> getAllemails()
    {
        List<String> emails = new ArrayList<>();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT email FROM userAccounts");

            while (result.next())
            {
                emails.add((result.getString("email")));
            }
        } catch (SQLException error) { System.err.println(error); }
        return emails;
    }

    /**
     * Retrieves a user account by its username.
     *
     * @param enteredUsername The username of the account to retrieve.
     * @return A UserAccount object if found, or null if no account matches the username.
     */
    public UserAccount getByUsername(String enteredUsername)
    {
        UserAccount userAccount = null;

        try
        {
            PreparedStatement getUserMate = connection.prepareStatement("SELECT * FROM userAccounts WHERE username = ?");
            getUserMate.setString(1, enteredUsername);

            ResultSet result = getUserMate.executeQuery();

            if (result.next())
            {
                userAccount = new UserAccount(
                        result.getString("username"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("email"),
                        result.getString("password")
                );
            }

        } catch (SQLException error) { System.err.println(error); }

        return userAccount;
    }

    /**
     * Validates a user's password by comparing it to the stored hashed password in the database.
     *
     * @param username The username of the account to validate.
     * @param inputPassword The plain-text password entered by the user.
     * @return True if the password is valid, false otherwise.
     */
    public boolean validatePassword(String username, String inputPassword) {
        String storedHashedPassword = getStoredHashedPassword(username);

        if (storedHashedPassword != null) {
            // Verify input password against the stored hash
            BCrypt.Result result = BCrypt.verifyer().verify(inputPassword.toCharArray(), storedHashedPassword);
            return result.verified;
        }
        return false;
    }

    /**
     * Retrieves the stored hashed password for a given username from the database.
     *
     * @param username The username for which to retrieve the password.
     * @return The stored hashed password as a string, or null if no password is found.
     */
    private String getStoredHashedPassword(String username) {
        String query = "SELECT password FROM UserAccounts WHERE username = ?";
        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("password");  // Return the stored hashed password
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates a user's password in the database.
     *
     * @param username The username of the account to update.
     * @param newPassword The new plain-text password to be hashed
     * @return True if the password update was successful, false otherwise.
     */
    public boolean updatePassword(String username, String newPassword) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
        String updateQuery = "UPDATE userAccounts SET password = ? WHERE username = ?";

        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setString(1, hashedPassword);  // Set the new hashed password
            statement.setString(2, username);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;  // Return true if the password was updated successfully

        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
        }
        return false;
    }
}
