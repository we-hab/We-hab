package edu.qut.cab302.wehab;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAccountDAO
{
    // Boring connection stuffs
    private Connection connection;
    public UserAccountDAO() { connection = DatabaseConnection.getInstance(); }

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

    public boolean LoginToAccount(String enteredUsername, String enteredPassword)
    {
        try
        {
            // What I need to do is check the username and password that is entered against the database and if both match, then login and if not, then return false and ask to try again.
            PreparedStatement tryLogin = connection.prepareStatement("SELECT username, password FROM userAccounts WHERE username = ?");
            tryLogin.setString(1, enteredUsername);
            ResultSet resultSet = tryLogin.executeQuery();

            if (resultSet.next())
            {
                String storedHashedPassword = resultSet.getString("password");

                BCrypt.Result result = BCrypt.verifyer().verify(enteredPassword.toCharArray(), storedHashedPassword);

                if (result.verified)
                {
                    System.out.println("Login Successful lol");
                    return true;
                }
                else
                {
                    System.out.println("Incorrect password. Try again");
                    return false;
                }
            }
            else
            {
                System.out.println("Username not found");
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

    private void close()
    {
        try
        {
            connection.close();
        }
        catch (SQLException error) { System.err.println(error); }
    }
}
