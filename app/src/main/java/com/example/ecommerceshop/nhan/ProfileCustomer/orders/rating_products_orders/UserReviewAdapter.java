package com.example.ecommerceshop.nhan.ProfileCustomer.orders.rating_products_orders;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.ReviewViewholder> {

    private Context context;
    private ArrayList<Review> reviews;
    private FirebaseAuth firebaseAuth;
    public UserReviewAdapter(Context context, ArrayList<Review> reviewList) {
        this.context = context;
        this.reviews = reviewList;
    }
    public void setFirebaseAuth(FirebaseAuth fbA)
    {
        this.firebaseAuth = fbA;
    }

    @NonNull
    @Override
    public UserReviewAdapter.ReviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_review, parent,false);
        return new UserReviewAdapter.ReviewViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReviewAdapter.ReviewViewholder holder, int position) {
        Review review = reviews.get(position);
        Picasso.get().load(Uri.parse(review.getAvatarCus())).into(holder.avtCus);
        holder.cusName.setText(review.getCustomerName());
        holder.RatingBar.setRating(review.getRating());
        holder.rvDate.setText(review.getReviewDate());
        holder.rvContent.setText(review.getContent());
        if(review.getRvResponse() != null){
            holder.rvResponse.setText(review.getRvResponse());
        }
        else {
            holder.responseBox.setVisibility(View.GONE);
        }
        ImgReviewAdapter imgReviewAdapter = new ImgReviewAdapter(context, review.getUriList());
        Picasso.get().load(Uri.parse(review.getProductAvatar())).into(holder.iv_productAvt);
        holder.tv_productName.setText(review.getProductName());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(review.getShopId()).child("Shop")
                        .child("Products").child(review.getProductId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                holder.tv_productCategory.setText(snapshot.child("productCategory").getValue(String.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                holder.tv_productCategory.setText("Smartphone");
                            }
                        });
        holder.listImgRv.setAdapter(imgReviewAdapter);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewholder extends RecyclerView.ViewHolder{
        ImageView avtCus;
        TextView cusName;
        RatingBar RatingBar;
        TextView rvDate, rvContent, rvResponse, tv_productName, tv_productCategory;
        RecyclerView listImgRv;
        LinearLayout responseBox;
        ImageView iv_productAvt;

        public ReviewViewholder(@NonNull View itemView) {
            super(itemView);
            avtCus = itemView.findViewById(R.id.avtCus);
            cusName = itemView.findViewById(R.id.cusName);
            RatingBar = itemView.findViewById(R.id.RatingBar);
            rvDate = itemView.findViewById(R.id.rvDate);
            rvContent = itemView.findViewById(R.id.rvContent);
            rvResponse = itemView.findViewById(R.id.rvResponse);
            listImgRv = itemView.findViewById(R.id.listImgRv);
            responseBox = itemView.findViewById(R.id.responseBox);
            tv_productName = itemView.findViewById(R.id.tv_productName);
            tv_productCategory = itemView.findViewById(R.id.tv_productCategory);
            iv_productAvt = itemView.findViewById(R.id.iv_productAvt);

        }
    }
}
