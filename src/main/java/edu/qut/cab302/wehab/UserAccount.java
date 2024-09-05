package edu.qut.cab302.wehab;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UserAccount
{
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String hashedPassword;

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