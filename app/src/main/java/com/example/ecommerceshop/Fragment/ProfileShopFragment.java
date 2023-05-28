package com.example.ecommerceshop.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.ecommerceshop.Activity.ImageAdsShopActivity;
import com.example.ecommerceshop.Activity.LoginActivity;
import com.example.ecommerceshop.Activity.MainUserActivity;
import com.example.ecommerceshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileShopFragment extends Fragment {
    View mview;
    RelativeLayout reviewNav, statisticalNav, settingNav, imgAdsNav, changeRoleNav, LogOut;
    FirebaseAuth firebaseAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_profile_shop, container, false);
        initUI();
        reviewNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        statisticalNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        settingNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
    }
}