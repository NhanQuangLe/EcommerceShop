package com.example.ecommerceshop.qui.shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerCategoryAdapter extends FragmentStatePagerAdapter {
    public ViewPagerCategoryAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CategoryFragment();
            case 1:
                return new CategoryFragment();
            case 2:
                return new CategoryFragment();
            default:
                return new CategoryFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title ="";
        switch (position){
            case 0:
                title="Laptop";
                break;
            case 1:
                title ="Điện thoại";
                break;
            case 2:
                title ="Phụ kiện";
                break;
        }
        return title;
    }
}
