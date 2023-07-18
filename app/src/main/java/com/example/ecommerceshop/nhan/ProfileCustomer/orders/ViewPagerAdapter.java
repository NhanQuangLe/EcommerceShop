package com.example.ecommerceshop.nhan.ProfileCustomer.orders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.ecommerceshop.nhan.ProfileCustomer.orders.cancel_orders.CancelOrderFragment;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.delivery_orders.DeliveryOrderFragment;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrdersFragment;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.unprocessed_orders.UnProcessdOrderFragment;

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
                return new UnProcessdOrderFragment();
            case 1:
                return new DeliveryOrderFragment();
            case 2:
                return new HistoryOrdersFragment();
            case 3:
                return new CancelOrderFragment();
        }
        return new HistoryOrdersFragment();
    }

    @Override
    public int getCount() {
        return 4;
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
                title = "Đang giao";
                break;
            case 2:
                title = "Đã giao";
                break;
            case 3:
                title = "Đã hủy";
                break;
        }
        return title;
    }
}
