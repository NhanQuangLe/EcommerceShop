package com.example.ecommerceshop.nhan.ProfileCustomer.addresses;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Address;
import com.example.ecommerceshop.nhan.Model.AddressItem;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.EditAddressActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserAddressActivity extends AppCompatActivity {
    public static final int EDIT_ACTIVITY = 1601;
    public static final int NEW_ACTIVITY = 1602;
    public static final int TRA_VE_TU_USER_ADDRESS_ACTIVITY = 1000;

    ArrayList<Address> listAddress;
    UserAddressAdapter userAddressAdapter;
    RecyclerView listAddressView;
    FirebaseAuth firebaseAuth;
    AppCompatButton aBtn_AddAddress;
    HashMap<String, String> mapWard;
    String deFaultId;
    ImageView ic_back;
    private ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    switch (result.getResultCode())
                    {
                        case EditAddressActivity.ACTIVITY_EDIT:
                            Address addressEdit = (Address) intent.getSerializableExtra("AddressReturn");
                            for(int i = 0 ; i < listAddress.size(); i++)
                            if(listAddress.get(i).getAddressId().equals(addressEdit.getAddressId()))
                            {
                                listAddress.set(i, addressEdit);
                                userAddressAdapter.notifyDataSetChanged();
                                UpdateAddressFirebase(addressEdit);
                                return;
                            }
                            break;
                        case EditAddressActivity.ACTIVITY_NEW:
                            Address addressNew = (Address) intent.getSerializableExtra("AddressReturn");
                            int MaxIndex;
                            if(listAddress.size() == 0)
                                MaxIndex = -1;
                            else MaxIndex = Integer.parseInt(listAddress.get(listAddress.size() - 1).getAddressId());
                            addressNew.setAddressId(MaxIndex + 1 + "");
                            AddAddressFirebase(addressNew);
                            break;
                    }
                }
            });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);
        firebaseAuth = FirebaseAuth.getInstance();
        listAddress = new ArrayList<>();
        listAddressView = findViewById(R.id.rv_ListAddress);
        aBtn_AddAddress = findViewById(R.id.aBtn_AddAddress);
        deFaultId = "";
        ic_back = findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        aBtn_AddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAddress();
            }
        });
        userAddressAdapter = new UserAddressAdapter(UserAddressActivity.this, listAddress, new IClickAddressListener() {
            @Override
            public void EditAddress(Address address) {
                EditUserAddress(address);
            }

            @Override
            public void ReturnAddressForPayment(Address address) {
                Intent tmp = getIntent();
                if(tmp.getBooleanExtra("isPayment", false)){
                    Intent i = new Intent();
                    i.putExtra("address",address);
                    setResult(TRA_VE_TU_USER_ADDRESS_ACTIVITY,i);
                    finish();
                }
            }
        });
        listAddressView.setAdapter(userAddressAdapter);
        GetData();

        mapWard = new HashMap<>();
    }
    private void GetData(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.child("Users")
                .child(firebaseAuth.getUid())
                .child("Customer")
                .child("Addresses")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Address address = snapshot.getValue(Address.class);
                        address.setDefault(snapshot.child("default").getValue(boolean.class));
                        if(address.isDefault() && deFaultId.equals(""))
                            deFaultId = address.getAddressId();
                        listAddress.add(address);
                        userAddressAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        for(int i = 0; i < listAddress.size(); i++)
                        {
                            Address ad = listAddress.get(i);
                            if(ad.getAddressId().equals(snapshot.child("addressId").getValue(String.class)))
                            {
                                ad = snapshot.getValue(Address.class);
                                ad.setDefault(snapshot.child("default").getValue(boolean.class));
                                listAddress.set(i, ad);
                                userAddressAdapter.notifyDataSetChanged();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        for(int i = 0; i < listAddress.size(); i++)
                        {
                            Address ad = listAddress.get(i);
                            if(ad.getAddressId().equals(snapshot.child("addressId").getValue(String.class)))
                            {
                                listAddress.remove(ad);
                                userAddressAdapter.notifyDataSetChanged();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void EditUserAddress(Address address)
    {
        Intent intent = new Intent(UserAddressActivity.this, EditAddressActivity.class);
        intent.putExtra("AddressSelected", address);
        intent.putExtra("Status", EDIT_ACTIVITY);
        mActivityLauncher.launch(intent);
    }
    private void AddAddress(){
        Intent intent = new Intent(UserAddressActivity.this, EditAddressActivity.class);
        intent.putExtra("Status", NEW_ACTIVITY);
        if(listAddress.size() == 0)
            intent.putExtra("IsBlank", true);
        mActivityLauncher.launch(intent);
    }
    private void UpdateAddressFirebase(Address address){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(firebaseAuth.getUid())
                .child("Customer")
                .child("Addresses");
        Map<String, Object> map = new HashMap<>();
        map.put("detail", address.getDetail());
        map.put("district", address.getDistrict());
        map.put("fullName", address.getFullName());
        map.put("default", address.isDefault());
        map.put("phoneNumber", address.getPhoneNumber());
        map.put("province", address.getProvince());
        map.put("ward", address.getWard());
        map.put("latitude", address.getLatitude());
        map.put("longitude", address.getLongitude());
        dbRef.child(address.getAddressId())
                .updateChildren(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if(address.isDefault()){
                            if (deFaultId!="" && !deFaultId.equals(address.getAddressId())){
                                dbRef.child(deFaultId).child("default").setValue(false);
                                deFaultId = address.getAddressId();
                            }
                        }
                        Toast.makeText(UserAddressActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void AddAddressFirebase(Address address){
        if(listAddress.size() == 0)
            address.setDefault(true);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(firebaseAuth.getUid())
                .child("Customer")
                .child("Addresses");
        dbRef.child(address.getAddressId()).setValue(address, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(address.isDefault()){
                    if (deFaultId!="" && !deFaultId.equals(address.getAddressId())){
                        dbRef.child(deFaultId).child("default").setValue(false);
                        deFaultId = address.getAddressId();
                    }
                }
                Toast.makeText(UserAddressActivity.this, "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
