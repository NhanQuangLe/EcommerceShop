package com.example.ecommerceshop.qui.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ecommerceshop.MainUserActivity;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityOrderSuccessBinding;

public class OrderSuccessActivity extends AppCompatActivity {

    private ActivityOrderSuccessBinding mActivityOrderSuccessBinding;
    private View mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityOrderSuccessBinding = ActivityOrderSuccessBinding.inflate(getLayoutInflater());
        mView = mActivityOrderSuccessBinding.getRoot();
        setContentView(mView);

        mActivityOrderSuccessBinding.btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderSuccessActivity.this, MainUserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}