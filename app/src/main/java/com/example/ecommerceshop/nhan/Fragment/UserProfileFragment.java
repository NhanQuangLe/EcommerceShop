package com.example.ecommerceshop.nhan.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Activity.UserOrdersActivity;
import com.example.ecommerceshop.nhan.Model.Customer;
import com.example.ecommerceshop.nhan.Model.Shop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {
    public static final int ORDER_HISTORY = 3;
    private ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                }
            });
    private View mView;
    private TextView tv_customerName, tv_customerEmail, tv_customerFollowers;
    private ImageView iv_customerAvatar;
    private ConstraintLayout cl_History_Order;
    public UserProfileFragment() {
        // Required empty public constructor
    }
    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        initUI();
        LoadData();
        setEventInteract();
        return mView;
    }
    private void initUI()
    {
        tv_customerName = mView.findViewById(R.id.tv_customerName);
        tv_customerEmail = mView.findViewById(R.id.tv_customerEmail);
        tv_customerFollowers = mView.findViewById(R.id.tv_customerNumberFollowers);
        iv_customerAvatar = mView.findViewById(R.id.iv_customerAvatar);
        cl_History_Order = mView.findViewById(R.id.cl_History_Order);
    }
    private void LoadData() {
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("Users/U9wPSNj9T1gmF7elkuS858UzJMl1");
        databaseReference.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_customerName.setText(snapshot.child("Name").getValue(String.class));
                tv_customerEmail.setText(snapshot.child("Email").getValue(String.class));
                tv_customerFollowers.setText(Long.toString(snapshot.child("Followers").getChildrenCount()));
                Picasso.get().load(snapshot.child("Avatar").getValue(String.class)).into(iv_customerAvatar);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEventInteract(){
        iv_customerAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        cl_History_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserOrdersActivity.class);
                intent.putExtra("OrderClickType", ORDER_HISTORY);
                mActivityLauncher.launch(intent);
            }
        });
    }
}