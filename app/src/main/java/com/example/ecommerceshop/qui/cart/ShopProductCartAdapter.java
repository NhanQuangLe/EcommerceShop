package com.example.ecommerceshop.qui.cart;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.databinding.AdapterShopListItemOnCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

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
                public void checkAllCheckbox() {
                    flat[0] =true;
                   holder.mBinding.checkBox.setChecked(false);
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
            if (shopProductCart.isChecked()){
                holder.mBinding.checkBox.setChecked(true);
            }
            else {
                holder.mBinding.checkBox.setChecked(false);
            }
            holder.mBinding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        flat[0] =false;
                        for (ProductCart cart :shopProductCart.getProductCarts()){
                            cart.setChecked(true);
                        }
                        productCartAdapter.setData(shopProductCart.getProductCarts());
                    }
                    else {
                        if (flat[0]==true) return;
                        for (ProductCart cart :shopProductCart.getProductCarts()){
                            cart.setChecked(false);
                        }
                        productCartAdapter.setData(shopProductCart.getProductCarts());

                    }
                }
            });
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
    public void setAllCheckboxChecked(boolean b){
        adapterShopListItemOnCartBinding.checkBox.setChecked(b);
    }

}
