package com.example.ecommerceshop.qui.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityPaymentBinding;
import com.example.ecommerceshop.databinding.FragmentCartBinding;
import com.example.ecommerceshop.qui.cart.CartFragment;
import com.example.ecommerceshop.qui.cart.ProductCart;
import com.example.ecommerceshop.qui.cart.ShopProductCart;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    private ActivityPaymentBinding mActivityPaymentBinding;
    private View mView;
    public static final int FRAGMENT_PAYMENT  = 0;
    public static final int FRAGMENT_ALL_PRODUCT2  = 1;
    public int mCurrentFragment = FRAGMENT_PAYMENT;
    public Fragment paymentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityPaymentBinding = ActivityPaymentBinding.inflate(getLayoutInflater());
        mView = mActivityPaymentBinding.getRoot();
        setContentView(mView);

        Bundle bundle = getIntent().getExtras();
        ArrayList<ProductCart> listSelectedCart = bundle.getParcelableArrayList("listSelectedCart");
        Bundle bundle1 = new Bundle();
        bundle1.putParcelableArrayList("listSelectedCart",listSelectedCart);
        paymentFragment = new PaymentFragment();
        paymentFragment.setArguments(bundle1);
        replaceFragment(paymentFragment);
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view,fragment);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commitAllowingStateLoss();
    }

}