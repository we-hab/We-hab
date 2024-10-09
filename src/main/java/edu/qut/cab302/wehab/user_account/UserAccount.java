package edu.qut.cab302.wehab.user_account;

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
     * Constructs a new UserAccount with the specified details.
     *
     * @param username the UNIQUE username for the account
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email address for the account
     * @param password the password for the account
     */

    public UserAccount(String username, String firstName, String lastName, String email, String password)
    {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hashedPassword = hashedPassword(password);
    }

    /**
     * @return the username
     */
    public String getUsername() { return username; }

    /**
     * @return the first name
     */
    public String getFirstName() { return firstName; }

    /**
     * @return the last name
     */
    public String getLastName() { return lastName; }

    /**
     * @return the email
     */
    public String getEmail() { return email; }

    /**
     * @return the hashed password
     */
    public String getHashedPassword() { return hashedPassword; }

    /**
     * Hashes the given password using BCrypt.
     *
     * @param password the password to hash
     * @return the hashed password
     */
    private String hashedPassword(String password) { return BCrypt.withDefaults().hashToString(12, password.toCharArray()); }


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
