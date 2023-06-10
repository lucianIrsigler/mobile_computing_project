package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.logintest.databinding.EmptyLayoutBinding;


public class HomePage extends AppCompatActivity {

    EmptyLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EmptyLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

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
        Fragment mainFragment = manager.findFragmentById(R.id.container);

        if (botNavFragment instanceof EmptyFragment ) {
            //in search screen goes back to main
            if (mainFragment instanceof HomeFragment) {
                ((HomeFragment) mainFragment).unHide();
            }else if (mainFragment instanceof FragmentViewProduct){
                utility.replaceFragment(manager,R.id.container,new HomeFragment(manager),"home");
                utility.replaceFragment(manager,R.id.container1,
                        new BottomNavigationFragment(manager),"bottomNavigation");
            }else if (mainFragment instanceof FragmentAddress){
                Product product = ((FragmentAddress) mainFragment).product;
                utility.replaceFragment(manager,R.id.container,
                        new FragmentViewProduct(product,manager),"viewProduct");
            }else if (mainFragment instanceof PaymentFragment){
                Product product = ((PaymentFragment) mainFragment).product;
                utility.replaceFragment(manager,R.id.container,
                        new FragmentViewProduct(product,manager),"viewProduct");
            }

        }else if (botNavFragment instanceof BottomNavigationFragment){
            //app goes to home tab before showing log out alert
            if (mainFragment instanceof HomeFragment) {
                showAlert();
            }else{
                utility.replaceFragment(getSupportFragmentManager(),
                        R.id.container,new HomeFragment(manager),"home");
            }
        }
    }

    public void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(dialogView);

        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        String title = "Logout";
        titleTextView.setText(title);

        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
        String message = "Are you sure you want to log out?";
        messageTextView.setText(message);

        final AlertDialog alertDialog = builder.create();

        Button buttonYes = dialogView.findViewById(R.id.btnYes);
        buttonYes.setOnClickListener(v -> {
            SharedPreferencesManager.initialize(this);
            SharedPreferencesManager.resetUserID();
            Intent intent = new Intent(HomePage.this,AccountActivity.class);
            intent.putExtra("action", "login");
            startActivity(intent);
        });
        Button buttonNo = dialogView.findViewById(R.id.btnNo);
        buttonNo.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }
}