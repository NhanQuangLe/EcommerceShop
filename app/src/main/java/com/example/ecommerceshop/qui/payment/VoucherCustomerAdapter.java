package com.example.ecommerceshop.qui.payment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.AdapterItemVoucherCustomerBinding;
import com.google.firebase.database.core.Context;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class VoucherCustomerAdapter extends RecyclerView.Adapter<VoucherCustomerAdapter.VoucherCustomerViewHolder>{

    private List<Voucher> mListVoucher;

    public interface ICheckedChangeListener{
        void sendStatus(boolean b, Voucher voucher);
    }
    private ICheckedChangeListener iCheckedChangeListener;

    public VoucherCustomerAdapter(ICheckedChangeListener iCheckedChangeListener) {
        this.iCheckedChangeListener = iCheckedChangeListener;
    }

    public void setData(List<Voucher> list){
        this.mListVoucher=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VoucherCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemVoucherCustomerBinding mBinding = AdapterItemVoucherCustomerBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new VoucherCustomerViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherCustomerViewHolder holder, int position) {
        Voucher voucher = mListVoucher.get(position);
        if (voucher!=null){
            holder.mBinding.tvVoucherCode.setText(voucher.getVouchercode());
            holder.mBinding.tvVoucherDes.setText(voucher.getVoucherdes());
            holder.mBinding.tvVoucherExpired.setText(voucher.getExpiredDate());
            holder.mBinding.discountPrice.setText(getPrice(voucher.getDiscountPrice()));
            if (voucher.isCanUse()){
                holder.mBinding.layoutVoucher.setBackgroundColor(Color.parseColor("#ECE8E8"));
            }
            else {
                holder.mBinding.layoutVoucher.setBackgroundColor(Color.parseColor("#D9D9D9"));
                holder.mBinding.checkbox.setEnabled(false);
            }
            if (voucher.isCheck()){
                holder.mBinding.checkbox.setChecked(true);
            }
            else {
                holder.mBinding.checkbox.setChecked(false);
            }
            holder.mBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        iCheckedChangeListener.sendStatus(b,voucher);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mListVoucher!=null) return mListVoucher.size();
        return 0;
    }

    public class VoucherCustomerViewHolder extends RecyclerView.ViewHolder{
        AdapterItemVoucherCustomerBinding mBinding;
        public VoucherCustomerViewHolder(@NonNull AdapterItemVoucherCustomerBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding=mBinding;
        }
    }
    public String getPrice(long money) {
        long res = money;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String str1 = currencyVN.format(res);
        return str1;
    }
}
