package com.example.ecommerceshop.Phat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.Phat.Model.OrderShop;
import com.example.ecommerceshop.R;

import java.util.ArrayList;

public class AdapterOrderShop extends RecyclerView.Adapter<AdapterOrderShop.OrderViewholder> {

    private Context context;
    private ArrayList<OrderShop> orderShops;

    public AdapterOrderShop(Context context, ArrayList<OrderShop> orderShops) {
        this.context = context;
        this.orderShops = orderShops;
    }

    @NonNull
    @Override
    public OrderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_item_shop, parent,false);
        return new OrderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return orderShops.size();
    }

    public static class OrderViewholder extends RecyclerView.ViewHolder{
        TextView orderId, orderDate, email_order, quantityItem, totalPrice, orderStatus;
        AppCompatButton btn_detail;
        public OrderViewholder(@NonNull View itemView) {
            super(itemView);
            orderId=itemView.findViewById(R.id.orderId);
            orderDate=itemView.findViewById(R.id.orderDate);
            email_order=itemView.findViewById(R.id.email_order);
            quantityItem=itemView.findViewById(R.id.quantityItem);
            totalPrice=itemView.findViewById(R.id.totalPrice);
            orderStatus=itemView.findViewById(R.id.orderStatus);
            btn_detail=itemView.findViewById(R.id.btn_detail);
        }
    }
}
