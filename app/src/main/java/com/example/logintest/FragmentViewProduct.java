package com.example.logintest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logintest.databinding.FragmentViewProductBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewProduct extends Fragment {
    private Product product;
    final HTTPHandler handler = new HTTPHandler();
    FragmentViewProductBinding binding;
    public FragmentViewProduct() {
        // Required empty public constructor
    }

    public FragmentViewProduct(Product product) {
        this.product = product;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewProductBinding.inflate(inflater, container, false);

        // Access the views in the fragment layout and update them with the product details
        TextView userNameTextView = binding.getRoot().findViewById(R.id.tvUsernameViewProduct);
        TextView productNameTextView = binding.getRoot().findViewById(R.id.tvProductNameViewProduct);
        TextView productPriceTextView = binding.getRoot().findViewById(R.id.tvPriceViewProduct);
        TextView productDescriptionTextView = binding.getRoot().findViewById(R.id.tvDescriptionViewProduct);
        TextView dateOfProduct = binding.getRoot().findViewById(R.id.tvDateViewProduct);
        RecyclerView showImageRecyclerView = binding.getRoot().findViewById(R.id.view_products_rv);


        productNameTextView.setText(product.getName());
        String price = String.format("R%.2f",product.getPrice());
        productPriceTextView.setText(price);
        productDescriptionTextView.setText(product.getDescription());

        try {
            JSONArray productDetails = getProductDetails();

            //set date
            String dateOutput = formatDate(productDetails);
            dateOfProduct.setText(dateOutput);

            //get userID and set username
            int userID = productDetails.getJSONObject(0).getInt("userID");
            userNameTextView.setText(getUsername(userID));

            //get image paths
            JSONArray imagePathResponse = getImagePaths();

            //get base64
            List<String> base64ImageList = getBase64Strings(imagePathResponse);

            //set recycler view
            ImageAdapterGetFromDatabase imageAdapterGetFromDatabase = new ImageAdapterGetFromDatabase(base64ImageList);
            showImageRecyclerView.setLayoutManager(new LinearLayoutManager(
                    getContext(), LinearLayoutManager.HORIZONTAL, false));
            showImageRecyclerView.setAdapter(imageAdapterGetFromDatabase);

            return binding.getRoot();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Search a products details
     * @return the JSONArray of the product details
     * @throws JSONException
     */
    private JSONArray getProductDetails() throws JSONException {
        JSONObject params = new JSONObject();
        params.put("productID", product.getProductID());

        return handler.getRequest(
                "https://lamp.ms.wits.ac.za/home/s2621933/php/searchoneproduct.php",
                params, JSONArray.class);
    }

    /**
     * Searches user table to retreive a username
     * @param userID userID to check
     * @return the username for the userID
     * @throws JSONException if something goes wrong with JSONObject
     */
    private String getUsername(long userID) throws JSONException {
        UsersManager usersManager = new UsersManager();
        JSONObject userName = usersManager.searchUserInfo(userID);
        return userName.getString("username");
    }

    /**
     *  formats the date output for the textview
     * @param productDetails product details includes "dateAdded"
     * @return the date formated
     * @throws JSONException if something goes wrong with getJSONObject
     */
    private String formatDate(JSONArray productDetails) throws JSONException {
        String dateAdded = productDetails.getJSONObject(0).getString("dateAdded");
        String[] substrings = dateAdded.split("-");
        String day = substrings[2];
        String month = substrings[1];
        String year = substrings[0];
        return day + "/" + month + "/" + year;
    }

    /**
     * Gets image paths for the product
     * @return JSONArray including the image paths
     * @throws JSONException if the params is unable to put "productID"
     */
    private JSONArray getImagePaths() throws JSONException {
        JSONObject params = new JSONObject();
        params.put("productID", product.getProductID());
        return handler.getRequest(
                "https://lamp.ms.wits.ac.za/home/s2621933/php/imageReader.php",
                params, JSONArray.class);
    }

    /**
     * Using the image paths, returns the base64 strings from the disk on the server
     * @param imagePathResponse JSONArray with paths
     * @return the base64 strings in a list
     * @throws JSONException if params fails/JSONObject creation fails
     */
    private List<String> getBase64Strings(JSONArray imagePathResponse) throws JSONException {
        JSONObject newParams = new JSONObject();

        for (int i = 0; i < imagePathResponse.length(); i++) {
            String imagePath = imagePathResponse.getJSONObject(i).getString("imagePath");
            newParams.put("image" + i, imagePath);
        }

        newParams.put("imageCount", imagePathResponse.length());

        List<String> base64ImageList = new ArrayList<>();

        JSONArray base64fileResponse = handler.postRequest(
                "https://lamp.ms.wits.ac.za/home/s2621933/php/base64_files.php",
                newParams, JSONArray.class);

        for (int i = 0; i < base64fileResponse.length(); i++) {
            base64ImageList.add(base64fileResponse.getJSONObject(i).getString("base64"));
        }

        return base64ImageList;
    }
}