package com.example.logintest;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private List<Product> productList;
    private FragmentManager manager ;

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
        if (decodedString.length!=0){
            Glide.with(holder.ivPhotoPlaceholder)
                    .asBitmap()
                    .load(decodedString)
                    .into(holder.ivPhotoPlaceholder);
        }


        holder.itemView.setOnClickListener(view -> {
            FragmentViewProduct fragmentViewProduct =
                    new FragmentViewProduct(product);

            manager.beginTransaction()
                    .replace(R.id.container, fragmentViewProduct,"viewProduct")
                    .replace(R.id.container1,new EmptyFragment(), "empty")
                    .replace(R.id.searchbarContainer,new EmptyFragment(), "empty")
                    .addToBackStack(FragmentViewProduct.class.getSimpleName())
                    .commit();
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView tvNamePlaceholder;
        public final TextView tvPricePlaceholder;
        public final ImageView ivPhotoPlaceholder;
        /**
         * Constructor for the ViewHolder class.
         *
         * @param itemView the item view representing a single item in the RecyclerView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvNamePlaceholder = itemView.findViewById(R.id.tvNamePlaceholder);
            tvPricePlaceholder = itemView.findViewById(R.id.tvPricePlaceholder);
            ivPhotoPlaceholder = itemView.findViewById(R.id.ivPhotoPlaceholder);
        }

        @Override
        public void onClick(View view) {
        }
    }
}
