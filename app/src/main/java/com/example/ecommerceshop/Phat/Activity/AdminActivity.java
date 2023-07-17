package com.example.ecommerceshop.Phat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ecommerceshop.Phat.Fragment.ListRegistrationAdminFragment;
import com.example.ecommerceshop.Phat.Fragment.ProfileAdminFragment;
import com.example.ecommerceshop.Phat.Fragment.listShopFragment;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.qui.homeuser.HomeFragmentUser;
import com.example.ecommerceshop.utilities.Constants;
import com.example.ecommerceshop.utilities.PreferenceManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView textView12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ReplaceFragment(new listShopFragment());
        (new PreferenceManagement(getApplicationContext())).putBoolean(Constants.KEY_USER_ADMIN,true);
        bottomNavigationView =findViewById(R.id.bottomNavigationView);
        textView12=findViewById(R.id.textView12);
        textView12.setText("SHOPS");
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.listShop:
                        ReplaceFragment(new listShopFragment());
                        textView12.setText("SHOPS");
                        break;
                    case R.id.ResAdmin:
                        ReplaceFragment(new ListRegistrationAdminFragment());
                        textView12.setText("REQUESTS");
                        break;
                    case R.id.profileAdmin:
                        ReplaceFragment(new ProfileAdminFragment());
                        textView12.setText("ADMIN PROFILE");
                        break;
                }
                return true;
            }
        });
    }

    private void ReplaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}