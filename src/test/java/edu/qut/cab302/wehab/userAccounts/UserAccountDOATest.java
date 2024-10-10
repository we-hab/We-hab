package edu.qut.cab302.wehab.userAccounts;

import edu.qut.cab302.wehab.user_account.UserAccount;
import edu.qut.cab302.wehab.user_account.UserAccountDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserAccountDOATest {

    private UserAccountDAO userAccountDAO;

    @BeforeEach
    void setup()
    {
        userAccountDAO = mock(UserAccountDAO.class);
    }

    @Test
    void testRegisterAccountSuccessfully()
    {
        UserAccount newAccount = new UserAccount("testUser", "testFirstName", "testLastName", "testEmail", "testPassword");
        doNothing().when(userAccountDAO).registerAccount(newAccount);

        List<String> usernames = new ArrayList<>();
        usernames.add("testUser");
        when(userAccountDAO.getAllusernames()).thenReturn(usernames);
        userAccountDAO.registerAccount(newAccount);

        List<String> users = userAccountDAO.getAllusernames();
        assertTrue(users.contains("testUser"));
    }

    @Test
    void testRegisterAccountWithExistingUsername()
    {
        UserAccount firstUsername = new UserAccount("testUser", "testFirstName", "testLastName", "testEmail", "testPassword");

        doNothing().when(userAccountDAO).registerAccount(firstUsername);

        List<String> usernames = new ArrayList<>();
        usernames.add("testUser");
        when(userAccountDAO.getAllusernames()).thenReturn(usernames);
        userAccountDAO.registerAccount(firstUsername);

        List<String> users = userAccountDAO.getAllusernames();
        assertEquals(1, users.stream().filter(username -> username.equals("testUser")).count());
    }

    @Test
    void testLoginWithCorrectUsernameAndPassword()
    {
        when(userAccountDAO.LoginToAccount("testUser", "testPassword")).thenReturn(true);

        boolean loginSuccessful = userAccountDAO.LoginToAccount("testUser", "testPassword");
        assertTrue(loginSuccessful);
    }

    @Test
    void testLoginWithIncorrectPassword()
    {
        when(userAccountDAO.LoginToAccount("testUser", "bobJaneTmart")).thenReturn(false);

        boolean loginSuccessful = userAccountDAO.LoginToAccount("testUser", "bobJaneTmart");
        assertFalse(loginSuccessful);
    }

    @Test
    void testLoginWithAUsernameThatDoesntExist()
    {
        when(userAccountDAO.LoginToAccount("testUserDoesntExist", "testPassword")).thenReturn(false);

        boolean loginSuccessful = userAccountDAO.LoginToAccount("testUserDoesntExist", "testPassword");
        assertFalse(loginSuccessful);
    }

    @Test
    void testLoginWithNothingEntered()
    {
        when(userAccountDAO.LoginToAccount("", "")).thenReturn(false);

        boolean loginSuccessful = userAccountDAO.LoginToAccount("", "");
        assertFalse(loginSuccessful);
    }

    @Test
    void testRegistrationWithEmptyFields()
    {
        UserAccount userAccount = new UserAccount("", "", "", "", "testPassword");

        doNothing().when(userAccountDAO).registerAccount(userAccount);
        List<String> usernames = new ArrayList<>();
        usernames.add("");
        when(userAccountDAO.getAllusernames()).thenReturn(usernames);

        userAccountDAO.registerAccount(userAccount);

        List<String> users = userAccountDAO.getAllusernames();
        assertTrue(users.contains(""));
    }

    @Test
    void testLoginWithTheSameEmail()
    {
        UserAccount userAccount1 = new UserAccount("testUser", "testFirstName", "testLastName", "testEmail@gmail.com", "testPassword");
        UserAccount userAccount2 = new UserAccount("testUser2", "testFirstName2", "testLastName2", "testEmail@gmail.com", "testPassword2");

        doNothing().when(userAccountDAO).registerAccount(userAccount1);
        doNothing().when(userAccountDAO).registerAccount(userAccount2);

        List<String> usernames = new ArrayList<>();
        usernames.add("testUser");
        usernames.add("testUser2");
        when(userAccountDAO.getAllusernames()).thenReturn(usernames);

        List<String> users = userAccountDAO.getAllusernames();
        assertTrue(users.contains("testUser"));
        assertTrue(users.contains("testUser2"));
    }

    @Test
    void testCharLimitFirstName()
    {
        String longInput = "a".repeat(256); // Over 255 characters
        UserAccount userAccount = new UserAccount("testUserLongFirstName", longInput, "testLastname", "testEmail@gmail.com", "testPassword");

        doNothing().when(userAccountDAO).registerAccount(userAccount);
        List<String> usernames = new ArrayList<>();
        usernames.add("testUserLongFirstName");
        when(userAccountDAO.getAllusernames()).thenReturn(usernames);

        List<String> users = userAccountDAO.getAllusernames();
        assertTrue(users.contains("testUserLongFirstName"));
    }

    @Test
    void testCharLimitLastName()
    {
        String longInput = "a".repeat(256);
        UserAccount userAccount = new UserAccount("testUserLongLastName", "firstNameTest", longInput, "testEmail@gmail.com", "testPassword");

        doNothing().when(userAccountDAO).registerAccount(userAccount);
        List<String> usernames = new ArrayList<>();
        usernames.add("testUserLongLastName");
        when(userAccountDAO.getAllusernames()).thenReturn(usernames);

        List<String> users = userAccountDAO.getAllusernames();
        assertTrue(users.contains("testUserLongLastName"));
    }

    @Test
    void testCharLimitUsername() {
        String longInput = "a".repeat(256);
        UserAccount userAccount = new UserAccount(longInput, "firstNameTest", "testLastname", "testEmail@gmail.com", "testPassword");

        doNothing().when(userAccountDAO).registerAccount(userAccount);

        List<String> usernames = new ArrayList<>();
        usernames.add(longInput);
        when(userAccountDAO.getAllusernames()).thenReturn(usernames);

        List<String> users = userAccountDAO.getAllusernames();
        assertTrue(users.contains(longInput));
    }
}