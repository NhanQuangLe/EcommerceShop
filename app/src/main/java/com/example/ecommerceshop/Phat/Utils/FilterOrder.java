package com.example.ecommerceshop.Phat.Utils;

import android.widget.Filter;

import com.example.ecommerceshop.Phat.Adapter.AdapterOrderShop;
import com.example.ecommerceshop.Phat.Adapter.AdapterProductShop;
import com.example.ecommerceshop.Phat.Model.OrderShop;
import com.example.ecommerceshop.Phat.Model.Product;

import java.util.ArrayList;

public class FilterOrder extends Filter {
    private AdapterOrderShop adapterOrderShop;
    private ArrayList<OrderShop> orderShops;

    public FilterOrder(AdapterOrderShop adapterOrderShop, ArrayList<OrderShop> orderShops) {
        this.adapterOrderShop = adapterOrderShop;
        this.orderShops = orderShops;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();
        if(charSequence !=null && charSequence.length()>0){
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<OrderShop> filterOrders = new ArrayList<>();
            for(int i=0; i<orderShops.size();i++){
                if(orderShops.get(i).getOrderId().toUpperCase().contains(charSequence)||orderShops.get(i).getReceiveAddress().getFullName().toUpperCase().contains(charSequence)){
                    filterOrders.add(orderShops.get(i));
                }
            }
            filterResults.count=filterOrders.size();
            filterResults.values=filterOrders;
        }
        else {
            filterResults.count=orderShops.size();
            filterResults.values=orderShops;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapterOrderShop.orderShops = (ArrayList<OrderShop>)filterResults.values;
        adapterOrderShop.notifyDataSetChanged();
    }
}
