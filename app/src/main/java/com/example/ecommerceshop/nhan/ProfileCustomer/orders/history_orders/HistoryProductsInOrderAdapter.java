package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.qui.product_detail.ProductDetailActivity;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        Glide.with(context).load(product.getProductAvatar()).into(holder.iv_ProductAvatar);
        holder.tv_ProductName.setText(product.getProductName());
        holder.tv_ProductBrand.setText(product.getProductBrand());
        holder.tv_ProductCategory.setText(product.getProductCategory());
        holder.tv_ProductPurchaseQuantity.setText(String.valueOf(product.getPurchaseQuantity()));
        String  price = Constants.convertToVND(product.getProductDiscountPrice());
        holder.tv_ProductDiscountPrice.setText(price);
        holder.item_order_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Users").child(product.getShopID())
                        .child("Shop")
                        .child("Products")
                        .child(product.getProductID())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                com.example.ecommerceshop.qui.homeuser.Product pd
                                        = snapshot.getValue(com.example.ecommerceshop.qui.homeuser.Product.class);
                                Intent intent = new Intent(context, ProductDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("product", pd);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                CustomToast.makeText(context,"Lỗi khi lấy dữ liệu",CustomToast.SHORT,CustomToast.ERROR).show();

                            }
                        });
            }
        });
    }
    @Override
    public int getItemCount() {
        return listProductInOrder.size();
    }

    public static class ProductsInOrderViewholder extends RecyclerView.ViewHolder{

        ImageView iv_ProductAvatar;

        TextView tv_ProductName, tv_ProductBrand, tv_ProductCategory, tv_ProductPurchaseQuantity, tv_ProductDiscountPrice;
        LinearLayout item_order_product;

        public ProductsInOrderViewholder(@NonNull View itemView) {
            super(itemView);
            item_order_product = itemView.findViewById(R.id.item_order_product);
            iv_ProductAvatar = itemView.findViewById(R.id.iv_ProductAvatar);
            tv_ProductName = itemView.findViewById(R.id.tv_ProductName);
            tv_ProductBrand = itemView.findViewById(R.id.tv_ProductBrand);
            tv_ProductCategory = itemView.findViewById(R.id.tv_ProductCategory);
            tv_ProductPurchaseQuantity = itemView.findViewById(R.id.tv_ProductPurchaseQuantity);
            tv_ProductDiscountPrice = itemView.findViewById(R.id.tv_ProductDiscountPrice);
        }
    }
}