package com.example.ecommerceshop.Phat.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.tinh.Activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileAdminFragment extends Fragment {
    View mview;
    RelativeLayout ly4,ly7;
    FirebaseAuth firebaseAuth;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_profile_admin, container, false);
        initUI();
        ly4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Statistical
            }
        });

        ly7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logout
                firebaseAuth.signOut();
                checkUser();
            }
        });
        return mview;
    }

    private void initUI() {
        ly4=mview.findViewById(R.id.ly4);
        ly7=mview.findViewById(R.id.ly7);
        firebaseAuth=FirebaseAuth.getInstance();
    }
    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
    }
}
