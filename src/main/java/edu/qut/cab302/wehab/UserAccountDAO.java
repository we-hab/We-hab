package edu.qut.cab302.wehab;

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
            PreparedStatement insertUser = connection.prepareStatement( "INSERT INTO userAccounts (username, firstName, lastName, email, password) VALUES (?, ?, ?, ?, ?)");
            insertUser.setString(1, userAccount.getUsername());
            insertUser.setString(2, userAccount.getFirstName());
            insertUser.setString(3, userAccount.getLastName());
            insertUser.setString(4, userAccount.getEmail());
            insertUser.setString(5, userAccount.getHashedPassword());
            insertUser.execute();

        } catch (SQLException error) { System.err.println(error); }
    }
    //// TEST
    public void testRegAccount()
    {
        try
        {
            PreparedStatement insertUser = connection.prepareStatement( "INSERT INTO userAccounts (username, firstName, lastName, email, password) VALUES (?, ?, ?, ?, ?)");
            insertUser.setString(1, "RyanIsAmazing");
            insertUser.setString(2, "Ryan");
            insertUser.setString(3, "Whaaaaa");
            insertUser.setString(4, "Ryan@gmail.com");
            insertUser.setString(5, "test");
            insertUser.execute();
        } catch (SQLException error) { System.err.println(error); }

    }
    /////

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

    private void close()
    {
        try
        {
            connection.close();
        }
        catch (SQLException error) { System.err.println(error); }
    }
}
