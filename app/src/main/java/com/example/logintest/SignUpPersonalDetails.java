package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignUpPersonalDetails extends AppCompatActivity {
    EditText editFirstName;
    EditText editLastName;
    EditText editPhoneNum;
    EditText editDOB;
    Button btnRegister;
    TextView alreadyHaveAccountlbl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register_pt2);

        editFirstName = findViewById(R.id.edFirstName);
        editLastName = findViewById(R.id.edLastName);
        editPhoneNum = findViewById(R.id.edPhoneNum);
        editDOB = findViewById(R.id.edDOB);
        btnRegister = findViewById(R.id.register_button);
        alreadyHaveAccountlbl = findViewById(R.id.tvAlreadyHaveAccount);

        alreadyHaveAccountlbl.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpPersonalDetails.this, LoginActivity.class);
            startActivity(intent);
        });

        HTTPHandler httpHandler = new HTTPHandler();
        JSONObject params = new JSONObject();

        btnRegister.setOnClickListener(view -> {
            String firstName = Objects.requireNonNull(editFirstName.getText().toString());
            String lastName = Objects.requireNonNull(editLastName.getText().toString());
            String phoneNum = Objects.requireNonNull(editPhoneNum.getText().toString());
            String DOB = Objects.requireNonNull(editDOB.getText().toString());
            if (firstName.isEmpty() || lastName.isEmpty()||phoneNum.isEmpty()||DOB.isEmpty()){
                System.out.println("error");
                //TODO show popup
            }else{
                Intent j = getIntent();
                String email = j.getStringExtra("email");
                String username = j.getStringExtra("username");
                String password = j.getStringExtra("password");
                String salt = bytesToHex(generateSalt());
                System.out.println(email);

                try{
                    params.put("firstname",firstName);
                    params.put("lastname",lastName);
                    params.put("phone",phoneNum);
                    params.put("dateOfBirth",DOB);
                    params.put("username",username);
                    params.put("password",password);
                    params.put("email",email);
                    params.put("salt",salt);
                }catch (Exception e){
                    System.out.println("Error");
                    //todo proper error handling
                }

                AtomicBoolean flag = new AtomicBoolean(false);
                while(!flag.get())
                {
                    String response = httpHandler.postRequest("https://lamp.ms.wits.ac.za/home/s2571291/users/insert_users/insertUser.php", params, String.class);
                    Toast.makeText(SignUpPersonalDetails.this, response, Toast.LENGTH_LONG).show();

                    if(response.equals("Username already exists")){
                        editFirstName.setError("Username already exists");
                        flag.set(true);
                    }
                    else if(response.equals("Salt already exists")){
                        try {
                            params.put("salt", bytesToHex(generateSalt()));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                            //todo proper error handling
                        }
                        flag.set(false);
                    }
                    else{
                        Intent intent = new Intent(SignUpPersonalDetails.this, LoginActivity.class);
                        intent.putExtra("sourceActivity", "AccountDetails");
                        flag.set(true);
                        startActivity(intent);
                        finish();
                    }
                }
            }
            });
    }


    //generating a random salt and hashing the password
    @NonNull
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[4];
        random.nextBytes(salt);
        return salt;
    }

    @NonNull
    private byte[] combineSaltAndPassword(@NonNull byte[] salt, @NonNull byte[] password) {
        byte[] saltedPassword = new byte[salt.length + password.length];
        System.arraycopy(salt, 0, saltedPassword, 0, salt.length);
        System.arraycopy(password, 0, saltedPassword, salt.length, password.length);
        return saltedPassword;
    }

    @NonNull
    private byte[] hashPassword(byte[] saltedPassword) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(saltedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing the password.", e);
        }
    }

    private final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    @Contract(value = "_ -> new", pure = true)
    public @NotNull String bytesToHex(@NonNull byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}

