package com.example.ecommerceshop.qui.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityCartBinding;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.Order;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrdersFragment;

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

        Intent intent = getIntent();

        if(intent.getIntExtra("Key", 0) == HistoryOrdersFragment.HISTORY_ORDER)
            replaceFragment(new CartFragment((Order) intent.getSerializableExtra("HistoryOrder")));
        else
            replaceFragment(new CartFragment());
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view,fragment);
        transaction.commitAllowingStateLoss();
    }
}