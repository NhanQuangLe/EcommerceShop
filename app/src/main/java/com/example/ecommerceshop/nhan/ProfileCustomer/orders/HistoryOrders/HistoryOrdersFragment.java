package com.example.ecommerceshop.nhan.ProfileCustomer.orders.HistoryOrders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerceshop.databinding.FragmentHistoryOrdersBinding;
import com.example.ecommerceshop.nhan.Model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryOrdersFragment extends Fragment {
    public HistoryOrdersFragment() {
    }
    FirebaseAuth firebaseAuth;
    FragmentHistoryOrdersBinding fragmentHistoryOrdersBinding;
    HistoryOrdersAdapter mHistoryAdapter;
    RecyclerView mHistoryAdapterView;
    ArrayList<HistoryOrder> listHistoryOrders;
    View mViewFragment;

    public static HistoryOrdersFragment newInstance() {
        HistoryOrdersFragment fragment = new HistoryOrdersFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHistoryOrdersBinding = FragmentHistoryOrdersBinding.inflate(inflater, container, false);
        mViewFragment = fragmentHistoryOrdersBinding.getRoot();
        listHistoryOrders = new ArrayList<>();
        mHistoryAdapterView = fragmentHistoryOrdersBinding.listOrder;
        firebaseAuth = FirebaseAuth.getInstance();
        LoadData();
        return mViewFragment;
    }

    public void LoadData() {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");

        dbReference.child(firebaseAuth.getUid())
                .child("Customer")
                .child("Orders").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listHistoryOrders.clear();
                        for(DataSnapshot ds : snapshot.getChildren())
                        {
                            HistoryOrder ho = new HistoryOrder();
                            ho.setCreateDate(ds.child("CreateDate").getValue(String.class));
                            ho.setOrderID(ds.child("OrderID").getValue(String.class));
                            ho.setProduct(ds.child("Product").getValue(Product.class));
                            ho.setReceiveAddress(ds.child("ReceiveAddress").getValue(String.class));
                            ho.setShopAvatar(ds.child("Shop").child("ShopAvatar").getValue(String.class));
                            ho.setShopName(ds.child("Shop").child("ShopName").getValue(String.class));
                            ho.setStatus(ds.child("Status").getValue(String.class));
                            ho.setTotalPrice(ds.child("TotalPrice").getValue(int.class));
                            ho.setTransportFee(ds.child("TransportFee").getValue(int.class));
                            listHistoryOrders.add(ho);
                        }

                        mHistoryAdapter = new HistoryOrdersAdapter(getContext(), listHistoryOrders);
                        mHistoryAdapterView.setAdapter(mHistoryAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}