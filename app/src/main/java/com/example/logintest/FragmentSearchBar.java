package com.example.logintest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.logintest.databinding.FragmentSearchBarBinding;

import java.util.List;

public class FragmentSearchBar extends Fragment {
    private FragmentSearchBarBinding binding;
    public androidx.appcompat.widget.SearchView searchView;
    private FragmentManager manager;

    private final ProductManager productManager = new ProductManager();
    private List<Product> searchResults;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBarBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.manager=getParentFragmentManager();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = binding.getRoot().findViewById(R.id.searchBar);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            boolean switched = false;
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchResults = productManager.searchProduct(query);
                FragmentSearchClass test = (FragmentSearchClass) manager.findFragmentById(R.id.container);
                if (test != null){
                    test.setProducts(searchResults);
                }

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    return false;
                }
                if (!switched) {
                    manager.beginTransaction()
                            .replace(R.id.container, new FragmentSearchClass(), "search")
                            .replace(R.id.container1, new EmptyFragment(), "empty")
                            .addToBackStack(FragmentSearchClass.class.getSimpleName())
                            .commit();
                    switched=true;
                }
                return true;
            }
        });
    }

}
