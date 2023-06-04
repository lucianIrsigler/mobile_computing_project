package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.logintest.databinding.FragmentBottomNavBinding;
import com.example.logintest.databinding.FragmentHomeBinding;

public class BottomNavigationFragment extends Fragment {

    private FragmentBottomNavBinding binding;

    public BottomNavigationFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomNavBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
}
