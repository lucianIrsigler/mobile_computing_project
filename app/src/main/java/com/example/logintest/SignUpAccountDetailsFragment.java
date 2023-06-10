package com.example.logintest;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.logintest.databinding.FragmentRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SignUpAccountDetailsFragment extends Fragment {

    private FragmentRegisterBinding binding;
    public EditText editEmail;
    public EditText editUsername;
    public EditText editPassword;
    EditText confirmPasswordTv;
    Button btnNext;
    TextView alreadyHaveAccountlbl;

    private FragmentManager manager;

    public SignUpAccountDetailsFragment(FragmentManager manager){
        this.manager = manager;
    }

    public SignUpAccountDetailsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editEmail = binding.getRoot().findViewById(R.id.edEmailRegister);
        editUsername = binding.getRoot().findViewById(R.id.edUsernameRegister);
        editPassword = binding.getRoot().findViewById(R.id.edPasswordRegister);
        confirmPasswordTv = binding.getRoot().findViewById(R.id.edPasswordConfirmRegister);
        btnNext = binding.getRoot().findViewById(R.id.next_button);
        ToggleButton tglPassword = binding.getRoot().findViewById(R.id.togglePwd);
        ToggleButton tglConfirmPassword = binding.getRoot().findViewById(R.id.toggleConfirmPwd);
        TextView errorText = binding.getRoot().findViewById(R.id.tvErrors);

        alreadyHaveAccountlbl = binding.getRoot().findViewById(R.id.tvLogin);

        alreadyHaveAccountlbl.setOnClickListener( view1-> utility.replaceFragment(manager,R.id.container,new LoginFragment(manager),"login")
        );

        btnNext.setOnClickListener(view1 -> {
            //check if all input edit texts have their errors enabled. If no, then proceed to next activity
            String email = Objects.requireNonNull(editEmail.getText()).toString();
            String username = Objects.requireNonNull(editUsername.getText()).toString();
            String password = Objects.requireNonNull(editPassword.getText()).toString();
            String confirmPassword = Objects.requireNonNull(confirmPasswordTv.getText()).toString();

            UsersManager usersManager = new UsersManager();

            JSONObject isValid =  usersManager.checkUsername(username);

            try {
                if (isValid.getInt("valid")==0){
                    String error = "Username already taken";
                    errorText.setText(error);
                    editUsername.setBackgroundResource(R.drawable.edterr);
                    return;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


            if (!password.equals(confirmPassword)
                    || email.isEmpty()
                    || username.isEmpty()
                    || password.isEmpty()
                    || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
            ){
                Toast.makeText(getActivity(), "Make sure all fields are valid", Toast.LENGTH_LONG).show();
            }else{
                SignUpPersonalDetailsFragment fragment = new SignUpPersonalDetailsFragment(manager);
                fragment.setArguments(email,username,password);
                utility.replaceFragment(manager,R.id.container,fragment,"signupPersonal");
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
                    errorText.setText("Email cannot be empty");
                    editEmail.setBackgroundResource(R.drawable.edterr);
                }
                else if(!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    errorText.setText("Invalid email");
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
                    errorText.setText("Username cannot be empty");
                }
                else if(!username.matches("^(?!.*\\..*\\.)[\\p{Alnum}.]{1,20}$")) {
                    editUsername.setBackgroundResource(R.drawable.edterr);
                    errorText.setText("Username can only have letters, numbers, and the following special characters: _ - . @");
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
                    errorText.setText("Password cannot be empty");
                    editPassword.setBackgroundResource(R.drawable.edterr);
                }
                else if(password.length() < 8) {
                    errorText.setText("Password must be at least 8 characters long");
                    editPassword.setBackgroundResource(R.drawable.edterr);
                }
                //password must contain at least one uppercase letter, one lowercase letter, one number, and one special character
                else if(!password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
                    errorText.setText("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character");
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
                    errorText.setText("Password cannot be empty");
                    confirmPasswordTv.setBackgroundResource(R.drawable.edterr);
                }
                else if(!password.equals(editPassword.getText().toString())) {
                    errorText.setText("Passwords must match");
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

        tglPassword.setOnClickListener(view1->{
            if (editPassword.getInputType()== InputType.TYPE_CLASS_TEXT){
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else {
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

        tglConfirmPassword.setOnClickListener(view1->{
            if (confirmPasswordTv.getInputType()==InputType.TYPE_CLASS_TEXT){
                confirmPasswordTv.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else {
                confirmPasswordTv.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

    }

}

