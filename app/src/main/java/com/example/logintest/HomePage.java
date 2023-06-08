package com.example.logintest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.logintest.databinding.FragmentBottomNavBinding;
import com.example.logintest.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.xmlpull.v1.XmlPullParser;


public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.container, homeFragment);

        BottomNavigationFragment botNavFragment = new BottomNavigationFragment();

        fragmentTransaction.add(R.id.container,botNavFragment);
        fragmentTransaction.commit();


    }

    /**
     * If the back key is pressed, it goes back to login instead of just crashing.
     * There is an alert dialog used here.
     */
    @Override
    public void onBackPressed() {
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
            startActivity(intent);
        });

        Button buttonNo = dialogView.findViewById(R.id.btnNo);
        buttonNo.setOnClickListener(v -> {
            alertDialog.dismiss();
            //todo reset user id
            SharedPreferencesManager.initialize(this);
            SharedPreferencesManager.resetUserID();
        });

        alertDialog.show();
    }
}
