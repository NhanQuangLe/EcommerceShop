package com.example.ecommerceshop.qui.shop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.databinding.AdapterItemAdsBinding;

import java.util.List;

public class AdsImageAdapter extends RecyclerView.Adapter<AdsImageAdapter.AdsImageViewHolder> {
    private List<String> mList;

    public void setData(List<String> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdsImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemAdsBinding mBinding = AdapterItemAdsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AdsImageViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsImageViewHolder holder, int position) {
        String uri = mList.get(position);
        if (uri!=null){
            Glide.with(holder.mBinding.getRoot()).load(uri).into(holder.mBinding.adsImage);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public class AdsImageViewHolder extends RecyclerView.ViewHolder {
        AdapterItemAdsBinding mBinding;

        public AdsImageViewHolder(@NonNull AdapterItemAdsBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
