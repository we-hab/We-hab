package edu.qut.cab302.wehab;

public class UserAccount
{
    private int id;
    private String firstName;
    private String lastName;
    private int age;

    public UserAccount(int id, String firstName, String lastName, int age)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public UserAccount(String firstName, String lastName, int age)
    {
        // Since the id is auto-incremented, it's nice to have a constructor without it.
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public int getAge() { return age; }
    public void setAge( int age ) { this.age = age; }

    @Override
    public String toString()
    {
        return "UserAccount{" +
                "id =" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }

}