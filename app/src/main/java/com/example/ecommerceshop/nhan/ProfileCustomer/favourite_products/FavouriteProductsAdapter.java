package com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Cart;
import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.nhan.Model.Shop;
import com.example.ecommerceshop.qui.product_detail.ProductDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

public class FavouriteProductsAdapter extends RecyclerView.Adapter<FavouriteProductsAdapter.FavouriteProductsViewholder> {

    private Context context;
    private ArrayList<Product> favouriteProducts;
    private FirebaseAuth firebaseAuth;
    public IClickFavouriteProductListener mOnClickFavouriteProductListener;
    public FavouriteProductsAdapter(Context context, ArrayList<Product> products, IClickFavouriteProductListener listener) {
        this.context = context;
        this.favouriteProducts = products;
        this.mOnClickFavouriteProductListener = listener;
    }
    public void setFirebaseAuth(FirebaseAuth fbA)
    {
        this.firebaseAuth = fbA;
    }

    @NonNull
    @Override
    public FavouriteProductsAdapter.FavouriteProductsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_favorite_product, parent,false);
        return new FavouriteProductsAdapter.FavouriteProductsViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteProductsAdapter.FavouriteProductsViewholder holder, int position) {
        Product favouriteProduct = favouriteProducts.get(position);
        Picasso.get().load(Uri.parse(favouriteProduct.getProductAvatar())).into(holder.iv_ProductAvatar);
        holder.tv_ProductCategory.setText(favouriteProduct.getProductCategory());
        holder.tv_ProductName.setText(favouriteProduct.getProductName());
        holder.tv_ProductSite.setText(favouriteProduct.getProductSite());
        holder.tv_ProductBrand.setText(favouriteProduct.getProductBrand());
        holder.tv_ProductSalePrice.setText(Integer.toString(favouriteProduct.getProductDiscountPrice()));
        holder.rb_ProductRating.setRating(favouriteProduct.getProductRating());

        holder.ib_AddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickFavouriteProductListener.addProductToCart(favouriteProduct);
            }
        });

        holder.ib_DeleteFavoriteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickFavouriteProductListener.deleteProduct(favouriteProduct);
            }
        });
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Users").child(favouriteProduct.getShopID())
                        .child("Shop")
                        .child("Products")
                        .child(favouriteProduct.getProductID())
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
                                Toast.makeText(context, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteProducts.size();
    }

    public static class FavouriteProductsViewholder extends RecyclerView.ViewHolder{
        ImageView iv_ProductAvatar;
        TextView tv_ProductCategory, tv_ProductName, tv_ProductSite, tv_ProductBrand, tv_ProductSalePrice;
        RatingBar rb_ProductRating;
        ImageButton ib_AddToCart, ib_DeleteFavoriteProduct;
        ConstraintLayout container;
        public FavouriteProductsViewholder(@NonNull View itemView) {
            super(itemView);
            iv_ProductAvatar = itemView.findViewById(R.id.iv_ProductAvatar);
            tv_ProductCategory = itemView.findViewById(R.id.tv_ProductCategory);
            tv_ProductName = itemView.findViewById(R.id.tv_ProductName);
            tv_ProductSite = itemView.findViewById(R.id.tv_ProductSite);
            tv_ProductBrand = itemView.findViewById(R.id.tv_ProductBrand);
            tv_ProductSalePrice = itemView.findViewById(R.id.tv_ProductSalePrice);
            rb_ProductRating = itemView.findViewById(R.id.rb_ProductRating);
            ib_AddToCart = itemView.findViewById(R.id.ib_AddToCart);
            ib_DeleteFavoriteProduct = itemView.findViewById(R.id.ib_DeleteFavoriteProduct);
            container = itemView.findViewById(R.id.container);
        }
    }
}
