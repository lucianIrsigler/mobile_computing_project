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

    public ProductManager() {
        httpHandler = new HTTPHandler();
    }

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


}