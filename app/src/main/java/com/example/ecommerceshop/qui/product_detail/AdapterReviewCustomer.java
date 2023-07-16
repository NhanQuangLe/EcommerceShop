package com.example.ecommerceshop.qui.product_detail;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Adapter.AdapterImgReviews;
import com.example.ecommerceshop.Phat.Model.Review;
import com.example.ecommerceshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterReviewCustomer extends RecyclerView.Adapter<AdapterReviewCustomer.ReviewViewHolder>{
    private Context context;
    private ArrayList<Review> reviews;

    public AdapterReviewCustomer(Context context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_review_cus, parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        Review review = reviews.get(position);
        Glide.with(context).load(Uri.parse(review.getAvatarCus())).into(holder.avtCus);
        holder.cusName.setText(review.getCustomerName());
        holder.rvDate.setText(review.getReviewedDate());
        holder.RatingBar.setRating((float) review.getRating());
        holder.RatingBar.setFocusable(false);
        holder.RatingBar.setIsIndicator(true);
        holder.rvContent.setText(review.getContent());
        if(review.getRvResponse() != null){
            holder.rvResponse.setText(review.getRvResponse());
        }
        else {
            holder.rvResponse.setVisibility(View.GONE);
        }
        AdapterImgReviews adapterImgReviews = new AdapterImgReviews(context,review.getUriList());
        holder.listImgRv.setAdapter(adapterImgReviews);
        final boolean[] flat = {false};
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+review.getCustomerId()+"/Customer/Reviews/"+review.getReviewId()+"/likeCommentUser");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long likeQuantity = snapshot.getChildrenCount();
                    holder.likeQuantity.setText(likeQuantity+"");
                    if (snapshot.child(mCurrentUser.getUid()).exists()){
                        holder.checkBoxLike.setChecked(true);
                    }
                    else {
                         holder.checkBoxLike.setChecked(false);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.checkBoxLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    ref.child(mCurrentUser.getUid()).setValue(mCurrentUser.getUid());
                }
                else {
                   ref.child(mCurrentUser.getUid()).removeValue();
                }
            }
        });



    }


    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avtCus;
        TextView cusName, rvDate, rvContent,rvResponse;
        AppCompatButton btnResponse;
        RatingBar RatingBar;
        RecyclerView listImgRv;
        TextView likeQuantity;
        CheckBox checkBoxLike;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            avtCus=itemView.findViewById(R.id.avtCus);
            cusName=itemView.findViewById(R.id.cusName);
            rvDate=itemView.findViewById(R.id.rvDate);
            rvContent=itemView.findViewById(R.id.rvContent);
            rvResponse=itemView.findViewById(R.id.rvResponse);
            btnResponse=itemView.findViewById(R.id.btnResponse);
            RatingBar=itemView.findViewById(R.id.RatingBar);
            listImgRv=itemView.findViewById(R.id.listImgRv);
            likeQuantity = itemView.findViewById(R.id.likeQuantity);
            checkBoxLike = itemView.findViewById(R.id.checkBoxLike);
        }
    }
}
