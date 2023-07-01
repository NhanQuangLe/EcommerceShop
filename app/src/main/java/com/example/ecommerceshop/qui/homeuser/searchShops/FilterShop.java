package com.example.ecommerceshop.qui.homeuser.searchShops;

import android.widget.Filter;

import com.example.ecommerceshop.Phat.Model.RequestShop;
import com.example.ecommerceshop.qui.homeuser.Product;
import com.example.ecommerceshop.qui.homeuser.searchProducts.ShopAdapter;

import java.util.ArrayList;
import java.util.List;

public class FilterShop extends Filter {
    private ShopAdapter shopAdapter;

    private List<RequestShop> shops;
    public FilterShop(ShopAdapter shopAdapter, List<RequestShop> shops) {
        this.shopAdapter = shopAdapter;
        this.shops = shops;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();
        if(charSequence !=null && charSequence.length()>0){
            charSequence = charSequence.toString().toLowerCase();
            ArrayList<RequestShop> filterproducts = new ArrayList<>();
            for(int i=0; i<shops.size();i++){
                if(shops.get(i).getShopName().toLowerCase().contains(charSequence)||shops.get(i).getShopAddress().toLowerCase().contains(charSequence)){
                    filterproducts.add(shops.get(i));
                }
            }
            filterResults.count=filterproducts.size();
            filterResults.values=filterproducts;
        }
        else {
            filterResults.count=shops.size();
            filterResults.values=shops;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        shopAdapter.mList = (ArrayList<RequestShop>)filterResults.values;
        shopAdapter.notifyDataSetChanged();
    }
}
