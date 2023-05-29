package com.example.ecommerceshop.homeuser;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.databinding.AdapterItemProductCustomerBinding;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> mList;
    private IClickProductItemListener iClickProductItemListener;

    public ProductAdapter(List<Product> mList,IClickProductItemListener listener ) {
        this.mList = mList;
        this.iClickProductItemListener = listener;
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
            holder.mBinding.productPrice.setText(product.getPrice());
            Glide.with(holder.mBinding.getRoot()).load(product.getUriList().get(0)).into(holder.mBinding.productImage);
            if (product.getProductDiscountPrice()==0){
                holder.mBinding.productDiscountPrice.setVisibility(View.GONE);
                holder.mBinding.frameDiscount.setVisibility(View.GONE);
            }
            else {
                holder.mBinding.productDiscountPrice.setText(product.getDiscountPrice());
                holder.mBinding.productDiscountPrice.setPaintFlags(holder.mBinding.productDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.mBinding.productDiscountPercent.setText(product.getPercentDiscount());
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

    public  class ProductViewHolder extends RecyclerView.ViewHolder{
        private AdapterItemProductCustomerBinding mBinding;
        public ProductViewHolder(@NonNull AdapterItemProductCustomerBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding=mBinding;
        }
    }
}
