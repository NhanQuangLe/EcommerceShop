package com.example.ecommerceshop.Phat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Activity.OrderDetailShopActivity;
import com.example.ecommerceshop.Phat.Activity.ResponseRVActivity;
import com.example.ecommerceshop.Phat.Model.Review;
import com.example.ecommerceshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.ReviewViewHolder>{
    private Context context;
    private ArrayList<Review> reviews;

    public AdapterReviews(Context context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_review_shop, parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        Glide.with(context).load(Uri.parse(review.getAvatarCus())).into(holder.avtCus);
        holder.cusName.setText(review.getCustomerName());
        holder.rvDate.setText(review.getReviewedDate());
        holder.RatingBar.setRating((float) review.getRating());
        holder.rvContent.setText(review.getContent());
        if(review.getRvResponse() != null){
            holder.rvResponse.setText(review.getRvResponse());
            holder.btnResponse.setVisibility(View.GONE);
        }
        else {
            holder.rvResponse.setVisibility(View.GONE);
            holder.btnResponse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ResponseRVActivity.class);
                    intent.putExtra("cusId", review.getCustomerId());
                    intent.putExtra("rvId", review.getReviewId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        AdapterImgReviews adapterImgReviews = new AdapterImgReviews(context,review.getUriList());
        holder.listImgRv.setAdapter(adapterImgReviews);

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

        }
    }
}
