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

    private FragmentAddressBinding binding;
    public final FragmentManager manager;

    public Product product;

    public void setArguments(Product product){
        this.product=product;
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
            if (!validateAddress(line1,line2,city,zip)){
                return;
            }

            FragmentPayment fragment = new FragmentPayment(manager);
            fragment.setArguments(product);

            manager.beginTransaction()
                    .replace(R.id.container,fragment,fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        });
    }

    public boolean validateAddress(String address,String suburb,String city,String zip){
        if(address.isEmpty()||suburb.isEmpty()||city.isEmpty()||zip.isEmpty()){
            Toast.makeText(getContext(),"Enter all fields",Toast.LENGTH_LONG).show();
            return false;
        }
        //Start with number (1 to 4 digits long), followed by a space, followed by a capital letter, followed by letters only
        //followed by a space, followed by a capital letter, followed by letters only
        //38 Hello Street
        if(!address.matches("^[A-Za-z0-9\\s.'-]{1,50}$")){
            Toast.makeText(getContext(),"Address is invalid",Toast.LENGTH_LONG).show();
            return false;
        }
        //Start with a capital letter, followed by letters only
        else if(!suburb.matches("^[A-Z][a-zA-Z]{2,19}$")){
            Toast.makeText(getContext(),
        "Suburb must start with a capital letter, followed by letters only"
                    ,Toast.LENGTH_LONG).show();
            return false;
        }

        //Start with a capital letter, followed by letters only
        else if(!city.matches("^[A-Z][a-zA-Z]{2,19}$")){
            Toast.makeText(getContext(),
        "City must start with a capital letter, followed by letters only"
                    ,Toast.LENGTH_LONG).show();
            return false;
        }
        //Start with a capital letter, followed by capital letters or numbers only
        else if(!zip.matches("^[A-Z0-9]{4,8}$")){
            Toast.makeText(getContext(),
                    "Zip must be between 4 and 8 numbers long"
                    ,Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
