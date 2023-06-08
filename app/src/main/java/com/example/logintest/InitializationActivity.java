package com.example.logintest;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class InitializationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_activity);
        replaceFragment(R.id.container,new SplashScreenFragment());
        Handler handler = new Handler();
        handler.postDelayed(this::switchToAccountOptions,2500);
    }

    private void replaceFragment(int id, Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id,fragment);
        fragmentTransaction.commit();
    }

    private void switchToAccountOptions(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,new AccountOptionsFragment());
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, 0, 0, 0);
        fragmentTransaction.commit();
    }

}
