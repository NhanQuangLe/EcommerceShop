package com.example.ecommerceshop.homeuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityHomeUserBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeUserActivity extends AppCompatActivity {


    private ActivityHomeUserBinding mActivityHomeUserBinding;
    public static int  ACTION_SEARCH = 0;
    public static int ACTION_CATEGORY= 1;
    public  int CATEGORY;
    public int ACTION;
    public static final int FRAGMENT_HOME  = 0;
    public static final int FRAGMENT_ALL_PRODUCT  = 1;
    public int mCurrentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHomeUserBinding = ActivityHomeUserBinding.inflate(getLayoutInflater());
        setContentView(mActivityHomeUserBinding.getRoot());

        replaceFragment(new HomeFragmentUser());

        setSupportActionBar(mActivityHomeUserBinding.toolbarHomeUser);
        mActivityHomeUserBinding.navView.setItemIconTintList(null);
        mActivityHomeUserBinding.buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mActivityHomeUserBinding.drawer.isDrawerOpen(GravityCompat.START)){
                    mActivityHomeUserBinding.drawer.openDrawer(GravityCompat.START);
                }

            }
        });
        Button btnBackward = mActivityHomeUserBinding.navView.inflateHeaderView(R.layout.header_view).findViewById(R.id.btnBackward);
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActivityHomeUserBinding.drawer.isDrawerOpen(GravityCompat.START)){
                    mActivityHomeUserBinding.drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        mActivityHomeUserBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ACTION = ACTION_SEARCH;
                mCurrentFragment = FRAGMENT_ALL_PRODUCT;
                replaceFragment(new AllProductsFragment());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mActivityHomeUserBinding.drawer.isDrawerOpen(GravityCompat.START)){
            mActivityHomeUserBinding.drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if (mCurrentFragment!=FRAGMENT_HOME){
                mCurrentFragment = FRAGMENT_HOME;
                replaceFragment(new HomeFragmentUser());
            }
            else {
                super.onBackPressed();
            }

        }

    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content_view,fragment);
        transaction.commitAllowingStateLoss();
    }

    public String getTextSearch(){
        return mActivityHomeUserBinding.editTextSearch.getText().toString().trim();
    }




}