package com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.ProductTypeViewholder> {

    private Context context;
    private ArrayList<String> productTypes;
    public ProductTypeAdapter(Context context, ArrayList<String> productTypes) {
        this.context = context;
        this.productTypes = productTypes;
    }

    @NonNull
    @Override
    public ProductTypeAdapter.ProductTypeViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_filter_product_type, parent,false);
        return new ProductTypeAdapter.ProductTypeViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTypeAdapter.ProductTypeViewholder holder, int position) {
        String productType = productTypes.get(position);

        holder.acBtn_ProductCategory.setText(productType);
    }

    @Override
    public int getItemCount() {
        return productTypes.size();
    }

    public static class ProductTypeViewholder extends RecyclerView.ViewHolder{

        AppCompatButton acBtn_ProductCategory;
        public ProductTypeViewholder(@NonNull View itemView) {
            super(itemView);
            acBtn_ProductCategory = itemView.findViewById(R.id.acBtn_ProductCategory);
        }
    }
}
