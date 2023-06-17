package com.example.logintest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logintest.databinding.FragmentSearchBinding;

import java.util.List;

public class FragmentSearchClass extends Fragment {

    private FragmentSearchBinding binding;
    private RecyclerView searchRecyclerView;
    private final SearchResultAdapter searchResultAdapter = new SearchResultAdapter();

    private FragmentManager manager;

    public FragmentSearchClass(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater,container,false);
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
    }

    public void setProducts(List<Product> results){
        searchRecyclerView = binding.getRoot().findViewById(R.id.search_recycler_view);
        searchResultAdapter.setManager(manager);
        searchRecyclerView.setAdapter(searchResultAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));
        searchResultAdapter.setProducts(results);
    }
}
