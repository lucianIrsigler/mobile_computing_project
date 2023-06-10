package com.example.logintest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.logintest.databinding.FragmentViewProductBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentViewProduct extends Fragment {
    private Product product;
    final HTTPHandler handler = new HTTPHandler();
    final UsersManager usersManager = new UsersManager();
    FragmentViewProductBinding binding;
    FragmentManager manager;

    public FragmentViewProduct(Product product,FragmentManager manager) {
        this.product = product;
        this.manager = manager;
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
        //RecyclerView showImageRecyclerView = binding.getRoot().findViewById(R.id.view_products_rv);
        Button buyProduct = binding.getRoot().findViewById(R.id.btnBuyNow);
        Button addRating = binding.getRoot().findViewById(R.id.btnAddRating);
        RatingBar ratingBar = binding.getRoot().findViewById(R.id.ratingBar);
        TextView avgStars = binding.getRoot().findViewById(R.id.tvAvgRating);

        ImageSlider showImageSlider = binding.getRoot().findViewById(R.id.view_products_rv);


        productNameTextView.setText(product.getName());
        String price = String.format("R%.2f",product.getPrice());
        productPriceTextView.setText(price);
        productDescriptionTextView.setText(product.getDescription());

        //sets text for views
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

            List<SlideModel> slideModels = new ArrayList<SlideModel>();

            for (int i = 0; i < imagePathResponse.length(); i++) {
                String imagePath = imagePathResponse.getJSONObject(i).getString("imagePath");
                String url = "https://lamp.ms.wits.ac.za/home/s2621933/php/getimage.php?imagepath="+imagePath;
                SlideModel slide = new SlideModel(url,ScaleTypes.FIT);
                slideModels.add(slide);
            }

            showImageSlider.setImageList(slideModels);

            //set rating bar and avg star textview
            RatingManager ratingManager = new RatingManager();

            float avg_stars = ratingManager.getAverageRating(product.getProductID());

            if (avg_stars==0){
                avgStars.setText("0");
            }else{
                avgStars.setText(Float.toString(avg_stars));
            }

            ratingBar.setRating(avg_stars);


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        //set onclicks

        buyProduct.setOnClickListener(view -> {
            FragmentAddress fragment = new FragmentAddress(manager);
            fragment.setArguments(product.getProductID());
            utility.replaceFragment(manager,R.id.container,fragment,"address");

        });

        addRating.setOnClickListener(view -> {
            TransactionManager transactionManager = new TransactionManager();
            int productID = product.getProductID();
            long userID = usersManager.getCurrentUserID();
            Log.i("rating", Integer.toString(productID));
            Log.i("rating",Long.toString(userID));

            if (transactionManager.checkTransaction(userID,productID)) {
                showAlert();

                //update stars
                RatingManager ratingManager = new RatingManager();

                float avg_stars = 0;
                try {
                    avg_stars = ratingManager.getAverageRating(product.getProductID());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if (avg_stars == 0) {
                    avgStars.setText("0");
                } else {
                    avgStars.setText(Float.toString(avg_stars));
                }
                ratingBar.setRating(avg_stars);

            }else{
                Toast.makeText(getContext(),
                        "You must buy the item before you can leave a review",
                        Toast.LENGTH_LONG).show();
            }
        });
        return binding.getRoot();
    }

    /**
     * Search a products details
     * @return the JSONArray of the product details
     * @throws JSONException incase JSONObject creation dies
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

    public void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.review_product_dialog, null);
        builder.setView(dialogView);

        TextView ratingNum = dialogView.findViewById(R.id.dialog_rating);
        Button submit = dialogView.findViewById(R.id.btnSubmit);
        Button cancel = dialogView.findViewById(R.id.btnCancel);
        RatingBar bar = dialogView.findViewById(R.id.rbRateYourPurchase);
        final AlertDialog alertDialog = builder.create();

        ratingNum.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
         }

         @Override
         public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             if (charSequence.toString().isEmpty()) {
                 bar.setRating(0);
                 return;
             }

             float stars = Float.parseFloat(charSequence.toString());

             if (stars<=5){
                 bar.setRating(stars);
             }else{
                 bar.setRating(0);
             }
         }
         @Override
         public void afterTextChanged(Editable editable) {

         }});

        submit.setOnClickListener(v -> {
            float stars = Float.parseFloat(ratingNum.getText().toString());

            if (stars > 5 || stars<0){
                Toast.makeText(getActivity(), "Invalid rating", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
            RatingManager ratingManager = new RatingManager();

            ratingManager.addRating(usersManager.getCurrentUserID(),
                    product.getProductID(),stars);

            alertDialog.dismiss();
        });

        cancel.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }



}