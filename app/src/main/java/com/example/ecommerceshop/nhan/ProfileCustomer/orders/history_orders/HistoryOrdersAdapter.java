package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryOrdersAdapter extends RecyclerView.Adapter<HistoryOrdersAdapter.OrderViewholder> {

    private Context context;
    private ArrayList<HistoryOrder> historyOrders;
    private HistoryProductsInOrderAdapter historyProductsInOrderAdapter;
    private IClickHistoryOrderListener mClickHistoryOrderListener;
    public HistoryOrdersAdapter(Context context, ArrayList<HistoryOrder> historyOrders, IClickHistoryOrderListener a) {
        this.context = context;
        this.historyOrders = historyOrders;
        this.mClickHistoryOrderListener = a;
    }
    @NonNull
    @Override
    public HistoryOrdersAdapter.OrderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_item, parent,false);
        return new OrderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryOrdersAdapter.OrderViewholder holder, int position) {
        HistoryOrder historyOrder = historyOrders.get(position);
        Picasso.get().load(Uri.parse(historyOrder.getShopAvt())).into(holder.iv_ShopAvatar);
        holder.tv_ShopName.setText(historyOrder.getShopName());
        historyProductsInOrderAdapter = new HistoryProductsInOrderAdapter(context, historyOrder.getItems());
        holder.rv_ProductList.setAdapter(historyProductsInOrderAdapter);
        holder.tv_SumMoney.setText(String.valueOf(historyOrder.getTotalPrice()));
        holder.aBtn_DetailOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickHistoryOrderListener.GoToOrderDetail(historyOrder);
            }
        });
        holder.aBtn_ReBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickHistoryOrderListener.GotoRebuy(historyOrder);
            }
        });
        holder.btn_RatingProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mClickHistoryOrderListener.GoToReview(historyOrder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyOrders.size();
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