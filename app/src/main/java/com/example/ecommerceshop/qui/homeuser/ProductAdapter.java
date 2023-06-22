package com.example.ecommerceshop.qui.homeuser;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Utils.FilterProduct;
import com.example.ecommerceshop.databinding.AdapterItemProductCustomerBinding;
import com.example.ecommerceshop.qui.homeuser.searchProducts.FilterProductUser;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {

    public List<Product> mList, filterList;
    private IClickProductItemListener iClickProductItemListener;
    private FilterProductUser filterProduct;
    public ProductAdapter(List<Product> mList,IClickProductItemListener listener ) {
        this.mList = mList;
        this.iClickProductItemListener = listener;
        this.filterList = mList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemProductCustomerBinding mBinding = AdapterItemProductCustomerBinding.inflate(LayoutInflater.from(parent.getContext())
        ,parent,false);
        return new ProductViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mList.get(position);
        if (product!=null){
            holder.mBinding.productName.setText(product.getProductName());
            holder.mBinding.productBrand.setText(product.getProductBrand());

            Glide.with(holder.mBinding.getRoot()).load(product.getUriList().get(0)).into(holder.mBinding.productImage);
            if (product.getProductDiscountPrice()==0){
                holder.mBinding.productDiscountPrice.setVisibility(View.GONE);
                holder.mBinding.frameDiscount.setVisibility(View.GONE);
                holder.mBinding.productPrice.setText(product.getPriceStr());
            }
            else {
                holder.mBinding.productDiscountPrice.setVisibility(View.VISIBLE);
                holder.mBinding.frameDiscount.setVisibility(View.VISIBLE);
                holder.mBinding.productPrice.setText(product.getPriceAfterDiscountStr());
                holder.mBinding.productDiscountPrice.setText(product.getPriceStr());
                holder.mBinding.productDiscountPrice.setPaintFlags(holder.mBinding.productDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.mBinding.productDiscountPercent.setText(product.getPercentDiscountStr());
            }
            holder.mBinding.layoutProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickProductItemListener.sentDataProduct(product);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if (mList!=null) return  mList.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        if(filterProduct == null){
            filterProduct=new FilterProductUser(this, filterList);
        }
        return filterProduct;
    }

    public  class ProductViewHolder extends RecyclerView.ViewHolder{
        private AdapterItemProductCustomerBinding mBinding;
        public ProductViewHolder(@NonNull AdapterItemProductCustomerBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding=mBinding;
        }
    }
}
