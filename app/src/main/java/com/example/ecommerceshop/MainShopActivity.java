package com.example.ecommerceshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ecommerceshop.Phat.Fragment.AddProductShopFragment;
import com.example.ecommerceshop.Phat.Fragment.HomePageShopFragment;
import com.example.ecommerceshop.Phat.Fragment.OrderListShopFragment;
import com.example.ecommerceshop.Phat.Fragment.ProfileShopFragment;
import com.example.ecommerceshop.Phat.Fragment.VoucherShopFragment;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.chat.MainChatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import com.example.ecommerceshop.R;

public class MainShopActivity extends AppCompatActivity {

    ListView listView ;
    TextView textView;
    ImageView imageChat;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop);
        ReplaceFragment(new HomePageShopFragment());
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        floatingActionButton=findViewById(R.id.fab);
        textView=findViewById(R.id.textView12);
        imageChat = findViewById(R.id.image_chat);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplaceFragment(new AddProductShopFragment());
                textView.setText("ADD PRODUCT");

            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeshop:
                        ReplaceFragment(new HomePageShopFragment());
                        textView.setText("PRODUCTS");
                        break;
                    case R.id.ordersshop:
                        ReplaceFragment(new OrderListShopFragment());
                        textView.setText("ORDERS");
                        break;
                    case R.id.vouchersshop:
                        ReplaceFragment(new VoucherShopFragment());
                        textView.setText("VOUCHERS");
                        break;
                    case R.id.profile_shop:
                        ReplaceFragment(new ProfileShopFragment());
                        textView.setText("MY PROFILE");
                        break;
                    case R.id.addProductshop:
                        ReplaceFragment(new AddProductShopFragment());
                        textView.setText("ADD PRODUCT");
                        break;
                }
                return true;
            }
        });
        imageChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainChatActivity.class);
                startActivity(intent);
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
