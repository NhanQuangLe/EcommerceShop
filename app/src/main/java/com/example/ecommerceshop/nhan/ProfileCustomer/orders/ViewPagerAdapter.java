package com.example.ecommerceshop.nhan.ProfileCustomer.orders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrdersFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                ;
                break;
            case 1:
                ;;
                break;
            case 2:
                ;;;
                break;
            case 3:
                return new HistoryOrdersFragment();
            case 4:
                ;;;;
                break;
        }
        return new HistoryOrdersFragment();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position)
        {
            case 0:
                title = "Chờ xác nhận";
                break;
            case 1:
                title = "Chờ lấy hàng";
                break;
            case 2:
                title = "Đang giao";
                break;
            case 3:
                title = "Đã giao";
                break;
            case 4:
                title = "Đã hủy";
                break;
        }
        return title;
    }
}
