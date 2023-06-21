package com.example.ecommerceshop.Phat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Activity.RegistrationToShopInAdminActivity;
import com.example.ecommerceshop.Phat.Model.RequestShop;
import com.example.ecommerceshop.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListRequest extends RecyclerView.Adapter<AdapterListRequest.RequestViewHolder> {
    Context context;
    ArrayList<RequestShop> requestShops;

    public AdapterListRequest(Context context, ArrayList<RequestShop> requestShops) {
        this.context = context;
        this.requestShops = requestShops;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_list_shops_admin, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestShop requestShop = requestShops.get(position);
        Glide.with(context).load(Uri.parse(requestShop.getShopAvt())).into(holder.imgview_item);
        holder.shopname.setText(requestShop.getShopName());
        holder.shopemail.setText(requestShop.getShopEmail());
        holder.shopphone.setText(requestShop.getShopPhone());
        holder.regisdate.setText(getDate(Long.parseLong(requestShop.getTimestamp())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, RegistrationToShopInAdminActivity.class);
                intent.putExtra("id", requestShop.getUid());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return requestShops.size();
    }
    private String getDate(long timestamp){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(timestamp);
    }
    public static class RequestViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imgview_item;
        TextView shopname, shopemail,shopphone,regisdate;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            imgview_item=itemView.findViewById(R.id.imgview_item);
            shopname=itemView.findViewById(R.id.shopname);
            shopemail=itemView.findViewById(R.id.shopemail);
            shopphone=itemView.findViewById(R.id.shopphone);
            regisdate=itemView.findViewById(R.id.regisdate);

        }
    }
}
