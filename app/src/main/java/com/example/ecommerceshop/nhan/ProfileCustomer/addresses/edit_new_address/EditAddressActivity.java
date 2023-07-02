package com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Address;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.UserAddressActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address.ChooseAddressActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EditAddressActivity extends AppCompatActivity {
    public static final int ACTIVITY_NEW = 1;
    public static final int ACTIVITY_EDIT = 2;
//    public static final int ACTIVITY_REMOVE = 3;
    FirebaseAuth firebaseAuth;
    EditText et_FullName, et_PhoneNumber, et_Detail;
    TextView tv_MainAddress;
    SwitchCompat sw_DeFaultAddress;
    AppCompatButton aBtn_DeleteAddress;
    Button btn_UpdateAddress;
    LinearLayout btn_ChooseAddress;
    Address addressNew;

    private ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if(result.getResultCode() == ChooseAddressActivity.SUCCESS_CREATE_ADDRESS)
                        tv_MainAddress.setText(intent.getStringExtra("Address"));
                }
            });
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shipping_address);
        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();

        int status = intent.getIntExtra("Status", UserAddressActivity.NEW_ACTIVITY);

        InitUI(status);

        if(status == UserAddressActivity.EDIT_ACTIVITY)
        {
            Address address = (Address)intent.getSerializableExtra("AddressSelected");
            SetContent(address, status);
        }
    }

    private void InitUI(int status){
        et_FullName = findViewById(R.id.et_FullName);
        et_PhoneNumber = findViewById(R.id.et_PhoneNumber);
        et_Detail = findViewById(R.id.et_Detail);
        tv_MainAddress = findViewById(R.id.tv_MainAddress);
        sw_DeFaultAddress = findViewById(R.id.sw_DefaultAddress);
        aBtn_DeleteAddress = findViewById(R.id.aBtn_DeleteAddress);
        btn_UpdateAddress = findViewById(R.id.btn_UpdateAddress);
        btn_ChooseAddress = findViewById(R.id.btn_ChooseAddress);
        btn_ChooseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAddressActivity.this, ChooseAddressActivity.class);
                if(status == UserAddressActivity.NEW_ACTIVITY){

                }
                mActivityLauncher.launch(intent);
            }
        });
        if(status == UserAddressActivity.NEW_ACTIVITY){
            aBtn_DeleteAddress.setVisibility(View.INVISIBLE);
            btn_UpdateAddress.setText("Thêm địa chỉ");
            addressNew = new Address();
            btn_UpdateAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateAddress(addressNew, status);
                }
            });
        }
    }

    private void SetContent(Address address, int status){
        et_FullName.setText(address.getFullName());
        et_PhoneNumber.setText(address.getPhoneNumber());
        et_Detail.setText(address.getDetail());
        tv_MainAddress.setText(address.GetAddressString());
        sw_DeFaultAddress.setChecked(address.isDefault());
        sw_DeFaultAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address.isDefault()){
                    sw_DeFaultAddress.setChecked(true);
                    Toast.makeText(EditAddressActivity.this, "Địa chỉ này đang là địa chỉ mặc định! Không được hủy chọn", Toast.LENGTH_SHORT).show();
                }
            }
        });
        aBtn_DeleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAddress(address);
            }
        });
        btn_UpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAddress(address, status);
            }
        });

    }

    private void DeleteAddress(Address address){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc chắn muốn xóa địa chỉ này không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(address.isDefault())
                        {
                            Toast.makeText(EditAddressActivity.this, "Không thể xóa địa chỉ mặc định", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                        dbRef.child("Users")
                                .child(firebaseAuth.getUid())
                                .child("Customer")
                                .child("Addresses")
                                .child(address.getAddressId())
                                .removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Toast.makeText(EditAddressActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void UpdateAddress(Address address, int status){
        if(CheckStringAndConstraint()){
            address.setFullName(et_FullName.getText().toString().trim());
            address.setPhoneNumber(et_PhoneNumber.getText().toString().trim());
            String[] DisWarDist = tv_MainAddress.getText().toString().split(",");
            address.setWard(DisWarDist[0].trim());
            address.setDistrict(DisWarDist[1].trim());
            address.setProvince(DisWarDist[2].trim());
            address.setDetail(et_Detail.getText().toString().trim());
            address.setDefault(sw_DeFaultAddress.isChecked());

            Intent intent = new Intent(EditAddressActivity.this, UserAddressActivity.class);
            if(status == UserAddressActivity.EDIT_ACTIVITY)
            {
                intent.putExtra("AddressReturn", address);
                setResult(ACTIVITY_EDIT, intent);
            }
            else if(status == UserAddressActivity.NEW_ACTIVITY)
            {
                intent.putExtra("AddressReturn", address);
                setResult(ACTIVITY_NEW, intent);
            }
            finish();
        }
    }
    private boolean CheckStringAndConstraint(){
        if(et_FullName.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "Vui lòng nhập đầy đủ họ tên", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(et_PhoneNumber.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return false;
        }
        String pattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
        if(!et_PhoneNumber.getText().toString().trim().matches(pattern))
        {
            Toast.makeText(this, "Vui lòng đúng định dạnh số điện thoại", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(tv_MainAddress.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "Vui lòng chọn địa chỉ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(et_Detail.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "Vui lòng nhập chi tiết địa chỉ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}