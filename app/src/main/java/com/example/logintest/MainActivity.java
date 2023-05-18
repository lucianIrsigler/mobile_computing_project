package com.example.logintest;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.btnLogin);

        //Make request using login.php with parameters in text fields
        PHPRequest phpReq = new PHPRequest("https://lamp.ms.wits.ac.za/home/s2571291/");

        ContentValues cv = new ContentValues();
        cv.put("Username", txtUN.getText().toString());

        btnLogin.setOnClickListener(view -> phpReq.doRequest(MainActivity.this, "cars", cv,
                response -> {
                    if(!response.equals("[]"))
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
                                Toast.makeText(MainActivity.this, "It's chaai mchana", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(NoSuchAlgorithmException ex){
                            Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                })
            );
    }

    public String processJSON(String json, String key)
    {
        String KeyValue = "";
        try {
            JSONArray jArr = new JSONArray(json);
            JSONObject item = jArr.getJSONObject(0);
            KeyValue = item.getString(key);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return KeyValue;
    }

    //Declaration of variables
    TextView txtUN = findViewById(R.id.txtUN);
    TextView txtPass = findViewById(R.id.txtPass);
}