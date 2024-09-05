package edu.qut.cab302.wehab;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAccountDAO
{
    // Boring connection stuffs
    private Connection connection;
    public UserAccountDAO() { connection = DatabaseConnection.getInstance(); }

    // Tables

    public void createTable()
    {
        try
        {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS userAccounts (" +
                            "id INTEGER PRIMARY KEY, " +
                            "firstName VARCHAR NOT NULL, " +
                            "lastName VARCHAR NOT NULL, " +
                            "email VARCHAR NOT NULL" +
                            "password VARCHAR NOT NULL" +
                            ")"
            );
        } catch (SQLException error) { System.err.println(error); }
    }

    public void registerAccount(UserAccount userAccount)
    {
        try
        {
            PreparedStatement insertUser = connection.prepareStatement( "INSERT INTO userAccounts (username, firstName, lastName, email, password) VALUES (?, ?, ?, ?, ?)");
            insertUser.setString(1, userAccount.getUsername());
            insertUser.setString(2, userAccount.getFirstName());
            insertUser.setString(3, userAccount.getLastName());
            insertUser.setString(4, userAccount.getEmail());
            insertUser.setString(5, userAccount.getPassword());
            insertUser.execute();

        } catch (SQLException error) { System.err.println(error); }
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
            updateAccount.setString(5, userAccount.getPassword());
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

    public static class Main {
        public static void main(String[] args) {
            UserAccountDAO userAccountDAO = new UserAccountDAO();
            userAccountDAO.createTable();

            // TEMP Records
            //userAccountDAO.insert(new UserAccount("Ryan", "Whiteman", 25));
            //userAccountDAO.insert(new UserAccount("Connor", "Beddow", 95));

            /* List all users
            List<UserAccount> users = userAccountDAO.getAll();
            for (UserAccount user : users) { System.out.println(user);
            */

            /* Retrieve a user by ID number
            UserAccount user = userAccountDAO.getById(2);
            System.out.println(user);
             */

            /* Update an existing user's account
            UserAccount user = userAccountDAO.getById(2);
            System.out.println("Before update:");
            System.out.println(user);

            user.setAge((59));
            userAccountDAO.update(user);
            System.out.println("After the updated age:");
            System.out.println(userAccountDAO.getById(2));
             */

            /* Delete an account
            System.out.println("Before deleting a record with id = 1:");
            for (UserAccount user : userAccountDAO.getAll()) {System.out.println(user);}

            userAccountDAO.delete(1); // Replace the 1 with the number
            System.out.println("After deleting the record with id - 1:");
            for (UserAccount user : userAccountDAO.getAll()) { System.out.println(user);}
            */

            userAccountDAO.close();
        }
    }

}
