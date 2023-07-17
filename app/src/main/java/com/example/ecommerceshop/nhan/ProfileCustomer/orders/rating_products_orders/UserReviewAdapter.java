package com.example.ecommerceshop.nhan.ProfileCustomer.orders.rating_products_orders;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.nhan.Model.Review;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products.FavouriteProductsAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products.IClickFavouriteProductListener;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryProductsInOrderAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.IClickHistoryOrderListener;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.ReviewViewholder> {

    private Context context;
    private ArrayList<Product> favouriteProducts;
    private FirebaseAuth firebaseAuth;
    public IClickFavouriteProductListener mOnClickFavouriteProductListener;
    public UserReviewAdapter(Context context, ArrayList<Product> products, IClickFavouriteProductListener listener) {
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
    public UserReviewAdapter.ReviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_review_cus, parent,false);
        return new UserReviewAdapter.ReviewViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReviewAdapter.ReviewViewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return favouriteProducts.size();
    }

    public static class ReviewViewholder extends RecyclerView.ViewHolder{

        public ReviewViewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
