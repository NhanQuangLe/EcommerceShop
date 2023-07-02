package com.example.ecommerceshop.qui.shop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.FragmentCategoryBinding;
import com.example.ecommerceshop.databinding.FragmentShopCustomerBinding;
import com.example.ecommerceshop.databinding.FragmentShopProductCategoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ShopProductCategoryFragment extends Fragment {
    private ViewPagerCategoryAdapter mViewPagerCategoryAdapter;
   private FragmentShopProductCategoryBinding mFragmentShopProductCategoryBinding;
   private View mView;
    private ShopActivityCustomer mActivity;
    private FirebaseUser mCurrentUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentShopProductCategoryBinding = FragmentShopProductCategoryBinding.inflate(inflater,container,false);
        mView = mFragmentShopProductCategoryBinding.getRoot();

        init();
        return mView;
    }
    private void init(){
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mActivity = (ShopActivityCustomer) getActivity();
        mViewPagerCategoryAdapter = new ViewPagerCategoryAdapter(mActivity.getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragmentShopProductCategoryBinding.viewPager.setAdapter(mViewPagerCategoryAdapter);
        mFragmentShopProductCategoryBinding.tabLayout.setupWithViewPager(mFragmentShopProductCategoryBinding.viewPager);
    }
}