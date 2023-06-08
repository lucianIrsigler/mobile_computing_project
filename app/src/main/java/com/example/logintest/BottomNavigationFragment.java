package com.example.logintest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.logintest.databinding.FragmentBottomNavBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationFragment extends Fragment {

    private FragmentManager manager;
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
                replaceFragment(manager,R.id.container,"home",new HomeFragment(manager));
            }else if(id==R.id.addProductNav){
                replaceFragment(manager,R.id.container,"addProduct",new AddProductFragment());
            }else{
                //R.id.userProfileNav
                replaceFragment(manager,R.id.container,"userProfile",new UserProfileFragment());
            }
            return false;
        });

        return binding.getRoot();
    }

    private void replaceFragment(FragmentManager manager,int id, String tag,Fragment fragment){
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(id,fragment,tag);
        fragmentTransaction.commit();
    }
}
