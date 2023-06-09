package com.example.logintest;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapterGetFromGallery extends RecyclerView.Adapter<ImageAdapterGetFromGallery.ViewHolder> {
    private List<Bitmap> imageList;
    private Bitmap defaultImage;

    public ImageAdapterGetFromGallery(List<Bitmap> imageList) {
        this.imageList = imageList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setImageList(List<Bitmap> imageList, Bitmap defaultImage){
        this.imageList = imageList;
        this.defaultImage = defaultImage;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap image = imageList.get(position);
        if (image != null) {
            holder.imageView.setImageBitmap(image);
        } else {
            holder.imageView.setImageBitmap(defaultImage);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}

