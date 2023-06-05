package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    private RecyclerView searchRecyclerView;
    private SearchResultAdapter searchResultAdapter;
    private ProductManager productManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresults);

        searchRecyclerView = findViewById(R.id.search_recycler_view);
        searchResultAdapter = new SearchResultAdapter();
        searchRecyclerView.setAdapter(searchResultAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the search query from the intent
        String searchQuery = getIntent().getStringExtra("searchQuery");

        // Initialize the ProductManager
        productManager = new ProductManager();

        // Perform the search and update the RecyclerView
        List<Product> searchResults = productManager.searchProduct(searchQuery);
        searchResultAdapter.setProducts(searchResults);
    }
}