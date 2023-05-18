package com.example.logintest;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashing
{
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull String bytesToHex(byte [] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

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
