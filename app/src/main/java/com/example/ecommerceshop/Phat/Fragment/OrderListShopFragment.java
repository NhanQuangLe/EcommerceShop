package com.example.ecommerceshop.Phat.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.Phat.Adapter.AdapterOrderShop;
import com.example.ecommerceshop.Phat.Adapter.AdapterProductShop;
import com.example.ecommerceshop.Phat.Model.OrderShop;
import com.example.ecommerceshop.Phat.Model.Product;
import com.example.ecommerceshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderListShopFragment extends Fragment {
    View mview;
    ImageView imageView4;
    EditText searchView;
    TextView orderstatustv;
    RecyclerView orderList;
    HorizontalScrollView statusList;
    boolean isfilter=false;
    AppCompatButton cancelled,completed,processing,unprocessed,allorders;
    ArrayList<OrderShop> orderShops;
    FirebaseAuth firebaseAuth;
    AdapterOrderShop adapterOrderShop;
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_order_list_shop, container, false);
        initUI();
        LoadAllOrders();
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isfilter){
                    statusList.setVisibility(View.VISIBLE);
                    imageView4.setImageResource(R.drawable.ic_un_filter);
                    isfilter=true;
                }
                else {
                    statusList.setVisibility(View.GONE);
                    imageView4.setImageResource(R.drawable.ic_filter);
                    isfilter=false;
                }
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    adapterOrderShop.getFilter().filter(charSequence);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        allorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadAllOrders();
                orderstatustv.setText("All orders");
                allorders.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
                unprocessed.setBackgroundResource(R.drawable.bg_filter_list_item);
                processing.setBackgroundResource(R.drawable.bg_filter_list_item);
                completed.setBackgroundResource(R.drawable.bg_filter_list_item);
                cancelled.setBackgroundResource(R.drawable.bg_filter_list_item);
            }
        });
        unprocessed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFilterOrderList("UnProcessed");
                orderstatustv.setText("UnProcessed");
                allorders.setBackgroundResource(R.drawable.bg_filter_list_item);
                unprocessed.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
                processing.setBackgroundResource(R.drawable.bg_filter_list_item);
                completed.setBackgroundResource(R.drawable.bg_filter_list_item);
                cancelled.setBackgroundResource(R.drawable.bg_filter_list_item);
            }
        });
        processing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFilterOrderList("Processing");
                orderstatustv.setText("Processing");
                allorders.setBackgroundResource(R.drawable.bg_filter_list_item);
                unprocessed.setBackgroundResource(R.drawable.bg_filter_list_item);
                processing.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
                completed.setBackgroundResource(R.drawable.bg_filter_list_item);
                cancelled.setBackgroundResource(R.drawable.bg_filter_list_item);
            }
        });
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFilterOrderList("Completed");
                orderstatustv.setText("Completed");
                allorders.setBackgroundResource(R.drawable.bg_filter_list_item);
                unprocessed.setBackgroundResource(R.drawable.bg_filter_list_item);
                processing.setBackgroundResource(R.drawable.bg_filter_list_item);
                completed.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
                cancelled.setBackgroundResource(R.drawable.bg_filter_list_item);
            }
        });
        cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFilterOrderList("Cancelled");
                orderstatustv.setText("Cancelled");
                allorders.setBackgroundResource(R.drawable.bg_filter_list_item);
                unprocessed.setBackgroundResource(R.drawable.bg_filter_list_item);
                processing.setBackgroundResource(R.drawable.bg_filter_list_item);
                completed.setBackgroundResource(R.drawable.bg_filter_list_item);
                cancelled.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
            }
        });
        return mview;
    }

    private void LoadFilterOrderList(String status) {
        orderShops = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderShops.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String uid = ""+ds.getRef().getKey();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(uid).child("Customer").child("Orders").orderByChild("shopId")
                            .equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for (DataSnapshot ds: snapshot.getChildren()){
                                            OrderShop orderShop = ds.getValue(OrderShop.class);
                                            if(orderShop.getOrderStatus().equals(status)){
                                                orderShops.add(orderShop);
                                            }
                                        }
                                        adapterOrderShop = new AdapterOrderShop(getContext(), orderShops);
                                        orderList.setAdapter(adapterOrderShop);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoadAllOrders() {
        orderShops = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderShops.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String uid = ""+ds.getRef().getKey();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(uid).child("Customer").child("Orders").orderByChild("shopId")
                            .equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for (DataSnapshot ds: snapshot.getChildren()){
                                            OrderShop orderShop = ds.getValue(OrderShop.class);
                                            orderShops.add(orderShop);
                                        }
                                        adapterOrderShop = new AdapterOrderShop(getContext(), orderShops);
                                        orderList.setAdapter(adapterOrderShop);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initUI(){
        imageView4 = mview.findViewById(R.id.imageView4);
        searchView = mview.findViewById(R.id.searchView);
        orderstatustv = mview.findViewById(R.id.orderstatustv);
        orderList = mview.findViewById(R.id.orderList);
        statusList = mview.findViewById(R.id.statusList);
        cancelled = mview.findViewById(R.id.cancelled);
        completed = mview.findViewById(R.id.completed);
        processing = mview.findViewById(R.id.processing);
        unprocessed = mview.findViewById(R.id.unprocessed);
        allorders = mview.findViewById(R.id.allorders);
        firebaseAuth=FirebaseAuth.getInstance();
    }
}