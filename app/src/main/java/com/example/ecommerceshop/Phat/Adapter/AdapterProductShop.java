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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.Phat.Activity.UpdateProductShopActivity;
import com.example.ecommerceshop.Phat.Model.Product;
import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.Phat.Utils.FilterProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterProductShop extends RecyclerView.Adapter<AdapterProductShop.ProductViewHolder> implements Filterable {
   private Context context;
   public ArrayList<Product> products, filterList;
   private FilterProduct filterProduct;
    public  AdapterProductShop (Context context, ArrayList<Product> products){
        this.context=context;
        this.products=products;
        this.filterList=products;
    }
    @NonNull
    @Override
    public AdapterProductShop.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_all_product_shop, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProductShop.ProductViewHolder holder, int position) {
        //get data
        Product product = products.get(position);
        String uid = product.getUid();
        String pid=product.getProductId();
        String name = product.getProductName();
        String des = product.getProductDescription();
        String brand = product.getProductBrand();
        String site = product.getProductSite();
        int price = product.getProductPrice();
        int quantity=product.getProductQuantity();
        String disnote=product.getProductDiscountNote();
        int disprice =product.getProductDiscountPrice();
        String category = product.getProductCategory();
        List<String> urilist=product.getUriList();

        //set data
        holder.pname.setText(name);
        holder.pbrand.setText(brand);
        if(disprice==0){
            holder.pdisprice.setText(Constants.convertToVND(price));
            holder.pprice.setVisibility(View.GONE);
            holder.discountTag.setVisibility(View.GONE);
        }
        else{
            holder.pdisprice.setText(Constants.convertToVND((price-disprice)));
            holder.pprice.setText(Constants.convertToVND(price));
            holder.discountTag.setVisibility(View.VISIBLE);
            holder.pdisnote.setText(disnote);
        }
        Picasso.get().load(Uri.parse(urilist.get(0))).placeholder(R.drawable.ic_add_a_photo).into(holder.imgProduct);
        holder.updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateProductShopActivity.class);
                intent.putExtra("productid", pid);
                context.startActivity(intent);
            }
        });
        if(product.isSold()){
            holder.StopSelling.setVisibility(View.GONE);
        }
        else {
            holder.StopSelling.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public Filter getFilter() {
        if(filterProduct == null){
            filterProduct=new FilterProduct(this, filterList);
        }
        return filterProduct;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView imgProduct, updatebtn;
        TextView pbrand, pname, pprice, pdisprice, pdisnote;
        FrameLayout discountTag, StopSelling;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct=itemView.findViewById(R.id.imgProduct);
            updatebtn=itemView.findViewById(R.id.updateBtn);
            pbrand=itemView.findViewById(R.id.pbrand);
            pname=itemView.findViewById(R.id.pname);
            pprice=itemView.findViewById(R.id.pprice);
            pdisprice=itemView.findViewById(R.id.pdisprice);
            pdisnote=itemView.findViewById(R.id.pdisnote);
            discountTag=itemView.findViewById(R.id.discountTag);
            StopSelling=itemView.findViewById(R.id.StopSelling);
        }
    }
}
