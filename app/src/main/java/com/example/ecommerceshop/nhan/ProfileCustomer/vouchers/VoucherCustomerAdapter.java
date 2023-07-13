package com.example.ecommerceshop.nhan.ProfileCustomer.vouchers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.qui.payment.Voucher;

import java.util.ArrayList;

public class VoucherCustomerAdapter extends RecyclerView.Adapter<VoucherCustomerAdapter.VoucherCustomerViewHolder> {
    private Context context;
    private ArrayList<Voucher> listVouchers;

    public VoucherCustomerAdapter(Context context, ArrayList<Voucher> vouchers) {
        this.context = context;
        this.listVouchers = vouchers;
    }

    @NonNull
    @Override
    public VoucherCustomerAdapter.VoucherCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_voucher_customer, parent,false);
        return new VoucherCustomerAdapter.VoucherCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherCustomerAdapter.VoucherCustomerViewHolder holder, int position) {
            Voucher voucher = listVouchers.get(position);
            holder.checkbox.setVisibility(View.INVISIBLE);
            holder.ib_DeleteVoucher.setVisibility(View.VISIBLE);
            holder.ib_DeleteVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listVouchers.remove(voucher);
                    notifyDataSetChanged();
                }
            });
            holder.tv_voucher_code.setText(voucher.getVouchercode());
            holder.tv_voucher_des.setText(voucher.getVoucherdes());
            holder.tv_voucher_expired.setText(voucher.getExpiredDate());
    }

    @Override
    public int getItemCount() {
            return listVouchers.size();
    }

    public static class VoucherCustomerViewHolder extends RecyclerView.ViewHolder{
        TextView tv_voucher_code, tv_voucher_des, tv_voucher_expired;
        ImageButton ib_DeleteVoucher;
        CheckBox checkbox;
        public VoucherCustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_voucher_code = itemView.findViewById(R.id.tv_voucher_code);
            tv_voucher_des = itemView.findViewById(R.id.tv_voucher_des);
            tv_voucher_expired = itemView.findViewById(R.id.tv_voucher_expired);
            ib_DeleteVoucher = itemView.findViewById(R.id.ib_DeleteVoucher);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
