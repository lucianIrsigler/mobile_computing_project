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

import com.example.logintest.databinding.FragmentBottomNavBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentBottomNavigation extends Fragment {

    private FragmentManager manager;
    private FragmentBottomNavBinding binding;


    public FragmentBottomNavigation(){}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.manager = getParentFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomNavBinding.inflate(inflater, container, false);
        BottomNavigationView botNav = binding.getRoot().findViewById(R.id.bottomNavView);

        botNav.setOnItemSelectedListener(item->{
            int id = item.getItemId();
            if (id==R.id.homeNav){
                manager.beginTransaction()
                        .replace(R.id.container,new FragmentHome(),"home")
                        .replace(R.id.searchbarContainer,new FragmentSearchBar(),"searchBar")
                        .commit();
            }else if(id==R.id.addProductNav){
                manager.beginTransaction()
                        .replace(R.id.container,new FragmentAddProduct(),"addProduct")
                        .replace(R.id.searchbarContainer,new EmptyFragment(),"empty")
                        .commit();
            }else{
                //R.id.userProfileNav
                manager.beginTransaction()
                        .replace(R.id.container,new FragmentUserProfile(),"userProfile")
                        .replace(R.id.searchbarContainer,new EmptyFragment(),"empty")
                        .commit();
            }
            return false;
        });

        return binding.getRoot();
    }


}
