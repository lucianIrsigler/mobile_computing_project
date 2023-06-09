package com.example.logintest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.logintest.Product;
import com.example.logintest.databinding.FragmentViewProductBinding;

public class FragmentViewProduct extends Fragment {
    private Product product;

    FragmentViewProductBinding binding;
    public FragmentViewProduct() {
        // Required empty public constructor
    }

    public FragmentViewProduct(Product product) {
        this.product = product;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewProductBinding.inflate(inflater, container, false);

        // Access the views in the fragment layout and update them with the product details
        TextView productNameTextView = binding.getRoot().findViewById(R.id.tvProductNameViewProduct);
        TextView productPriceTextView = binding.getRoot().findViewById(R.id.tvPriceViewProduct);
        TextView productDescriptionTextView=binding.getRoot().findViewById(R.id.tvDescriptionViewProduct);

        productNameTextView.setText(product.getName());
        productPriceTextView.setText(String.valueOf(product.getPrice()));
        productDescriptionTextView.setText(product.getDescription());

        return binding.getRoot();
    }
}