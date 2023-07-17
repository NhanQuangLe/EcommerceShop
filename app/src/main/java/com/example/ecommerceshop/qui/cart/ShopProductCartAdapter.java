package com.example.ecommerceshop.qui.cart;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.databinding.AdapterShopListItemOnCartBinding;
import com.example.ecommerceshop.qui.shop.ShopActivityCustomer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ShopProductCartAdapter extends RecyclerView.Adapter<ShopProductCartAdapter.ShopProductCartViewHolder>{

    private Context mContext;
    private IClickProductCartItemListener iClickProductCartItemListener;
    private AdapterShopListItemOnCartBinding adapterShopListItemOnCartBinding;

    public ShopProductCartAdapter(Context mContext, IClickProductCartItemListener iClickProductCartItemListener) {
        this.mContext = mContext;
        this.iClickProductCartItemListener = iClickProductCartItemListener;
    }

    private List<ShopProductCart> mList;

    public void setData(List<ShopProductCart> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShopProductCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         adapterShopListItemOnCartBinding = AdapterShopListItemOnCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ShopProductCartViewHolder(adapterShopListItemOnCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopProductCartViewHolder holder, int position) {
        holder.setAdapter(this);
        final boolean[] flat = {false};
        ShopProductCart shopProductCart = mList.get(position);
        if(shopProductCart!=null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+shopProductCart.getShopId()
            +"/Shop/ShopInfos/shopAvt");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String avt = snapshot.getValue(String.class);
                    Glide.with(mContext).load(avt).into(holder.mBinding.imageShop);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            holder.mBinding.shopName.setText(shopProductCart.getShopName());
            FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (shopProductCart.getShopId()!=null){
                if (shopProductCart.getShopId().equals(mCurrentUser.getUid())){
                    holder.mBinding.yourShop.setVisibility(View.VISIBLE);
                }
                else {
                    holder.mBinding.yourShop.setVisibility(View.GONE);
                }
            }
            holder.mBinding.viewShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ShopActivityCustomer.class);
                    String shopId = shopProductCart.getShopId();
                    intent.putExtra("shopId", shopId);
                    ((CartActivity)mContext).startActivity(intent);
                }
            });

             ProductCartAdapter productCartAdapter = new ProductCartAdapter(mContext, new IClickProductCartItemListener() {
                @Override
                public void sendParentAdapter(boolean b, ProductCart productCart) {

                    if (b) iClickProductCartItemListener.addListSelectedItem(productCart);
                    else iClickProductCartItemListener.removeListSelectedItem(productCart);
                }

                @Override
                public void addListSelectedItem(ProductCart productCart) {

                }

                @Override
                public void removeListSelectedItem(ProductCart productCart) {

                }



                @Override
                public void sendInfoProduct(ProductCart productCart) {
                    iClickProductCartItemListener.showProductDetail(productCart);
                }

                @Override
                public void showProductDetail(ProductCart productCart) {

                }
            });
            productCartAdapter.setData(shopProductCart.getProductCarts());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,RecyclerView.VERTICAL,false);
            holder.mBinding.rcvProductCart.setLayoutManager(linearLayoutManager);
            holder.mBinding.rcvProductCart.setAdapter(productCartAdapter);


        }
    }



    @Override
    public int getItemCount() {
        if (mList!=null) return mList.size();
        return 0;
    }

    public class ShopProductCartViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView.Adapter<?> adapter;
        AdapterShopListItemOnCartBinding mBinding;
        public ShopProductCartViewHolder(@NonNull AdapterShopListItemOnCartBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding=mBinding;
        }
        public void setAdapter(RecyclerView.Adapter<?> adapter) {
            this.adapter = adapter;
        }

        public RecyclerView.Adapter<?> getAdapter() {
            return adapter;
        }
    }


}
