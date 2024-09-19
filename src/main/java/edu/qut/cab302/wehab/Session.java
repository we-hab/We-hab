package edu.qut.cab302.wehab;

public class Session
{
    private static Session instance;
    private UserAccount loggedInUser;

    private Session() { }

    public static Session getInstance()
    {
        if (instance == null) { instance = new Session(); } return instance;
    }

    public UserAccount getLoggedInUser() { return loggedInUser; }

    public void setLoggedInUser(UserAccount loggedInUser) { this.loggedInUser = loggedInUser; }

    public void clearLoggedInUser() {
        loggedInUser = null;
    }
}
