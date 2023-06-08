package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.logintest.databinding.EmptyLayoutBinding;

public class AccountActivity extends AppCompatActivity {

    EmptyLayoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EmptyLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String loginOrRegister = intent.getStringExtra("action");

        if (loginOrRegister.equals("login")){
            utility.replaceFragment(getSupportFragmentManager(),
                    R.id.container,new LoginFragment(getSupportFragmentManager()),"login");
        }else{
            //action=register
            utility.replaceFragment(getSupportFragmentManager(),R.id.container,
                    new SignUpAccountDetailsFragment(getSupportFragmentManager()),
                    "signUpAccount");
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment mainFragment = manager.findFragmentById(R.id.container);

        if (mainFragment instanceof SignUpAccountDetailsFragment){
            utility.replaceFragment(getSupportFragmentManager(),
                    R.id.container,new LoginFragment(getSupportFragmentManager()),"login");
        }else if (mainFragment instanceof SignUpPersonalDetailsFragment){
            utility.replaceFragment(getSupportFragmentManager(),R.id.container,
                    new SignUpAccountDetailsFragment(getSupportFragmentManager()),
                    "signUpAccount");
        }
    }

}
