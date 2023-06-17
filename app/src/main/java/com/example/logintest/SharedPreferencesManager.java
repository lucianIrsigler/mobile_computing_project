package com.example.logintest;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {
    private static final String _prefName = "MyPrefs";
    private static final String _userID = "userId";
    private static final String _userName = "username";

    private static List<Product> recommendedProducts = new ArrayList<>();

    private static JSONObject _userInfo = null;

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

    public static void storeUserName(String userName) {
        sharedPreferences.edit().putString(_userName, userName).apply();
    }

    public static String retrieveUserName() {
        return sharedPreferences.getString(_userName,"");
    }

    public static void storeUserInfo(JSONObject userInfo){
        _userInfo = userInfo;
    }

    public static JSONObject retriveUserInfo(){
        return _userInfo;
    }

    public static void setRecommendedProducts(){
        recommendedProducts = ProductManager.getRecommendedProducts();
    }
    public static List<Product> getRecommendedProducts(){
        if (recommendedProducts.size()==0){
            setRecommendedProducts();
        }

        return recommendedProducts;
    }
}
