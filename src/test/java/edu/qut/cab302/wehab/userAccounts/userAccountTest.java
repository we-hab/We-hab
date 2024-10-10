package edu.qut.cab302.wehab.userAccounts;
import at.favre.lib.crypto.bcrypt.BCrypt;
import edu.qut.cab302.wehab.user_account.UserAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class userAccountTest
{
    @Test
    void testUserAccountCreation()
    {
        UserAccount account = new UserAccount("user222222","Jos","aaaa","blah@gmail.com","passssssssssss");
        assertEquals("user222222", account.getUsername());
        assertEquals("Jos", account.getFirstName());
        assertEquals("aaaa",account.getLastName());
        assertEquals("blah@gmail.com", account.getEmail());
    }

    @Test
    void testPasswordIsHashed()
    {
        String plainPassword = "RyanIsAmazing";
        UserAccount account = new UserAccount("RyanDoesntLikeOnions", "Hairy","Feet", "AnEmail@gmail.com", plainPassword);

        String hashedPass = account.getHashedPassword();
        assertTrue(BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPass).verified);
    }

    @Test
    void testEmptyFields()
    {
        UserAccount newAccount = new UserAccount("","","","","password");

        assertEquals("", newAccount.getUsername());
        assertEquals("", newAccount.getFirstName());
        assertEquals("", newAccount.getLastName());
        assertEquals("", newAccount.getEmail());
        assertNotEquals("password",newAccount.getHashedPassword());
        assertTrue(BCrypt.verifyer().verify("password".toCharArray(), newAccount.getHashedPassword()).verified);
    }

    @Test
    void testNullPassword()
    {
        assertThrows(NullPointerException.class, () ->
        {
            new UserAccount("email","pasword","firstname","lastname@gmail.com",null);
        });
    }
}
