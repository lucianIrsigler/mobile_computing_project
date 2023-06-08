package com.example.logintest;

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

public class BottomNavigationFragment extends Fragment {

    private final FragmentManager manager;
    private FragmentBottomNavBinding binding;


    public BottomNavigationFragment(FragmentManager manager){
        this.manager = manager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomNavBinding.inflate(inflater, container, false);
        BottomNavigationView botNav = binding.getRoot().findViewById(R.id.bottomNavView);

        botNav.setOnItemSelectedListener(item->{
            int id = item.getItemId();

            if (id==R.id.homeNav){
                utility.replaceFragment(manager,R.id.container, new HomeFragment(manager), "home");
            }else if(id==R.id.addProductNav){
                utility.replaceFragment(manager,R.id.container, new AddProductFragment(), "addProduct");
            }else{
                //R.id.userProfileNav
                utility.replaceFragment(manager,R.id.container, new UserProfileFragment(), "userProfile");
            }
            return false;
        });

        return binding.getRoot();
    }


}
