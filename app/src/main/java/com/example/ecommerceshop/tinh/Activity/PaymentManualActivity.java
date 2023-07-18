package com.example.ecommerceshop.tinh.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.ecommerceshop.R;

public class PaymentManualActivity extends AppCompatActivity {

    private ImageView buttonBackSidebar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_manual);

        buttonBackSidebar = findViewById(R.id.buttonBackSidebar);

        buttonBackSidebar.setOnClickListener(view -> onBackPressed());
    }
}