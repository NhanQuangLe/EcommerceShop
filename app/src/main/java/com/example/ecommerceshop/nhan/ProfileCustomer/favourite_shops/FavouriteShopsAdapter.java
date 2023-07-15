package com.example.ecommerceshop.nhan.ProfileCustomer.favourite_shops;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Shop;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouriteShopsAdapter extends RecyclerView.Adapter<FavouriteShopsAdapter.FavouriteShopsViewholder> {
    private Context context;
    public ArrayList<Shop> favouriteShops;
    public IClickFavouriteShopListener mClickFavouriteShopListener;
    public FavouriteShopsAdapter(Context context, ArrayList<Shop> shops, IClickFavouriteShopListener listener) {
        this.context = context;
        this.favouriteShops = shops;
        this.mClickFavouriteShopListener = listener;
    }

    @NonNull
    @Override
    public FavouriteShopsAdapter.FavouriteShopsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_shop_favourite, parent,false);
        return new FavouriteShopsAdapter.FavouriteShopsViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteShopsAdapter.FavouriteShopsViewholder holder, int position) {
        Shop favouriteShop = favouriteShops.get(position);
        Picasso.get().load(Uri.parse(favouriteShop.getShopAvatar())).into(holder.iv_ShopAvatar);
        holder.tv_ShopName.setText(favouriteShop.getShopName());
        holder.tv_ShopEmail.setText(favouriteShop.getShopEmail());
        holder.tv_ShopAddress.setText(favouriteShop.getShopAddress());
        holder.tv_ShopRating.setText(favouriteShop.getRating() + "");
        holder.tv_NumberFollower.setText(Long.toString(favouriteShop.getNumberFollowers()));
        holder.btn_UnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickFavouriteShopListener.UnFollowShop(favouriteShop);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteShops.size();
    }

    public static class FavouriteShopsViewholder extends RecyclerView.ViewHolder{
          ImageView iv_ShopAvatar;
          TextView tv_ShopName, tv_ShopEmail, tv_ShopAddress, tv_ShopRating, tv_NumberFollower;
          AppCompatButton btn_UnFollow;
        public FavouriteShopsViewholder(@NonNull View itemView) {
            super(itemView);
            iv_ShopAvatar = itemView.findViewById(R.id.iv_ShopAvatar);
            tv_ShopName = itemView.findViewById(R.id.tv_ShopName);
            tv_ShopEmail = itemView.findViewById(R.id.tv_ShopEmail);
            tv_ShopAddress = itemView.findViewById(R.id.tv_ShopAddress);
            tv_ShopRating = itemView.findViewById(R.id.tv_ShopRating);
            tv_NumberFollower = itemView.findViewById(R.id.tv_NumberFollower);
            btn_UnFollow = itemView.findViewById(R.id.btn_UnFollow);
        }
    }
}
