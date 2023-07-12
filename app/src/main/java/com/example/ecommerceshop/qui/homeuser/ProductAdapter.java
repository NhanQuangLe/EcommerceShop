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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
            setRate(product.getProductId(),holder);
            holder.mBinding.ratingBar.setFocusable(false);
            holder.mBinding.ratingBar.setIsIndicator(true);
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
    private void setRate(String productId, @NonNull ProductViewHolder holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        final float[] rate = {0};
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int[] temp = {0};
                final int[] i = {0};
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DatabaseReference ref2 = dataSnapshot.getRef().child("Customer/Reviews");
                    ref2.orderByChild("productId").equalTo(productId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                if (dataSnapshot1.exists()){
                                    int rating = dataSnapshot1.child("rating").getValue(Integer.class);
                                    temp[0] +=rating;
                                    i[0]++;
                                    rate[0] = (float)temp[0]/i[0];
                                    holder.mBinding.ratingBar.setRating(rate[0]);
                                    holder.mBinding.productRate.setText("("+rate[0] +")");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
