package com.example.ecommerceshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ecommerceshop.chat.MainChatActivity;
import com.example.ecommerceshop.databinding.ActivityHomeUserBinding;
import com.example.ecommerceshop.nhan.ProfileCustomer.UserProfileFragment;
import com.example.ecommerceshop.qui.cart.CartActivity;
import com.example.ecommerceshop.qui.homeuser.HomeFragmentUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainUserActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private ActivityHomeUserBinding mActivityHomeUserBinding;
    public static int  ACTION_SEARCH = 0;
    public static int ACTION_CATEGORY= 1;
    public  int CATEGORY;
    public int ACTION;
    public static final int FRAGMENT_HOME  = 0;
    public static final int FRAGMENT_ALL_PRODUCT  = 1;
    public static final int FRAGMENT_ALL_SHOP  = 2;
    public int mCurrentFragment = FRAGMENT_HOME;
    private HomeFragmentUser homeFragmentUser;
    public  String textSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHomeUserBinding = ActivityHomeUserBinding.inflate(getLayoutInflater());
        setContentView(mActivityHomeUserBinding.getRoot());
        homeFragmentUser = new HomeFragmentUser();
        //replaceFragment(homeFragmentUser);
        replaceFragment(homeFragmentUser);
        bottomNavigationView =findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        replaceFragment(new HomeFragmentUser());
                        break;
                    case R.id.cart:
                        Intent i = new Intent(getApplicationContext(), CartActivity.class);
                        startActivity(i);
                        break;
//                    case R.id.notification:
//                        ReplaceFragment(new VoucherShopFragment());
//                        textView.setText("VOUCHERS");
//                        break;
                    case R.id.profile_user:
                        replaceFragment(new UserProfileFragment());
                        break;
                    case R.id.chat:
                    {
                        Intent i2 = new Intent(getApplicationContext(), MainChatActivity.class);
                        startActivity(i2);
                        break;
                    }
                }
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (homeFragmentUser.getmFragmentHomeUserBinding()!=null){
            if (homeFragmentUser.getmFragmentHomeUserBinding().drawer.isDrawerOpen(GravityCompat.START)){
                homeFragmentUser.getmFragmentHomeUserBinding().drawer.closeDrawer(GravityCompat.START);
            }
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
        return this.textSearch;
    }

}