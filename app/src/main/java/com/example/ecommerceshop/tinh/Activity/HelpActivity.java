package com.example.ecommerceshop.tinh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.tinh.models.HelpViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class HelpActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        tabLayout = findViewById(R.id.tab_layout_help);
        viewPager = findViewById(R.id.view_pager);
        buttonBack = findViewById(R.id.backbtn);

        HelpViewPagerAdapter helpViewPagerAdapter = new HelpViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(helpViewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        buttonBack.setOnClickListener(v -> onBackPressed());
    }
}