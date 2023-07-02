package com.example.ecommerceshop.Phat.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Model.OrderItem;
import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;

import java.util.ArrayList;

public class AdapterListItemOrderDetail extends RecyclerView.Adapter<AdapterListItemOrderDetail.itemOrderViewHolder>{
    private Context context;
    private ArrayList<OrderItem>  orderItems;

    public AdapterListItemOrderDetail(Context context, ArrayList<OrderItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public itemOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_in_order_list, parent, false);
        return new itemOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemOrderViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        Glide.with(context).load(Uri.parse(orderItem.getpAvatar())).into(holder.imgview_item);
        holder.ordItemName.setText(orderItem.getpName());
        holder.ordItemBrand.setText(orderItem.getpBrand());
        holder.orderItemQuantity.setText(String.valueOf(orderItem.getpQuantity()));
        holder.ordItemPrice.setText(Constants.convertToVND(orderItem.getpPrice()));
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class itemOrderViewHolder extends RecyclerView.ViewHolder{
        ImageView imgview_item;
        TextView ordItemName, ordItemBrand, orderItemQuantity, ordItemPrice;
        public itemOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgview_item=itemView.findViewById(R.id.imgview_item);
            ordItemName=itemView.findViewById(R.id.ordItemName);
            ordItemBrand=itemView.findViewById(R.id.ordItemBrand);
            orderItemQuantity=itemView.findViewById(R.id.orderItemQuantity);
            ordItemPrice=itemView.findViewById(R.id.ordItemPrice);

        }
    }
}
