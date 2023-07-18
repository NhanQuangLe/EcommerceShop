package com.example.ecommerceshop.nhan.ProfileCustomer.orders.rating_products_orders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class ReviewViewPagerAdapter extends FragmentStatePagerAdapter {
    public ReviewViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new RatedProductFragment();
            case 1:
                return new NotRatedProductFragment();
        }
        return new RatedProductFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position)
        {
            case 0:
                title = "Đã đánh giá";
                break;
            case 1:
                title = "Chưa đánh giá";
                break;
        }
        return title;
    }
}
