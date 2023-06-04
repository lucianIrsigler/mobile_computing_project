package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class prodAddScreen extends AppCompatActivity {
    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productDescriptionEditText;
    private EditText productCategoryEditText;
    // Define the product manager instance
    private ProductManager productManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addproduct);


        productManager = new ProductManager();

        // Find the EditText fields
        productNameEditText = findViewById(R.id.productNameEditText);
        productDescriptionEditText = findViewById(R.id.productDescriptionEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        productCategoryEditText = findViewById(R.id.productCategoryEditText);

        Button addProductButton = findViewById(R.id.addProductButton);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the product details from user input (e.g., name, description, price, category)
                String productName = productNameEditText.getText().toString();
                String productDescription = productDescriptionEditText.getText().toString();
                double productPrice = Double.parseDouble(productPriceEditText.getText().toString());
                String productCategory = productCategoryEditText.getText().toString();

                ImageView productImageView = findViewById(R.id.productImageView);
                Bitmap bitmap = ((BitmapDrawable) productImageView.getDrawable()).getBitmap();

                try {
                    // Call the addProduct method of the product manager to add the product
                    productManager.addProduct(productName, productDescription, productPrice, productCategory, bitmap);
                    Toast.makeText(prodAddScreen.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(prodAddScreen.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}