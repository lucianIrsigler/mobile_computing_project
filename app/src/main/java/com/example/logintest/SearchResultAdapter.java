package com.example.logintest;

import android.location.GnssAntennaInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.tvPricePlaceholder.setText(String.valueOf(product.getPrice()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamePlaceholder;
        TextView tvPricePlaceholder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamePlaceholder = itemView.findViewById(R.id.tvNamePlaceholder);
            tvPricePlaceholder = itemView.findViewById(R.id.tvPricePlaceholder);

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
