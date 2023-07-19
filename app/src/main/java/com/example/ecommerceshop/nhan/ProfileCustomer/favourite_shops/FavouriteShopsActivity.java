package com.example.ecommerceshop.nhan.ProfileCustomer.favourite_shops;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityFavouriteShopsBinding;
import com.example.ecommerceshop.nhan.Model.Shop;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouriteShopsActivity extends AppCompatActivity {

    RecyclerView listFavouriteShopView;
    FavouriteShopsAdapter favouriteShopsAdapter;
    ArrayList<Shop> listFavouriteShop;
    FirebaseAuth firebaseAuth;
    ImageView ic_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_shops);
        firebaseAuth = FirebaseAuth.getInstance();
        listFavouriteShopView = findViewById(R.id.listFavouriteShop);
        listFavouriteShop = new ArrayList<>();
        favouriteShopsAdapter = new FavouriteShopsAdapter(FavouriteShopsActivity.this, listFavouriteShop, new IClickFavouriteShopListener() {
            @Override
            public void UnFollowShop(Shop shop) {
                ClickUnFolloweShop(shop);
            }
        });
        listFavouriteShopView.setAdapter(favouriteShopsAdapter);
        ic_back = findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        GetData();
    }

    private void GetData() {
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");

        firebaseDatabase.child(firebaseAuth.getUid()).child("Customer")
                .child("Followers")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        firebaseDatabase.child(snapshot.getValue(String.class) + "")
                                .child("Shop")
                                .child("ShopInfos").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot s) {
                                        Shop shop = new Shop();
                                        shop.setShopID(s.child("uid").getValue(String.class));
                                        shop.setShopAddress(s.child("shopAddress").getValue(String.class));
                                        shop.setShopAvatar(s.child("shopAvt").getValue(String.class));
                                        shop.setShopEmail(s.child("shopEmail").getValue(String.class));
                                        shop.setRating(0);
                                        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int countFollower = 0;
                                                double averageStar = 0;
                                                int numReview = 0;
                                                for(DataSnapshot ds : snapshot.getChildren()){
                                                    if(ds.child("Customer/Followers").exists())
                                                        for(DataSnapshot shopID : ds.child("Customer/Followers").getChildren())
                                                            if(shopID.getValue(String.class).equals(s.child("uid").getValue(String.class)))
                                                                countFollower = countFollower + 1;

                                                    if(ds.child("Customer/Reviews").exists())
                                                        for(DataSnapshot shopID : ds.child("Customer/Reviews").getChildren())
                                                            if(shopID.child("shopId").getValue(String.class).equals(s.child("uid").getValue(String.class)))
                                                            {
                                                                numReview = numReview + 1;
                                                                averageStar = averageStar + shopID.child("rating").getValue(Double.class);
                                                            }
                                                }
                                                if(numReview != 0)
                                                    shop.setRating(averageStar / numReview);
                                                else
                                                    shop.setRating(0);
                                                shop.setNumberFollowers(countFollower);
                                                shop.setShopName(s.child("shopName").getValue(String.class));
                                                for(int i = 0; i < listFavouriteShop.size(); i++)
                                                    if(listFavouriteShop.get(i).getShopID().equals(s.child("uid").getValue(String.class)))
                                                    {
                                                        listFavouriteShop.set(i, shop);
                                                        favouriteShopsAdapter.notifyDataSetChanged();
                                                        return;
                                                    }
                                                listFavouriteShop.add(shop);
                                                favouriteShopsAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                CustomToast.makeText(getApplicationContext(),"Fail",CustomToast.SHORT,CustomToast.ERROR).show();

                                            }
                                        });

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        CustomToast.makeText(getApplicationContext(),error.getMessage()+  "",CustomToast.SHORT,CustomToast.ERROR).show();

                                    }
                                });
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        for(int i = 0; i < listFavouriteShop.size(); i++)
                        {
                            if(listFavouriteShop.get(i).getShopID().equals(snapshot.getValue(String.class)))
                            {
                                listFavouriteShop.remove(i);
                                favouriteShopsAdapter.notifyDataSetChanged();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        }
    public void ClickUnFolloweShop(Shop favouriteShop){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(firebaseAuth.getUid())
                .child("Customer")
                .child("Followers");
        dbRef.child(favouriteShop.getShopID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        CustomToast.makeText(getApplicationContext(),"Đã hủy theo dõi",CustomToast.SHORT,CustomToast.SUCCESS).show();

                    }
                });
    }

 }