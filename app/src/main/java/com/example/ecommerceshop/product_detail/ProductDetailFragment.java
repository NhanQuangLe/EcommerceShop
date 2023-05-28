package com.example.ecommerceshop.product_detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityProductDetailBinding;
import com.example.ecommerceshop.databinding.FragmentAllProductsBinding;
import com.example.ecommerceshop.databinding.FragmentProductDetailBinding;


public class ProductDetailFragment extends Fragment {

    private FragmentProductDetailBinding mFragmentAllProductsBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentAllProductsBinding = FragmentProductDetailBinding.inflate(inflater,container,false);
        return mFragmentAllProductsBinding.getRoot();
    }
}