package com.example.ecommerceshop.qui.shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerShopAdapter extends FragmentStatePagerAdapter {
    public ViewPagerShopAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ShopCustomerFragment();
            case 1:
                return new ShopProductCategoryFragment();
            default:
                return new ShopCustomerFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title ="";
        switch (position){
            case 0:
                title="Shop";
                break;
            case 1:
                title ="Danh mục sản phẩm";
                break;
        }
        return title;
    }
}
