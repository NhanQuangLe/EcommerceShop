package com.example.ecommerceshop.qui.homeuser.searchProducts;

import android.widget.Filter;

import com.example.ecommerceshop.qui.homeuser.Product;
import com.example.ecommerceshop.qui.homeuser.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class FilterProductUser extends Filter {
    private ProductAdapter productAdapter;
    private List<Product> products;

    public FilterProductUser(ProductAdapter productAdapter, List<Product> products) {
        this.productAdapter = productAdapter;
        this.products = products;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();
        if(charSequence !=null && charSequence.length()>0){
            charSequence = charSequence.toString().toLowerCase();
            ArrayList<Product> filterproducts = new ArrayList<>();
            for(int i=0; i<products.size();i++){
                if(products.get(i).getProductName().toLowerCase().contains(charSequence)||products.get(i).getProductCategory().toLowerCase().contains(charSequence)){
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
        productAdapter.mList = (ArrayList<Product>)filterResults.values;
        productAdapter.notifyDataSetChanged();
    }
}
