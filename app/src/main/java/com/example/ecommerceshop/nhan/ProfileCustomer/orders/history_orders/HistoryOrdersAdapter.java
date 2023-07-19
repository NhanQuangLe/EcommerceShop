package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.nhan.Model.Review;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.Order;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.review.ProductInReviewAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.review.ReviewActivity;
import com.example.ecommerceshop.qui.shop.ShopActivityCustomer;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryOrdersAdapter extends RecyclerView.Adapter<HistoryOrdersAdapter.OrderViewholder> {

    private Context context;
    private ArrayList<Order> orders;
    private HistoryProductsInOrderAdapter historyProductsInOrderAdapter;
    private IClickHistoryOrderListener mClickHistoryOrderListener;
    public HistoryOrdersAdapter(Context context, ArrayList<Order> orders, IClickHistoryOrderListener a) {
        this.context = context;
        this.orders = orders;
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
        holder.btn_RatingProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickHistoryOrderListener.GoToReview(order);
            }
        });
        holder.item_order_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopActivityCustomer.class);
                intent.putExtra("shopId", order.getShopId());
                context.startActivity(intent);
            }
        });
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getUid()).child("Customer").child("Reviews");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Product> productList = order.getItems();
                int count = 0;
                for (int i = 0; i < productList.size(); i++) {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.child("productId").getValue(String.class).equals(productList.get(i).getProductID())) {
                            count = count + 1;
                        }
                    }
                }
                if(count == productList.size()){
                    holder.btn_Rate.setText("Đã đánh giá");
                    holder.btn_Rate.setTextColor(Color.parseColor("#00c5a5"));
                    holder.btn_RatingProduct.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                CustomToast.makeText(context,"Lỗi hệ thống",CustomToast.SHORT,CustomToast.ERROR).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewholder extends RecyclerView.ViewHolder{
        ImageView iv_ShopAvatar;
        TextView tv_ShopName;
        RecyclerView rv_ProductList;
        public TextView tv_SumMoney, btn_Rate;
        AppCompatButton aBtn_DetailOrder, aBtn_ReBuy;
        public LinearLayout btn_RatingProduct, container;
        ConstraintLayout item_order_shop;

        public OrderViewholder(@NonNull View itemView) {
            super(itemView);
            iv_ShopAvatar = itemView.findViewById(R.id.iv_ShopAvatar);
            tv_ShopName = itemView.findViewById(R.id.tv_ShopName);
            rv_ProductList = itemView.findViewById(R.id.rv_ProductList);
            tv_SumMoney = itemView.findViewById(R.id.tv_SumMoney);
            aBtn_DetailOrder = itemView.findViewById(R.id.aBtn_DetailOrder);
            aBtn_ReBuy = itemView.findViewById(R.id.aBtn_ReBuy);
            btn_RatingProduct = itemView.findViewById(R.id.btn_RatingProduct);
            container = itemView.findViewById(R.id.container);
            item_order_shop = itemView.findViewById(R.id.item_order_shop);
            btn_Rate = itemView.findViewById(R.id.btn_Rate);
        }
    }
}