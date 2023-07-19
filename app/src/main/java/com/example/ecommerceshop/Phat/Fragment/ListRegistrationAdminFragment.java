package com.example.ecommerceshop.Phat.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerceshop.Phat.Adapter.AdapterListRequest;
import com.example.ecommerceshop.Phat.Adapter.AdapterProductShop;
import com.example.ecommerceshop.Phat.Model.Product;
import com.example.ecommerceshop.Phat.Model.RequestShop;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListRegistrationAdminFragment extends Fragment {

    View mview;
    RecyclerView requestList;
    ArrayList<RequestShop> requestShops;
    AdapterListRequest adapterListRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mview=inflater.inflate(R.layout.fragment_list_registration_admin, container, false);
        requestList=mview.findViewById(R.id.requestList);
        requestShops=new ArrayList<>();
        loadData();
        return mview;
    }

    private void loadData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestShops.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    RequestShop requestShop = ds.getValue(RequestShop.class);
                    requestShops.add(requestShop);
                }
                adapterListRequest = new AdapterListRequest(getContext(), requestShops);
                requestList.setAdapter(adapterListRequest);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                CustomToast.makeText(getContext(),""+ error.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();

            }
        });
    }
}