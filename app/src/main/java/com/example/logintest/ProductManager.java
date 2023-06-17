package com.example.logintest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductManager {
    private final HTTPHandler httpHandler;
    public ProductManager() {
        httpHandler = new HTTPHandler();
    }

    /**
     * Adds a product
     * @param product Product to add
     * @param userID userID of seller
     * @param category category of the product
     */
    public /*String*/void addProduct(Product product,long userID, String category) {
        JSONObject params = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        try {
            params.put("userID",userID);
            params.put("productID",product.getProductID());
            params.put("name", product.getName());
            params.put("description", product.getDescription());
            params.put("price", product.getPrice());
            params.put("category", category);
            params.put("date_added", currentDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String addProductUrl = "https://lamp.ms.wits.ac.za/home/s2621933/php/addproduct.php";
        /*return*/ httpHandler.postRequest(addProductUrl, params, String.class);
    }


    /**
     * searching a product
     * @param productName name of product being searched for
     */
    public /*void*/List<Product> searchProduct(String productName) {
        List<Product> searchResults = new ArrayList<>();

        try {
            JSONObject params = new JSONObject();
            params.put("search", productName);

            String searchUrl = "https://lamp.ms.wits.ac.za/home/s2621933/php/searchproduct.php";
            String response = httpHandler.getRequest(searchUrl, params, String.class);

            // populate the searchResults list
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonProduct = jsonArray.getJSONObject(i);
                int productID= Integer.parseInt(jsonProduct.getString("productID"));
                String name = jsonProduct.getString("productName");
                String description = jsonProduct.getString("productDescription");
                double price = jsonProduct.getDouble("price");

                Product product = new Product(name, description, price,productID);
                searchResults.add(product);
            }
        } catch (JSONException e) {
            Log.e("searchProduct",e.getMessage());
        }

        return searchResults;

    }

    /**
     * Creates product list given JSONArray with productIDs in it
     * @param products JSONArray with productIDs in it
     * @return List<Product> with products in it
     */

    public List<Product> createProductsFromArray(JSONArray products){

        List<Product> output = new ArrayList<>();

        for (int i =0; i < products.length();i++){
            try {
                int productID = Integer.parseInt(
                        products.getJSONObject(0).getString("productID"));

                JSONObject params = new JSONObject();
                params.put("productID",productID );

                 JSONArray response =httpHandler.getRequest(
                        "https://lamp.ms.wits.ac.za/home/s2621933/php/searchoneproduct.php",
                        params, JSONArray.class);


                 JSONObject obj = response.getJSONObject(0);

                 String name = obj.getString("productName");
                 String description = obj.getString("productDescription");
                 double price = obj.getDouble("price");

                Product product = new Product(name,description,price,productID);
                output.add(product);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        return output;
    }

    public /*void*/List<Product> searchProductUserID(long userID) {
        List<Product> searchResults = new ArrayList<>();

        try {
            JSONObject params = new JSONObject();
            params.put("userID", userID);

            String searchUrl = "https://lamp.ms.wits.ac.za/home/s2621933/php/getProductsUserID.php";
            String response = httpHandler.getRequest(searchUrl, params, String.class);

            // populate the searchResults list
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonProduct = jsonArray.getJSONObject(i);
                int productID= Integer.parseInt(jsonProduct.getString("productID"));
                String name = jsonProduct.getString("productName");
                String description = jsonProduct.getString("productDescription");
                double price = jsonProduct.getDouble("price");

                Product product = new Product(name, description, price,productID);
                searchResults.add(product);
            }
        } catch (JSONException e) {
            Log.e("searchProduct",e.getMessage());
        }
        return searchResults;
    }

    public boolean isSeller(long userID,int productID) {
        JSONObject params = new JSONObject();

        try {
            params.put("userID",userID);
            params.put("productID",productID);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String response = httpHandler.postRequest("https://lamp.ms.wits.ac.za/home/s2621933/php/isSeller.php",
                params,String.class);

        return response.equals("true");
    }

    /**
     * searching for all products from a catergory
     * @param category name of products looking for
     */
    public List<Product> findAllInCategory(String category) {
        List<Product> searchResults = new ArrayList<>();
        JSONObject params = new JSONObject();

        try {
            params.put("category", category); //
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String searchUrl = "https://lamp.ms.wits.ac.za/home/s2621933/php/searchcategory.php";

        String response = httpHandler.getRequest(searchUrl, params, String.class);

        try {
            // populate the searchResults list
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonProduct = jsonArray.getJSONObject(i);
                int productID = Integer.parseInt(jsonProduct.getString("productID"));
                String name = jsonProduct.getString("productName");
                String description = jsonProduct.getString("productDescription");
                double price = jsonProduct.getDouble("price");

                Product product = new Product(name, description, price, productID);
                searchResults.add(product);
            }
        }catch (JSONException ex) {
            throw new RuntimeException(ex);
        }

        return searchResults;
    }

    public static List<Product> getRecommendedProducts() {
        List<Product> randomItems = new ArrayList<>();

        try {
            JSONObject params = new JSONObject();
            String randomItemsUrl =
                    "https://lamp.ms.wits.ac.za/home/s2621933/php/selectRandomProducts.php";

            HTTPHandler httpHandler = new HTTPHandler();
            String response = httpHandler.getRequest(randomItemsUrl, params, String.class);
            // Parse the response and populate the randomItems list
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < 2; i++) {
                JSONObject jsonProduct = jsonArray.getJSONObject(i);
                String name = jsonProduct.getString("productName");
                String description = jsonProduct.getString("productDescription");
                double price = jsonProduct.getDouble("price");
                int productID= Integer.parseInt(jsonProduct.getString("productID"));
                Product product = new Product(name, description, price, productID);
                randomItems.add(product);
            }
        } catch (JSONException e) {
            Log.e("getRandomItems", e.getMessage());
        }

        return randomItems;

    }

}