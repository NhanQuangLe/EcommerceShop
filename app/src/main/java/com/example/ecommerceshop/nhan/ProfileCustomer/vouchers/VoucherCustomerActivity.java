package com.example.ecommerceshop.nhan.ProfileCustomer.vouchers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.qui.payment.Voucher;
import com.example.ecommerceshop.toast.CustomToast;
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
    Button btnBackward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_customer);
        listVoucherView = findViewById(R.id.rcv_voucher);
        listVoucher = new ArrayList<>();
        voucherCustomerAdapter = new VoucherCustomerAdapter(VoucherCustomerActivity.this, listVoucher);
        listVoucherView.setAdapter(voucherCustomerAdapter);
        firebaseAuth = FirebaseAuth.getInstance();
        btnBackward = findViewById(R.id.btnBackward);
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        GetData();
    }
    private void GetData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .child("Customer")
                .child("Vouchers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listVoucher.clear();
                        for(DataSnapshot vc : snapshot.getChildren()){
//                            if(vc.child("used").getValue(Boolean.class) == true)
//                                continue;
                            ref.child(vc.child("shopId").getValue(String.class))
                                    .child("Shop")
                                    .child("Vouchers")
                                    .child(vc.child("voucherid").getValue(String.class))
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Voucher voucher = new Voucher();
                                            voucher.setUsed(vc.child("used").getValue(Boolean.class));
                                            voucher.setVoucherid(vc.child("voucherid").getValue(String.class));
                                            voucher.setVouchercode(snapshot.child("vouchercode").getValue(String.class));
                                            voucher.setVoucherdes(snapshot.child("voucherdes").getValue(String.class));
                                            voucher.setDiscountPrice(snapshot.child("discountPrice").getValue(int.class));
                                            voucher.setExpiredDate(snapshot.child("expiredDate").getValue(String.class));
                                            listVoucher.add(voucher);
                                            voucherCustomerAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            CustomToast.makeText(getApplicationContext(),"Fail to get vouchers",CustomToast.SHORT,CustomToast.ERROR).show();

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        CustomToast.makeText(getApplicationContext(),"Fail to get vouchers",CustomToast.SHORT,CustomToast.ERROR).show();

                    }
                });


    }
}
