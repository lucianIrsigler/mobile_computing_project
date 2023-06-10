package com.example.logintest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GnssAntennaInfo;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.internal.http2.Http2Connection;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private static List<Product> productList;


    public void setProducts(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public SearchResultAdapter() {
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    private static OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //R.layout.search_result_item
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.search_recycler_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvNamePlaceholder.setText(product.getName());
        String price = String.format("R%.2f",product.getPrice());
        holder.tvPricePlaceholder.setText(price);

        //set image
        JSONObject newParams = new JSONObject();

        try {
            newParams.put("productID",product.getProductID());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HTTPHandler httpHandler = new HTTPHandler();

        String response = httpHandler.postRequest(
                "https://lamp.ms.wits.ac.za/home/s2621933/php/selectoneproductimage.php",
                newParams, String.class);

        Log.i("search",response);

        byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.ivPhotoPlaceholder.setImageBitmap(decodedBitmap);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamePlaceholder;
        TextView tvPricePlaceholder;
        ImageView ivPhotoPlaceholder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamePlaceholder = itemView.findViewById(R.id.tvNamePlaceholder);
            tvPricePlaceholder = itemView.findViewById(R.id.tvPricePlaceholder);
            ivPhotoPlaceholder = itemView.findViewById(R.id.ivPhotoPlaceholder);

            itemView.setOnClickListener(v -> {
                Log.i("onclick","called");
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(productList.get(position));
                }
            });
        }
    }
}
