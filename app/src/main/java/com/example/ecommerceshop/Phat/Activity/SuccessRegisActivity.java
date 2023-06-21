
package com.example.ecommerceshop.Phat.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ecommerceshop.MainUserActivity;
import com.example.ecommerceshop.R;

public class SuccessRegisActivity extends AppCompatActivity {

    AppCompatButton backtohome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_regis);
        backtohome=findViewById(R.id.backtohome);
        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuccessRegisActivity.this, MainUserActivity.class));
                finish();
            }
        });
    }
}