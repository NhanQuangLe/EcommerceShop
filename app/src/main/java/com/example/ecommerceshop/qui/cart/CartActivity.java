package com.example.ecommerceshop.qui.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityCartBinding;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding mActivityCartBinding;
    private View mView;

    public static final int FRAGMENT_CART  = 0;
    public static final int FRAGMENT_ALL_PRODUCT2  = 1;
    public int mCurrentFragment = FRAGMENT_CART;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCartBinding = ActivityCartBinding.inflate(getLayoutInflater());
        mView = mActivityCartBinding.getRoot();
        setContentView(mView);


        replaceFragment(new CartFragment());
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view,fragment);
        transaction.commitAllowingStateLoss();
    }
}