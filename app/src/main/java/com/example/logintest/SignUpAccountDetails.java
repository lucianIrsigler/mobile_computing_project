package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

public class SignUpAccountDetails extends AppCompatActivity {
    EditText editEmail;
    EditText editUsername;
    EditText editPassword;
    EditText confirmPasswordTv;
    Button btnNext;
    TextView alreadyHaveAccountlbl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);

        editEmail = findViewById(R.id.edEmailRegister);;
        editUsername = findViewById(R.id.edUsernameRegister);
        editPassword = findViewById(R.id.edPasswordRegister);
        confirmPasswordTv = findViewById(R.id.edPasswordConfirmRegister);
        btnNext = findViewById(R.id.next_button);
        alreadyHaveAccountlbl = findViewById(R.id.tvLogin);

        alreadyHaveAccountlbl.setOnClickListener( view->{
            Intent intent = new Intent(SignUpAccountDetails.this, LoginActivity.class);
            startActivity(intent);
        }
        );

        btnNext.setOnClickListener(view -> {
            //check if all input edit texts have their errors enabled. If no, then proceed to next activity
                String email = Objects.requireNonNull(editEmail.getText()).toString();
                String username = Objects.requireNonNull(editUsername.getText()).toString();
                String password = Objects.requireNonNull(editPassword.getText()).toString();
                String confirmPassword = Objects.requireNonNull(confirmPasswordTv.getText()).toString();


                if (!password.equals(confirmPassword)
                        || email.isEmpty()
                        || username.isEmpty()
                        || password.isEmpty()
                        || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
                ){
                    System.out.println("error");
                    //TODO show popup
                }else{
                    Intent intent = new Intent(SignUpAccountDetails.this, SignUpPersonalDetails.class);
                    intent.putExtra("email", email);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("sourceActivity", "PersonalDetails");
                    startActivity(intent);
                }
            });

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = Objects.requireNonNull(editEmail.getText()).toString();
                if(email.isEmpty()) {
                    editEmail.setError("Email cannot be empty");
                }
                else if(!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    editEmail.setError("Invalid email");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String username = Objects.requireNonNull(editUsername.getText()).toString();
                if(username.isEmpty()) {
                    editUsername.setError("Username cannot be empty");
                }
                else if(!username.matches("^(?!.*\\..*\\.)[\\p{Alnum}.]{1,20}$")) {
                    editUsername.setError("Username can only have letters, numbers, and the following special characters: _ - . @");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = Objects.requireNonNull(editPassword.getText()).toString();

                if(password.isEmpty()) {
                    editPassword.setError("Password cannot be empty");
                }
                else if(password.length() < 8) {
                    editPassword.setError("Password must be at least 8 characters long");
                }
                //password must contain at least one uppercase letter, one lowercase letter, one number, and one special character
                else if(!password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
                    editPassword.setError("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        confirmPasswordTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = Objects.requireNonNull(confirmPasswordTv.getText()).toString();

                if(password.isEmpty()) {
                    confirmPasswordTv.setError("Password cannot be empty");
                }
                else if(!password.equals(editPassword.getText().toString())) {
                    confirmPasswordTv.setError("Passwords must match");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}
