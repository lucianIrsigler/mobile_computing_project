package com.example.logintest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logintest.databinding.FragmentHomeBinding;

import java.util.List;

public class FragmentHome extends Fragment {
    private FragmentManager manager;
    private FragmentHomeBinding binding;



    public FragmentHome() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.manager = getParentFragmentManager();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //searchView = binding.getRoot().findViewById(R.id.searchBar);
        RecyclerView recommendedRecyclerView = binding.getRoot().findViewById(R.id.recommendedRecyclerView);

        TextView userName = binding.getRoot().findViewById(R.id.tvWelcomeUser);

        //set username

        SharedPreferencesManager.initialize(getContext());
        userName.setText(String.format("Welcome %s",SharedPreferencesManager.retrieveUserName()));

        //recycler for recommended products
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        SearchResultAdapter recommendedProductAdapter = new SearchResultAdapter();
        recommendedProductAdapter.setManager(manager);
        recommendedRecyclerView.setAdapter(recommendedProductAdapter);

        // recommended products products
        SharedPreferencesManager.initialize(getContext());
        List<Product> recommendedProducts = SharedPreferencesManager.getRecommendedProducts();

        recommendedProductAdapter.setProducts(recommendedProducts);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }



}

