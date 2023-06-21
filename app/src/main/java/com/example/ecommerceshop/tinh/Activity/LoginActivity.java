package com.example.ecommerceshop.tinh.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.MainShopActivity;
import com.example.ecommerceshop.Phat.Activity.AdminActivity;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.MainUserActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private AppCompatImageView buttonBack;
    private EditText loginEmail, loginPass;
    private TextView textForgotPass, textSignUp, textErrorEmail, textErrorPassword;
    private Button loginButton;
    private ProgressBar loginProgressBar;
    private ImageView eyeImagePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        InitUI();
        setListener();
    }
    private void setListener() {
        textSignUp.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
        textForgotPass.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        buttonBack.setOnClickListener(view -> onBackPressed());
        eyeImagePass.setImageResource(R.drawable.ic_eye);
        eyeImagePass.setOnClickListener(view -> {
            HandleEyePassword();
        });
        loginButton.setOnClickListener(view -> {
            Boolean checkEmail = IsValidLoginEmail();
            Boolean checkPassword = IsValidLoginPassword();
            if (checkEmail && checkPassword)
            {
                Login();
            }
        });
    }

    private void Login() {
        loading(true);
        String email = loginEmail.getText().toString().trim();
        String pass = loginPass.getText().toString().trim();
        if(email.equals("admin@gmail.com")&&pass.equals("16032003")){
            loading(false);
            Toast.makeText(LoginActivity.this, "Login Successful !", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            finish();
        }
        else {
            auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(authResult -> {
                loading(false);
                Toast.makeText(LoginActivity.this, "Login Successful !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainShopActivity.class));
                finish();
            }).addOnFailureListener(e -> {
                loading(false);
                Toast.makeText(LoginActivity.this, "Login Failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }

    }

    private void InitUI()
    {
        loginEmail= findViewById(R.id.editTextEmail);
        loginPass = findViewById(R.id.editTextPassword);
        textErrorEmail = findViewById(R.id.textErrorEmail);
        textErrorPassword = findViewById(R.id.textErrorPassword);
        textForgotPass = findViewById(R.id.textForgotPass);
        textSignUp = findViewById(R.id.textSignUpAccount);
        buttonBack = findViewById(R.id.buttonBack);
        loginButton = findViewById(R.id.buttonLogin);
        eyeImagePass = findViewById(R.id.eyePassword);
        loginProgressBar = findViewById(R.id.progressBar);
    }
    private void HandleEyePassword()
    {
        if (loginPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
        {
            loginPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            eyeImagePass.setImageResource(R.drawable.ic_eye);
        }
        else
        {
            loginPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            eyeImagePass.setImageResource(R.drawable.ic_not_eye);
        }
    }
    private Boolean IsValidLoginEmail()
    {
        if (loginEmail.getText().toString().trim().isEmpty())
        {
            showToast("Please enter email to login!");
            loginEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Please enter email to login!");
            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(loginEmail.getText().toString()).matches())
        {
            showToast("Please enter valid email!");
            loginEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Please enter valid email!");
            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            loginEmail.setBackgroundResource(R.drawable.background_input);
            textErrorEmail.setVisibility(View.INVISIBLE);
            return true;
        }
    }
    private Boolean IsValidLoginPassword()
    {
        if (loginPass.getText().toString().trim().isEmpty())
        {
            showToast("Please enter password to login!");
            loginPass.setBackgroundResource(R.drawable.background_input_error);
            textErrorPassword.setText("Please enter password to login!");
            textErrorPassword.setTextColor(Color.parseColor("#E10000"));
            textErrorPassword.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            if (!loginPass.getText().toString().trim().isEmpty() && loginPass.getText().toString().trim().length() < 8)
            {
                showToast("A password longer than 8 characters!");
                loginPass.setBackgroundResource(R.drawable.background_input_error);
                textErrorPassword.setText("A password longer than 8 characters!");
                textErrorPassword.setTextColor(Color.parseColor("#E10000"));
                textErrorPassword.setVisibility(View.VISIBLE);
                return false;
            }

            else
            {
                loginPass.setBackgroundResource(R.drawable.background_input);
                textErrorEmail.setVisibility(View.INVISIBLE);
                return true;
            }
        }
    }
    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void loading(Boolean isLoading)
    {
        if(isLoading)
        {
            loginButton.setVisibility(View.INVISIBLE);
            loginProgressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            loginButton.setVisibility(View.VISIBLE);
            loginProgressBar.setVisibility(View.INVISIBLE);
        }
    }

}