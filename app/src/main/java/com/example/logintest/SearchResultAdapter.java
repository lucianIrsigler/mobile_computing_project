package com.example.logintest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private List<Product> productList;

    public void setProducts(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }
    public SearchResultAdapter() {}

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
        }
    }
}
