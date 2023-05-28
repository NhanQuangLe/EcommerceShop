package com.example.ecommerceshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.ecommerceshop.Fragment.AddProductShopFragment;
import com.example.ecommerceshop.Fragment.HomePageShopFragment;
import com.example.ecommerceshop.Fragment.OrderListShopFragment;
import com.example.ecommerceshop.Fragment.ProfileShopFragment;
import com.example.ecommerceshop.Fragment.UserProfileFragment;
import com.example.ecommerceshop.Fragment.VoucherShopFragment;
import com.example.ecommerceshop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainUserActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
//                    case R.id.homeshop:
//                        ReplaceFragment(new HomePageShopFragment());
//
//                        break;
//                    case R.id.ordersshop:
//                        ReplaceFragment(new OrderListShopFragment());
//
//                        break;
//                    case R.id.vouchersshop:
//                        ReplaceFragment(new VoucherShopFragment());
//
//                        break;
                    case R.id.profile_user:
                        ReplaceFragment(new VoucherShopFragment());

                        break;
//                    case R.id.addProductshop:
//                        ReplaceFragment(new AddProductShopFragment());
//
//                        break;
                }
                return true;
            }
        });
    }
    private void ReplaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}