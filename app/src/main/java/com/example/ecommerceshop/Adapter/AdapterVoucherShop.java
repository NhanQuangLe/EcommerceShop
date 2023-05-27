package com.example.ecommerceshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.Activity.UpdateVoucherShopActivity;
import com.example.ecommerceshop.Model.Voucher;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.Utils.FilterProduct;
import com.example.ecommerceshop.Utils.FilterVoucher;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdapterVoucherShop extends RecyclerView.Adapter<AdapterVoucherShop.voucherHolder> implements Filterable {
   private Context context;
   public ArrayList<Voucher> vouchers,filterlist;
    private FilterVoucher filterVoucher;

    public AdapterVoucherShop(Context context, ArrayList<Voucher> vouchers) {
        this.context = context;
        this.vouchers = vouchers;
        this.filterlist=vouchers;
    }

    @NonNull
    @Override
    public voucherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_voucher_item_shop, parent, false);
        return new voucherHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull voucherHolder holder, int position) {
        //get data
        Voucher voucher = vouchers.get(position);
        String id = voucher.getVoucherid();
        String code = voucher.getVouchercode();
        String des = voucher.getVoucherdes();
        String expireddate = voucher.getExpiredDate();
        int quant= voucher.getQuantity();
        int disprice = voucher.getDiscountPrice();
        int minprice = voucher.getMinimumPrice();

        //set data into viewholder
        holder.vouchercode.setText(code);
        holder.discountprice.setText(String.valueOf(disprice));
        holder.expireddate.setText(expireddate);
        holder.quantity.setText(String.valueOf(quant));
        holder.startdate.setText(getDate(Long.parseLong(id)));
        DecimalFormat df = new DecimalFormat("00");
        boolean checkdate=false;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        String today = day+"/"+month+"/"+year;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date mtoday = simpleDateFormat.parse(today);
            Date expiredDate = simpleDateFormat.parse(expireddate);
            if(expiredDate.compareTo(mtoday)<0){
                checkdate=true;
            }
        }catch (Exception e){

        }
        if(checkdate||quant==0){
            holder.voucherstatus.setText("Expired");
            holder.voucherstatus.setTextColor(context.getColor(R.color.primary_red));
        }
        else{
            holder.voucherstatus.setText("UnExpired");
            holder.voucherstatus.setTextColor(0xFF4F9B14);
        }
        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateVoucherShopActivity.class);
                intent.putExtra("voucherid", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }
    private String getDate(long timestamp){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(timestamp);
    }

    @Override
    public Filter getFilter() {
        if(filterVoucher == null){
            filterVoucher=new FilterVoucher(this, filterlist);
        }
        return filterVoucher;
    }

    public static  class voucherHolder extends RecyclerView.ViewHolder{
        TextView vouchercode, startdate,discountprice,expireddate,quantity,voucherstatus;
        AppCompatButton btn_detail;
        public voucherHolder(@NonNull View itemView) {
            super(itemView);
            vouchercode=itemView.findViewById(R.id.vouchercode);
            startdate=itemView.findViewById(R.id.startdate);
            discountprice=itemView.findViewById(R.id.discountprice);
            expireddate=itemView.findViewById(R.id.expireddate);
            quantity=itemView.findViewById(R.id.quantity);
            voucherstatus=itemView.findViewById(R.id.voucherstatus);
            btn_detail=itemView.findViewById(R.id.btn_detail);
        }
    }
}
