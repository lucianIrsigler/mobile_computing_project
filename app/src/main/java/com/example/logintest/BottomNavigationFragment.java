package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.logintest.databinding.FragmentBottomNavBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationFragment extends Fragment {

    private FragmentBottomNavBinding binding;

    public BottomNavigationFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomNavBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        BottomNavigationView view1 = binding.getRoot().findViewById(R.id.bottomNavView);

        Menu menu = view1.getMenu();

        MenuItem homeNav = menu.findItem(R.id.homeNav);
        MenuItem addProduct = menu.findItem(R.id.addProductNav);
        MenuItem userProfile = menu.findItem(R.id.userProfileNav);

        homeNav.setOnMenuItemClickListener(
                v->{
                    Intent intent = new Intent(getActivity(),HomePage.class);
                    startActivity(intent);
                    return false;
        });

        addProduct.setOnMenuItemClickListener(
                v->{
                    Intent intent = new Intent(getActivity(),prodAddScreen.class);
                    startActivity(intent);
                    return false;
                }
        );

        userProfile.setOnMenuItemClickListener(
                v->{
                    Intent intent = new Intent(getActivity(),UserProfileActivity.class);
                    startActivity(intent);
                    return false;
                });

        return view;
    }
}
