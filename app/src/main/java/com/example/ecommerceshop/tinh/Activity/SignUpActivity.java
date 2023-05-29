package com.example.ecommerceshop.tinh.Activity;

import androidx.annotation.Nullable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.MainActivity;
import com.example.ecommerceshop.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
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
    private ImageButton googleButton;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        InitUI();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            navigateToSecondActivity();
        }
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
        googleButton = findViewById(R.id.buttonGoogle);
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
        googleButton.setOnClickListener(v -> LoginWithGoogle());
    }

    private void LoginWithGoogle() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
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
    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(activity_sign_up.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
