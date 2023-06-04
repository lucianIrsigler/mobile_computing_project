package com.example.signupvalidation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignUpAccountDetails extends AppCompatActivity
{
    TextInputLayout textInputLayout_Username;
    TextInputLayout textInputLayout_Password;
    TextInputLayout textInputLayout_Email;
    TextInputEditText textInputEditText_Username;
    TextInputEditText textInputEditText_Password;
    TextInputEditText textInputEditText_Email;
    Button btnRegister;
    Button btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_account_details);

        textInputLayout_Username = findViewById(R.id.TextInputLayout_Username);
        textInputLayout_Password = findViewById(R.id.TextInputLayout_Password);
        textInputLayout_Email = findViewById(R.id.TextInputLayout_Email);
        textInputEditText_Username = findViewById(R.id.TextInputEditText_Username);
        textInputEditText_Password = findViewById(R.id.TextInputEditText_Password);
        textInputEditText_Email = findViewById(R.id.TextInputEditText_Email);
        btnRegister = findViewById(R.id.btnRegister);
        btnReturn = findViewById(R.id.btnReturn);

        btnReturn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpAccountDetails.this, SignUpPersonalDetails.class);
            intent.putExtra("sourceActivity", "AccountDetails");
            startActivity(intent);
        });

        HTTPHandler httpHandler = new HTTPHandler();
        JSONObject params = new JSONObject();

        btnRegister.setOnClickListener(view -> {
            //check if all fields have errors enabled
            //if no, then register user
            //if yes, then display error message
            textInputLayout_Password.setErrorEnabled(false);
            if(textInputLayout_Username.isErrorEnabled() || textInputLayout_Password.isErrorEnabled() || textInputLayout_Email.isErrorEnabled()){
                Toast.makeText(SignUpAccountDetails.this, "Ensure all fields are filled correctly", Toast.LENGTH_LONG).show();
            }
            else{
                Intent j = getIntent();
                String fname = j.getStringExtra("fname");
                String lname = j.getStringExtra("lname");
                String phone = j.getStringExtra("phone");
                String dob = j.getStringExtra("dob");
                String username = Objects.requireNonNull(textInputEditText_Username.getText()).toString();
                String password = Objects.requireNonNull(textInputEditText_Password.getText()).toString();
                String email = Objects.requireNonNull(textInputEditText_Email.getText()).toString();
                String salt = bytesToHex(generateSalt());

                try{
                    params.put("firstname", fname);
                    params.put("lastname", lname);
                    params.put("phone", phone);
                    params.put("dateOfBirth", dob);
                    params.put("username", username);
                    params.put("password", bytesToHex(hashPassword(combineSaltAndPassword(salt.getBytes(), password.getBytes()))));
                    params.put("email", email);
                    params.put("salt", salt);

                    AtomicBoolean flag = new AtomicBoolean(false);
                    while(!flag.get())
                    {
                        String response = httpHandler.postRequest("https://lamp.ms.wits.ac.za/home/s2571291/users/insert_users/insertUser.php", params, String.class);
                        Toast.makeText(SignUpAccountDetails.this, response, Toast.LENGTH_LONG).show();

                        if(response.equals("Username already exists")){
                            textInputLayout_Username.setErrorEnabled(true);
                            textInputLayout_Username.setError("Username already exists");
                            flag.set(true);
                        }
                        else if(response.equals("Salt already exists")){
                            params.put("salt", bytesToHex(generateSalt()));
                            flag.set(false);
                        }
                        else{
                            Intent intent = new Intent(SignUpAccountDetails.this, LoginPage.class);
                            intent.putExtra("sourceActivity", "AccountDetails");
                            flag.set(true);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        textInputEditText_Username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String username = Objects.requireNonNull(textInputEditText_Username.getText()).toString();
                if(username.isEmpty()) {
                    textInputLayout_Username.setError("Username cannot be empty");
                }
                else if(!username.matches("^(?!.*\\..*\\.)[\\p{Alnum}.]{1,20}$")) {
                    textInputLayout_Username.setError("Username can only have letters, numbers, and the following special characters: _ - . @");
                }
                else {
                    textInputLayout_Username.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputEditText_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = Objects.requireNonNull(textInputEditText_Password.getText()).toString();

                if(password.isEmpty()) {
                    textInputLayout_Password.setError("Password cannot be empty");
                }
                else if(password.length() < 8) {
                    textInputLayout_Password.setError("Password must be at least 8 characters long");
                }
                //password must contain at least one uppercase letter, one lowercase letter, one number, and one special character
                else if(!password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
                    textInputLayout_Password.setError("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character");
                }
                else {
                    textInputLayout_Password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputEditText_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = Objects.requireNonNull(textInputEditText_Email.getText()).toString();
                if(email.isEmpty()) {
                    textInputLayout_Email.setError("Email cannot be empty");
                }
                else if(!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    textInputLayout_Email.setError("Email must be in the format:");
                }
                else {
                    textInputLayout_Email.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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