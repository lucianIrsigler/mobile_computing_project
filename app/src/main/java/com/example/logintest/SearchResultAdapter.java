package com.example.logintest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private static List<Product> productList;
    FragmentManager manager;




    public void setProducts(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public SearchResultAdapter() {
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setManager(FragmentManager manager){
        this.manager = manager;
    }

    private static OnItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //R.layout.search_result_item
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.search_recycler_view_row, parent, false);
        return new ViewHolder(view);
    }
    /**
     * Binds the data to the ViewHolder at the specified position.
     *
     * @param holder   the ViewHolder to bind the data to
     * @param position the position of the item in the list
     */
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

        byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.ivPhotoPlaceholder.setImageBitmap(decodedBitmap);

        holder.itemView.setOnClickListener(view -> {
            FragmentViewProduct fragmentViewProduct =
                    new FragmentViewProduct(product,manager);

            manager.beginTransaction()
                    .replace(R.id.container, fragmentViewProduct)
                    .addToBackStack(null)
                    .commit();
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamePlaceholder;
        TextView tvPricePlaceholder;
        ImageView ivPhotoPlaceholder;
        /**
         * Constructor for the ViewHolder class.
         *
         * @param itemView the item view representing a single item in the RecyclerView
         */
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
