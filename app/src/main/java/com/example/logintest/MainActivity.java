package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    //Declaration of variables
    //seeing if this works
    TextView txtUN;
    TextView txtPass;
    //yes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_splash_screen);

        Handler handler = new Handler();

        handler.postDelayed(this::switchToLogin,2000);
    }

    private void switchToLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

        /*txtUN = findViewById(R.id.txtUN);
        txtPass = findViewById(R.id.txtPass);

        Button btnLogin = findViewById(R.id.btnLogin);

        //Make request using login.php with parameters in text fields
        PHPRequest phpReq = new PHPRequest("https://lamp.ms.wits.ac.za/home/s2571291/");

        ContentValues cv = new ContentValues();
        cv.put("Username", txtUN.getText().toString());

        btnLogin.setOnClickListener(view -> phpReq.doRequest(MainActivity.this, "login", cv,
                response -> {
                    if(response.length() >= 10)
                    {
                        String salt = processJSON(response, "Salt");
                        String resp_username = processJSON(response, "Username");
                        String resp_password = processJSON(response, "Password");

                        PasswordHashing p = new PasswordHashing();

                        try{
                            if(txtUN.getText().toString().equals(resp_username) && resp_password.equals(p.hashedPassword(salt.getBytes(), txtPass.getText().toString()))){
                                Toast.makeText(MainActivity.this, "Correct details", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Wrong details", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(NoSuchAlgorithmException ex){
                            Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "No details exist", Toast.LENGTH_SHORT).show();
                    }

                })
            );
    }
    */
}
