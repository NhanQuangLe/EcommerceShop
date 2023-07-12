package com.example.ecommerceshop.Phat.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Model.TopProduct;
import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterTopProducts extends RecyclerView.Adapter<AdapterTopProducts.topProductViewholder>{
    Context context;
    ArrayList<TopProduct> topProducts;

    public AdapterTopProducts(Context context, ArrayList<TopProduct> topProducts) {
        this.context = context;
        this.topProducts = topProducts;
    }

    @NonNull
    @Override
    public topProductViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_top_products_shop, parent,false);
        return new topProductViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull topProductViewholder holder, int position) {
        TopProduct topProduct= topProducts.get(position);
        holder.top.setText(String.valueOf(position+1));
        holder.pName.setText(topProduct.getName());
        holder.pprice.setText(Constants.convertToVND(topProduct.getPrice()));
        holder.pSold.setText(String.valueOf(topProduct.getQuan()));
        Glide.with(context).load(Uri.parse(topProduct.getAvatar())).into(holder.imgview_item);
    }

    @Override
    public int getItemCount() {
        return topProducts.size();
    }

    public  static  class topProductViewholder extends RecyclerView.ViewHolder {
        TextView top, pName, pprice, pSold;
        ImageView imgview_item;
        public topProductViewholder(@NonNull View itemView) {
            super(itemView);
            top=itemView.findViewById(R.id.top);
            pName=itemView.findViewById(R.id.pName);
            pprice=itemView.findViewById(R.id.pprice);
            pSold=itemView.findViewById(R.id.pSold);
            imgview_item=itemView.findViewById(R.id.imgview_item);

        }
    }
}
