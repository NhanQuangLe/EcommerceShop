package com.example.ecommerceshop.nhan.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ecommerceshop.R;
import com.google.android.material.tabs.TabLayout;

public class UserOrdersActivity extends AppCompatActivity {
    private PlaceholderFragment.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static TabLayout tabLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_orders);
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new PlaceholderFragment.SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(intent.getIntExtra("OrderClickType", 0)).select();
    }
    public static class PlaceholderFragment extends Fragment {

        private static final String KEY_COLOR = "key_color";

        public PlaceholderFragment() {
        }

        // Method static dạng singleton, cho phép tạo fragment mới, lấy tham số đầu vào để cài đặt màu sắc.
        public static PlaceholderFragment newInstance(int color) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(KEY_COLOR, color);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.rl_fragment);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            switch (getArguments().getInt(KEY_COLOR)) {
                case 1:
                    relativeLayout.setBackgroundColor(Color.GREEN); textView.setText("Green");
                    break;
                case 2:
                    relativeLayout.setBackgroundColor(Color.RED);textView.setText("red");
                    break;
                case 3:
                    relativeLayout.setBackgroundColor(Color.YELLOW);textView.setText("yellow");
                    break;
                default:
                    relativeLayout.setBackgroundColor(Color.GREEN);textView.setText("Green");
                    break;
            }

            return rootView;
        }

        public static class SectionsPagerAdapter extends FragmentPagerAdapter {
            public SectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                return PlaceholderFragment.newInstance(position + 1);
            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Chờ xác nhận";
                    case 1:
                        return "Chờ lấy hàng";
                    case 2:
                        return "Đang giao";
                    case 3:
                        return "Đã giao";
                    case 4:
                        return "Đã hủy";
                }
                return null;
            }
        }

    }
}
