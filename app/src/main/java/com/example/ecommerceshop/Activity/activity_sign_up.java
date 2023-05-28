package com.example.ecommerceshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.ecommerceshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class activity_sign_up extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private Button buttonSignUp;
    private TextView loginTextView, textErrorEmail, textErrorPassword;
    private ProgressBar signupProgressBar;
    private AppCompatImageView buttonBack;
    private ImageView eyeImagePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        InitUI();
        setListeners();
    }

    void InitUI()
    {
        signupEmail= findViewById(R.id.editTextEmail);
        signupPassword = findViewById(R.id.editTextPassword);
        textErrorEmail = findViewById(R.id.textErrorEmail);
        textErrorPassword = findViewById(R.id.textErrorPassword);
        eyeImagePass = findViewById(R.id.eyePassword);
        loginTextView = findViewById(R.id.textHaveAccount);
        buttonBack = findViewById(R.id.buttonBack);
        signupProgressBar = findViewById(R.id.progressBar);
        buttonSignUp = findViewById(R.id.buttonSignup);
    }
    private void setListeners() {
        buttonBack.setOnClickListener(view -> onBackPressed());
        loginTextView.setOnClickListener(view -> startActivity(new Intent(activity_sign_up.this, activity_login.class)));
        eyeImagePass.setImageResource(R.drawable.ic_eye);
        eyeImagePass.setOnClickListener(view -> {
            HandleEyePassword();
        });
        buttonSignUp.setOnClickListener(view -> {
            Boolean checkEmail = IsValidSignUpEmail();
            Boolean checkPassword = IsValidSignUpPassword();
            if (checkEmail && checkPassword)
                signUp();
        });
    }

    private void signUp() {
        loading(true);
        String email = signupEmail.getText().toString().trim();
        String pass = signupPassword.getText().toString().trim();
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                loading(false);
                Toast.makeText(activity_sign_up.this, "SignUp Successful !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(activity_sign_up.this, activity_login.class));
            }
            else
            {
                loading(false);
                Toast.makeText(activity_sign_up.this, "SignUp Failed! " + Objects.requireNonNull(task.getException()).getMessage() + ". Try signing up with a new email account or login with this one!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void HandleEyePassword()
    {
        if (signupPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
        {
            signupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            eyeImagePass.setImageResource(R.drawable.ic_eye);
        }
        else
        {
            signupPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            eyeImagePass.setImageResource(R.drawable.ic_not_eye);
        }
    }
    private Boolean IsValidSignUpEmail()
    {
        if (signupEmail.getText().toString().trim().isEmpty())
        {
            showToast("Please enter email!");
            signupEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Please enter email!");
            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(signupEmail.getText().toString()).matches())
        {
            showToast("Please enter valid email!");
            signupEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Please enter valid email!");
            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            signupEmail.setBackgroundResource(R.drawable.background_input);
            textErrorEmail.setText("Valid email account");
            textErrorEmail.setTextColor(Color.parseColor("#08FF00"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return true;
        }
    }
    private Boolean IsValidSignUpPassword()
    {
        if (signupPassword.getText().toString().trim().isEmpty())
        {
            showToast("Please enter password!");
            signupPassword.setBackgroundResource(R.drawable.background_input_error);
            textErrorPassword.setText("Please enter password!");
            textErrorPassword.setTextColor(Color.parseColor("#E10000"));
            textErrorPassword.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            if (signupPassword.getText().toString().trim().length() < 8)
            {
                showToast("Please enter a password longer than 8 characters!");
                signupPassword.setBackgroundResource(R.drawable.background_input_error);
                textErrorPassword.setText("Please enter a password longer than 8 characters!");
                textErrorPassword.setTextColor(Color.parseColor("#E10000"));
                textErrorPassword.setVisibility(View.VISIBLE);
                return false;
            }
            else
            {
                signupPassword.setBackgroundResource(R.drawable.background_input);
                textErrorPassword.setText("Valid password");
                textErrorPassword.setTextColor(Color.parseColor("#08FF00"));
                textErrorPassword.setVisibility(View.VISIBLE);
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
            buttonSignUp.setVisibility(View.INVISIBLE);
            signupProgressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            buttonSignUp.setVisibility(View.VISIBLE);
            signupProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
