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
        String url = "https://lamp.ms.wits.ac.za/~s2621933/php/getuserinfo.php";

        return handler.getRequest(url,params,JSONObject.class);
    }

    public JSONObject searchUserInfo(long userID){
        //assign params
        JSONObject params = new JSONObject();
        try {
            params.put("userID",userID);
        }catch (JSONException e){
            System.out.println("error");
        }

        //url to get request
        String url = "https://lamp.ms.wits.ac.za/~s2621933/php/getuserinfo.php";

        return handler.getRequest(url,params,JSONObject.class);
    }

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
