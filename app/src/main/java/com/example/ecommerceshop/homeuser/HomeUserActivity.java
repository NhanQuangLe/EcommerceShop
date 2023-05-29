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