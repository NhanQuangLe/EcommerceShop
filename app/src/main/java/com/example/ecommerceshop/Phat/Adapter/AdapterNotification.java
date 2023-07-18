package com.example.ecommerceshop.Phat.Adapter;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Model.Notification;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.OrderDetailActivity;


import java.util.ArrayList;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.notificationViewHolder> {
    Context context;
    ArrayList<Notification> notifications;

    public AdapterNotification(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public notificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_notification_items, parent, false);
        return new notificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull notificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        Glide.with(context).load(Uri.parse(notification.getAvt())).into(holder.imgview_item);
        if(notification.getOrderStatus().equals("Completed")){
            holder.titleNotification.setText("Bạn có đơn hàng đã giao thành công!");
            holder.contentNotification.setText("Đơn hàng " +notification.getOrderId()+" đã hoàn thành. Bạn hãy đánh giá sản phẩm để nhận được những voucher hấp dẫn nhé!");
        }
        if(notification.getOrderStatus().equals("Processing")){
            holder.titleNotification.setText("Bạn có đơn hàng đang trên đường giao!");
            holder.contentNotification.setText("Đơn hàng " +notification.getOrderId()+" đang trong quá trình vận chuyển và dự kiến sẽ đến trong 1-2 ngày tới. Vui lòng bỏ qua nếu bạn đã nhận được hàng");
        }
        if(notification.getOrderStatus().equals("Cancelled")){
            holder.titleNotification.setText("Bạn có đơn hàng đã bị hủy!");
            holder.contentNotification.setText("Đơn hàng " +notification.getOrderId()+" đã bị hủy vì lý do khách quan. Xin lỗi bạn rất nhiều về sự bất tiện này.");
        }
        holder.regisdate.setText(notification.getDateNotifi());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderId", notification.getOrderId());
                intent.putExtra("isNoti", true);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class notificationViewHolder extends RecyclerView.ViewHolder{
        ImageView imgview_item;
        TextView titleNotification, contentNotification,regisdate;
        public notificationViewHolder(@NonNull View itemView) {
            super(itemView);
            imgview_item=itemView.findViewById(R.id.imgview_item);
            titleNotification=itemView.findViewById(R.id.titleNotification);
            contentNotification=itemView.findViewById(R.id.contentNotification);
            regisdate=itemView.findViewById(R.id.regisdate);

        }
    }
}
