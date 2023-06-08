package com.example.logintest;

import android.content.Intent;
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

        editEmail = findViewById(R.id.edEmailRegister);
        editUsername = findViewById(R.id.edUsernameRegister);
        editPassword = findViewById(R.id.edPasswordRegister);
        confirmPasswordTv = findViewById(R.id.edPasswordConfirmRegister);
        btnNext = findViewById(R.id.next_button);
        ToggleButton tglPassword = findViewById(R.id.togglePwd);
        ToggleButton tglConfirmPassword = findViewById(R.id.toggleConfirmPwd);
        TextView errorText = findViewById(R.id.tvErrors);


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
                //todo make error beter
                Toast.makeText(SignUpAccountDetails.this, "Make sure all fields are valid", Toast.LENGTH_LONG).show();
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
                    errorText.setText(R.string.email_cannot_be_empty);
                    editEmail.setBackgroundResource(R.drawable.edterr);
                }
                else if(!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    errorText.setText(R.string.invalid_email);
                    editEmail.setBackgroundResource(R.drawable.edterr);
                }else{
                    errorText.setText("");
                    editEmail.setBackgroundResource(R.drawable.edtnormal);
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
                    editUsername.setBackgroundResource(R.drawable.edterr);
                    errorText.setText(R.string.username_cannot_be_empty);
                }
                else if(!username.matches("^(?!.*\\..*\\.)[\\p{Alnum}.]{1,20}$")) {
                    editUsername.setBackgroundResource(R.drawable.edterr);
                    errorText.setText(R.string.username_can_only_have_letters_numbers_and_the_following_special_characters);
                }else{
                    editUsername.setBackgroundResource(R.drawable.edtnormal);
                    errorText.setText("");
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
                    errorText.setText(R.string.password_cannot_be_empty);
                    editPassword.setBackgroundResource(R.drawable.edterr);
                }
                else if(password.length() < 8) {
                    errorText.setText(R.string.password_must_be_at_least_8_characters_long);
                    editPassword.setBackgroundResource(R.drawable.edterr);
                }
                //password must contain at least one uppercase letter, one lowercase letter, one number, and one special character
                else if(!password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
                    errorText.setText(R.string.password_requirements);
                    editPassword.setBackgroundResource(R.drawable.edterr);
                }else{
                    errorText.setText("");
                    editPassword.setBackgroundResource(R.drawable.edtnormal);
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
                    errorText.setText(R.string.password_cannot_be_empty);
                    confirmPasswordTv.setBackgroundResource(R.drawable.edterr);
                }
                else if(!password.equals(editPassword.getText().toString())) {
                    errorText.setText(R.string.passwords_must_match);
                    confirmPasswordTv.setBackgroundResource(R.drawable.edterr);
                }else{
                    errorText.setText("");
                    confirmPasswordTv.setBackgroundResource(R.drawable.edtnormal);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tglPassword.setOnClickListener(view->{
            if (editPassword.getInputType()==InputType.TYPE_CLASS_TEXT){
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else {
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

        tglConfirmPassword.setOnClickListener(view->{
            if (confirmPasswordTv.getInputType()==InputType.TYPE_CLASS_TEXT){
                confirmPasswordTv.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else {
                confirmPasswordTv.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

    }
}
