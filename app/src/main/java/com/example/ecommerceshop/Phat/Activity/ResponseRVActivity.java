package com.example.ecommerceshop.Phat.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ResponseRVActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_dialog_sheet_response_rv);
        TextInputEditText responseRv = findViewById(R.id.responseRv);
        Button btnSendResponse = findViewById(R.id.btnSendResponse);
        ImageView btnBack = findViewById(R.id.btnBack);
        String cusid = getIntent().getStringExtra("cusId");
        String rvId = getIntent().getStringExtra("rvId");
        btnSendResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String respone=responseRv.getText().toString().trim();
                if(respone.isEmpty()){
                    CustomToast.makeText(getApplicationContext(),"Your response is empty!",CustomToast.SHORT,CustomToast.ERROR).show();
                    return;
                }
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("rvResponse", respone);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.child(cusid).child("Customer").child("Reviews").child(rvId)
                        .updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                CustomToast.makeText(getApplicationContext(),"Response to your customer successfully!",CustomToast.SHORT,CustomToast.SUCCESS).show();

                                onBackPressed();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                CustomToast.makeText(getApplicationContext(),e.getMessage()+"",CustomToast.SHORT,CustomToast.ERROR).show();

                            }
                        });
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
