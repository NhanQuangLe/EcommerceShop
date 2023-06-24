package com.example.ecommerceshop.tinh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private AppCompatImageView buttonBack;
    private EditText editTextEmail;
    private TextView textErrorEmail, textGoToLogin;
    private ProgressBar progress;
    private Button buttonConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        auth = FirebaseAuth.getInstance();
        InitUI();
        setListener();
    }

    private void setListener() {
        buttonBack.setOnClickListener(view -> startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class)));
        textGoToLogin.setOnClickListener(view -> startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class)));
        buttonConfirm.setOnClickListener(view -> {
            if (IsValidEmail())
            {
                OnClickConfirmReset();
            }
        });
    }
    private void OnClickConfirmReset() {
        String email = editTextEmail.getText().toString().trim();
        loading(true);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                loading(false);
                buttonConfirm.setVisibility(View.INVISIBLE);
                textGoToLogin.setVisibility(View.VISIBLE);
                textErrorEmail.setText("Email valid. Please check your email to reset password!");
                textErrorEmail.setTextColor(Color.parseColor("#08FF00"));
                textErrorEmail.setVisibility(View.VISIBLE);
                Toast.makeText(ForgotPasswordActivity.this, "Email sent!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                loading(false);
                buttonConfirm.setVisibility(View.VISIBLE);
                Toast.makeText(ForgotPasswordActivity.this, "Email sent failed or Email may not be registered!", Toast.LENGTH_SHORT).show();
                textErrorEmail.setText("Email sent failed or Email may not be registered!");
                textErrorEmail.setTextColor(Color.parseColor("#E10000"));
                textErrorEmail.setVisibility(View.VISIBLE);
            }
        });
    }
    private boolean IsValidEmail() {
        if (editTextEmail.getText().toString().trim().isEmpty())
        {
            showToast("Please enter email to reset password!");
            editTextEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Please enter email to reset password!");
            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches())
        {
            showToast("Please enter valid email!");
            editTextEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Please enter valid email!");
            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            editTextEmail.setBackgroundResource(R.drawable.background_input);
            textErrorEmail.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private void InitUI() {
        buttonBack = findViewById(R.id.buttonBack);
        editTextEmail = findViewById(R.id.editTextEmailForgot);
        textErrorEmail = findViewById(R.id.textErrorEmail);
        textGoToLogin = findViewById(R.id.textGoToLogin);
        progress = findViewById(R.id.progressBar);
        buttonConfirm = findViewById(R.id.buttonResetPassword);
    }
    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void loading(Boolean isLoading)
    {
        if(isLoading)
        {
            buttonConfirm.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
        }
        else
        {
            buttonConfirm.setVisibility(View.VISIBLE);
            progress.setVisibility(View.INVISIBLE);
        }
    }
}