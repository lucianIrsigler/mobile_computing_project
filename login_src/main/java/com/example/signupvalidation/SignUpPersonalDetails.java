package com.example.signupvalidation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.*;
import java.util.Objects;

public class SignUpPersonalDetails extends AppCompatActivity
{
    //Declaration of Variables
    TextInputLayout textInputLayout_Firstname;
    TextInputLayout textInputLayout_Lastname;
    TextInputLayout textInputLayout_PhoneNumber;
    TextInputLayout textInputLayout_DateOfBirth;
    TextInputEditText textInputEditText_Firstname;
    TextInputEditText textInputEditText_Lastname;
    TextInputEditText textInputEditText_PhoneNumber;
    TextInputEditText textInputEditText_DateOfBirth;
    Button btnNext;
    Button btnBack;
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_personal_details);

        textInputLayout_Firstname = findViewById(R.id.TextInputLayout_Firstname);
        textInputLayout_Lastname = findViewById(R.id.TextInputLayout_Lastname);
        textInputLayout_PhoneNumber = findViewById(R.id.TextInputLayout_Phone);
        textInputLayout_DateOfBirth = findViewById(R.id.TextInputLayout_DateOfBirth);
        textInputEditText_Firstname = findViewById(R.id.TextInputEditText_Firstname);
        textInputEditText_Lastname = findViewById(R.id.TextInputEditText_Lastname);
        textInputEditText_PhoneNumber = findViewById(R.id.TextInputEditText_Phone);
        textInputEditText_DateOfBirth = findViewById(R.id.TextInputEditText_DateOfBirth);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnHelp = findViewById(R.id.btnHelp);

        btnNext.setOnClickListener(view -> {
            //check if all input edit texts have their errors enabled. If no, then proceed to next activity
            if (textInputLayout_Firstname.isErrorEnabled() || textInputLayout_Lastname.isErrorEnabled() || textInputLayout_PhoneNumber.isErrorEnabled() || textInputLayout_DateOfBirth.isErrorEnabled()) {
                Toast.makeText(SignUpPersonalDetails.this, "Please ensure all fields are filled correctly before proceeding", Toast.LENGTH_LONG).show();
            } else {
                String fname = Objects.requireNonNull(textInputEditText_Firstname.getText()).toString();
                String lname = Objects.requireNonNull(textInputEditText_Lastname.getText()).toString();
                String phone = "+27" + Objects.requireNonNull(textInputEditText_PhoneNumber.getText());
                String dob = Objects.requireNonNull(textInputEditText_DateOfBirth.getText()).toString();

                Intent intent = new Intent(SignUpPersonalDetails.this, SignUpAccountDetails.class);
                intent.putExtra("fname", fname);
                intent.putExtra("lname", lname);
                intent.putExtra("phone", phone);
                intent.putExtra("dob", dob);
                intent.putExtra("sourceActivity", "PersonalDetails");
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpPersonalDetails.this, LoginPage.class);
            intent.putExtra("sourceActivity", "PersonalDetails");
            startActivity(intent);
            finish();
        });

        //Text Changed Listeners
        textInputEditText_Firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                String firstName = Objects.requireNonNull(textInputEditText_Firstname.getText()).toString().trim();

                if (firstName.isEmpty()) {
                    textInputLayout_Firstname.setError("Please enter your first name");
                }
                else if(firstName.length() < 3){
                    textInputLayout_Firstname.setError("First name must be at least 3 characters long");
                }
                else if (!firstName.matches("^[A-Z][A-Za-z\\s'-]*[a-z]$")){
                    textInputLayout_Firstname.setError("First name format is incorrect. See 'help' for more information");
                }
                else {
                    textInputLayout_Firstname.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputEditText_Lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                String lastName = Objects.requireNonNull(textInputEditText_Lastname.getText()).toString();

                if (lastName.isEmpty()) {
                    textInputLayout_Lastname.setError("Please enter your last name");
                }
                else if (lastName.length() < 3){
                    textInputLayout_Lastname.setError("Last name must be at least 3 characters long");
                }
                else if (!lastName.matches("^[A-Z][A-Za-z\\s'-]*[a-z]$")){
                    textInputLayout_Lastname.setError("Last name format is incorrect. See 'help' for more information");
                }
                else {
                    textInputLayout_Lastname.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputEditText_DateOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                //validate date of birth using regex
                //date of birth must be in the format yyyy-mm-dd
                String dobStr = Objects.requireNonNull(textInputEditText_DateOfBirth.getText()).toString();

                if (dobStr.isEmpty()) {
                    textInputLayout_DateOfBirth.setError("Please enter your date of birth");
                }
                else if (!dobStr.matches("^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|1\\d|2[0-8]|3[0-1])$")){
                    textInputLayout_DateOfBirth.setError("Date of birth format is incorrect. See 'help' for more information");
                }
                else{
                    LocalDate today = LocalDate.now();
                    LocalDate dob = LocalDate.parse(dobStr);
                    Period p = Period.between(dob, today);

                    assert p != null;
                    if(dob.isAfter(today)){
                        textInputLayout_DateOfBirth.setError("Date of birth cannot be in the future");
                    }
                    else if (p.getYears() < 18){
                        textInputLayout_DateOfBirth.setError("You must be 18 years or older to register");
                    }
                    else {
                        textInputLayout_DateOfBirth.setErrorEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputEditText_PhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                //validate phone number using regex
                //phone number must be 9 characters long
                String phoneNumber = Objects.requireNonNull(textInputEditText_PhoneNumber.getText()).toString();

                if (phoneNumber.isEmpty()) {
                    textInputLayout_PhoneNumber.setError("Please enter your phone number");
                }
                else if (phoneNumber.length() != 9){
                    textInputLayout_PhoneNumber.setError("Phone number 9 characters long");
                }
                else {
                    textInputLayout_PhoneNumber.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        try{
            String fname = Objects.requireNonNull(textInputEditText_Firstname.getText()).toString();
            String lname = Objects.requireNonNull(textInputEditText_Lastname.getText()).toString();
            String phone = Objects.requireNonNull(textInputEditText_PhoneNumber.getText()).toString();
            String dob = Objects.requireNonNull(textInputEditText_DateOfBirth.getText()).toString();

            boolean empty = fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || dob.isEmpty();

            if(!empty) {
                // Save the data to SharedPreferences (only if all fields are filled in)
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("fname", fname);
                editor.putString("lname", lname);
                editor.putString("phone", phone);
                editor.putString("dob", dob);
                editor.apply();
            }
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            if (getIntent().getStringExtra("sourceActivity").equals("AccountDetails")) {
                // Get the data from SharedPreferences
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                String fname = preferences.getString("fname", "");
                String lname = preferences.getString("lname", "");
                String phone = preferences.getString("phone", "");
                String dob = preferences.getString("dob", "");

                // Set the data to EditTexts
                textInputEditText_Firstname.setText(fname);
                textInputEditText_Lastname.setText(lname);
                textInputEditText_PhoneNumber.setText(phone);
                textInputEditText_DateOfBirth.setText(dob);
            }
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }
}