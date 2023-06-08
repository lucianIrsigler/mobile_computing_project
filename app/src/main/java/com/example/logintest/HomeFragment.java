package com.example.logintest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.logintest.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentManager manager;
    public androidx.appcompat.widget.SearchView searchView;
    private FragmentHomeBinding binding;

    public HomeFragment(FragmentManager manager) {
        this.manager = manager;
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        searchView = binding.getRoot().findViewById(R.id.searchBar);

        //todo maybe add onclick if java allows it
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Start the SearchResultsActivity and pass the search query
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                manager.beginTransaction()
                        .replace(R.id.container,
                                new ProductSearchIntermediateFragment(manager,newText),
                                "searchIntermediate")
                        .commit();


                replaceFragment(manager, R.id.container1,"empty",new EmptyFragment());

                return false;
            }
        });

        return binding.getRoot();
    }

    private void replaceFragment(FragmentManager manager, int id, String tag, Fragment fragment){
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(id,fragment,tag);
        fragmentTransaction.commit();
    }


}

