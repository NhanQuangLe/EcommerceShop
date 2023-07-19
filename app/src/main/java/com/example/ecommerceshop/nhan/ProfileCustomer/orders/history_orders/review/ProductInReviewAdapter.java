package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.review;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Review;
import com.example.ecommerceshop.toast.CustomToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductInReviewAdapter extends RecyclerView.Adapter<ProductInReviewAdapter.ReviewViewholder> {

    private Context context;
    private ArrayList<Review> reviewList;
    private IClickProductInReviewListener mClickProductInReviewListener;
    public ProductInReviewAdapter(Context context, ArrayList<Review> reviewList, IClickProductInReviewListener clickListener) {
        this.context = context;
        this.reviewList = reviewList;
        this.mClickProductInReviewListener = clickListener;
    }
    @NonNull
    @Override
    public ProductInReviewAdapter.ReviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_write_rating_product, parent,false);
        return new ProductInReviewAdapter.ReviewViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductInReviewAdapter.ReviewViewholder holder, int position) {
        Review review = reviewList.get(position);
        Picasso.get().load(Uri.parse(review.getProductAvatar())).into(holder.iv_ProductAvatar);
        holder.tv_ProductName.setText(review.getProductName());
        ArrayList<String> uriList = review.getUriList();
        ImageReviewAdapter imageReviewAdapter = new ImageReviewAdapter(context, uriList, new ImageReviewAdapter.IClickImageReview() {
            @Override
            public void RemoveImage(Uri uri) {
                uriList.remove(uri);
                notifyDataSetChanged();
            }
        });
        holder.rv_ListImage.setAdapter(imageReviewAdapter);
        holder.btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickProductInReviewListener.RemoveReview(review);
            }
        });
        holder.rb_Rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mClickProductInReviewListener.RatingBarChange(ratingBar, (float)v, b, review);
            }
        });
        holder.btn_AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(review.getUriList().size() == 5)
                {
                    CustomToast.makeText(context,"Chỉ được chọn tối đa 5 ảnh",CustomToast.SHORT,CustomToast.ERROR).show();

                    return;
                }
                mClickProductInReviewListener.AddImage(imageReviewAdapter, review);
            }
        });
    }


    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewholder extends RecyclerView.ViewHolder{
        ImageView iv_ProductAvatar;
        TextView tv_ProductName;
        ImageView btn_Close;
        RatingBar rb_Rating;
        EditText et_Comment;
        RecyclerView rv_ListImage;
        LinearLayout btn_AddImage;
        public ReviewViewholder(@NonNull View itemView) {
            super(itemView);
            iv_ProductAvatar = itemView.findViewById(R.id.iv_ProductAvatar);
            tv_ProductName = itemView.findViewById(R.id.tv_ProductName);
            btn_Close = itemView.findViewById(R.id.btn_Close);
            rb_Rating = itemView.findViewById(R.id.rb_Rating);
            et_Comment = itemView.findViewById(R.id.et_Comment);
            rv_ListImage = itemView.findViewById(R.id.rv_ListImage);
            btn_AddImage = itemView.findViewById(R.id.btn_AddImage);
        }
    }
}