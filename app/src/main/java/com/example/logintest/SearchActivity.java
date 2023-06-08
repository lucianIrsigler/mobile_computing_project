package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBar = findViewById(R.id.searchBar);



        // Set up the OnQueryTextListener
        // TODO commented code below needs to go where OG search bar is as the below will take user from that screen to this searchresults screen
        //TODO replace Intent(SearchActivity.this, SearchResultsActivity.class); with activities needed


        /*
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Start the SearchResultsActivity and pass the search query
                Intent intent = new Intent(SearchActivity.this, SearchResultsActivity.class);
                intent.putExtra("searchQuery", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change (optional)
                return false;
            }
        });*/
    }
}