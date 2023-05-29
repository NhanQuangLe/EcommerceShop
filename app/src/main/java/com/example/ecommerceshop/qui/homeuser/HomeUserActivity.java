package com.example.ecommerceshop.qui.homeuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityHomeUserBinding;

public class HomeUserActivity extends AppCompatActivity {


    private ActivityHomeUserBinding mActivityHomeUserBinding;
    public static int  ACTION_SEARCH = 0;
    public static int ACTION_CATEGORY= 1;
    public  int CATEGORY;
    public int ACTION;
    public static final int FRAGMENT_HOME  = 0;
    public static final int FRAGMENT_ALL_PRODUCT  = 1;
    public int mCurrentFragment = FRAGMENT_HOME;
    private HomeFragmentUser homeFragmentUser;
    public  String textSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHomeUserBinding = ActivityHomeUserBinding.inflate(getLayoutInflater());
        setContentView(mActivityHomeUserBinding.getRoot());
        homeFragmentUser = new HomeFragmentUser();
        replaceFragment(homeFragmentUser);

    }

    @Override
    public void onBackPressed() {

        if (homeFragmentUser.getmFragmentHomeUserBinding().drawer.isDrawerOpen(GravityCompat.START)){
            homeFragmentUser.getmFragmentHomeUserBinding().drawer.closeDrawer(GravityCompat.START);
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