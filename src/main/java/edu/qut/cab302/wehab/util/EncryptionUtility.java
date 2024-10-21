package edu.qut.cab302.wehab.util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Utility class for performing AES encryption and decryption using a 16-byte key
 * and CBC (Cipher Block Chaining) mode with PKCS5 padding. The key and IV (Initialisation Vector)
 * are predefined within the class for simplicity.
 */
public class EncryptionUtility {

    // 16-byte AES key represented as a string
    private static String plainTextKey = "put16ByteKeyHere";

    // Convert the key string into a byte array to be used for AES encryption
    private static byte[] plainTextKeyBytes = plainTextKey.getBytes();

    // Create the AES SecretKey object using the 16-byte key
    private static SecretKey key = new SecretKeySpec(plainTextKeyBytes, "AES");

    // 16-byte IV string
    private static final String ivString = "put16ByteKeyHere";

    // Convert the IV string into a byte array
    private static byte[] ivBytes = ivString.getBytes();

    // Create the IvParameterSpec object using the IV byte array
    private static IvParameterSpec iv = new IvParameterSpec(ivBytes);

    /**
     * Encrypts the given plain text using AES/CBC/PKCS5Padding.
     *
     * @param plainText The text to encrypt.
     * @return The encrypted text encoded in Base64.
     * @throws NoSuchPaddingException If the requested padding scheme is not available.
     * @throws NoSuchAlgorithmException If the AES algorithm is not available.
     * @throws IllegalBlockSizeException If the input length is not a multiple of the block size.
     * @throws BadPaddingException If the padding is incorrect or if the cipher is in an invalid state.
     * @throws InvalidAlgorithmParameterException If the provided IV is invalid.
     * @throws InvalidKeyException If the provided encryption key is invalid.
     */
    public static String encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {

        // Trim any leading or trailing spaces from the plain text
        plainText = plainText.trim();

        // Initialise the AES cipher in encryption mode with CBC and PKCS5Padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        // Perform the encryption operation on the plain text
        byte[] cipherText = cipher.doFinal(plainText.getBytes());

        // Encode the encrypted byte array into a Base64 string
        String cipherTextBase64 = Base64.getEncoder().encodeToString(cipherText);

        return cipherTextBase64;
    }

    /**
     * Decrypts the given Base64 encoded cipher text using AES/CBC/PKCS5Padding.
     *
     * @param cipherText The Base64 encoded encrypted text.
     * @return The decrypted plain text.
     * @throws NoSuchPaddingException If the requested padding scheme is not available.
     * @throws NoSuchAlgorithmException If the AES algorithm is not available.
     * @throws IllegalBlockSizeException If the input length is not a multiple of the block size.
     * @throws BadPaddingException If the padding is incorrect or if the cipher is in an invalid state.
     * @throws InvalidKeyException If the provided decryption key is invalid.
     * @throws InvalidAlgorithmParameterException If the provided IV is invalid.
     */
    public static String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

        // Trim any leading or trailing spaces from the plain text
        cipherText = cipherText.trim();

//        Log the cipher text to be decrypted (for debugging purposes)
//        System.out.println("CipherText before decrypt: [" + cipherText + "]");

        // Initialise the AES cipher in decryption mode with CBC and PKCS5Padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        // Decode the Base64 encoded cipher text and decrypt it
        byte[] plainTextBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));

        // Convert the decrypted byte array into a string (plain text) and return it
        return new String(plainTextBytes);
    }
}
