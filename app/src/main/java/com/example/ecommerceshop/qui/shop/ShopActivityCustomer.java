package com.example.ecommerceshop.qui.shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.MainUserActivity;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityShopCustomerBinding;
import com.example.ecommerceshop.qui.cart.CartActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;

public class ShopActivityCustomer extends AppCompatActivity {

    private ActivityShopCustomerBinding mActivityShopCustomerBinding;
    private View mView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerShopAdapter mViewPagerAdapter;
    private String shopId;



    private String tag;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityShopCustomerBinding = ActivityShopCustomerBinding.inflate(getLayoutInflater());
        mView = mActivityShopCustomerBinding.getRoot();
        setContentView(mView);

        init();
        iListener();

    }


    public void init() {

        mViewPagerAdapter = new ViewPagerShopAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mActivityShopCustomerBinding.viewPager.setAdapter(mViewPagerAdapter);
        mActivityShopCustomerBinding.tabLayout.setupWithViewPager(mActivityShopCustomerBinding.viewPager);
        Intent intent = getIntent();
        this.shopId = intent.getStringExtra("shopId");
        tag="Laptop";
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        setInfoShop();

    }
    private void iListener() {
        mActivityShopCustomerBinding.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/Followers");
                ref.child(shopId).setValue(shopId);
//                mActivityShopCustomerBinding.btnFollow.setVisibility(View.GONE);
//                mActivityShopCustomerBinding.btnUnfollow.setVisibility(View.VISIBLE);
            }
        });
        mActivityShopCustomerBinding.btnUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/Followers");
                ref.child(shopId).removeValue();
            }
        });
        mActivityShopCustomerBinding.btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mActivityShopCustomerBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mActivityShopCustomerBinding.editTextSearch.getText().toString().trim();
                Intent intent = new Intent(getApplicationContext(),AllFilterProductActivity.class);
                intent.putExtra("clickType",0);
                intent.putExtra("textSearch",text);
                intent.putExtra("shopId",shopId);
                startActivity(intent);
            }
        });
        mActivityShopCustomerBinding.navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainUserActivity.class);
                startActivity(i);
                finish();
            }
        });
        mActivityShopCustomerBinding.navCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(i);
            }
        });
    }


    private void setInfoShop() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + this.shopId + "/Shop/ShopInfos");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String shopName = snapshot.child("shopName").getValue(String.class);
                String shopAddress = snapshot.child("shopAddress").getValue(String.class);
                String shopAvt = snapshot.child("shopAvt").getValue(String.class);

                mActivityShopCustomerBinding.shopName.setText(shopName);
                mActivityShopCustomerBinding.shopAddress.setText(shopAddress);
                Glide.with(getApplicationContext()).load(shopAvt).into(mActivityShopCustomerBinding.shopAvatar);
                setShopFollowers();
                setShopRate();

            }

            private void setShopRate() {
                final float[] rate = {0};
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final int[] temp = {0};
                        final int[] i = {0};
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            DatabaseReference ref2 = dataSnapshot.getRef().child("Customer/Reviews");
                            ref2.orderByChild("shopId").equalTo(shopId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                        if (dataSnapshot1.exists()){
                                            int rating = dataSnapshot1.child("rating").getValue(Integer.class);
                                            temp[0] +=rating;
                                            i[0]++;
                                            rate[0] = (float)temp[0]/i[0];
                                            mActivityShopCustomerBinding.tvShopRating.setText(rate[0]+"");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            private void setShopFollowers() {
                final int[] followers = {0};
                DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference("Users");
                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (followers[0] !=0) followers[0] =0;
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            DatabaseReference myRef3 = dataSnapshot.getRef().child("Customer").child("Followers");
                            myRef3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                        String shopIdFollower = dataSnapshot1.getValue(String.class);
                                        if (shopIdFollower.equals(shopId)) {
                                            followers[0]++;
                                            break;
                                        }
                                    }
                                    DecimalFormat df = new DecimalFormat();
                                    df.setMaximumFractionDigits(1);
                                    String followersStr;
                                    if (followers[0] <1000) followersStr = String.valueOf(followers[0]);
                                    else followersStr= df.format(followers[0] *1.0/1000)+"k";

                                    mActivityShopCustomerBinding.tvNumberFollower.setText(followersStr);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setVisibleButtonFollow();
    }

    private void setVisibleButtonFollow() {
        if (shopId.equals(mCurrentUser.getUid())){
            mActivityShopCustomerBinding.btnUnfollow.setVisibility(View.GONE);
            mActivityShopCustomerBinding.btnFollow.setVisibility(View.GONE);
            return;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/Followers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(shopId).exists()){
                    mActivityShopCustomerBinding.btnUnfollow.setVisibility(View.VISIBLE);
                    mActivityShopCustomerBinding.btnFollow.setVisibility(View.GONE);
                }
                else {
                    mActivityShopCustomerBinding.btnUnfollow.setVisibility(View.GONE);
                    mActivityShopCustomerBinding.btnFollow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getShopId() {
        return shopId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}