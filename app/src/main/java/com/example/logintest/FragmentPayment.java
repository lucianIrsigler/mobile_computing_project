package com.example.logintest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.logintest.databinding.FragmentPaymentBinding;

import java.time.LocalDate;

public class FragmentPayment extends Fragment {
    FragmentPaymentBinding binding;
    final FragmentManager manager;

    public Product product;

    public void setArguments(Product product){
        this.product=product;
    }

    public FragmentPayment(FragmentManager manager){
        this.manager = manager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText cardHolderEdit = binding.getRoot().findViewById(R.id.edCardholder);
        EditText cardNumEdit = binding.getRoot().findViewById(R.id.edCardNumber);
        EditText expDateEdit = binding.getRoot().findViewById(R.id.edExpirationDate);
        EditText cvvEdit = binding.getRoot().findViewById(R.id.edCVV);
        Button makePayment = binding.getRoot().findViewById(R.id.btnMakePayment);

        makePayment.setOnClickListener(view1->{
            String cardName = cardHolderEdit.getText().toString();
            String cardNum = cardNumEdit.getText().toString();
            String expDate = expDateEdit.getText().toString();
            String cvv = cvvEdit.getText().toString();

            if (!validate(cardName,cardNum,expDate,cvv)){
                return;
            }

            TransactionManager transactionManager = new TransactionManager();
            UsersManager usersManager = new UsersManager();
            long userID = usersManager.getCurrentUserID();

            transactionManager.insertTransaction(userID,product.getProductID());

            Toast.makeText(getContext(), "Purchase was successful", Toast.LENGTH_SHORT).show();

            manager.beginTransaction()
                    .replace(R.id.container,new FragmentViewProduct(product),FragmentViewProduct.class.getSimpleName())
                    .addToBackStack(null)
                    .commit();

        });
    }

    private boolean validate(String accountHolderName, String cardNumber, String expiryDate, String cvv){
        if(accountHolderName.isEmpty()||cardNumber.isEmpty()||cvv.isEmpty()||expiryDate.isEmpty()){
            Toast.makeText(getContext(),"Enter all fields",Toast.LENGTH_LONG).show();
            return false;
        }
        //Start with Mr, Mrs or Miss, followed by a space, followed by a capital letter, followed by letters only
        //Mr B Z Nhlebela or Ms L Bayat or Miss A B C Random
        if (!accountHolderName.matches("^(?:Mr|Mrs|Miss) [A-Z](?: [A-Z])*(?: [A-Z][a-zA-Z]+)+$")){
            Toast.makeText(getContext(),
                    "Account holder format is: Title firstname lastname. eg) Mr L surname",Toast.LENGTH_LONG).show();
            return false;
        }
        //16-digit number only
        else if (!cardNumber.matches("^[0-9]{16}$")) {
            Toast.makeText(getContext(),"16 digits are required for the card number",Toast.LENGTH_LONG).show();
            return false;
        }

        String[] dates = expiryDate.split("/");
        LocalDate now = LocalDate.now();

        //Start with 2 digits, followed by a /, followed by 2 digits
        if (!expiryDate.matches("^[0-9]{2}/[0-9]{2}$")){
            Toast.makeText(getContext(),"Must be 2 numbers followed by a / then another 2 numbers",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        else if(Integer.parseInt(dates[0]) <= now.getMonthValue() && Integer.parseInt("20"+dates[1]) <= now.getYear()){
            Toast.makeText(getContext(),"Expired card",Toast.LENGTH_LONG).show();
            return false;
        }

        //3-digit number only
        if (!cvv.matches("^[0-9]{3}$")){
            Toast.makeText(getContext(),"Invalid cvv",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
