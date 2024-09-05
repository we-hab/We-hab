package edu.qut.cab302.wehab;

public class UserAccount
{
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserAccount(String username, String firstName, String lastName, String email, String password)
    {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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
    public String getPassword() { return password; }
    public void getPassword(String password) { this.password = password; }

    @Override
    public String toString()
    {
        return "UserAccount{" +
                "username =" + username +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email=" + email +
                ", password=" + password +
                '}';
    }

}