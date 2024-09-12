package edu.qut.cab302.wehab;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Represents a user account in the application.
 * Stores account details such as username, email and password.
 */

public class UserAccount
{

    // MAKE USERNAME UNIQUE, IF ALREADY IN DB THEN DON'T CREATE.
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String hashedPassword;

    /**
     * Constructs a new UserAccount with the specified username and password.
     *
     * @param email the email address for the account.
     * @param username The UNIQUE username for the account.
     * @param password The password for the account.
     */

    public UserAccount(String username, String firstName, String lastName, String email, String password)
    {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hashedPassword = hashedPassword(password);
    }

    // Username - Primary Key
    public String getUsername() { return username; }
    public void getUsername(String username) { this.username = username; }

    // First and Last Name
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    // Email
    public String getEmail() { return email; }
    public void setEmail( String email ) { this.email = email; }


    //Password
    private String hashedPassword(String password) { return BCrypt.withDefaults().hashToString(12, password.toCharArray()); }

    public boolean checkPassword(String password)
    {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.hashedPassword);
        return result.verified;
    }

    public String getHashedPassword() { return hashedPassword; }
    public void getPassword(String password) { this.hashedPassword = hashedPassword(password); }

    @Override
    public String toString()
    {
        return "UserAccount{" +
                "username =" + username +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email=" + email + '\'' +
                ", password=" + hashedPassword + '\'' +
                '}';
    }
}
