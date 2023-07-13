package com.example.ecommerceshop.nhan.ProfileCustomer.vouchers;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.qui.payment.Voucher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class VoucherCustomerActivity extends AppCompatActivity {
    ArrayList<Voucher> listVoucher;
    VoucherCustomerAdapter voucherCustomerAdapter;
    RecyclerView listVoucherView;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_customer);
        listVoucherView = findViewById(R.id.rcv_voucher);
        listVoucher = new ArrayList<>();
        voucherCustomerAdapter = new VoucherCustomerAdapter(VoucherCustomerActivity.this, listVoucher);
        listVoucherView.setAdapter(voucherCustomerAdapter);
        firebaseAuth = FirebaseAuth.getInstance();
        GetData();
    }
    private void GetData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
        ref.child(firebaseAuth.getUid())
                .child("Customer")
                .child("Voucher")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot vc : snapshot.getChildren()){
                            ref.child(vc.child("shopId").getValue(String.class))
                                    .child("Shop")
                                    .child("Vouchers")
                                    .child(vc.child("voucherid").getValue(String.class))
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Voucher voucher = new Voucher();
                                            voucher.setVoucherid(vc.child("voucherid").getValue(String.class));
                                            voucher.setVouchercode(snapshot.child("vouchercode").getValue(String.class));
                                            voucher.setVouchercode(snapshot.child("voucherdes").getValue(String.class));
                                            voucher.setVouchercode(snapshot.child("expiredDate").getValue(String.class));
                                            listVoucher.add(voucher);
                                            voucherCustomerAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(VoucherCustomerActivity.this, "Fail to get vouchers", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(VoucherCustomerActivity.this, "Fail to get vouchers", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
