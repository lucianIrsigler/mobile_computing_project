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

public class FragmentViewProduct extends Fragment {
    private Product product;

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
        View view = inflater.inflate(R.layout.fragment_view_product, container, false);

        // Access the views in the fragment layout and update them with the product details
        TextView productNameTextView = view.findViewById(R.id.tvProductNameViewProduct);
        TextView productPriceTextView = view.findViewById(R.id.tvPriceViewProduct);
        TextView productDescriptionTextView=view.findViewById(R.id.tvDescriptionViewProduct);

        productNameTextView.setText(product.getName());
        productPriceTextView.setText(String.valueOf(product.getPrice()));
        productDescriptionTextView.setText(product.getDescription());

        return view;
    }
}