package com.example.ecommerceshop.qui.product_detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityProductDetailBinding;
import com.example.ecommerceshop.qui.homeuser.Product;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailFragment.ISenDataListener {
    private ActivityProductDetailBinding mActivityProductDetailBinding;
    private String textSearch;
    ProductDetailFragment productDetailFragment;
    public static final int FRAGMENT_PRODUCT_DETAIL_ACTIVITY  = 0;
    public static final int FRAGMENT_ALL_PRODUCT2  = 1;
    public int mCurrentFragment = FRAGMENT_PRODUCT_DETAIL_ACTIVITY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mActivityProductDetailBinding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(mActivityProductDetailBinding.getRoot());

        Bundle bundle = getIntent().getExtras();
        Product product = (Product) bundle.get("product");
        productDetailFragment = new ProductDetailFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("product",product);
        productDetailFragment.setArguments(bundle1);
        replaceFragment(productDetailFragment);

    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view,fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void senDataAndReplaceFragment(String textSearch) {
//        this.textSearch = textSearch;
        mCurrentFragment = FRAGMENT_ALL_PRODUCT2;
        Bundle bundle = new Bundle();
        bundle.putString("text",textSearch);
        AllProductsFragment2 allProductsFragment2 = new AllProductsFragment2();
        allProductsFragment2.setArguments(bundle);
        replaceFragment(allProductsFragment2);
    }


    public String getTextSearch(){
        return this.textSearch;
    }

    public void backToDetail(){
        mCurrentFragment = FRAGMENT_PRODUCT_DETAIL_ACTIVITY;
        replaceFragment(productDetailFragment);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment!=FRAGMENT_PRODUCT_DETAIL_ACTIVITY){
            mCurrentFragment = FRAGMENT_PRODUCT_DETAIL_ACTIVITY;
            replaceFragment(productDetailFragment);
        }
        else {
            super.onBackPressed();
        }
    }
}