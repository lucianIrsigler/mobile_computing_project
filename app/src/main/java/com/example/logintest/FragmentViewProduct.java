package com.example.logintest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logintest.Product;
import com.example.logintest.databinding.FragmentViewProductBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewProduct extends Fragment {
    private Product product;

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
        // Inflate the layout for this fragment
        binding = FragmentViewProductBinding.inflate(inflater, container, false);

        // Access the views in the fragment layout and update them with the product details
        TextView productNameTextView = binding.getRoot().findViewById(R.id.tvProductNameViewProduct);
        TextView productPriceTextView = binding.getRoot().findViewById(R.id.tvPriceViewProduct);
        TextView productDescriptionTextView = binding.getRoot().findViewById(R.id.tvDescriptionViewProduct);
        TextView dateOfProduct = binding.getRoot().findViewById(R.id.tvDateViewProduct);
        RecyclerView showImageRecyclerView = binding.getRoot().findViewById(R.id.view_products_rv);


        productNameTextView.setText(product.getName());
        String price = String.format("R%.2f",product.getPrice());
        productPriceTextView.setText(price);
        productDescriptionTextView.setText(product.getDescription());

        HTTPHandler handler = new HTTPHandler();

        JSONObject params = new JSONObject();

        try {
            params.put("productID", product.getProductID());


            JSONArray productDetails = handler.getRequest(
                "https://lamp.ms.wits.ac.za/home/s2621933/php/searchoneproduct.php",
                params, JSONArray.class);


            String dateAdded = productDetails.getJSONObject(0).getString("dateAdded");
            String[] substrings = dateAdded.split("-");
            String day = substrings[2];
            String month = substrings[1];
            String year = substrings[0];

            String dateOutput = day + "/" + month + "/" + year;
            dateOfProduct.setText(dateOutput);

            //todo USERNAME

            JSONObject params1 = new JSONObject();

            params1.put("productID", 5);

            JSONArray imagePathResponse = handler.getRequest(
                    "https://lamp.ms.wits.ac.za/home/s2621933/php/imageReader.php",
                    params1, JSONArray.class);


            JSONObject newParams = new JSONObject();

            for (int i = 0; i < imagePathResponse.length(); i++) {
                String imagePath = imagePathResponse.getJSONObject(i).getString("imagePath");
                newParams.put("image" + i, imagePath);
            }

            newParams.put("imageCount", imagePathResponse.length());

            List<String> base64ImageList = new ArrayList<>();
            Log.i("test", newParams.toString());

            JSONArray base64fileResponse = handler.postRequest(
                    "https://lamp.ms.wits.ac.za/home/s2621933/php/base64_files.php",
                    newParams, JSONArray.class);

            for (int i = 0; i < base64fileResponse.length(); i++) {
                base64ImageList.add(base64fileResponse.getJSONObject(i).getString("base64"));
            }

            ImageAdapterGetFromDatabase imageAdapterGetFromDatabase = new ImageAdapterGetFromDatabase(base64ImageList);

            showImageRecyclerView.setLayoutManager(new LinearLayoutManager(
                    getContext(), LinearLayoutManager.HORIZONTAL, false));
            showImageRecyclerView.setAdapter(imageAdapterGetFromDatabase);

            return binding.getRoot();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}