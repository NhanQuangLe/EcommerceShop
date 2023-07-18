package com.example.ecommerceshop.nhan.ProfileCustomer.orders.rating_products_orders;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.R;

import java.util.List;
public class ImgReviewAdapter extends RecyclerView.Adapter<ImgReviewAdapter.imgViewHolder>{
    Context context;
    List<String> arrayList;

    public ImgReviewAdapter(Context context, List<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ImgReviewAdapter.imgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_img_in_review_shop, parent,false);
        return new ImgReviewAdapter.imgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgReviewAdapter.imgViewHolder holder, int position) {
        String str = arrayList.get(position);
        Glide.with(context).load(Uri.parse(str)).into(holder.imagephoto);
    }

    @Override
    public int getItemCount() {
        return arrayList!=null ? arrayList.size():0;
    }

    public static class imgViewHolder extends RecyclerView.ViewHolder{
        ImageView imagephoto;
        public imgViewHolder(@NonNull View itemView) {
            super(itemView);
            imagephoto=itemView.findViewById(R.id.imagephoto);
        }
    }
}
