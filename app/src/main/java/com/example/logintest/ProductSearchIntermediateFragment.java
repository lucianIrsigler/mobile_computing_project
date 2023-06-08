package com.example.logintest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logintest.databinding.ProductSearchIntermediateBinding;

import java.util.List;

public class ProductSearchIntermediateFragment extends Fragment {
    private FragmentManager manager;
    public androidx.appcompat.widget.SearchView searchView;
    private ProductSearchIntermediateBinding binding;

    public String query;

    public ProductSearchIntermediateFragment(){}

    public ProductSearchIntermediateFragment(FragmentManager manager,String query) {
        this.manager = manager;
        this.query=query;
        // Required empty public constructor
    }


    public void setQuery(String query) {
        this.query = query;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProductSearchIntermediateBinding.inflate(inflater, container, false);
        searchView = binding.getRoot().findViewById(R.id.searchBar);

        //todo make it look like we didnt steal this
        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setText(this.query);

        //todo keyboard disappears so need to fix that too

        //todo maybe add onclick if java allows it
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RecyclerView searchRecyclerView = binding.getRoot().findViewById(R.id.search_recycler_view);
                SearchResultAdapter searchResultAdapter = new SearchResultAdapter();
                searchRecyclerView.setAdapter(searchResultAdapter);
                searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                // Initialize the ProductManager
                ProductManager productManager = new ProductManager();

                // Perform the search and update the RecyclerView
                List<Product> searchResults = productManager.searchProduct(query);

                Log.i("test",Integer.toString(searchResults.toArray().length));
                searchResultAdapter.setProducts(searchResults);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RecyclerView searchRecyclerView = binding.getRoot().findViewById(R.id.search_recycler_view);
                SearchResultAdapter searchResultAdapter = new SearchResultAdapter();
                searchRecyclerView.setAdapter(searchResultAdapter);
                searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                // Initialize the ProductManager
                ProductManager productManager = new ProductManager();

                // Perform the search and update the RecyclerView
                List<Product> searchResults = productManager.searchProduct(newText);
                searchResultAdapter.setProducts(searchResults);

                return true;
            }
        });

        return binding.getRoot();
    }
}
