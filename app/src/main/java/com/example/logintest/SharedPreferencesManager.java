package com.example.logintest;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String _prefName = "MyPrefs";
    private static final String _userID = "userId";

    private static SharedPreferences sharedPreferences;

    public static void initialize(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(_prefName, Context.MODE_PRIVATE);
        }
    }

    public static void storeUserId(long userId) {
        sharedPreferences.edit().putLong(_userID, userId).apply();
    }

    public static long retrieveUserId() {
        return sharedPreferences.getLong(_userID, -1);
    }

    public static void resetUserID() {
        sharedPreferences.edit().putLong(_userID, -1).apply();
    }
}
