package edu.qut.cab302.wehab.database;

import edu.qut.cab302.wehab.user_account.UserAccount;

/**
 * Singleton class to manage the current user session in the application.
 * It keeps track of the currently logged-in user and provides methods
 * to retrieve or clear the session.
 */
public class Session
{
    private static Session instance;
    private UserAccount loggedInUser;


    private Session()
    {

    }

    /**
     * Returns the single instance of the Session class (Singleton pattern).
     * If no instance exists, a new one will be created.
     *
     * @return The singleton instance of the Session class.
     */
    public static Session getInstance()
    {
        if (instance == null) { instance = new Session(); } return instance;
    }

    /**
     * Gets the currently logged-in user.
     *
     * @return The currently logged-in UserAccount object, or null if no user is logged in.
     */
    public UserAccount getLoggedInUser() { return loggedInUser; }

    /**
     * Sets the currently logged-in user for the session.
     *
     * @param loggedInUser The UserAccount object representing the logged-in user.
     */
    public void setLoggedInUser(UserAccount loggedInUser) { this.loggedInUser = loggedInUser; }

    /**
     * Clears the logged-in user, effectively logging the user out of the session.
     */
    public void clearLoggedInUser() {
        loggedInUser = null;
    }
}
