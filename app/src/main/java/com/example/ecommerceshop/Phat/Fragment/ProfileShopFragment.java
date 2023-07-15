package com.example.ecommerceshop.Phat.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.MainUserActivity;
import com.example.ecommerceshop.Phat.Activity.ImageAdsShopActivity;
import com.example.ecommerceshop.Phat.Activity.ProfileSettingShopActivity;
import com.example.ecommerceshop.Phat.Activity.ReviewShopActivity;
import com.example.ecommerceshop.Phat.Activity.StatiscalShopActivity;
import com.example.ecommerceshop.Phat.Model.RequestShop;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.tinh.Activity.LoginActivity;
import com.example.ecommerceshop.utilities.PreferenceManagement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileShopFragment extends Fragment {
    View mview;
    RelativeLayout reviewNav, statisticalNav, settingNav, imgAdsNav, changeRoleNav, LogOut;
    FirebaseAuth firebaseAuth;
    TextView shopName, shopEmail, shopPhone;
    CircleImageView shopAvatar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_profile_shop, container, false);
        initUI();
        Loadprofile();
        reviewNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ReviewShopActivity.class));
            }
        });
        statisticalNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), StatiscalShopActivity.class));
            }
        });
        settingNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileSettingShopActivity.class));
            }
        });
        imgAdsNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ImageAdsShopActivity.class));
            }
        });
        changeRoleNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainUserActivity.class));
                getActivity().finish();
            }
        });
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                checkUser();
            }
        });
        return mview;
    }

    private void Loadprofile() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Shop").child("ShopInfos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RequestShop requestShop = snapshot.getValue(RequestShop.class);
                if(requestShop!= null){
                    shopPhone.setText(requestShop.getShopPhone());
                    if(getActivity()!=null){
                        Glide.with(getActivity()).load(Uri.parse(requestShop.getShopAvt())).into(shopAvatar);
                    }
                    shopName.setText(requestShop.getShopName());
                    shopEmail.setText(requestShop.getShopEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
    }

    private void initUI() {
        reviewNav = mview.findViewById(R.id.ly4);
        statisticalNav = mview.findViewById(R.id.ly5);
        settingNav = mview.findViewById(R.id.ly6);
        imgAdsNav = mview.findViewById(R.id.ly7);
        changeRoleNav = mview.findViewById(R.id.ly8);
        LogOut = mview.findViewById(R.id.ly9);
        firebaseAuth=FirebaseAuth.getInstance();
        shopName = mview.findViewById(R.id.shopName);
        shopAvatar = mview.findViewById(R.id.shopAvatar);
        shopEmail = mview.findViewById(R.id.shopEmail);
        shopPhone = mview.findViewById(R.id.shopPhone);
    }
}