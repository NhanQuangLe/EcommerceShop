package com.example.ecommerceshop.nhan.ProfileCustomer.orders.cancel_orders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.Order;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrdersAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryProductsInOrderAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.IClickHistoryOrderListener;
import com.example.ecommerceshop.qui.shop.ShopActivityCustomer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CancelOrderAdapter extends RecyclerView.Adapter<CancelOrderAdapter.OrderViewholder> {

    private Context context;
    private ArrayList<Order> orders;
    private HistoryProductsInOrderAdapter historyProductsInOrderAdapter;
    private IClickHistoryOrderListener mClickHistoryOrderListener;

    public CancelOrderAdapter(Context context, ArrayList<Order> orders, IClickHistoryOrderListener a) {
        this.context = context;
        this.orders = orders;
        this.mClickHistoryOrderListener = a;
    }

    @NonNull
    @Override
    public CancelOrderAdapter.OrderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_item, parent, false);
        return new CancelOrderAdapter.OrderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CancelOrderAdapter.OrderViewholder holder, int position) {
        Order order = orders.get(position);
        Picasso.get().load(Uri.parse(order.getShopAvt())).into(holder.iv_ShopAvatar);
        holder.tv_ShopName.setText(order.getShopName());
        historyProductsInOrderAdapter = new HistoryProductsInOrderAdapter(context, order.getItems());
        holder.rv_ProductList.setAdapter(historyProductsInOrderAdapter);
        String  price = Constants.convertToVND(order.getTotalPrice());
        holder.tv_SumMoney.setText(price);
        holder.aBtn_DetailOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickHistoryOrderListener.GoToOrderDetail(order);
            }
        });
        holder.aBtn_ReBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickHistoryOrderListener.GotoRebuy(order);
            }
        });
        holder.btn_Rate.setText("Đã hủy");
        holder.btn_Rate.setTextColor(Color.parseColor("#4c4b4b"));
        holder.item_order_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopActivityCustomer.class);
                intent.putExtra("shopId", order.getShopId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewholder extends RecyclerView.ViewHolder {
        ImageView iv_ShopAvatar;
        TextView tv_ShopName;
        RecyclerView rv_ProductList;
        TextView tv_SumMoney;
        AppCompatButton aBtn_DetailOrder, aBtn_ReBuy;
        TextView btn_Rate;
        LinearLayout container;
        ConstraintLayout item_order_shop;

        public OrderViewholder(@NonNull View itemView) {
            super(itemView);
            iv_ShopAvatar = itemView.findViewById(R.id.iv_ShopAvatar);
            tv_ShopName = itemView.findViewById(R.id.tv_ShopName);
            rv_ProductList = itemView.findViewById(R.id.rv_ProductList);
            tv_SumMoney = itemView.findViewById(R.id.tv_SumMoney);
            aBtn_DetailOrder = itemView.findViewById(R.id.aBtn_DetailOrder);
            aBtn_ReBuy = itemView.findViewById(R.id.aBtn_ReBuy);
            btn_Rate = itemView.findViewById(R.id.btn_Rate);
            container = itemView.findViewById(R.id.container);
            item_order_shop = itemView.findViewById(R.id.item_order_shop);
        }
    }
}