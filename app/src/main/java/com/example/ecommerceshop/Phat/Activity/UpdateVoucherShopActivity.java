package com.example.ecommerceshop.Phat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.Phat.Model.Voucher;
import com.example.ecommerceshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;

public class UpdateVoucherShopActivity extends AppCompatActivity {

    ImageView btnback, btndelete;
    TextInputEditText voucherCode,voucherDes,voucherDiscountPrice,minimumPrice,voucherQuantity;
    TextView expiredDate;
    Button btnUpdateVoucher;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    boolean isdelete = false;
    String voucherid, vouchercode, voucherdes, exprieddate;
    int quantity, disprice, miniprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_updelete_voucher_shop);
        initUI();
        voucherid=getIntent().getStringExtra("voucherid");
        if(!isdelete) LoadVoucherDetail();
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        expiredDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepickDialog();
            }
        });
        btnUpdateVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputData();
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateVoucherShopActivity.this);
                builder.setTitle("Delete...").setMessage("Are you sure you want to delete voucher "+ voucherCode.getText().toString()+" ?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isdelete=true;
                                deleteVoucher(voucherid);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .show();
            }
        });
    }

    private void deleteVoucher(String voucherid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Shop").child("Vouchers").child(voucherid).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onBackPressed();
                        Toast.makeText(getApplicationContext(), "Delete voucher successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateVoucherShopActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void LoadVoucherDetail() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Shop").child("Vouchers").child(voucherid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Voucher voucher = new Voucher();
                        voucher=snapshot.getValue(Voucher.class);
                        if(voucher != null ){
                            voucherCode.setText(voucher.getVouchercode());
                            voucherDes.setText(voucher.getVoucherdes());
                            voucherQuantity.setText(String.valueOf(voucher.getQuantity()));
                            voucherDiscountPrice.setText(String.valueOf(voucher.getDiscountPrice()));
                            minimumPrice.setText(String.valueOf(voucher.getMinimumPrice()));
                            expiredDate.setText(voucher.getExpiredDate());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UpdateVoucherShopActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void InputData() {
        vouchercode=voucherCode.getText().toString().trim();
        voucherdes=voucherDes.getText().toString().trim();
        exprieddate=expiredDate.getText().toString().trim();
        if(TextUtils.isEmpty(vouchercode)){
            Toast.makeText(UpdateVoucherShopActivity.this, "Voucher Code is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(voucherdes)){
            Toast.makeText(UpdateVoucherShopActivity.this, "Voucher Description is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(exprieddate.equals("Ngày hết hạn")){
            Toast.makeText(UpdateVoucherShopActivity.this, "Voucher Expired date is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(voucherQuantity.getText().toString().trim())){
            Toast.makeText(UpdateVoucherShopActivity.this, "Voucher quantity is required...", Toast.LENGTH_SHORT).show();
            return;
        } else quantity = Integer.parseInt(voucherQuantity.getText().toString().trim());
        if(TextUtils.isEmpty(voucherDiscountPrice.getText().toString().trim())){
            Toast.makeText(UpdateVoucherShopActivity.this, "Voucher discount price is required...", Toast.LENGTH_SHORT).show();
            return;
        } else disprice = Integer.parseInt(voucherDiscountPrice.getText().toString().trim());
        if(TextUtils.isEmpty(minimumPrice.getText().toString().trim())){
            Toast.makeText(UpdateVoucherShopActivity.this, "Voucher quantity is required...", Toast.LENGTH_SHORT).show();
            return;
        } else miniprice = Integer.parseInt(minimumPrice.getText().toString().trim());

        UploadData();
    }

    private void UploadData() {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        Voucher voucher = new Voucher(voucherid, vouchercode, voucherdes, exprieddate, disprice,quantity, miniprice);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Shop").child("Vouchers").child(voucherid).setValue(voucher)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        LoadVoucherDetail();
                        Toast.makeText(UpdateVoucherShopActivity.this, "Update voucher successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void datepickDialog(){
        Calendar c = Calendar.getInstance();
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mMonth = c.get(Calendar.MONTH);
        int mYear = c.get(Calendar.YEAR);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                DecimalFormat decimalFormat = new DecimalFormat("00");
                String pDay = decimalFormat.format(day);
                String pMonth = decimalFormat.format(month);
                String pYear = ""+year;
                String pDate=pDay+"/"+pMonth+"/"+pYear;
                expiredDate.setText(pDate);
            }
        }, mYear,mMonth,mDay);
        dialog.show();
        dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
    }
    
    private void initUI() {
        btnback=findViewById(R.id.btnback);
        btndelete=findViewById(R.id.btndelete);
        voucherCode=findViewById(R.id.voucherCode);
        voucherDes=findViewById(R.id.voucherDes);
        voucherDiscountPrice=findViewById(R.id.voucherDiscountPrice);
        minimumPrice=findViewById(R.id.minimumPrice);
        voucherQuantity=findViewById(R.id.voucherQuantity);
        expiredDate=findViewById(R.id.expiredDate);
        btnUpdateVoucher=findViewById(R.id.btnUpdateVoucher);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(UpdateVoucherShopActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }
}