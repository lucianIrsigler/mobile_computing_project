package com.example.logintest;

import android.graphics.Bitmap;
public class Product {
    private String name;
    private String description;
    private double price;
    private String category;
    private Bitmap image;
    private int productID;

    public Product(String name, String description, double price, int productID) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.productID=productID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getProductID() {
        return productID;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


}
