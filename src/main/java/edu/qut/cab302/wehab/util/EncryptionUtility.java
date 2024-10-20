package edu.qut.cab302.wehab.util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Utility class that provides methods for encrypting and decrypting strings using AES encryption in CBC mode.
 * The encryption uses a static key and initialization vector (IV).
 */
public class EncryptionUtility {

    private static String plainTextKey = "put16ByteKeyHere";  // Static key for encryption and decryption
    private static byte[] plainTextKeyBytes = plainTextKey.getBytes();
    private static SecretKey key = new SecretKeySpec(plainTextKeyBytes, "AES");

    private static final String ivString = "put16ByteKeyHere";  // Static initialization vector for AES CBC mode
    private static byte[] ivBytes = ivString.getBytes();
    private static IvParameterSpec iv = new IvParameterSpec(ivBytes);

    /**
     * Encrypts the provided plain text using AES/CBC/PKCS5Padding.
     *
     * @param plainText The string to be encrypted.
     * @return The encrypted string, encoded in Base64.
     * @throws NoSuchPaddingException If the padding scheme is unavailable.
     * @throws NoSuchAlgorithmException If the AES algorithm is unavailable.
     * @throws IllegalBlockSizeException If the block size is invalid.
     * @throws BadPaddingException If padding is incorrect.
     * @throws InvalidAlgorithmParameterException If the algorithm parameters (IV) are invalid.
     * @throws InvalidKeyException If the provided encryption key is invalid.
     */
    public static String encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        plainText = plainText.trim();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        String cipherTextBase64 = Base64.getEncoder().encodeToString(cipherText);
        return cipherTextBase64;
    }

    /**
     * Decrypts the provided Base64-encoded cipher text using AES/CBC/PKCS5Padding.
     *
     * @param cipherText The Base64-encoded cipher text to be decrypted.
     * @return The decrypted plain text string.
     * @throws NoSuchPaddingException If the padding scheme is unavailable.
     * @throws NoSuchAlgorithmException If the AES algorithm is unavailable.
     * @throws IllegalBlockSizeException If the block size is invalid.
     * @throws BadPaddingException If padding is incorrect.
     * @throws InvalidAlgorithmParameterException If the algorithm parameters (IV) are invalid.
     * @throws InvalidKeyException If the provided decryption key is invalid.
     */
    public static String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        cipherText = cipherText.trim();
        System.out.println("CipherText before decrypt: [" + cipherText + "]");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainTextBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainTextBytes);
    }
}
