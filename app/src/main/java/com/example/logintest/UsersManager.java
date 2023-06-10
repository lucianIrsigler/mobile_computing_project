package com.example.logintest;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UsersManager extends AppCompatActivity {

    final HTTPHandler handler = new HTTPHandler();
    public UsersManager(){}

    public long getCurrentUserID(){
        Context context = this;
        SharedPreferencesManager.initialize(context);
        return SharedPreferencesManager.retrieveUserId();

    }
    /**
     * Retrieves the user information for the current user.
     *
     * @return a JSONObject containing the user information
     */
    public JSONObject getUserInformation(){
        //get saved user id
        long userID = getCurrentUserID();

        //assign params
        JSONObject params = new JSONObject();
        try {
            params.put("userID",userID);
        }catch (JSONException e){
            System.out.println("error");
        }

        //url to get request
        String url = "https://lamp.ms.wits.ac.za/home/s2621933/php/getuserinfo.php";

        return handler.getRequest(url,params,JSONObject.class);
    }
    /**
     * Searches for user information based on the provided user ID.
     *
     * @param userID the ID of the user to search for
     * @return a JSONObject containing the user information
     */
    public JSONObject searchUserInfo(long userID){
        //assign params
        JSONObject params = new JSONObject();
        try {
            params.put("userID",userID);
        }catch (JSONException e){
            System.out.println("error");
        }

        //url to get request
        String url = "https://lamp.ms.wits.ac.za/home/s2621933/php/getuserinfo.php";

        return handler.getRequest(url,params,JSONObject.class);
    }
    /**
     * Checks if the provided username is available.
     *
     * @param username the username to check
     * @return a JSONObject containing the result of the username check
     */
    public JSONObject checkUsername(String username){
        JSONObject params = new JSONObject();
        try {
            params.put("username",username);
        }catch (JSONException e){
            System.out.println("error");
        }
        //url to get request
        String url = "https://lamp.ms.wits.ac.za/home/s2621933/php/checkusername.php";
        return handler.postRequest(url,params,JSONObject.class);
    }
}
