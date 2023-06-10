package com.example.logintest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 Manages review adding,and selecting
 */
public class RatingManager {
    final HTTPHandler handler = new HTTPHandler();
    public RatingManager(){}

    /**
     * Inserts a rating
     * @param userID userID of user writing the review
     * @param productID productID of the product being reviewed
     * @param stars rating
     * @param date date of review added
     */
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
     * Returns a specific review
     *
     * @param userID the userID to get
     * @param productID the productID to get
     * @return the JSONArray of the specific review
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
     * @param value value to specify productID/userID
     * @return ratings based on productID/userID
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

    /**
     * Get the average rating of a a product
     * @param productID productID of product
     * @return  -1.0 if product not found otherwise returns the rating
     */
    public float getAverageRating(Integer productID) throws JSONException {
        JSONObject array = new JSONObject();

        array.put("productID", productID);

        String url = "https://lamp.ms.wits.ac.za/home/s2621933/php/getaveragerating.php";
        JSONObject response = handler.getRequest(url,array,JSONObject.class);

        if (response.has("error")){
            return 0;
        }else{
            String rating = response.getString("average_rating");
            return Float.parseFloat(rating);
            }
        }
}
