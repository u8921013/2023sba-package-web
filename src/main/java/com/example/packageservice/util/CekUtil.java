package com.example.packageservice.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CekUtil {

    /**
     * Generate random 128-bit CEK and encrypt it with given master key using AES.
     * This is a simplified example; in real life you would use stronger key management.
     */
    public static String generateAndEncryptCek(byte[] masterKey) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey cek = keyGen.generateKey();

            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(masterKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(cek.getEncoded());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate/encrypt CEK", e);
        }
    }

    /**
     * Decrypt CEK using master key. Only for reference.
     */
    public static byte[] decryptCek(String encrypted, byte[] masterKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(masterKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(Base64.getDecoder().decode(encrypted));
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt CEK", e);
        }
    }
}
