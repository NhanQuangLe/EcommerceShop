package com.example.ecommerceshop.Phat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.Phat.Activity.OrderDetailShopActivity;
import com.example.ecommerceshop.Phat.Model.OrderShop;
import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.Phat.Utils.FilterOrder;
import com.example.ecommerceshop.Phat.Utils.FilterProduct;
import com.example.ecommerceshop.R;

import java.util.ArrayList;

public class AdapterOrderShop extends RecyclerView.Adapter<AdapterOrderShop.OrderViewholder> implements Filterable {

    private Context context;
    public  ArrayList<OrderShop> orderShops, filterList;
    private FilterOrder filterOrder;

    public AdapterOrderShop(Context context, ArrayList<OrderShop> orderShops) {
        this.context = context;
        this.orderShops = orderShops;
        this.filterList=orderShops;
    }

    @NonNull
    @Override
    public OrderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_item_shop, parent,false);
        return new OrderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewholder holder, int position) {

        OrderShop orderShop= orderShops.get(position);
        String id = orderShop.getOrderId();
        holder.orderId.setText(orderShop.getOrderId());
        holder.orderDate.setText(orderShop.getOrderedDate());
        holder.receiverPhone.setText(orderShop.getReceiveAddress().getPhoneNumber());
        holder.receiverName.setText(orderShop.getReceiveAddress().getFullName());
        holder.orderStatus.setText(orderShop.getOrderStatus());
        holder.totalPrice.setText(Constants.convertToVND(orderShop.getTotalPrice()));

        if(orderShop.getOrderStatus().equals("UnProcessed")){
            holder.orderStatus.setTextColor(Color.RED);
        }
        if(orderShop.getOrderStatus().equals("Processing")){
            holder.orderStatus.setTextColor(Color.BLUE);
        }
        if(orderShop.getOrderStatus().equals("Completed")){
            holder.orderStatus.setTextColor(context.getColor(R.color.green1));
        }
        if(orderShop.getOrderStatus().equals("Cancelled")){
            holder.orderStatus.setTextColor(Color.GRAY);
        }
        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetailShopActivity.class);
                intent.putExtra("orderid", id);
                intent.putExtra("cusid", orderShop.getCustomerId());
                intent.putExtra("voucherUsedId",orderShop.getVoucherUsedId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderShops.size();
    }

    @Override
    public Filter getFilter() {
        if(filterOrder == null){
            filterOrder=new FilterOrder(this, filterList);
        }
        return filterOrder;
    }

    public static class OrderViewholder extends RecyclerView.ViewHolder{
        TextView orderId, orderDate, receiverName, receiverPhone, totalPrice, orderStatus;
        AppCompatButton btn_detail;
        public OrderViewholder(@NonNull View itemView) {
            super(itemView);
            orderId=itemView.findViewById(R.id.orderId);
            orderDate=itemView.findViewById(R.id.orderDate);
            receiverName=itemView.findViewById(R.id.receiverName);
            receiverPhone=itemView.findViewById(R.id.receiverPhone);
            totalPrice=itemView.findViewById(R.id.totalPrice);
            orderStatus=itemView.findViewById(R.id.orderStatus);
            btn_detail=itemView.findViewById(R.id.btn_detail);
        }
    }
}
