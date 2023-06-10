package com.example.logintest;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

//visibility -> 0 (visible) , 1(gone but still space), 2-> (gone and no space)

public class HomeFragment extends Fragment {
    private final FragmentManager manager;
    public androidx.appcompat.widget.SearchView searchView;
    private FragmentHomeBinding binding;

    private RecyclerView recommendedRecyclerView;
    private SearchResultAdapter recommendedProductAdapter;
    private RecyclerView searchRecyclerView;
    private SearchResultAdapter searchResultAdapter;

    private List<Product> searchResults;

    private final ProductManager productManager = new ProductManager();

    //add delay to search
    private Handler handler = new Handler();
    private Runnable runnable;

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
        ImageView imageView = binding.getRoot().findViewById(R.id.imageView);
        TextView recommendedText = binding.getRoot().findViewById(R.id.tvRecommendedHeading);



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

        boolean isHidden = false;

        searchResultAdapter = new SearchResultAdapter();
        searchResultAdapter.setManager(manager);
        searchRecyclerView.setAdapter(searchResultAdapter);
        //searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false));

        //todo maybe add onclick if java allows it
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!isHidden){
                    imageView.setVisibility(View.GONE);
                    recommendedRecyclerView.setVisibility(View.GONE);
                    recommendedText.setVisibility(View.GONE);
                    searchRecyclerView.setVisibility(View.VISIBLE);
                    utility.replaceFragment(manager, R.id.container1,new EmptyFragment(),"empty");
                }

                searchResults = productManager.searchProduct(query);
                searchResultAdapter.setProducts(searchResults);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!isHidden){
                    imageView.setVisibility(View.GONE);
                    recommendedRecyclerView.setVisibility(View.GONE);
                    recommendedText.setVisibility(View.GONE);
                    searchRecyclerView.setVisibility(View.VISIBLE);
                    utility.replaceFragment(manager, R.id.container1,new EmptyFragment(),"empty");
                }
                handler.removeCallbacks(runnable);
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        // Perform your action here
                        searchResults = productManager.searchProduct(newText);
                        searchResultAdapter.setProducts(searchResults);
                    }
                };

                handler.postDelayed(runnable, 500);
                return true;
            }
        });


        return binding.getRoot();
    }

    public void unHide(){
        searchView = binding.getRoot().findViewById(R.id.searchBar);
        recommendedRecyclerView = binding.getRoot().findViewById(R.id.recommendedRecyclerView);
        searchRecyclerView = binding.getRoot().findViewById(R.id.search_recycler_view);
        ImageView imageView = binding.getRoot().findViewById(R.id.imageView);
        TextView recommendedText = binding.getRoot().findViewById(R.id.tvRecommendedHeading);

        imageView.setVisibility(View.VISIBLE);
        recommendedRecyclerView.setVisibility(View.VISIBLE);
        recommendedText.setVisibility(View.VISIBLE);
        searchRecyclerView.setVisibility(View.GONE);

        utility.replaceFragment(manager, R.id.container1
                ,new BottomNavigationFragment(manager)
                ,"bottomNavigation");

        searchResults.clear();
        searchResultAdapter.setProducts(searchResults);
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
                    int productID= Integer.parseInt(jsonProduct.getString("productID"));

                    Product product = new Product(name, description, price, productID);
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

