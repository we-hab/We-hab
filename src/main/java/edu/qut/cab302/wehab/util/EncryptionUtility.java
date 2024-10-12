package edu.qut.cab302.wehab.util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionUtility {

    private static String plainTextKey = "put16ByteKeyHere";
    private static byte[] plainTextKeyBytes = plainTextKey.getBytes();
    private static SecretKey key = new SecretKeySpec(plainTextKeyBytes, "AES");

    private static final String ivString = "put16ByteKeyHere";
    private static byte[] ivBytes = ivString.getBytes();
    private static IvParameterSpec iv = new IvParameterSpec(ivBytes);

    public static String encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        plainText = plainText.trim();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        String cipherTextBase64 = Base64.getEncoder().encodeToString(cipherText);
        return cipherTextBase64;
    }

    public static String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        cipherText = cipherText.trim();
        System.out.println("CipherText before decrypt: [" + cipherText + "]");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainTextBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainTextBytes);
    }
}
