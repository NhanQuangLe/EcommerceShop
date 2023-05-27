package com.example.ecommerceshop.Utils;

import android.widget.Filter;

import com.example.ecommerceshop.Adapter.AdapterVoucherShop;
import com.example.ecommerceshop.Model.Product;
import com.example.ecommerceshop.Model.Voucher;

import java.util.ArrayList;

public class FilterVoucher extends Filter {
    AdapterVoucherShop adapterVoucherShop;
    ArrayList<Voucher> vouchers;

    public FilterVoucher(AdapterVoucherShop adapterVoucherShop, ArrayList<Voucher> vouchers) {
        this.adapterVoucherShop = adapterVoucherShop;
        this.vouchers = vouchers;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();
        if(charSequence !=null && charSequence.length()>0){
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Voucher> filterVouchers = new ArrayList<>();
            for(int i=0; i<vouchers.size();i++){
                if(vouchers.get(i).getVouchercode().toUpperCase().contains(charSequence)){
                    filterVouchers.add(vouchers.get(i));
                }
            }
            filterResults.count=filterVouchers.size();
            filterResults.values=filterVouchers;
        }
        else {
            filterResults.count=vouchers.size();
            filterResults.values=vouchers;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapterVoucherShop.vouchers = (ArrayList<Voucher>)filterResults.values;
        adapterVoucherShop.notifyDataSetChanged();
    }
}
