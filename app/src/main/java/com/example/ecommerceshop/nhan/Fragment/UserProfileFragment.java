package com.example.ecommerceshop.nhan.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileFragment extends Fragment {
    private View mView;
    FirebaseAuth firebaseAuth;
    private TextView customerName, customerEmail, customerFollowers;
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

//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth.signInWithEmailAndPassword("vanphat@gmail.com", "16032003");
        intUI();
        LoadData();
        return mView;
    }

    private void intUI()
    {
        customerName = mView.findViewById(R.id.customerName);
        customerEmail = mView.findViewById(R.id.customerEmail);
        customerFollowers = mView.findViewById(R.id.customerNumberFollowers);
    }
    private void LoadData()
    {
        Toast.makeText(getContext(),  "Đang lấy data", Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ProductType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String str = ds.getValue(String.class);
                    Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}