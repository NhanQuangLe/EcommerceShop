package com.example.ecommerceshop.nhan.ProfileCustomer.orders.HistoryOrders;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryOrdersAdapter extends RecyclerView.Adapter<HistoryOrdersAdapter.OrderViewholder> {

    private Context context;
    private ArrayList<HistoryOrder> historyOrders;
    public HistoryOrdersAdapter(Context context, ArrayList<HistoryOrder> historyOrders) {
        this.context = context;
        this.historyOrders = historyOrders;
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
        Picasso.get().load(Uri.parse(historyOrder.getShopAvatar())).into(holder.iv_ShopAvatar);
        holder.tv_ShopName.setText(historyOrder.getShopName());

        Picasso.get().load(Uri.parse(historyOrder.getProduct().getProductAvatar())).into(holder.iv_ProductAvatar);
        holder.tv_ProductName.setText(historyOrder.getProduct().getProductName());
        holder.tv_ProductBrand.setText(historyOrder.getProduct().getProductBrand());
        holder.tv_ProductCategory.setText(historyOrder.getProduct().getProductCategory());
        holder.tv_ProductPurchaseQuantity.setText(Integer.toString(historyOrder.getProduct().getPurchaseQuantity()));
        holder.tv_ProductDiscountPrice.setText(Integer.toString(historyOrder.getProduct().getProductDiscountPrice()));

        holder.tv_SumMoney.setText(Integer.toString(historyOrder.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return historyOrders.size();
    }

    public static class OrderViewholder extends RecyclerView.ViewHolder{
        ImageView iv_ShopAvatar;
        TextView tv_ShopName;

        ImageView iv_ProductAvatar;

        TextView tv_ProductName, tv_ProductBrand, tv_ProductCategory, tv_ProductPurchaseQuantity, tv_ProductDiscountPrice;
        TextView tv_SumMoney;

        public OrderViewholder(@NonNull View itemView) {
            super(itemView);
            iv_ShopAvatar = itemView.findViewById(R.id.iv_ShopAvatar);
            tv_ShopName = itemView.findViewById(R.id.tv_ShopName);
            iv_ProductAvatar = itemView.findViewById(R.id.iv_ProductAvatar);
            tv_ProductName = itemView.findViewById(R.id.tv_ProductName);
            tv_ProductBrand = itemView.findViewById(R.id.tv_ProductBrand);
            tv_ProductCategory = itemView.findViewById(R.id.tv_ProductCategory);
            tv_ProductPurchaseQuantity = itemView.findViewById(R.id.tv_ProductPurchaseQuantity);
            tv_ProductDiscountPrice = itemView.findViewById(R.id.tv_ProductDiscountPrice);
            tv_SumMoney = itemView.findViewById(R.id.tv_SumMoney);
        }
    }
}