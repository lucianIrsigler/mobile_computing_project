package com.example.logintest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TransactionManager{

    final HTTPHandler handler = new HTTPHandler();

    public TransactionManager(){}

    /**
     * check if user has bought an item. in the transactions table in the database,
     * it stores the userID and productID, so we search for an entry with inputted userID
     * and productID. If present, then returns true
     * @param userID userID to check
     * @param productID productID to check
     * @return true if user has bought the item, else false.
     */
    boolean checkTransaction(long userID,int productID){
        JSONObject params = new JSONObject();
        try {
            params.put("userID",userID);
            params.put("productID",productID);
            String response = handler.getRequest(
                    "https://lamp.ms.wits.ac.za/home/s2621933/php/checktransaction.php",
                    params,String.class);

            return response.equals("true");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Inserts a transaction record for a user and a product.
     *
     * @param userID    the ID of the user
     * @param productID the ID of the product
     */
    void insertTransaction(long userID,int productID){
        JSONObject params = new JSONObject();
        try {
            params.put("userID",userID);
            params.put("productID",productID);
            handler.postRequest(
                    "https://lamp.ms.wits.ac.za/home/s2621933/php/inserttransaction.php",
                    params,String.class);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
