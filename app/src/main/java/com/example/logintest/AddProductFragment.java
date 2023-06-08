package com.example.logintest;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.logintest.databinding.AddproductBinding;

public class AddProductFragment extends Fragment {
    private AddproductBinding binding;
    private final ProductManager productManager = new ProductManager();
    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productDescriptionEditText;
    private Spinner productCategorySpinner;

    public AddProductFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddproductBinding.inflate(inflater, container, false);

        productNameEditText = binding.getRoot().findViewById(R.id.productNameEditText);
        productDescriptionEditText = binding.getRoot().findViewById(R.id.productDescriptionEditText);
        productPriceEditText = binding.getRoot().findViewById(R.id.productPriceEditText);
        productCategorySpinner = binding.getRoot().findViewById(R.id.productCategorySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productCategorySpinner.setAdapter(adapter);
        Button addProductButton = binding.getRoot().findViewById(R.id.addProductButton);
        addProductButton.setOnClickListener(v -> {
            // Get the product details from user input (e.g., name, description, price, category)
            String productName = productNameEditText.getText().toString();
            String productDescription = productDescriptionEditText.getText().toString();
            double productPrice = Double.parseDouble(productPriceEditText.getText().toString());
            String productCategory = productCategorySpinner.getSelectedItem().toString();

            ImageView productImageView = binding.getRoot().findViewById(R.id.productImageView);
            Bitmap bitmap = ((BitmapDrawable) productImageView.getDrawable()).getBitmap();

            try {
                // Call the addProduct method of the product manager to add the product
                productManager.addProduct(productName, productDescription, productPrice, productCategory, bitmap);
                Toast.makeText(getActivity(), "Product added successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed to add product", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();

    }
}
