package com.example.logintest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity
{
    EditText editUsername;
    EditText editPassword;

    Button btnLogin;

    TextView lblForgotPassword;

    TextView signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);


        editUsername = findViewById(R.id.edUsername);
        editPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.login_button);
        lblForgotPassword = findViewById(R.id.tvForgotPassword);
        signUpText = findViewById(R.id.signUpText);
        ToggleButton tglPassword = findViewById(R.id.togglePwd);


        //Make request using login.php with parameters in text fields
        HTTPHandler httpHandler = new HTTPHandler();

        JSONObject params = new JSONObject();

        signUpText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpAccountDetails.class);
            intent.putExtra("sourceActivity", "LoginPage");
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> {
            try{
                params.put("Username", Objects.requireNonNull(editUsername.getText()).toString());

                JSONArray response = httpHandler.getRequest("https://lamp.ms.wits.ac.za/home/s2571291/users/login_users/login.php", params, JSONArray.class);

                if(!response.toString().equals("[]"))
                {
                    String salt = response.getJSONObject(0).getString("Salt");
                    String resp_username = response.getJSONObject(0).getString("Username");
                    String resp_password = response.getJSONObject(0).getString("Password");

                    PasswordHashing p = new PasswordHashing();

                    try{
                        if(editUsername.getText().toString().equals(resp_username) && resp_password.equals(p.hashedPassword(salt.getBytes(), Objects.requireNonNull(editPassword.getText()).toString()))){
                            Intent intent = new Intent(LoginActivity.this, HomePage.class);
                            intent.putExtra("sourceActivity", "LoginPage");
                            finish();

                            //todo STORE USER ID AFTER LOGIN
                            SharedPreferencesManager.initialize(this);
                            SharedPreferencesManager.storeUserId(12345);

                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch(NoSuchAlgorithmException ex){
                        ex.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
            catch(JSONException ex){
                ex.printStackTrace();
            }
        });

        editUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Objects.requireNonNull(editUsername.getText()).toString().isEmpty()){
                    editUsername.setError("Please enter your Username");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                if(Objects.requireNonNull(editPassword.getText()).toString().isEmpty()){
                    editPassword.setError("Please enter your Password");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tglPassword.setOnClickListener(view->{
            if (editPassword.getInputType()== InputType.TYPE_CLASS_TEXT){
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else {
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });
    }

    @Override
    public void onBackPressed() {


    }
}
