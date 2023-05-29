package com.example.ecommerceshop.Phat.Utils;

import android.widget.Filter;

import com.example.ecommerceshop.Phat.Adapter.AdapterProductShop;
import com.example.ecommerceshop.Phat.Model.Product;

import java.util.ArrayList;

public class FilterProduct extends Filter {
    private AdapterProductShop adapterProductShop;
    private ArrayList<Product> products;

    public FilterProduct(AdapterProductShop adapterProductShop, ArrayList<Product> products) {
        this.adapterProductShop = adapterProductShop;
        this.products = products;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();
        if(charSequence !=null && charSequence.length()>0){
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Product> filterproducts = new ArrayList<>();
            for(int i=0; i<products.size();i++){
                if(products.get(i).getProductName().toUpperCase().contains(charSequence)||products.get(i).getProductCategory().toUpperCase().contains(charSequence)){
                    filterproducts.add(products.get(i));
                }
            }
            filterResults.count=filterproducts.size();
            filterResults.values=filterproducts;
        }
        else {
            filterResults.count=products.size();
            filterResults.values=products;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapterProductShop.products = (ArrayList<Product>)filterResults.values;
        adapterProductShop.notifyDataSetChanged();
    }
}
