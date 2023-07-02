package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.review;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Product;


import java.util.ArrayList;

public class ImageReviewAdapter extends RecyclerView.Adapter<ImageReviewAdapter.ImageReviewViewholder> {

    private Context context;
    private ArrayList<Uri> imageUriList;
    public interface IClickImageReview {
        void RemoveImage(Uri uri);
    }
    private IClickImageReview mClickImageReview;
    public ImageReviewAdapter(Context context, ArrayList<Uri> imageUriList, IClickImageReview mClickListener) {
        this.context = context;
        this.imageUriList = imageUriList;
        this.mClickImageReview = mClickListener;
    }
    @NonNull
    @Override
    public ImageReviewAdapter.ImageReviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_image_in_review, parent,false);
        return new ImageReviewAdapter.ImageReviewViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageReviewAdapter.ImageReviewViewholder holder, int position) {
        Uri uriImage = imageUriList.get(position);
        holder.iv_mainImage.setImageURI(uriImage);
        holder.btn_Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUriList.remove(uriImage);
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return imageUriList.size();
    }
    public static class ImageReviewViewholder extends RecyclerView.ViewHolder{
        ImageView iv_mainImage;
        LinearLayout btn_Remove;
        public ImageReviewViewholder(@NonNull View itemView) {
            super(itemView);
            iv_mainImage = itemView.findViewById(R.id.iv_mainImage);
            btn_Remove = itemView.findViewById(R.id.btn_Remove);
        }
    }
}