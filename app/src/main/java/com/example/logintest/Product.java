package com.example.logintest;

public class Product {
    private final String name;
    private final String description;
    private final double price;
    private final int productID;

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


}
