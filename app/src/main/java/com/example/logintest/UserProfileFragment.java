package com.example.logintest;

import android.os.Bundle;
import android.util.Log;
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
import org.w3c.dom.Text;

import java.util.List;

public class UserProfileFragment extends Fragment {
    private FragmentUserProfileBinding binding;
    final UsersManager usersManager = new UsersManager();
    final ProductManager productManager = new ProductManager();

    TextView textUsername;
    TextView textID;
    TextView textEmail;
    TextView textItems;
    TextView textReviews;

    private FragmentManager manager;

    public UserProfileFragment(FragmentManager manager) {
        this.manager = manager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        textUsername = binding.getRoot().findViewById(R.id.tvUsernameUP);
        textID = binding.getRoot().findViewById(R.id.tvIDUP);
        textEmail = binding.getRoot().findViewById(R.id.tvEmailUP);
        textItems = binding.getRoot().findViewById(R.id.tvItemCount);
        textReviews = binding.getRoot().findViewById(R.id.tvReviewCount);
        RecyclerView productsListed = binding.getRoot().findViewById(R.id.products_rv);
        RecyclerView productsReviewed = binding.getRoot().findViewById(R.id.reviews_rv);

        //listed products (get 20 of the users posted)
        productsListed.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));

        SearchResultAdapter productsListedAdapter = new SearchResultAdapter();
        productsListedAdapter.setManager(manager);
        productsListed.setAdapter(productsListedAdapter);
        List<Product> products= productManager.searchProductUserID(usersManager.getCurrentUserID());

        productsListedAdapter.setProducts(products);


        //listed reviews
        productsReviewed.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));

        SearchResultAdapter productsReviewedAdapter = new SearchResultAdapter();
        productsReviewedAdapter.setManager(manager);
        productsReviewed.setAdapter(productsReviewedAdapter);

        //get products that user left review on
        RatingManager ratingManager = new RatingManager();
        JSONArray productIDS = ratingManager.getUserReviews(usersManager.getCurrentUserID());
        List<Product> productsReviews = productManager.createProductsFromArray(productIDS);

        productsReviewedAdapter.setProducts(productsReviews);


        updateUserProfile();
        return binding.getRoot();
    }

    private void updateUserProfile(){
        //TODO get userid to set things
        SharedPreferencesManager.initialize(getActivity());
        long userId = SharedPreferencesManager.retrieveUserId();
        JSONObject userInfo = usersManager.getUserInformation();

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

