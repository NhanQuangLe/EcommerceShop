package com.example.ecommerceshop.qui.payment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.databinding.AdapterItemOnCart2Binding;
import com.example.ecommerceshop.qui.cart.ProductCart;

import java.util.List;

public class ProductCartAdapter2 extends RecyclerView.Adapter<ProductCartAdapter2.ProductCartAdapter2ViewHolder>{

    private List<ProductCart> mListProductCart;
    public void setData(List<ProductCart> list){
        this.mListProductCart=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductCartAdapter2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemOnCart2Binding adapterItemOnCart2Binding = AdapterItemOnCart2Binding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ProductCartAdapter2ViewHolder(adapterItemOnCart2Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCartAdapter2ViewHolder holder, int position) {
        ProductCart productCart = mListProductCart.get(position);
        if (productCart!=null){
            holder.adapterItemOnCart2Binding.productName.setText(productCart.getProductName());
            Glide.with(holder.adapterItemOnCart2Binding.getRoot()).load(productCart.getUri()).into(holder.adapterItemOnCart2Binding.productImage);
            if (productCart.getProductDiscountPrice()==0){
                holder.adapterItemOnCart2Binding.finalPrice.setText(productCart.getProductPriceStr());
            }
            else {
                holder.adapterItemOnCart2Binding.finalPrice.setText(productCart.getProductDiscountPriceStr());
            }
            holder.adapterItemOnCart2Binding.productQuantity.setText(""+productCart.getProductQuantity());
        }
    }

    @Override
    public int getItemCount() {
        if (mListProductCart!=null) return mListProductCart.size();
        return 0;
    }

    public class ProductCartAdapter2ViewHolder extends RecyclerView.ViewHolder{
        AdapterItemOnCart2Binding adapterItemOnCart2Binding;

        public ProductCartAdapter2ViewHolder(@NonNull AdapterItemOnCart2Binding adapterItemOnCart2Binding) {
            super(adapterItemOnCart2Binding.getRoot());
            this.adapterItemOnCart2Binding=adapterItemOnCart2Binding;
        }
    }
}
