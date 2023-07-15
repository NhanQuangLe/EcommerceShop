package com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
    private IClickProductType mClickProductType;
    public ProductTypeAdapter(Context context, ArrayList<String> productTypes, IClickProductType listener) {
        this.context = context;
        this.productTypes = productTypes;
        this.mClickProductType = listener;
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
        holder.acBtn_ProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.acBtn_ProductCategory.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#D9D9D9"))){
                    mClickProductType.IClickFilter(productType, true);
                    holder.acBtn_ProductCategory.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                }
                else{
                    mClickProductType.IClickFilter(productType, false);
                    holder.acBtn_ProductCategory.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D9D9D9")));
                }
            }
        });
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
