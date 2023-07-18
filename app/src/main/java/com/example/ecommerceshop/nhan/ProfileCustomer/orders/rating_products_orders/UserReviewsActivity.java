package com.example.ecommerceshop.nhan.ProfileCustomer.orders.rating_products_orders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Cart;
import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.nhan.Model.Review;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products.FavouriteProductsActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products.FavouriteProductsAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products.IClickFavouriteProductListener;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products.IClickProductType;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products.ProductTypeAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class UserReviewsActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ImageView ic_back;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_reviews);
        firebaseAuth = FirebaseAuth.getInstance();
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        ic_back = findViewById(R.id.ic_back);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ReviewViewPagerAdapter viewPagerAdapter = new ReviewViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).select();

    }
}
