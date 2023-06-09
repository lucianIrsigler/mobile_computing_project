package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import com.example.logintest.databinding.FragmentLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    EditText editUsername;
    EditText editPassword;
    Button btnLogin;
    TextView lblForgotPassword;
    TextView signUpText;

    FragmentManager manager;
    public LoginFragment(FragmentManager manager){
        this.manager=manager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editUsername = binding.getRoot().findViewById(R.id.edUsername);
        editPassword = binding.getRoot().findViewById(R.id.edPassword);
        btnLogin = binding.getRoot().findViewById(R.id.login_button);
        lblForgotPassword = binding.getRoot().findViewById(R.id.tvForgotPassword);
        signUpText = binding.getRoot().findViewById(R.id.signUpText);
        ToggleButton tglPassword = binding.getRoot().findViewById(R.id.togglePwd);

        //Make request using login.php with parameters in text fields
        HTTPHandler httpHandler = new HTTPHandler();

        JSONObject params = new JSONObject();

        signUpText.setOnClickListener(view1 -> utility.replaceFragment(manager,R.id.container,
                new SignUpAccountDetailsFragment(manager),"signUpAccount"));

        btnLogin.setOnClickListener(view1 -> {
            try{
                params.put("Username", Objects.requireNonNull(editUsername.getText()).toString());

                JSONArray response = httpHandler.getRequest("https://lamp.ms.wits.ac.za/home/s2621933/php/login.php", params, JSONArray.class);

                if(!response.toString().equals("[]"))
                {
                    String salt = response.getJSONObject(0).getString("Salt");
                    String resp_username = response.getJSONObject(0).getString("Username");
                    String resp_password = response.getJSONObject(0).getString("Password");

                    PasswordHashing p = new PasswordHashing();

                    try{
                        if(editUsername.getText().toString().equals(resp_username) && resp_password.equals(p.hashedPassword(salt.getBytes(), Objects.requireNonNull(editPassword.getText()).toString()))){
                            Intent intent = new Intent(getActivity(), HomePage.class);
                            intent.putExtra("sourceActivity", "LoginPage");
                            startActivity(intent);

                            JSONObject params1 = new JSONObject();
                            params1.put("username",resp_username);

                            String response1 =
                                    httpHandler.getRequest("https://lamp.ms.wits.ac.za/home/s2621933/php/getUserID.php",
                                            params1,String.class
                                    );

                            SharedPreferencesManager.initialize(getActivity());
                            SharedPreferencesManager.storeUserId(Integer.parseInt(response1));

                        }
                        else{
                            Toast.makeText(getActivity(), "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch(NoSuchAlgorithmException ex){
                        ex.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
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

        tglPassword.setOnClickListener(view1->{
            if (editPassword.getInputType()== InputType.TYPE_CLASS_TEXT){
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else {
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });
    }


}
