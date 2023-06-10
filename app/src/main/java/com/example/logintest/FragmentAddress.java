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

import com.example.logintest.databinding.FragmentAddressBinding;

public class FragmentAddress extends Fragment {

    FragmentAddressBinding binding;
    FragmentManager manager;

    public int productID;

    public void setArguments(int productID){
        this.productID=productID;
    }

    public FragmentAddress(FragmentManager manager){
        this.manager = manager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddressBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText addressLine1 = binding.getRoot().findViewById(R.id.edAddressLine1);
        EditText addressLine2 = binding.getRoot().findViewById(R.id.edAddressLine2);
        EditText cityEdit = binding.getRoot().findViewById(R.id.edCity);
        EditText zipEdit = binding.getRoot().findViewById(R.id.edZIPCode);
        Button addAddress = binding.getRoot().findViewById(R.id.btnAddAddress);

        addAddress.setOnClickListener(view1->{
            String line1 = addressLine1.getText().toString();
            String line2 = addressLine2.getText().toString();
            String city = cityEdit.getText().toString();
            String zip = zipEdit.getText().toString();

            //checks if input is empty
            if (line1.isEmpty()||line2.isEmpty()||city.isEmpty()||zip.isEmpty()){
                Toast.makeText(getContext(),"Enter all fields",Toast.LENGTH_LONG).show();
                return;
            }

            PaymentFragment fragment = new PaymentFragment(manager);
            fragment.setArguments(productID);

            utility.replaceFragment(manager,R.id.container, fragment,"payment");
        });


    }
}
