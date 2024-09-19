import edu.qut.cab302.wehab.UserAccount;
import edu.qut.cab302.wehab.UserAccountDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserAccountDOATest
{
    private UserAccountDAO userAccountDAO;

    @BeforeEach
    void setup()
    {
        userAccountDAO = new UserAccountDAO();
    }

    // Test of successful registration
    @Test
    void testRegisterAccountSuccessfully()
    {
        UserAccount newAccount = new UserAccount("testUser", "testFirstName", "testLastName", "testEmail", "testPassword");
        userAccountDAO.registerAccount(newAccount);

        // Check against all the usernames in the database
        List<String> users = userAccountDAO.getAllusernames();
        assertTrue(users.contains("testUser"));
    }

    // Test using a username that's already taken
    @Test
    void testRegisterAccountWithExistingUsername()
    {
        UserAccount firstUsername = new UserAccount("testUser", "testFirstName", "testLastName", "testEmail", "testPassword");
        userAccountDAO.registerAccount(firstUsername);

        UserAccount secondUsername = new UserAccount("testUser", "testFirstName2", "testLastName2", "testEmail2", "testPassword");
        userAccountDAO.registerAccount(secondUsername);

        // Check against all the usernames in the database
        List<String> users = userAccountDAO.getAllusernames();
        assertEquals(1, users.stream().filter(username -> username.equals("testUser")).count());
    }

    // Test login with correct username and password
    @Test
    void testLoginWithCorrectUsernameAndPassword()
    {
        UserAccount userAccount = new UserAccount("testUser", "testFirstName", "testLastName", "testEmail", "testPassword");
        userAccountDAO.registerAccount(userAccount);

        boolean loginSuccessful = userAccountDAO.LoginToAccount("testUser", "testPassword");
        assertTrue(loginSuccessful);
    }

    // Test login with incorrect password
    @Test
    void testLoginWithIncorrectPassword()
    {
        UserAccount userAccount = new UserAccount("testUser", "testFirstName", "testLastName", "testEmail", "testPassword");
        userAccountDAO.registerAccount(userAccount);

        boolean loginSuccessful = userAccountDAO.LoginToAccount("testUser", "bobJaneTmart");
        assertFalse(loginSuccessful);
    }

    // Test login with username that doesn't exist
    @Test
    void testLoginWithAUsernameThatDoesntExist()
    {
        boolean loginSuccessful = userAccountDAO.LoginToAccount("testUser", "testPassword");
        assertFalse(loginSuccessful);
    }

    // Test login with empty fields
    @Test
    void testLoginWithNothingEntered()
    {
        boolean loginSuccessful = userAccountDAO.LoginToAccount("", "");
        assertFalse(loginSuccessful);
    }

    // Test registration with empty fields
    @Test
    void testRegistrationWithEmptyFields()
    {
        UserAccount userAccount = new UserAccount("", "", "", "", "testPassword");
        userAccountDAO.registerAccount(userAccount);

        List<String> users = userAccountDAO.getAllusernames();
        assertFalse(users.contains(""));
    }

    // Test registering an account with the same email
    @Test
    void testLoginWithTheSameEmail()
    {
        UserAccount userAccount = new UserAccount("testUser", "testFirstName", "testLastName", "testEmail@gmail.com", "testPassword");
        userAccountDAO.registerAccount(userAccount);

        UserAccount userAccount2 = new UserAccount("testUser2", "testFirstName2", "testLastName2", "testEmail@gmail.com", "testPassword2");
        userAccountDAO.registerAccount(userAccount2);

        List<String> users = userAccountDAO.getAllusernames();
        assertTrue(users.contains("testUser"));
        assertTrue(users.contains("testUser2"));
    }

    // Test inputting more than 255 characters into first name
    @Test
    void testCharLimitFirstName()
    {
        String longInput = "a".repeat(266);
        UserAccount userAccount = new UserAccount("testUser", longInput, "testLastname", "testEmail@gmail.com", "testPassword");
        userAccountDAO.registerAccount(userAccount);

        List<String> users = userAccountDAO.getAllusernames();
        assertFalse(users.contains("testUser"));
    }

    // Test inputting more than 255 characters into last name
    @Test
    void testCharLimitLastName()
    {
        String longInput = "a".repeat(266);
        UserAccount userAccount = new UserAccount("testUser", "firstNameTest", longInput, "testEmail@gmail.com", "testPassword");
        userAccountDAO.registerAccount(userAccount);

        List<String> users = userAccountDAO.getAllusernames();
        assertFalse(users.contains("testUser"));
    }

    // Test inputting more than 255 characters into username
    @Test
    void testCharLimitUsername()
    {
        String longInput = "a".repeat(266);
        UserAccount userAccount = new UserAccount(longInput, "firstNameTest", "testLastname", "testEmail@gmail.com", "testPassword");
        userAccountDAO.registerAccount(userAccount);

        List<String> users = userAccountDAO.getAllusernames();
        assertFalse(users.contains("testUser"));
    }
}
