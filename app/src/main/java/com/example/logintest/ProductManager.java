package com.example.logintest;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        try {
            params.put("name", productName);
            params.put("description", productDescription);
            params.put("price", productPrice);
            params.put("category", category);
            //params.put("image", bitmapToBase64(bitmap));
            params.put("date_added", currentDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        String addProductUrl = "https://lamp.ms.wits.ac.za/home/s2621933/php/addproduct.php";
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
    public /*void*/List<Product> searchProduct(String productName) {
        List<Product> searchResults = new ArrayList<>();

        try {
            JSONObject params = new JSONObject();
            params.put("productName", productName);

            String searchUrl = "https://lamp.ms.wits.ac.za/home/s2621933/php/searchproduct.php";
            String response = httpHandler.getRequest(searchUrl, params, String.class);

            // Parse the response and populate the searchResults list
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonProduct = jsonArray.getJSONObject(i);
                String name = jsonProduct.getString("name");

                String description = jsonProduct.getString("description");
                double price = jsonProduct.getDouble("price");
                // Get other product properties if needed

                // Create a new Product object and add it to the searchResults list
                Product product = new Product(name, description, price);
                searchResults.add(product);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return searchResults;
       /* JSONObject params = new JSONObject();

        try {
            params.put("productName", productName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String searchUrl = "https://lamp.ms.wits.ac.za/home/s2621933/php/searchproduct.php";
        httpHandler.getRequest(searchUrl, params, String.class);*/
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

        String searchUrl = "https://lamp.ms.wits.ac.za/home/s2621933/php/searchcategory.php";
        httpHandler.getRequest(searchUrl, params, String.class); // Make a GET request to search for products in the specified category
    }


}