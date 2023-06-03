package com.example.logintest;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductManager {
    private final HTTPHandler httpHandler;
//TODO reponses in the activity screen
    public ProductManager() {
        httpHandler = new HTTPHandler();
    }
    /**
     * Adding a product
     * @param productName name of product
     * @param productDescription description of the product being added
     * @param productPrice price
     * @param category category of product
     * @param bitmap image of product
     */
    public /*String*/void addProduct(String productName, String productDescription, double productPrice, String category, Bitmap bitmap) {
        JSONObject params = new JSONObject();

        try {
            params.put("name", productName);
            params.put("description", productDescription);
            params.put("price", productPrice);
            params.put("category", category);
            params.put("image", bitmapToBase64(bitmap));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String addProductUrl = "https://lamp.ms.wits.ac.za/home/s2571291/addproduct.php";
        /*return*/ httpHandler.postRequest(addProductUrl, params, String.class);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    /**
     * searching a product
     * @param productName name of product being searched for
     */
    public void searchProduct(String productName) {
        JSONObject params = new JSONObject();

        try {
            params.put("search", productName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String searchUrl = "https://lamp.ms.wits.ac.za/home/s2571291/searchproduct.php";
        httpHandler.getRequest(searchUrl, params, String.class);
    }
    /**
     * searching for all products from a catergory
     * @param category name of products looking for
     */
    public void findAllInCategory(String category) {
        JSONObject params = new JSONObject();

        try {
            params.put("category", category); // Set the category parameter
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String searchUrl = "https://lamp.ms.wits.ac.za/home/s2571291/searchcategory.php";
        httpHandler.getRequest(searchUrl, params, String.class); // Make a GET request to search for products in the specified category
    }


}