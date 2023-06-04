package com.example.logintest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_profile);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        BottomNavigationFragment bottomNavigationFragment = new BottomNavigationFragment();
        fragmentTransaction.replace(R.id.container, bottomNavigationFragment);
        fragmentTransaction.commit();

        //TODO get userid to set things
        SharedPreferencesManager.initialize(this);
        long userId = SharedPreferencesManager.retrieveUserId();

        System.out.println(userId);

    }
}
