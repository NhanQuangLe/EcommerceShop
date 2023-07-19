package com.example.ecommerceshop.tinh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.toast.CustomToast;
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
            hideKeyboard(view);
            if (IsValidEmail())
            {
                OnClickConfirmReset();
            }
        });

        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        editTextEmail.setOnClickListener(v -> {
            if (editTextEmail.getText().toString().trim().isEmpty())
            {
                editTextEmail.setBackgroundResource(R.drawable.background_input_error);
                textErrorEmail.setText("Vui lòng nhập email để đặt lại mật khẩu!");
                textErrorEmail.setTextColor(Color.parseColor("#E10000"));
                textErrorEmail.setVisibility(View.VISIBLE);
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches())
            {
                editTextEmail.setBackgroundResource(R.drawable.background_input_error);
                textErrorEmail.setText("Vui lòng nhập email hợp lệ!");
                textErrorEmail.setTextColor(Color.parseColor("#E10000"));
                textErrorEmail.setVisibility(View.VISIBLE);
            }
            else
            {
                editTextEmail.setBackgroundResource(R.drawable.background_input);
                textErrorEmail.setVisibility(View.INVISIBLE);
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
                textErrorEmail.setText("Email hợp lệ. Vui lòng kiểm tra email của bạn để đặt lại mật khẩu!");
                textErrorEmail.setTextColor(Color.parseColor("#08FF00"));
                textErrorEmail.setVisibility(View.VISIBLE);
                CustomToast.makeText(ForgotPasswordActivity.this, "Email sent!", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
            }
            else
            {
                loading(false);
                buttonConfirm.setVisibility(View.VISIBLE);
                textErrorEmail.setText("Gửi email không thành công hoặc Email có thể chưa được đăng ký!");
                textErrorEmail.setTextColor(Color.parseColor("#E10000"));
                textErrorEmail.setVisibility(View.VISIBLE);
            }
        });
    }
    private boolean IsValidEmail() {
        if (editTextEmail.getText().toString().trim().isEmpty())
        {
            editTextEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Vui lòng nhập email để đặt lại mật khẩu!");
            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches())
        {
            editTextEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Vui lòng nhập email hợp lệ!");
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
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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