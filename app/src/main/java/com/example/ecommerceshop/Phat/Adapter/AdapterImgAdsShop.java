package com.example.ecommerceshop.Phat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Model.Photo;
import com.example.ecommerceshop.R;

import java.util.ArrayList;

public class AdapterImgAdsShop extends RecyclerView.Adapter<AdapterImgAdsShop.PhotoViewholder> {
    private   Context context;
    private ArrayList<Photo> photoArrayList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }
    public AdapterImgAdsShop(Context context,  ArrayList<Photo> photoArrayList) {
        this.context = context;
        this.photoArrayList = photoArrayList;
    }

    @NonNull
    @Override
    public PhotoViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_ads_img_shop,parent,false);
        return new PhotoViewholder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewholder holder, int position) {
        Photo photo = photoArrayList.get(position);
        Glide.with(context).load(photo.getUri()).into(holder.imgAds);

    }

    @Override
    public int getItemCount() {
        return photoArrayList.size();
    }

    public static class PhotoViewholder extends RecyclerView.ViewHolder {
        ImageView imgAds, delBtn;
        public PhotoViewholder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            imgAds=itemView.findViewById(R.id.imgAds);
            delBtn=itemView.findViewById(R.id.deletebtn);
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
