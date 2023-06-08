package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.logintest.databinding.EmptyLayoutBinding;


public class HomePage extends AppCompatActivity {

    EmptyLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        Fragment botNavFragment = manager.findFragmentById(R.id.container1);

        if (botNavFragment instanceof EmptyFragment) {
            replaceFragment(R.id.container,new HomeFragment(manager),"home");
            replaceFragment(R.id.container1,new BottomNavigationFragment(manager),"bottomNav");
        }else if (botNavFragment instanceof BottomNavigationFragment){
            //app goes to home tab before showing log out alert
            Fragment mainFragment = manager.findFragmentById(R.id.container);
            if (mainFragment instanceof HomeFragment) {
                showAlert();
            }else{
                replaceFragment(R.id.container,new HomeFragment(manager),"home");
            }
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
            //todo reset user id
            SharedPreferencesManager.initialize(this);
            SharedPreferencesManager.resetUserID();
            Intent intent = new Intent(HomePage.this,AccountActivity.class);
            intent.putExtra("action", "login");
            startActivity(intent);
        });

        Button buttonNo = dialogView.findViewById(R.id.btnNo);
        buttonNo.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void replaceFragment(int id, Fragment fragment,String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id,fragment,tag);
        fragmentTransaction.commit();
    }

}
