package com.example.ecommerceshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.Model.Voucher;
import com.example.ecommerceshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.Calendar;

public class AddVoucherActivity extends AppCompatActivity {

    ImageView btnBack;
    TextInputEditText voucherCode,voucherDes,voucherDiscountPrice,minimumPrice,voucherQuantity;
    TextView expiredDate;
    Button btnAddVoucher;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voucher_shop);
        initUI();
        btnBack.setOnClickListener(new View.OnClickListener() {
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
        btnAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputData();
            }
        });
    }
    String vouchercode, voucherdes, exprieddate;
    int quantity, disprice, miniprice;
    private void InputData() {
        vouchercode=voucherCode.getText().toString().trim();
        voucherdes=voucherDes.getText().toString().trim();
        exprieddate=expiredDate.getText().toString().trim();
        if(TextUtils.isEmpty(vouchercode)){
            Toast.makeText(AddVoucherActivity.this, "Voucher Code is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(voucherdes)){
            Toast.makeText(AddVoucherActivity.this, "Voucher Description is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(exprieddate.equals("Ngày hết hạn")){
            Toast.makeText(AddVoucherActivity.this, "Voucher Expired date is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(voucherQuantity.getText().toString().trim())){
            Toast.makeText(AddVoucherActivity.this, "Voucher quantity is required...", Toast.LENGTH_SHORT).show();
            return;
        } else quantity = Integer.parseInt(voucherQuantity.getText().toString().trim());
        if(TextUtils.isEmpty(voucherDiscountPrice.getText().toString().trim())){
            Toast.makeText(AddVoucherActivity.this, "Voucher discount price is required...", Toast.LENGTH_SHORT).show();
            return;
        } else disprice = Integer.parseInt(voucherDiscountPrice.getText().toString().trim());
        if(TextUtils.isEmpty(minimumPrice.getText().toString().trim())){
            Toast.makeText(AddVoucherActivity.this, "Voucher quantity is required...", Toast.LENGTH_SHORT).show();
            return;
        } else miniprice = Integer.parseInt(minimumPrice.getText().toString().trim());

        UploadData();
    }

    private void UploadData() {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        String timestamp =""+ System.currentTimeMillis();
        Voucher voucher = new Voucher(timestamp, vouchercode, voucherdes, exprieddate, disprice,quantity, miniprice);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Shop").child("Vouchers").child(timestamp).setValue(voucher)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        ResetData();
                        Toast.makeText(AddVoucherActivity.this, "Add voucher successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ResetData() {
        voucherCode.setText("");
        voucherDes.setText("");
        voucherQuantity.setText("");
        voucherDiscountPrice.setText("");
        minimumPrice.setText("");
        expiredDate.setText("Ngày hết hạn");
    }

    private void datepickDialog(){
        Calendar c = Calendar.getInstance();
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mMonth = c.get(Calendar.MONTH)+1;
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
        btnBack=findViewById(R.id.btnBack);
        voucherCode=findViewById(R.id.voucherCode);
        voucherDes=findViewById(R.id.voucherDes);
        voucherDiscountPrice=findViewById(R.id.voucherDiscountPrice);
        minimumPrice=findViewById(R.id.minimumPrice);
        voucherQuantity=findViewById(R.id.voucherQuantity);
        expiredDate=findViewById(R.id.expiredDate);
        btnAddVoucher=findViewById(R.id.btnAddVoucher);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(AddVoucherActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }
}