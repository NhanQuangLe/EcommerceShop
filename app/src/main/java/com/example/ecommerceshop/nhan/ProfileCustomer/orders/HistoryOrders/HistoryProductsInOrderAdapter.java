package com.example.ecommerceshop.nhan.ProfileCustomer.orders.HistoryOrders;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryProductsInOrderAdapter extends RecyclerView.Adapter<HistoryProductsInOrderAdapter.ProductsInOrderViewholder> {

    private Context context;
    private ArrayList<Product> listProductInOrder;
    public HistoryProductsInOrderAdapter(Context context, ArrayList<Product> listProductInOrder) {
        this.context = context;
        this.listProductInOrder = listProductInOrder;
    }

    @NonNull
    @Override
    public HistoryProductsInOrderAdapter.ProductsInOrderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_product_in_order, parent,false);
        return new HistoryProductsInOrderAdapter.ProductsInOrderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryProductsInOrderAdapter.ProductsInOrderViewholder holder, int position) {
        Product product = listProductInOrder.get(position);
        Picasso.get().load(Uri.parse(product.getProductAvatar())).into(holder.iv_ProductAvatar);
        holder.tv_ProductName.setText(product.getProductName());
        holder.tv_ProductBrand.setText(product.getProductBrand());
        holder.tv_ProductCategory.setText(product.getProductCategory());
        holder.tv_ProductPurchaseQuantity.setText(String.valueOf(product.getPurchaseQuantity()));
        holder.tv_ProductDiscountPrice.setText(String.valueOf(product.getProductDiscountPrice()));
    }

    @Override
    public int getItemCount() {
        return listProductInOrder.size();
    }

    public static class ProductsInOrderViewholder extends RecyclerView.ViewHolder{

        ImageView iv_ProductAvatar;

        TextView tv_ProductName, tv_ProductBrand, tv_ProductCategory, tv_ProductPurchaseQuantity, tv_ProductDiscountPrice;

        public ProductsInOrderViewholder(@NonNull View itemView) {
            super(itemView);
            iv_ProductAvatar = itemView.findViewById(R.id.iv_ProductAvatar);
            tv_ProductName = itemView.findViewById(R.id.tv_ProductName);
            tv_ProductBrand = itemView.findViewById(R.id.tv_ProductBrand);
            tv_ProductCategory = itemView.findViewById(R.id.tv_ProductCategory);
            tv_ProductPurchaseQuantity = itemView.findViewById(R.id.tv_ProductPurchaseQuantity);
            tv_ProductDiscountPrice = itemView.findViewById(R.id.tv_ProductDiscountPrice);
        }
    }
}