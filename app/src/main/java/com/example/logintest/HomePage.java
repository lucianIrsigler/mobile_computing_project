package com.example.logintest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.logintest.databinding.EmptyLayoutBinding;
import com.example.logintest.databinding.FragmentBottomNavBinding;
import com.example.logintest.databinding.FragmentHomeBinding;
import com.example.logintest.databinding.HomePageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;
import java.util.Objects;


public class HomePage extends AppCompatActivity {

    EmptyLayoutBinding binding;
    final ProductManager productManager = new ProductManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(productManager.searchProduct("p"));
        binding = EmptyLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                        .replace(R.id.container,new HomeFragment(fragmentManager),"home")
                        .replace(R.id.container1, new BottomNavigationFragment(fragmentManager),
                                "bottomNav")
                        .commit();

        fragmentManager.executePendingTransactions();
    }




    /**
     * If the back key is pressed, it goes back to login instead of just crashing.
     * There is an alert dialog used here.
     */
    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.container1);

        if (fragment instanceof EmptyFragment) {
            replaceFragment(R.id.container,"home",new HomeFragment(manager));
            replaceFragment(R.id.container1,"bottomNav",new BottomNavigationFragment(manager));
        }else if (fragment instanceof BottomNavigationFragment){
            showAlert();
        }
    }

    public void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(dialogView);

        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        titleTextView.setText("Logout");

        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
        messageTextView.setText("Are you sure you want to log out?");

        final AlertDialog alertDialog = builder.create();

        Button buttonYes = dialogView.findViewById(R.id.btnYes);
        buttonYes.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this,LoginActivity.class);
            //todo reset user id
            SharedPreferencesManager.initialize(this);
            SharedPreferencesManager.resetUserID();
            startActivity(intent);
        });

        Button buttonNo = dialogView.findViewById(R.id.btnNo);
        buttonNo.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void replaceFragment(int id, String tag,Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id,fragment,tag);
        fragmentTransaction.commit();
    }

}
