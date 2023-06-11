package com.example.ecommerceshop.Phat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.Phat.Adapter.AdapterOrderShop;
import com.example.ecommerceshop.Phat.Model.OrderShop;
import com.example.ecommerceshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatiscalShopActivity extends AppCompatActivity {

    TextView cplQuan,unCplQuan,ccQuan,totalRevenue;
    ArrayList<OrderShop> orderShops;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_statiscal_shop);
        initUi();
        loadData();
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
                                        int cpl=0, unCpl=0, cancel=0, totalrevenue=0;
                                        for (DataSnapshot ds: snapshot.getChildren()){
                                            OrderShop orderShop = ds.getValue(OrderShop.class);
                                            if(orderShop.getOrderStatus().equals("Completed")){
                                                cpl=cpl+1;
                                                totalrevenue=totalrevenue+ Integer.parseInt(orderShop.getTotalPrice());
                                            }
                                            else if(orderShop.getOrderStatus().equals("Cancelled")){
                                                cancel=cancel+1;
                                            }
                                            else{
                                                unCpl=unCpl+1;
                                            }
                                        }

                                        cplQuan.setText(String.valueOf(cpl));
                                        unCplQuan.setText(String.valueOf(unCpl));
                                        ccQuan.setText(String.valueOf(cancel));
                                        totalRevenue.setText(String.valueOf(totalrevenue) + " VND");
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
        unCplQuan=findViewById(R.id.unCplQuan);
        ccQuan=findViewById(R.id.ccQuan);
        totalRevenue=findViewById(R.id.totalRevenue);
        firebaseAuth=FirebaseAuth.getInstance();
    }
}