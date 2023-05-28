package com.example.ecommerceshop.product_detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityProductDetailBinding;

public class ProductDetailActivity extends AppCompatActivity {
    private ActivityProductDetailBinding mActivityProductDetailBinding;
    public static final int FRAGMENT_HOME  = 0;
    public int mCurrentFragment = FRAGMENT_HOME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mActivityProductDetailBinding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(mActivityProductDetailBinding.getRoot());
        replaceFragment(new ProductDetailFragment());

    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view,fragment);
        transaction.commitAllowingStateLoss();
    }
}