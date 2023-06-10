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
        TextView userName = binding.getRoot().findViewById(R.id.tvWelcomeUser);

        //set username
        UsersManager usersManager = new UsersManager();
        JSONObject userInfo = usersManager.getUserInformation();

        try {
            String username = userInfo.getString("username");
            userName.setText(String.format("Welcome %s",username));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //recycler for recommended products
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recommendedProductAdapter = new SearchResultAdapter();
        recommendedProductAdapter.setManager(manager);
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
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handler.removeCallbacks(runnable);
                runnable = () -> {
                    if (!isHidden){
                        userName.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        recommendedRecyclerView.setVisibility(View.GONE);
                        recommendedText.setVisibility(View.GONE);
                        searchRecyclerView.setVisibility(View.VISIBLE);
                        utility.replaceFragment(manager, R.id.container1,new EmptyFragment(),"empty");
                    }
                    searchResults = productManager.searchProduct(query);
                    searchResultAdapter.setProducts(searchResults);
                };

                handler.postDelayed(runnable, 200);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handler.removeCallbacks(runnable);
                runnable = () -> {
                    if (!isHidden){
                        userName.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        recommendedRecyclerView.setVisibility(View.GONE);
                        recommendedText.setVisibility(View.GONE);
                        searchRecyclerView.setVisibility(View.VISIBLE);
                        utility.replaceFragment(manager, R.id.container1,new EmptyFragment(),"empty");
                    }

                    searchResults = productManager.searchProduct(newText);
                    searchResultAdapter.setProducts(searchResults);
                };

                handler.postDelayed(runnable, 200);
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
        TextView userName = binding.getRoot().findViewById(R.id.tvWelcomeUser);

        imageView.setVisibility(View.VISIBLE);
        recommendedRecyclerView.setVisibility(View.VISIBLE);
        recommendedText.setVisibility(View.VISIBLE);
        userName.setVisibility(View.VISIBLE);
        searchRecyclerView.setVisibility(View.GONE);

        utility.replaceFragment(manager, R.id.container1
                ,new BottomNavigationFragment(manager)
                ,"bottomNavigation");

        searchResults.clear();
        searchResultAdapter.setProducts(searchResults);
    }

    private List<Product> getRecommendedProducts() {
        List<Product> randomItems = new ArrayList<>();

        try {
            JSONObject params = new JSONObject();
            String randomItemsUrl =
                    "https://lamp.ms.wits.ac.za/home/s2621933/php/selectRandomProducts.php";

            HTTPHandler httpHandler = new HTTPHandler();
            String response = httpHandler.getRequest(randomItemsUrl, params, String.class);
            // Parse the response and populate the randomItems list
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < 2; i++) {
                JSONObject jsonProduct = jsonArray.getJSONObject(i);
                String name = jsonProduct.getString("productName");
                String description = jsonProduct.getString("productDescription");
                double price = jsonProduct.getDouble("price");
                int productID= Integer.parseInt(jsonProduct.getString("productID"));
                Product product = new Product(name, description, price, productID);
                randomItems.add(product);
            }
        } catch (JSONException e) {
            Log.e("getRandomItems", e.getMessage());
        }

        return randomItems;

    }
}

