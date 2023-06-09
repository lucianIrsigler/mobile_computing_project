package com.example.logintest;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.logintest.databinding.FragmentRegisterPt2Binding;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignUpPersonalDetailsFragment extends Fragment {

    private FragmentRegisterPt2Binding binding;
    public String email;
    public String userName;
    public String password;

    FragmentManager manager;

    EditText editFirstName;
    EditText editLastName;
    EditText editPhoneNum;
    EditText editDOB;
    Button btnRegister;
    TextView alreadyHaveAccountlbl;

    EditText errorText;

    String DOBstorage="00-00-0000";

    private DatePickerDialog datePickerDialog;

    public void setArguments(String email,String userName,String password){
        this.email=email;
        this.userName=userName;
        this.password=password;
    }

    public SignUpPersonalDetailsFragment(FragmentManager manager){
        this.manager=manager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterPt2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editFirstName = binding.getRoot().findViewById(R.id.edFirstName);
        editLastName = binding.getRoot().findViewById(R.id.edLastName);
        editPhoneNum = binding.getRoot().findViewById(R.id.edPhoneNum);
        editDOB = binding.getRoot().findViewById(R.id.edDOB);
        errorText = binding.getRoot().findViewById(R.id.tvErrors);
        btnRegister = binding.getRoot().findViewById(R.id.register_button);
        alreadyHaveAccountlbl = binding.getRoot().findViewById(R.id.tvAlreadyHaveAccount);

        initDatePicker();

        alreadyHaveAccountlbl.setOnClickListener(view1 -> utility.replaceFragment(manager,R.id.container,
                new LoginFragment(manager),"login"));

        datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
            LocalDate today = LocalDate.now();

            try {
                LocalDate dob = LocalDate.of(i,i1,i2);

                Period p = Period.between(dob, today);

                assert p != null;
                if(dob.isAfter(today)){
                    errorText.setText(R.string.date_of_birth_cannot_be_in_the_future);
                    editDOB.setBackgroundResource(R.drawable.edterr);
                }
                else if (p.getYears() < 16){
                    errorText.setText(R.string.you_must_be_16_years_or_older_to_register);
                    editDOB.setBackgroundResource(R.drawable.edterr);
                }else{
                    errorText.setText("");
                    editDOB.setBackgroundResource(R.drawable.edtnormal);
                }
            }catch (DateTimeParseException e){
                errorText.setText(R.string.invalid_date);
                editDOB.setBackgroundResource(R.drawable.edterr);
            }
        });

        btnRegister.setOnClickListener(view1 -> {
            String firstName = Objects.requireNonNull(editFirstName.getText().toString());
            String lastName = Objects.requireNonNull(editLastName.getText().toString());
            String phoneNum = Objects.requireNonNull(editPhoneNum.getText().toString());
            String DOB = Objects.requireNonNull(editDOB.getText().toString());

            HTTPHandler httpHandler = new HTTPHandler();
            JSONObject params = new JSONObject();

            if (isInvalidDate()){
                return;
            }

            if (firstName.isEmpty() || lastName.isEmpty()||phoneNum.isEmpty()||DOB.isEmpty()){
                Toast.makeText(getActivity(), "Make sure all fields are filled", Toast.LENGTH_LONG).show();
            }else{
                String salt = bytesToHex(generateSalt());
                System.out.println(email);

                try{
                    params.put("firstname", firstName);
                    params.put("lastname", lastName);
                    params.put("phone", phoneNum);
                    params.put("dateOfBirth", DOB);
                    params.put("username", userName);
                    params.put("password", bytesToHex(hashPassword(combineSaltAndPassword(salt.getBytes(), password.getBytes()))));
                    params.put("email", email);
                    params.put("salt", salt);
                }catch (Exception e){
                    System.out.println("Error");
                    //todo proper error handling
                }

                AtomicBoolean flag = new AtomicBoolean(false);
                while(!flag.get())
                {
                    //String url = "https://lamp.ms.wits.ac.za/home/s2571291/users/insert_users/insertUser.php";
                    String url = "https://lamp.ms.wits.ac.za/home/s2621933/php/insertUser.php";
                    String response = httpHandler.postRequest(url, params, String.class);
                    Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

                    if(response.equals("Salt already exists")){
                        try {
                            params.put("salt", bytesToHex(generateSalt()));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                            //todo proper error handling
                        }
                        flag.set(false);
                    }
                    else{
                        utility.replaceFragment(manager,R.id.container,new LoginFragment(manager),"login");
                        flag.set(true);
                    }
                }
            }
        });

        //Text Changed Listeners
        editFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                String firstName = Objects.requireNonNull(editFirstName.getText()).toString().trim();
                //todo idk the editFirstName and editLastName does not change its background to red
                if (firstName.isEmpty()) {
                    errorText.setText(R.string.please_enter_your_first_name);
                    editFirstName.setBackgroundResource(R.drawable.edterr);
                }
                else if(firstName.length() < 3){
                    errorText.setText(R.string.first_name_must_be_at_least_3_characters_long);
                    editFirstName.setBackgroundResource(R.drawable.edterr);
                }
                else if (!firstName.matches("^[A-Z][A-Za-z\\s'-]*[a-z]$")){
                    errorText.setText(R.string.first_name_format_is_incorrect_see_help_for_more_information);
                    editFirstName.setBackgroundResource(R.drawable.edterr);
                }else{
                    errorText.setText("");
                    editFirstName.setBackgroundResource(R.drawable.edtnormal);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                String lastName = Objects.requireNonNull(editLastName.getText()).toString();

                if (lastName.isEmpty()) {
                    errorText.setText(R.string.please_enter_your_last_name);
                    editLastName.setBackgroundResource(R.drawable.edterr);
                }
                else if (lastName.length() < 3){
                    errorText.setText(R.string.last_name_must_be_at_least_3_characters_long);
                    editLastName.setBackgroundResource(R.drawable.edterr);
                }
                else if (!lastName.matches("^[A-Z][A-Za-z\\s'-]*[a-z]$")){
                    errorText.setText(R.string.last_name_format_is_incorrect_see_help_for_more_information);
                    editLastName.setBackgroundResource(R.drawable.edterr);
                }else{
                    errorText.setText("");
                    editLastName.setBackgroundResource(R.drawable.edtnormal);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editDOB.setOnClickListener(view1 -> openDatePicker());

        editPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                //validate phone number using regex
                //phone number must be 9 characters long
                String phoneNumber = Objects.requireNonNull(editPhoneNum.getText()).toString();

                if (phoneNumber.isEmpty()) {
                    errorText.setText(R.string.please_enter_your_phone_number);
                    editPhoneNum.setBackgroundResource(R.drawable.edterr);
                }
                else if (phoneNumber.length() != 9){
                    errorText.setText(R.string.phone_number_9_characters_long);
                    editPhoneNum.setBackgroundResource(R.drawable.edterr);
                }else{
                    errorText.setText("");
                    editPhoneNum.setBackgroundResource(R.drawable.edtnormal);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean isInvalidDate() {
        return errorText.getText().toString().isEmpty();
    }

    @SuppressWarnings("deprecation")
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                editDOB.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getActivity(),
                style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year)
    {
        //set so we can validate
        DOBstorage = day + "-" + month + "-" + year;
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        Map<Integer, String> monthsMap = utility.createMonthMap();
        return monthsMap.get(month);
    }

    public void openDatePicker()
    {
        datePickerDialog.show();
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
