package com.example.logintest;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageLoader {
    private final Context context;

    public ImageLoader(Context context) {
        this.context = context;
    }

    public void loadImage(String imageUrl, ImageView imageView) {
        Picasso.get().load(imageUrl).into(imageView);
    }
}