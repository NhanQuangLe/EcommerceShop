package com.example.ecommerceshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Fragment.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainUserActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
//                    case R.id.home:
//                        ReplaceFragment(new HomePageShopFragment());
//                        textView.setText("PRODUCTS");
//                        break;
//                    case R.id.cart:
//                        ReplaceFragment(new OrderListShopFragment());
//                        textView.setText("ORDERS");
//                        break;
//                    case R.id.notification:
//                        ReplaceFragment(new VoucherShopFragment());
//                        textView.setText("VOUCHERS");
//                        break;
                    case R.id.profile_user:
                        ReplaceFragment(new UserProfileFragment());
                        break;
//                    case R.id.chat:
//                        ReplaceFragment(new AddProductShopFragment());
//                        textView.setText("ADD PRODUCT");
//                        break;
                }
                return true;
            }
        });

    }
    private void ReplaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutUser, fragment);
        fragmentTransaction.commit();
    }


}