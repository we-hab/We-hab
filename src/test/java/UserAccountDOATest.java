import edu.qut.cab302.wehab.UserAccount;
import edu.qut.cab302.wehab.UserAccountDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    // Test to see how long I can make my username


    // Test login with correct username and password

    // Test login with incorrect password

    // Test login with username that doesn't exist

    // Test registration with empty fields

    // Test login with empty fields



}
