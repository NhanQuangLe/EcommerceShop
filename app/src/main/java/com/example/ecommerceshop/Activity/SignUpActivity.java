package com.example.ecommerceshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.ecommerceshop.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    EditText name, phone, address,email,password,shopname,deliveryfee;
    AppCompatButton signup;
    String txtname, txtphone, txtaddress, txtemail, txtpassword, txtshopname, txtdeliveryfee;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup=findViewById(R.id.Resgiter);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        shopname=findViewById(R.id.shopname);
        deliveryfee=findViewById(R.id.deliveryfee);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtname=name.getText().toString().trim();
                txtphone=phone.getText().toString().trim();
                txtaddress=address.getText().toString().trim();
                txtemail=email.getText().toString().trim();
                txtpassword=password.getText().toString().trim();
                txtshopname=shopname.getText().toString().trim();
                txtdeliveryfee=deliveryfee.getText().toString().trim();
                progressDialog.setMessage("Creating account...");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(txtemail,txtpassword)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        saveFirebaseData();
                    }
                });

            }
        });

    }

    private void saveFirebaseData() {
        progressDialog.setMessage("Saving info...");
        String timestamp=""+System.currentTimeMillis();
        HashMap<String, Object> info = new HashMap<>();
        info.put("uid", ""+firebaseAuth.getUid());
        info.put("name", ""+txtname);
        info.put("phone", ""+txtphone);
        info.put("address", ""+txtaddress);
        info.put("email", ""+txtemail);
        info.put("shopname", ""+txtshopname);
        info.put("deliveryfee", ""+txtdeliveryfee);
        info.put("timestamp", ""+timestamp);
        info.put("userType", "Seller");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).setValue(info)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                startActivity(new Intent(SignUpActivity.this, MainShopActivity.class));
                finish();
            }
        });
    }
}