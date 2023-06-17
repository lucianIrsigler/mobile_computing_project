package com.example.logintest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logintest.databinding.FragmentUserProfileBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FragmentUserProfile extends Fragment {
    private FragmentUserProfileBinding binding;
    final UsersManager usersManager = new UsersManager();
    final ProductManager productManager = new ProductManager();

    TextView textUsername;
    TextView textID;
    TextView textEmail;
    TextView textItems;
    TextView textReviews;

    public FragmentManager manager;

    public FragmentUserProfile() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.manager = getParentFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        utility.replaceFragment(manager,R.id.searchbarContainer,new EmptyFragment(),"empty");

        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        textUsername = binding.getRoot().findViewById(R.id.tvUsernameUP);
        textID = binding.getRoot().findViewById(R.id.tvIDUP);
        textEmail = binding.getRoot().findViewById(R.id.tvEmailUP);
        textItems = binding.getRoot().findViewById(R.id.tvItemCount);
        textReviews = binding.getRoot().findViewById(R.id.tvReviewCount);
        RecyclerView productsListedView = binding.getRoot().findViewById(R.id.products_rv);
        RecyclerView productsReviewedView = binding.getRoot().findViewById(R.id.reviews_rv);

        // listed products
        productsListedView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        SearchResultAdapter productsListedAdapter = new SearchResultAdapter();
        productsListedAdapter.setManager(manager);
        productsListedView.setAdapter(productsListedAdapter);
        List<Product> productsListedO = productManager.searchProductUserID(usersManager.getCurrentUserID());
        productsListedAdapter.setProducts(productsListedO);


        // reviewed products
        productsReviewedView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        SearchResultAdapter productsReviewedAdapter = new SearchResultAdapter();
        productsReviewedAdapter.setManager(manager);
        productsReviewedView.setAdapter(productsReviewedAdapter);

        // get products that the user left reviews on
        RatingManager ratingManager = new RatingManager();
        JSONArray productIDS = ratingManager.getUserReviews(usersManager.getCurrentUserID());
        List<Product> productsReviewedR = productManager.createProductsFromArray(productIDS);

        productsReviewedAdapter.setProducts(productsReviewedR);

        updateUserProfile();
        return binding.getRoot();
    }



    private void updateUserProfile(){
        SharedPreferencesManager.initialize(getActivity());
        long userId = SharedPreferencesManager.retrieveUserId();
        JSONObject userInfo = SharedPreferencesManager.retriveUserInfo();

        try {
            textUsername.setText(userInfo.getString("username"));
            textID.setText(Long.toString(userId));
            textEmail.setText(userInfo.getString("email"));
            textItems.setText(userInfo.getString("numProducts"));
            textReviews.setText(userInfo.getString("numReviews"));
        }catch (JSONException e){
            Toast.makeText(getActivity(),"Invalid user",Toast.LENGTH_SHORT).show();
        }
    }

}

