package com.example.logintest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RatingManager {
    final HTTPHandler handler = new HTTPHandler();
    public RatingManager(){};


    public void addRating(Integer userID, Integer productID, Double stars, String  date){
        String url = "https://lamp.ms.wits.ac.za/home/s2621933/php/addrating.php";
        JSONObject params = new JSONObject();
        try {
            params.put("userID", userID);
            params.put("productID",productID);
            params.put("stars",stars);
            params.put("date",date);
        }catch (JSONException e) {
            System.out.println("error");
        }
        handler.postRequest(url,params,String.class);
    }

    /**
     *
     * @param userID
     * @param productID
     * @return
     */
    public JSONArray getRating(Integer userID,Integer productID){
        JSONObject params = new JSONObject();

        try {
            params.put("mode",2);
            params.put("userID",userID);
            params.put("productID",productID);
        }catch (JSONException e){
            System.out.println("error");
        }
        String url = "https://lamp.ms.wits.ac.za/home/s2621933/php/selectrating.php";

        return handler.getRequest(url,params,JSONArray.class);
    }

    /**
     * GETS ratings based on userID/productID
     * @param mode mode for the GET request. 0->use the userID, 1->use the productID
     * @param value value to get
     * @return rating based on productID/userID
     */
    public JSONArray getRatings(Integer mode,Integer value){
        JSONObject params = new JSONObject();

        try {
            params.put("mode", mode);
            params.put("value",value);
        }catch (JSONException e){
            System.out.println("error");
        }
        String url = "https://lamp.ms.wits.ac.za/home/s2621933/php/selectrating.php";

        return handler.getRequest(url,params,JSONArray.class);
    }


    public double getAverageRating(Integer productID){
        JSONObject array = new JSONObject();

        try {
            array.put("productID", productID);
        }catch (JSONException e){
            System.out.println("error");
        }

        String url = "https://lamp.ms.wits.ac.za/home/s2621933/php/getaveragerating.php";
        JSONObject response = handler.getRequest(url,array,JSONObject.class);

        if (response.has("error")){
            try {
                System.out.println(response.getString("error"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return -1.0;
        }else{
            try {
                String rating = response.getString("average_rating");
                return Double.parseDouble(rating);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
