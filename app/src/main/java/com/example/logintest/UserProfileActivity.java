package com.example.logintest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class UserProfileActivity extends AppCompatActivity {

    UsersManager usersManager = new UsersManager();

    TextView textUsername;
    TextView textID;
    TextView textEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_profile);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BottomNavigationFragment bottomNavigationFragment = new BottomNavigationFragment();
        fragmentTransaction.replace(R.id.container, bottomNavigationFragment);
        fragmentTransaction.commit();

        textUsername = findViewById(R.id.tvUsernameUP);
        textID = findViewById(R.id.tvIDUP);
        textEmail = findViewById(R.id.tvEmailUP);
        updateUserProfile();
    }

    private void updateUserProfile(){
        //TODO get userid to set things
        SharedPreferencesManager.initialize(this);
        long userId = SharedPreferencesManager.retrieveUserId();

        JSONObject userInfo = usersManager.getUserInformation();

        try {
            textUsername.setText(userInfo.getString("username"));
            textID.setText(Long.toString(userId));
            textEmail.setText(userInfo.getString("email"));
        }catch (JSONException e){
            Toast.makeText(this,"Invalid user",Toast.LENGTH_SHORT).show();
        }


    }



}
