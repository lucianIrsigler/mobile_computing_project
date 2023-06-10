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

public class PaymentFragment extends Fragment {

    FragmentPaymentBinding binding;
    FragmentManager manager;

    public Product product;

    public void setArguments(Product product){
        this.product=product;
    }

    public PaymentFragment(FragmentManager manager){
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
        //todo date time picker for expDate

        makePayment.setOnClickListener(view1->{
            String cardName = cardHolderEdit.getText().toString();
            String cardNum = cardNumEdit.getText().toString();
            String expDate = expDateEdit.getText().toString();
            String cvv = cvvEdit.getText().toString();

            if (cardName.isEmpty()||cardNum.isEmpty()||expDate.isEmpty()||cvv.isEmpty()){
                Toast.makeText(getContext(),"Enter all fields",Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(getContext(), "Purchase was successful", Toast.LENGTH_SHORT).show();

            TransactionManager transactionManager = new TransactionManager();
            UsersManager usersManager = new UsersManager();
            long userID = usersManager.getCurrentUserID();

            transactionManager.insertTransaction(userID,product.getProductID());

        });

    }
}
