package com.example.ecommerceshop.nhan.ProfileCustomer.orders.unprocessed_orders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.Phat.Model.Notification;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.EditAddressActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.Order;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.OrderDetailActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.delivery_orders.DeliveryOrderAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryProductsInOrderAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.IClickHistoryOrderListener;
import com.example.ecommerceshop.qui.shop.ShopActivityCustomer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class UnProcessedOrderAdapter extends RecyclerView.Adapter<UnProcessedOrderAdapter.OrderViewholder> {
    private Context context;
    private ArrayList<Order> orders;
    private HistoryProductsInOrderAdapter historyProductsInOrderAdapter;
    private IClickHistoryOrderListener mClickHistoryOrderListener;
    public UnProcessedOrderAdapter(Context context, ArrayList<Order> orders, IClickHistoryOrderListener a) {
        this.context = context;
        this.orders = orders;
        this.mClickHistoryOrderListener = a;
    }
    @NonNull
    @Override
    public UnProcessedOrderAdapter.OrderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_item, parent,false);
        return new UnProcessedOrderAdapter.OrderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnProcessedOrderAdapter.OrderViewholder holder, int position) {
        Order order = orders.get(position);
        Picasso.get().load(Uri.parse(order.getShopAvt())).into(holder.iv_ShopAvatar);
        holder.tv_ShopName.setText(order.getShopName());
        historyProductsInOrderAdapter = new HistoryProductsInOrderAdapter(context, order.getItems());
        holder.rv_ProductList.setAdapter(historyProductsInOrderAdapter);
        holder.tv_SumMoney.setText(String.valueOf(order.getTotalPrice()));
        holder.aBtn_ReBuy.setText("Hủy đơn hàng");
        holder.btn_Rate.setText("Chở xác nhận");
        holder.btn_Rate.setTextColor(Color.parseColor("#4c4b4b"));
        holder.aBtn_DetailOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickHistoryOrderListener.GoToOrderDetail(order);
            }
        });
        holder.aBtn_ReBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Ecommerce shop")
                        .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này không?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                ref.child("Users").child(FirebaseAuth.getInstance().getUid())
                                        .child("Customer").child("Orders")
                                        .child(order.getOrderId()).child("orderStatus")
                                        .setValue("Cancelled").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                orders.remove(order);
                                                notifyDataSetChanged();
                                                saveNotification("Cancelled", order);
                                                Toast.makeText(context, "Hủy đơn thành công", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();


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
        TextView btn_Rate;
        LinearLayout bottomButtons, container;
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
            bottomButtons = itemView.findViewById(R.id.bottomButtons);
            container = itemView.findViewById(R.id.container);
            item_order_shop = itemView.findViewById(R.id.item_order_shop);
        }
    }
    private void saveNotification(String selectOpt, Order od) {
        String timestamp = ""+System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();

        // Lấy giờ, phút, ngày, tháng, năm hiện tại
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Vì Calendar.MONTH bắt đầu từ 0
        int year = calendar.get(Calendar.YEAR);
        String date = hour+":"+minute+" "+day+"/"+month+"/"+year;
        Notification notification = new Notification(od.getShopAvt(), od.getOrderId(), "Cancelled", od.getCustomerId(), date);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(FirebaseAuth.getInstance().getUid()).child("Customer")
                .child("Notifications").child(timestamp).setValue(notification);
    }
}