package com.example.signupvalidation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class LoginPage extends AppCompatActivity
{
    TextInputLayout textInputLayout_Username;
    TextInputEditText textInputEditText_Username;
    TextInputLayout textInputLayout_Password;
    TextInputEditText textInputEditText_Password;
    TextView lblForgotPassword;
    Button btnLogin;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputLayout_Username = findViewById(R.id.TextInputLayout_Username);
        textInputEditText_Username = findViewById(R.id.TextInputEditText_Username);
        textInputLayout_Password = findViewById(R.id.TextInputLayout_Password);
        textInputEditText_Password = findViewById(R.id.TextInputEditText_Password);
        lblForgotPassword = findViewById(R.id.lblForgotPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        //Make request using login.php with parameters in text fields
        HTTPHandler httpHandler = new HTTPHandler();

        JSONObject params = new JSONObject();

        btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginPage.this, SignUpPersonalDetails.class);
            intent.putExtra("sourceActivity", "LoginPage");
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> {
            try{
                params.put("Username", Objects.requireNonNull(textInputEditText_Username.getText()).toString());

                JSONArray response = httpHandler.getRequest("https://lamp.ms.wits.ac.za/home/s2571291/users/login_users/login.php", params, JSONArray.class);

                if(!response.toString().equals("[]"))
                {
                    String salt = response.getJSONObject(0).getString("Salt");
                    String resp_username = response.getJSONObject(0).getString("Username");
                    String resp_password = response.getJSONObject(0).getString("Password");

                    PasswordHashing p = new PasswordHashing();

                    try{
                        if(textInputEditText_Username.getText().toString().equals(resp_username) && resp_password.equals(p.hashedPassword(salt.getBytes(), Objects.requireNonNull(textInputEditText_Password.getText()).toString()))){
                            Intent intent = new Intent(LoginPage.this, HomePage.class);
                            intent.putExtra("sourceActivity", "LoginPage");
                            finish();
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoginPage.this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch(NoSuchAlgorithmException ex){
                        ex.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(LoginPage.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
            catch(JSONException ex){
                ex.printStackTrace();
            }
        });

        textInputEditText_Username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Objects.requireNonNull(textInputEditText_Username.getText()).toString().isEmpty()){
                    textInputLayout_Username.setError("Please enter your Username");
                }
                else{
                    textInputLayout_Username.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputEditText_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                if(Objects.requireNonNull(textInputEditText_Password.getText()).toString().isEmpty()){
                    textInputLayout_Password.setError("Please enter your Password");
                }
                else{
                    textInputLayout_Password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
