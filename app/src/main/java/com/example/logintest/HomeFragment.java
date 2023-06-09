package com.example.logintest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logintest.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private final FragmentManager manager;
    public androidx.appcompat.widget.SearchView searchView;
    private FragmentHomeBinding binding;

    private RecyclerView recommendedRecyclerView;
    private SearchResultAdapter recommendedProductAdapter;
    private RecyclerView searchRecyclerView;
    private SearchResultAdapter searchResultAdapter;

    public HomeFragment(FragmentManager manager) {
        this.manager = manager;
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        searchView = binding.getRoot().findViewById(R.id.searchBar);
        recommendedRecyclerView = binding.getRoot().findViewById(R.id.recommendedRecyclerView);
        searchRecyclerView = binding.getRoot().findViewById(R.id.search_recycler_view);

        //recycler for recommended products
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recommendedProductAdapter = new SearchResultAdapter();
        recommendedRecyclerView.setAdapter(recommendedProductAdapter);

        // reco products
        List<Product> recommendedProducts = getRecommendedProducts();
        recommendedProductAdapter.setProducts(recommendedProducts);
        if (recommendedProducts.isEmpty()) {

            Toast.makeText(getActivity(), "No Recommendations at this time", Toast.LENGTH_LONG).show();
        } else {
            recommendedProductAdapter.setProducts(recommendedProducts);
        }
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


                utility.replaceFragment(manager, R.id.container1,new EmptyFragment(),"empty");

                return false;
            }
        });

        return binding.getRoot();
    }

    private List<Product> getRecommendedProducts() {
        //todo can use FragmentViewProduct here as well once working so recommned prod can be clickable
        List<Product> randomItems = new ArrayList<>();
        int numberOfItems = 2;//doesnt work
        String productName="";
        try {
            JSONObject params = new JSONObject();
            params.put("search", productName);

            String randomItemsUrl = "https://lamp.ms.wits.ac.za/home/s2621933/php/searchproduct.php";

            HTTPHandler httpHandler = new HTTPHandler();
            String response = httpHandler.getRequest(randomItemsUrl, params, String.class);

            // Parse the response and populate the randomItems list
            JSONArray jsonArray = new JSONArray(response);
            int totalItems = jsonArray.length();


            if (totalItems >= numberOfItems) {

                List<Integer> indices = getRandomIndices(totalItems, numberOfItems);

                for (int i = 0; i < indices.size(); i++) {
                    JSONObject jsonProduct = jsonArray.getJSONObject(indices.get(i));
                    String name = jsonProduct.getString("productName");
                    String description = jsonProduct.getString("productDescription");
                    double price = jsonProduct.getDouble("price");

                    Product product = new Product(name, description, price);
                    randomItems.add(product);
                }
            }
        } catch (JSONException e) {
            Log.e("getRandomItems", e.getMessage());
        }

        return randomItems;

    }

    private List<Integer> getRandomIndices(int totalItems, int numberOfItems) {
        List<Integer> indices = new ArrayList<>();
        int count = 0;
        while (count < numberOfItems) {
            int index = (int) (Math.random() * totalItems) + 2;
            if (!indices.contains(index)) {
                indices.add(index);
                count++;
            }
        }
        return indices;
    }


}

