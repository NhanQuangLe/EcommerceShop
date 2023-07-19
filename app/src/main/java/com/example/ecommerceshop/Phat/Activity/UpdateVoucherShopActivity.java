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
import com.example.ecommerceshop.toast.CustomToast;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    Voucher voucher;
    private AlertDialog alertDialog;
    private int t=0;

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

                                if(checkexpired(voucher.getExpiredDate(), voucher.getQuantity())){
                                    isdelete=true;
                                    deleteVoucher(voucherid);
                                }
                                else{
                                    checkExistInCus(voucherid);
                                }
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

    private void checkExistInCus(String voucherid) {
        final boolean[] flat = {false};
        t = 1;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int num = (int) snapshot.getChildrenCount();
                for(DataSnapshot ds: snapshot.getChildren()){
                    if (flat[0] ) break;
                    String uid = ""+ds.getRef().getKey();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(uid).child("Customer").child("Vouchers").child(voucherid)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        flat[0] =true;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateVoucherShopActivity.this);
                                        builder.setTitle("Notification").setMessage("The voucher has been saved by the customer! Can not be deleted!")
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        return;
                                                    }
                                                });
                                        alertDialog = builder.create();
                                        alertDialog.show();
                                    }
                                    if (t==num){
                                        isdelete=true;
                                        deleteVoucher(voucherid);
                                    }
                                    t++;
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    CustomToast.makeText(getApplicationContext(),error.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();

                                }
                            });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                CustomToast.makeText(getApplicationContext(),error.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();

            }
        });
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
    private void deleteVoucher(String voucherid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Shop").child("Vouchers").child(voucherid).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot ds: snapshot.getChildren()){
                                    String uid = ""+ds.getRef().getKey();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                    ref.child(uid).child("Customer").child("Vouchers").child(voucherid).removeValue();
                                }
                                onBackPressed();
                                CustomToast.makeText(getApplicationContext(),"Delete voucher successfully!",CustomToast.SHORT,CustomToast.SUCCESS).show();

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                CustomToast.makeText(getApplicationContext(),""+ error.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CustomToast.makeText(getApplicationContext(),""+ e.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();

                    }
                });
    }

    private void LoadVoucherDetail() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Shop").child("Vouchers").child(voucherid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         voucher = new Voucher();
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
                        CustomToast.makeText(getApplicationContext(),""+ error.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();

                    }
                });
    }

    private void InputData() {
        vouchercode=voucherCode.getText().toString().trim();
        voucherdes=voucherDes.getText().toString().trim();
        exprieddate=expiredDate.getText().toString().trim();
        if(TextUtils.isEmpty(vouchercode)){
            CustomToast.makeText(getApplicationContext(),"Voucher Code is required...",CustomToast.SHORT,CustomToast.ERROR).show();

            return;
        }
        if(TextUtils.isEmpty(voucherdes)){
            CustomToast.makeText(getApplicationContext(),"Voucher Description is required...",CustomToast.SHORT,CustomToast.ERROR).show();
            return;
        }
        if(exprieddate.equals("Ngày hết hạn")){
            CustomToast.makeText(getApplicationContext(),"Voucher Expired date is required...",CustomToast.SHORT,CustomToast.ERROR).show();

            return;
        }
        if(TextUtils.isEmpty(voucherQuantity.getText().toString().trim())){
            CustomToast.makeText(getApplicationContext(),"Voucher quantity is required...",CustomToast.SHORT,CustomToast.ERROR).show();
            return;
        } else quantity = Integer.parseInt(voucherQuantity.getText().toString().trim());
        if(TextUtils.isEmpty(voucherDiscountPrice.getText().toString().trim())){
            CustomToast.makeText(getApplicationContext(),"Voucher discount price is required...",CustomToast.SHORT,CustomToast.ERROR).show();
            return;
        } else disprice = Integer.parseInt(voucherDiscountPrice.getText().toString().trim());
        if(TextUtils.isEmpty(minimumPrice.getText().toString().trim())){
            CustomToast.makeText(getApplicationContext(),"Voucher quantity is required...",CustomToast.SHORT,CustomToast.ERROR).show();

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
                        CustomToast.makeText(getApplicationContext(),"Update voucher successfully",CustomToast.SHORT,CustomToast.SUCCESS).show();

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
                String pMonth = decimalFormat.format(month+1);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}