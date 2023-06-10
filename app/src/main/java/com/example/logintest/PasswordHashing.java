package com.example.logintest;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashing
{
    private final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    /**
     * Converts a byte array to a hexadecimal string representation.
     *
     * @param bytes the byte array to convert
     * @return the hexadecimal string representation of the byte array
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    /**
     * Generates a hashed password using the SHA-256 algorithm and the given salt and password.
     *
     * @param salt     the salt used for hashing
     * @param password the password to be hashed
     * @return the hashed password as a hexadecimal string
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
     */
    public String hashedPassword(byte[] salt, String password) throws NoSuchAlgorithmException
    {
        byte[] saltedPassword = new byte[salt.length + password.getBytes().length];
        System.arraycopy(salt, 0, saltedPassword, 0, salt.length);
        System.arraycopy(password.getBytes(), 0, saltedPassword, salt.length, password.getBytes().length);

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = messageDigest.digest(saltedPassword);

        return bytesToHex(hashedPassword);
    }
}
