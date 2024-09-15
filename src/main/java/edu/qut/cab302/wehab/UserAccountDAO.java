package edu.qut.cab302.wehab;

import at.favre.lib.crypto.bcrypt.BCrypt;

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
    public UserAccountDAO() { connection = DatabaseConnection.getInstance(); }

    // A class to register a new user into the database.
    public void registerAccount(UserAccount userAccount)
    {
        try
        {
            PreparedStatement tryRegister = connection.prepareStatement( "INSERT INTO userAccounts (username, firstName, lastName, email, password) VALUES (?, ?, ?, ?, ?)");
            tryRegister.setString(1, userAccount.getUsername());
            tryRegister.setString(2, userAccount.getFirstName());
            tryRegister.setString(3, userAccount.getLastName());
            tryRegister.setString(4, userAccount.getEmail());
            tryRegister.setString(5, userAccount.getHashedPassword());
            tryRegister.execute();

        } catch (SQLException error) { System.err.println(error); }
    }


    /**
     * A class to login an existing user, will compare against the database for the entered username and password, and will return true or false if all correct.
     * @param enteredUsername The username created by the user
     * @param enteredPassword The password created of the user
     * @return
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



    public void update(UserAccount userAccount)
    {
        try
        {
            PreparedStatement updateAccount = connection.prepareStatement("UPDATE userAccounts SET username = ?, firstName = ?, lastName = ?, email = ?, password = ? WHERE username = ?");
            updateAccount.setString(1, userAccount.getUsername());
            updateAccount.setString(2, userAccount.getFirstName());
            updateAccount.setString(3, userAccount.getLastName());
            updateAccount.setString(4, userAccount.getEmail());
            updateAccount.setString(5, userAccount.getHashedPassword());
            updateAccount.setString(6, userAccount.getUsername());
            updateAccount.execute();

        } catch (SQLException error) { System.err.println(error); }
    }

    public void delete(String username)
    {
        try
        {
            PreparedStatement deleteUser = connection.prepareStatement("DELETE FROM userAccounts WHERE username = ?");
            deleteUser.setString(1, username);
            deleteUser.execute();

        } catch (SQLException error) { System.err.println(error); }
    }

    public List<UserAccount> getAll()
    {
        List<UserAccount> users = new ArrayList<>();
        try
        {
            Statement getAll = connection.createStatement();
            ResultSet rs = getAll.executeQuery("SELECT * FROM userAccounts");
            while (rs.next())
            {
                users.add(
                        new UserAccount(
                            rs.getString("username"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("email"),
                            rs.getString("password")
                        )
                );
            }
        } catch (SQLException error) { System.err.println(error); }
        return users;
    }

    public UserAccount getById(int id)
    {
        try
        {
            PreparedStatement getUser = connection.prepareStatement("SELECT * FROM userAccounts WHERE username = ?");
            getUser.setInt(1, id);
            ResultSet rs = getUser.executeQuery();
            if (rs.next())
            {
                return new UserAccount(
                        rs.getString("username"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (SQLException error) { System.err.println(error); }
        return null;
    }

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

    private void close()
    {
        try
        {
            connection.close();
        }
        catch (SQLException error) { System.err.println(error); }
    }

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
}
