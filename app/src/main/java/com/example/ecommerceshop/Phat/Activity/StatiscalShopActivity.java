package com.example.ecommerceshop.Phat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.Phat.Adapter.AdapterOrderShop;
import com.example.ecommerceshop.Phat.Model.OrderShop;
import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StatiscalShopActivity extends AppCompatActivity {

    TextView cplQuan,unCplQuan,ccQuan,totalRevenue;
    ArrayList<OrderShop> orderShops;
    FirebaseAuth firebaseAuth;
    ImageView btnback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_statiscal_shop);
        initUi();
        loadData();
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadData() {
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
                                        int cpl=0, unCpl=0, cancel=0;
                                        long totalrevenue = 0;
                                        for (DataSnapshot ds: snapshot.getChildren()){
                                            OrderShop orderShop = ds.getValue(OrderShop.class);
                                            orderShops.add(orderShop);

                                        }
                                        for(OrderShop orderShop : orderShops){
                                            if(orderShop.getOrderStatus().equals("Completed")){
                                                cpl++;
                                                totalrevenue = totalrevenue + orderShop.getTotalPrice();
                                            }
                                            if(orderShop.getOrderStatus().equals("Cancelled")){
                                                cancel=cancel+1;
                                            }
                                            if (orderShop.getOrderStatus().equals("Processing") || orderShop.getOrderStatus().equals("UnProcess")){
                                                unCpl=unCpl+1;
                                            }
                                            unCplQuan.setText(String.valueOf(unCpl));
                                            ccQuan.setText(String.valueOf(cancel));
                                            cplQuan.setText(String.valueOf(cpl));

                                            totalRevenue.setText(Constants.convertToVND(totalrevenue));
                                        }

                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initUi() {
        cplQuan=findViewById(R.id.cplQuan);
        btnback=findViewById(R.id.btnback);
        unCplQuan=findViewById(R.id.unCplQuan);
        ccQuan=findViewById(R.id.ccQuan);
        totalRevenue=findViewById(R.id.totalRevenue);
        firebaseAuth=FirebaseAuth.getInstance();
    }
}