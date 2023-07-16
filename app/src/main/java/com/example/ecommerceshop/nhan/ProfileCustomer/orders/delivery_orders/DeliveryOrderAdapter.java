package com.example.ecommerceshop.nhan.ProfileCustomer.orders.delivery_orders;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.Order;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrdersAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryProductsInOrderAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.IClickHistoryOrderListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DeliveryOrderAdapter extends RecyclerView.Adapter<DeliveryOrderAdapter.OrderViewholder> {

    private Context context;
    private ArrayList<Order> orders;
    private HistoryProductsInOrderAdapter historyProductsInOrderAdapter;
    private IClickHistoryOrderListener mClickHistoryOrderListener;
    public DeliveryOrderAdapter(Context context, ArrayList<Order> orders, IClickHistoryOrderListener a) {
        this.context = context;
        this.orders = orders;
        this.mClickHistoryOrderListener = a;
    }
    @NonNull
    @Override
    public DeliveryOrderAdapter.OrderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_item, parent,false);
        return new DeliveryOrderAdapter.OrderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryOrderAdapter.OrderViewholder holder, int position) {
        Order order = orders.get(position);
        Picasso.get().load(Uri.parse(order.getShopAvt())).into(holder.iv_ShopAvatar);
        holder.tv_ShopName.setText(order.getShopName());
        historyProductsInOrderAdapter = new HistoryProductsInOrderAdapter(context, order.getItems());
        holder.rv_ProductList.setAdapter(historyProductsInOrderAdapter);
        holder.tv_SumMoney.setText(String.valueOf(order.getTotalPrice()));
        holder.aBtn_DetailOrder.setVisibility(View.GONE);
        holder.aBtn_ReBuy.setVisibility(View.GONE);
        holder.btn_RatingProduct.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewholder extends RecyclerView.ViewHolder{
        ImageView iv_ShopAvatar;
        TextView tv_ShopName;
        RecyclerView rv_ProductList;
        TextView tv_SumMoney;
        AppCompatButton aBtn_DetailOrder, aBtn_ReBuy;
        LinearLayout btn_RatingProduct;

        public OrderViewholder(@NonNull View itemView) {
            super(itemView);
            iv_ShopAvatar = itemView.findViewById(R.id.iv_ShopAvatar);
            tv_ShopName = itemView.findViewById(R.id.tv_ShopName);
            rv_ProductList = itemView.findViewById(R.id.rv_ProductList);
            tv_SumMoney = itemView.findViewById(R.id.tv_SumMoney);
            aBtn_DetailOrder = itemView.findViewById(R.id.aBtn_DetailOrder);
            aBtn_ReBuy = itemView.findViewById(R.id.aBtn_ReBuy);
            btn_RatingProduct = itemView.findViewById(R.id.btn_RatingProduct);
        }
    }
}