package edu.qut.cab302.wehab.userAccounts;
import at.favre.lib.crypto.bcrypt.BCrypt;
import edu.qut.cab302.wehab.user_account.UserAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class userAccountTest
{
    private MockedStatic<BCrypt> bcryptMock;

    @BeforeEach
    public void SetUp()
    {
        bcryptMock= mockStatic(BCrypt.class);
    }

    @AfterEach
    public void Fin()
    {
        bcryptMock.close();
    }

    @Test
    void testUserAccountCreation()
    {
        String plainPassword = "passssssssssss";
        String mockHashedPassword = "$2a$10$CwTycUXWue0Thq9StjUM0u";

        BCrypt.Hasher mockHasher = mock(BCrypt.Hasher.class);
        when(mockHasher.hashToString(anyInt(), any(char[].class))).thenReturn(mockHashedPassword);
        bcryptMock.when(BCrypt::withDefaults).thenReturn(mockHasher);

        UserAccount account = new UserAccount("user222222", "Jos", "aaaa", "blah@gmail.com", plainPassword);

        assertEquals("user222222", account.getUsername());
        assertEquals("Jos", account.getFirstName());
        assertEquals("aaaa", account.getLastName());
        assertEquals("blah@gmail.com", account.getEmail());
        assertEquals(mockHashedPassword, account.getHashedPassword());
    }

    @Test
    void testPasswordIsHashed()
    {
        String plainPassword = "RyanIsAmazing";
        String mockHashedPassword = "$2a$10$CwTycUXWue0Thq9StjUM0u";

        // Mock BCrypt.Hasher and the hashToString method
        BCrypt.Hasher mockHasher = mock(BCrypt.Hasher.class);
        when(mockHasher.hashToString(anyInt(), any(char[].class))).thenReturn(mockHashedPassword);

        // Mock BCrypt.withDefaults() to return the mocked Hasher
        bcryptMock.when(BCrypt::withDefaults).thenReturn(mockHasher);

        UserAccount account = new UserAccount("RyanDoesntLikeOnions", "Hairy", "Feet", "AnEmail@gmail.com", plainPassword);
        assertEquals(mockHashedPassword, account.getHashedPassword());
    }

    @Test
    void testEmptyFields()
    {
        String mockHashedPassword = "$2a$10$CwTycUXWue0Thq9StjUM0u";

        BCrypt.Hasher mockHasher = mock(BCrypt.Hasher.class);
        when(mockHasher.hashToString(anyInt(), any(char[].class))).thenReturn(mockHashedPassword);

        bcryptMock.when(BCrypt::withDefaults).thenReturn(mockHasher);

        UserAccount newAccount = new UserAccount("", "", "", "", "password");

        assertEquals("", newAccount.getUsername());
        assertEquals("", newAccount.getFirstName());
        assertEquals("", newAccount.getLastName());
        assertEquals("", newAccount.getEmail());

        assertNotEquals("password", newAccount.getHashedPassword());
        assertEquals(mockHashedPassword, newAccount.getHashedPassword());
    }

    @Test
    void testNullPassword()
    {
        assertThrows(NullPointerException.class, () -> {
            new UserAccount("email", "pasword", "firstname", "lastname@gmail.com", null);
        });
    }
}