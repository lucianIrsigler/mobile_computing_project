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

        //ensures that the bottom nav doesnt go up as the keyboard goes up
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                        .replace(R.id.container,new FragmentHome(),
                                FragmentHome.class.getSimpleName())
                        .replace(R.id.container1, new FragmentBottomNavigation(),
                                FragmentBottomNavigation.class.getSimpleName())
                        .replace(R.id.searchbarContainer, new FragmentSearchBar(),
                                FragmentSearchBar.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();
    }


    /**
     * If the back key is pressed, it goes back to login instead of just crashing.
     * There is an alert dialog used here.
     */
    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment mainFragment = manager.findFragmentById(R.id.container);
        Fragment searchBar = manager.findFragmentById(R.id.searchbarContainer);

        if (mainFragment != null) {
            if (mainFragment instanceof FragmentHome) {
                showAlert();
            }else if (mainFragment instanceof FragmentAddProduct
                    || mainFragment instanceof FragmentUserProfile
                    ||mainFragment instanceof FragmentViewProduct
            ){
                manager.beginTransaction()
                        .replace(R.id.container, new FragmentHome(),
                                FragmentHome.class.getSimpleName())
                        .replace(R.id.searchbarContainer,new FragmentSearchBar(),
                                FragmentSearchBar.class.getSimpleName())
                        .replace(R.id.container1,new FragmentBottomNavigation(),
                                FragmentBottomNavigation.class.getSimpleName())
                        .commit();
            }else {
                //covers when we go from FragmentSearchClass back to home
                if (searchBar instanceof FragmentSearchBar){
                    ((FragmentSearchBar) searchBar).searchView.setQuery("",false);
                }

                manager.popBackStackImmediate(mainFragment.getClass().getSimpleName(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
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