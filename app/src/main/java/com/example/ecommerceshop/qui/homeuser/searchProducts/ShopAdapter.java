package com.example.ecommerceshop.qui.homeuser.searchProducts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Model.RequestShop;
import com.example.ecommerceshop.databinding.AdapterItemShopCustomerBinding;
import com.example.ecommerceshop.qui.homeuser.searchShops.FilterShop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> implements Filterable {
    public List<RequestShop> mList, filterList;
    private FilterShop filterShop;

    public interface IClickShopItemListener{
         void sendDataShop(RequestShop shop);
    }

    public void setiClickShopItemListener(IClickShopItemListener iClickShopItemListener) {
        this.iClickShopItemListener = iClickShopItemListener;
    }

    private IClickShopItemListener iClickShopItemListener;

    public void setData(List<RequestShop> mList){
        this.mList=mList;
        this.filterList=mList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemShopCustomerBinding mBinding = AdapterItemShopCustomerBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ShopViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        RequestShop shop = mList.get(position);
        if (shop!=null){
            final int[] followers = {0};
            Glide.with(holder.mBinding.getRoot()).load(shop.getShopAvt()).into(holder.mBinding.shopAvatar);
            holder.mBinding.shopName.setText(shop.getShopName());
            holder.mBinding.shopAddress.setText(shop.getShopAddress());
            String shopId = shop.getUid();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users/"+shopId+"/Shop/Products");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    holder.mBinding.shopProductQuantity.setText(snapshot.getChildrenCount()+"");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference("Users");
            myRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (followers[0] !=0) followers[0] =0;
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        DatabaseReference myRef3 = dataSnapshot.getRef().child("Customer").child("Followers");
                        myRef3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                    String shopIdFollower = dataSnapshot1.getValue(String.class);
                                    if (shopIdFollower.equals(shopId)) {
                                        followers[0]++;
                                        break;
                                    }
                                }
                                DecimalFormat df = new DecimalFormat();
                                df.setMaximumFractionDigits(1);
                                String followersStr;
                                if (followers[0] <1000) followersStr = String.valueOf(followers[0]);
                                else followersStr= df.format(followers[0] *1.0/1000);

                                holder.mBinding.shopFollower.setText(followersStr);
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
            FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (shopId.equals(mCurrentUser.getUid())){
                holder.mBinding.yourShop.setVisibility(View.VISIBLE);
            }
            DatabaseReference myRef3 = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/Followers");
            myRef3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        String shopId2 = dataSnapshot.getValue(String.class);
                        if (shopId.equals(shopId2)){
                            holder.mBinding.btnFollow.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            holder.mBinding.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickShopItemListener.sendDataShop(shop);
                }
            });
            setShopRate(shopId,holder);


        }
    }

    @Override
    public int getItemCount() {
        if (mList!=null) return mList.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        if(filterShop == null){
            filterShop=new FilterShop(this, filterList);
        }
        return filterShop;
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder{
        AdapterItemShopCustomerBinding mBinding;
        public ShopViewHolder(@NonNull AdapterItemShopCustomerBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
    private void setShopRate(String shopId, ShopViewHolder holder) {
        final float[] rate = {0};
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int[] temp = {0};
                final int[] i = {0};
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DatabaseReference ref2 = dataSnapshot.getRef().child("Customer/Reviews");
                    ref2.orderByChild("shopId").equalTo(shopId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                if (dataSnapshot1.exists()){
                                    int rating = dataSnapshot1.child("rating").getValue(Integer.class);
                                    temp[0] +=rating;
                                    i[0]++;
                                    rate[0] = (float)temp[0]/i[0];
                                    holder.mBinding.rating.setText(rate[0]+"");
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
