package com.example.ecommerceshop.Phat.Fragment;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecommerceshop.Phat.Activity.AddVoucherActivity;
import com.example.ecommerceshop.Phat.Adapter.AdapterVoucherShop;
import com.example.ecommerceshop.Phat.Model.Voucher;
import com.example.ecommerceshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VoucherShopFragment extends Fragment {
    View mview;
    ImageView imageView4;
    EditText searchView;
    TextView voucherstatustv;
    RecyclerView voucherList;
    LinearLayout statusList;
    AppCompatButton All, UnExpired, Expired;
    Button btnAddVoucher;
    boolean isfilter=false;
    FirebaseAuth firebaseAuth;
    ArrayList<Voucher> voucherArrayList;
    AdapterVoucherShop adapterVoucherShop;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_voucher_shop, container, false);
        initUI();
        LoadAllVouchers();
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    adapterVoucherShop.getFilter().filter(charSequence);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadAllVouchers();
                voucherstatustv.setText("All");
                All.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
                UnExpired.setBackgroundResource(R.drawable.bg_filter_list_item);
                Expired.setBackgroundResource(R.drawable.bg_filter_list_item);
            }
        });
        UnExpired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadUnExpiredVoucherList();
                voucherstatustv.setText("UnExpired");
                All.setBackgroundResource(R.drawable.bg_filter_list_item);
                UnExpired.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
                Expired.setBackgroundResource(R.drawable.bg_filter_list_item);
            }
        });
        Expired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadExpiredVoucherList();
                voucherstatustv.setText("Expired");
                All.setBackgroundResource(R.drawable.bg_filter_list_item);
                UnExpired.setBackgroundResource(R.drawable.bg_filter_list_item);
                Expired.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
            }
        });
        btnAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddVoucherActivity.class));
            }
        });
        return mview;
    }

    private boolean checkexpired(String expireddate, int quant){
        boolean checkdate=false;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        String today = day+"/"+month+"/"+year;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date mtoday = simpleDateFormat.parse(today);
            Date expiredDate = simpleDateFormat.parse(expireddate);
            if(expiredDate.compareTo(mtoday)<0){
                checkdate=true;
            }
        }catch (Exception e){

        }
        return checkdate || quant==0;
    }
    private void LoadExpiredVoucherList() {
        voucherArrayList = new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Shop").child("Vouchers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        voucherArrayList.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            Voucher voucher = ds.getValue(Voucher.class);
                            if (checkexpired(voucher.getExpiredDate(), voucher.getQuantity())) {
                                voucherArrayList.add(voucher);
                            }

                        }
                        adapterVoucherShop=new AdapterVoucherShop(getContext(), voucherArrayList);
                        voucherList.setAdapter(adapterVoucherShop);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void LoadUnExpiredVoucherList() {
        voucherArrayList = new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Shop").child("Vouchers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        voucherArrayList.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            Voucher voucher = ds.getValue(Voucher.class);
                            if (!checkexpired(voucher.getExpiredDate(), voucher.getQuantity())) {
                                voucherArrayList.add(voucher);
                            }

                        }
                        adapterVoucherShop=new AdapterVoucherShop(getContext(), voucherArrayList);
                        voucherList.setAdapter(adapterVoucherShop);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void LoadAllVouchers() {
        voucherArrayList = new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Shop").child("Vouchers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        voucherArrayList.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            Voucher voucher = ds.getValue(Voucher.class);
                            voucherArrayList.add(voucher);
                        }
                        adapterVoucherShop=new AdapterVoucherShop(getContext(), voucherArrayList);
                        voucherList.setAdapter(adapterVoucherShop);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }




    private void initUI(){
        imageView4 = mview.findViewById(R.id.imageView4);
        searchView = mview.findViewById(R.id.searchView);
        voucherstatustv = mview.findViewById(R.id.voucherstatustv);
        voucherList = mview.findViewById(R.id.voucherList);
        statusList = mview.findViewById(R.id.statusList);
        All = mview.findViewById(R.id.All);
        UnExpired = mview.findViewById(R.id.UnExpired);
        Expired = mview.findViewById(R.id.Expired);
        btnAddVoucher = mview.findViewById(R.id.btnAddVoucher);
        firebaseAuth=FirebaseAuth.getInstance();
    }
}