package com.example.ecommerceshop.Phat.Adapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Activity.ReviewDetailProductActivity;
import com.example.ecommerceshop.Phat.Activity.UpdateProductShopActivity;
import com.example.ecommerceshop.Phat.Model.Product;
import com.example.ecommerceshop.Phat.Model.RatedProduct;
import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.Phat.Utils.FilterProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterRatedProducts extends RecyclerView.Adapter<AdapterRatedProducts.RatedProductViewHolder> {
    private Context context;
    public ArrayList<RatedProduct> products;

    public  AdapterRatedProducts (Context context, ArrayList<RatedProduct> products){
        this.context=context;
        this.products=products;
    }
    @NonNull
    @Override
    public AdapterRatedProducts.RatedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_review_products_shop, parent, false);
        return new RatedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRatedProducts.RatedProductViewHolder holder, int position) {
        //get data
        RatedProduct ratedProduct = products.get(position);
        holder.pName.setText(ratedProduct.getProductName());
        holder.pbrand.setText(ratedProduct.getProductBrand());
        holder.pprice.setText(Constants.convertToVND(ratedProduct.getProductPrice()));
        holder.rvNum.setText(String.valueOf(ratedProduct.getReviewNum()));
        Glide.with(context).load(Uri.parse(ratedProduct.getUriList().get(0))).into(holder.imgview_item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReviewDetailProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("pid", ratedProduct.getProductId());
                intent.putExtra("rvNums", String.valueOf(ratedProduct.getReviewNum()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public static class RatedProductViewHolder extends RecyclerView.ViewHolder{

        ImageView imgview_item;
        TextView pbrand, pName, pprice,rvNum;

        public RatedProductViewHolder(@NonNull View itemView) {
            super(itemView);
            pbrand=itemView.findViewById(R.id.pbrand);
            pName=itemView.findViewById(R.id.pName);
            pprice=itemView.findViewById(R.id.pprice);
            imgview_item=itemView.findViewById(R.id.imgview_item);
            rvNum=itemView.findViewById(R.id.rvNum);
        }
    }
}
