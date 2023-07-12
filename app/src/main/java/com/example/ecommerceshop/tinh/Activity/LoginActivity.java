package com.example.ecommerceshop.tinh.Activity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.MainShopActivity;
import com.example.ecommerceshop.MainUserActivity;
import com.example.ecommerceshop.Phat.Activity.AdminActivity;
import com.example.ecommerceshop.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private AppCompatImageView buttonBack;
    private EditText loginEmail, loginPass;
    private TextView textForgotPass, textSignUp, textErrorEmail, textErrorPassword;
    private Button loginButton;
    private ProgressBar loginProgressBar;
    private ImageView eyeImagePass;
    private LinearLayout googleButton;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        InitUI();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            navigateToSecondActivity();
        }
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser!=null)
        {
            if (currentUser.getEmail().equals("admin@gmail.com"))
            {
                finish();
                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else
            {
                finish();
                Intent intent = new Intent(LoginActivity.this, MainUserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        }
        setListener();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);

    }

    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(LoginActivity.this, MainUserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setListener() {
        textSignUp.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
        textForgotPass.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        buttonBack.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
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
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.e("e", Objects.requireNonNull(e.getMessage()).trim());
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                Intent intent = new Intent(LoginActivity.this, MainUserActivity.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Failed...........", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Login() {
        String email = loginEmail.getText().toString().trim();
        String pass = loginPass.getText().toString().trim();
        loading(true);
        if(email.equals("admin@gmail.com")&&pass.equals("16032003")){
            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(authResult -> {
                loading(false);
                Toast.makeText(LoginActivity.this, "Login Successful !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                finish();
            }).addOnFailureListener(e -> {
                loading(false);
                Toast.makeText(LoginActivity.this, "Login Failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
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
        googleButton = findViewById(R.id.buttonGoogle);
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
            loginEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Please enter email to login!");
            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(loginEmail.getText().toString()).matches())
        {
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