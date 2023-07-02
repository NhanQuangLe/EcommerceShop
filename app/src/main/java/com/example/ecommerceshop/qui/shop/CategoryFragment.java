package com.example.ecommerceshop.qui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.databinding.FragmentCategoryBinding;
import com.example.ecommerceshop.databinding.FragmentShopCustomerBinding;
import com.example.ecommerceshop.qui.homeuser.IClickProductItemListener;
import com.example.ecommerceshop.qui.homeuser.Product;
import com.example.ecommerceshop.qui.homeuser.ProductAdapter;
import com.example.ecommerceshop.qui.payment.Voucher;
import com.example.ecommerceshop.qui.product_detail.ProductDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CategoryFragment extends Fragment  {

    private FirebaseUser mCurrentUser;
    private ShopActivityCustomer mActivity;
    private String shopId;
    private FragmentCategoryBinding mFragmentCategoryBinding;
    private ViewPagerCategoryAdapter mViewPagerCategoryAdapter;
    private View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentCategoryBinding = FragmentCategoryBinding.inflate(inflater,container,false);
        mView = mFragmentCategoryBinding.getRoot();
        init();

        return mView;
    }

    public void init(){
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mActivity = (ShopActivityCustomer) getActivity();
        this.shopId = mActivity.getShopId();



    }
}