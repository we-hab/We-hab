package edu.qut.cab302.wehab.database;
import edu.qut.cab302.wehab.models.user_account.UserAccount;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SessionTest
{
    @BeforeEach
    public void clearSessionBeforeTest()
    {
        Session sesion = Session.getInstance();
        sesion.clearLoggedInUser();
    }

    @Test
    public void testGetInstance_ShouldReturnSingletonSession()
    {
        Session session1 = Session.getInstance();
        Session session2 = Session.getInstance();
        assertNotNull(session1);
        assertNotNull(session2);
        assertSame(session1, session2, "Session should return the same instance.");
    }

    @Test
    public void testSetLoggedInUser_ShouldStoreUserInSession()
    {
        UserAccount user = new UserAccount("testUserr", "test", "user", "test@gmail.com", "password123");
        Session session = Session.getInstance();
        session.setLoggedInUser(user);
        assertEquals(user, session.getLoggedInUser(), "The user should be stored in the session.");
    }

    @Test
    public void testGetLoggedInUser_ShouldReturnNullIfNOUserSet()
    {
        Session session = Session.getInstance();
        assertNull(session.getLoggedInUser(), "The session should return null if no user is logged in.");
    }

    @Test
    public void testClearLoggedInUser_ShouldLogoutUser()
    {
        UserAccount user = new UserAccount("testUserre", "test", "user", "test@gmail.com", "password123");
        Session session = Session.getInstance();
        session.setLoggedInUser(user);
        session.clearLoggedInUser();
        assertNull(session.getLoggedInUser(), "The session should have no logged-in user after clearing.");
    }

}