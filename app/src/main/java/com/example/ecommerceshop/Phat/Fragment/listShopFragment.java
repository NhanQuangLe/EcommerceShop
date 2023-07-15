package com.example.ecommerceshop.Phat.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.Phat.Adapter.AdapterListRequest;
import com.example.ecommerceshop.Phat.Adapter.AdapterListShopAdmin;
import com.example.ecommerceshop.Phat.Model.RequestShop;
import com.example.ecommerceshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class listShopFragment extends Fragment {
    View mview;
    RecyclerView requestList;
    TextView quntity;
    ArrayList<RequestShop> requestShops;
    AdapterListShopAdmin adapterListShopAdmin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mview=inflater.inflate(R.layout.fragment_list_shop_in_admin, container, false);
        requestList=mview.findViewById(R.id.shopList);
        quntity=mview.findViewById(R.id.shopQuantity);
        loadData();
        return mview;
    }

    private void loadData() {
        requestShops=new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestShops.clear();
                for(DataSnapshot ds: snapshot.getChildren()) {
                    String uid = "" + ds.getRef().getKey();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(uid).child("Shop").child("ShopInfos")
                            .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                RequestShop rq = snapshot.getValue(RequestShop.class);
                                requestShops.add(rq);
                                adapterListShopAdmin=new AdapterListShopAdmin(getContext(),requestShops);
                                requestList.setAdapter(adapterListShopAdmin);
                                quntity.setText(String.valueOf(requestShops.size()));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
