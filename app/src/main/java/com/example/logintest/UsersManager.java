package com.example.logintest;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class UsersManager extends AppCompatActivity {

    final HTTPHandler handler = new HTTPHandler();
    public UsersManager(){}

    public JSONObject getUserInformation(){
        Context context = this;

        //get saved user id
        SharedPreferencesManager.initialize(context);
        long userId = SharedPreferencesManager.retrieveUserId();

        //assign params
        JSONObject params = new JSONObject();
        try {
            params.put("userID",userId);
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
