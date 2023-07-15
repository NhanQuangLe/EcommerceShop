package com.example.ecommerceshop.tinh.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.MainUserActivity;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.utilities.Constants;
import com.example.ecommerceshop.utilities.PreferenceManagement;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private PreferenceManagement preferenceManagement;
    private EditText signupEmail, signupPassword, signupConfirmPass;
    private Button buttonSignUp;
    private TextView loginTextView, textErrorEmail, textErrorPassword, textErrorConfirmPassword;
    private ProgressBar signupProgressBar;
    private ImageView eyeImagePass, eyeConfirmPassword;
    private LinearLayout googleButton;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        preferenceManagement = new PreferenceManagement(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        InitUI();
        setListeners();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);
    }

    void InitUI()
    {
        signupEmail= findViewById(R.id.editTextEmail);
        signupPassword = findViewById(R.id.editTextPassword);
        textErrorEmail = findViewById(R.id.textErrorEmail);
        textErrorPassword = findViewById(R.id.textErrorPassword);
        eyeImagePass = findViewById(R.id.eyePassword);
        loginTextView = findViewById(R.id.textHaveAccount);
        signupProgressBar = findViewById(R.id.progressBar);
        buttonSignUp = findViewById(R.id.buttonSignup);
        googleButton = findViewById(R.id.buttonGoogle);
        signupConfirmPass = findViewById(R.id.editTextConfirmPass);
        textErrorConfirmPassword = findViewById(R.id.textErrorConfirmPass);
        eyeConfirmPassword = findViewById(R.id.eyeConfirmPassword);
    }
    private void setListeners() {
        loginTextView.setOnClickListener(view -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
        eyeImagePass.setImageResource(R.drawable.ic_eye);
        eyeImagePass.setOnClickListener(view -> HandleEyePassword());
        eyeConfirmPassword.setImageResource(R.drawable.ic_eye);
        eyeConfirmPassword.setOnClickListener(view -> HandleEyeConfirmPassword());
        buttonSignUp.setOnClickListener(view -> {
            hideKeyboard(view);
            Boolean checkEmail = IsValidSignUpEmail();
            Boolean checkPassword = IsValidSignUpPassword();
            Boolean checkConfirmPass = IsValidSignUpConfirmPassword();
            if (checkEmail && checkPassword && checkConfirmPass)
                signUp();
        });
        googleButton.setOnClickListener(v -> LoginWithGoogle());
        signupEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        signupEmail.setOnClickListener(v -> {
            if (signupEmail.getText().toString().trim().isEmpty())
            {
                signupEmail.setBackgroundResource(R.drawable.background_input_error);
                textErrorEmail.setText("Vui lòng nhập email để tiếp tục!");
                textErrorEmail.setTextColor(Color.parseColor("#E10000"));
                textErrorEmail.setVisibility(View.VISIBLE);
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(signupEmail.getText().toString()).matches())
            {
                signupEmail.setBackgroundResource(R.drawable.background_input_error);
                textErrorEmail.setText("Vui lòng nhập email hợp lệ!");
                textErrorEmail.setTextColor(Color.parseColor("#E10000"));
                textErrorEmail.setVisibility(View.VISIBLE);
            }
            else
            {
                signupEmail.setBackgroundResource(R.drawable.background_input);
                textErrorEmail.setVisibility(View.INVISIBLE);
            }
        });
        signupPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        signupPassword.setOnClickListener(v -> {
            if (signupPassword.getText().toString().trim().isEmpty())
            {
                signupPassword.setBackgroundResource(R.drawable.background_input_error);
                textErrorPassword.setText("Vui lòng nhập mật khẩu để tiếp tục!");
                textErrorPassword.setTextColor(Color.parseColor("#E10000"));
                textErrorPassword.setVisibility(View.VISIBLE);
            }
            else
            {
                if (signupPassword.getText().toString().trim().length() < 8)
                {
                    signupPassword.setBackgroundResource(R.drawable.background_input_error);
                    textErrorPassword.setText("Mật khẩu phải có độ dài từ 8 ký tự!");
                    textErrorPassword.setTextColor(Color.parseColor("#E10000"));
                    textErrorPassword.setVisibility(View.VISIBLE);
                }
                else
                {
                    signupPassword.setBackgroundResource(R.drawable.background_input);
                    textErrorPassword.setVisibility(View.INVISIBLE);
                }
            }
        });
        signupConfirmPass.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        signupConfirmPass.setOnClickListener(v -> {
            if (signupConfirmPass.getText().toString().trim().isEmpty())
            {
                signupConfirmPass.setBackgroundResource(R.drawable.background_input_error);
                textErrorConfirmPassword.setText("Vui lòng nhập lại mật khẩu!");
                textErrorConfirmPassword.setTextColor(Color.parseColor("#E10000"));
                textErrorConfirmPassword.setVisibility(View.VISIBLE);
            }
            else
            {
                String pass = signupPassword.getText().toString().trim();
                String Confirm_pass = signupConfirmPass.getText().toString().trim();
                if (!pass.equals(Confirm_pass))
                {
                    signupConfirmPass.setBackgroundResource(R.drawable.background_input_error);
                    textErrorConfirmPassword.setText("Không chính xác với mật khẩu bên trên. Vui lòng nhập lại!");
                    textErrorConfirmPassword.setTextColor(Color.parseColor("#E10000"));
                    textErrorConfirmPassword.setVisibility(View.VISIBLE);
                }
                else
                {
                    signupConfirmPass.setBackgroundResource(R.drawable.background_input);
                    textErrorConfirmPassword.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void HandleEyeConfirmPassword() {
        if (signupConfirmPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
        {
            signupConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            eyeConfirmPassword.setImageResource(R.drawable.ic_eye);
        }
        else
        {
            signupConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            eyeConfirmPassword.setImageResource(R.drawable.ic_not_eye);
        }
    }

    private Boolean IsValidSignUpConfirmPassword() {
        if (signupConfirmPass.getText().toString().trim().isEmpty())
        {
            signupConfirmPass.setBackgroundResource(R.drawable.background_input_error);
            textErrorConfirmPassword.setText("Vui lòng nhập lại mật khẩu!");
            textErrorConfirmPassword.setTextColor(Color.parseColor("#E10000"));
            textErrorConfirmPassword.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            String pass = signupPassword.getText().toString().trim();
            String Confirm_pass = signupConfirmPass.getText().toString().trim();
            if (!pass.equals(Confirm_pass))
            {
                signupConfirmPass.setBackgroundResource(R.drawable.background_input_error);
                textErrorConfirmPassword.setText("Không chính xác với mật khẩu bên trên. Vui lòng nhập lại!");
                textErrorConfirmPassword.setTextColor(Color.parseColor("#E10000"));
                textErrorConfirmPassword.setVisibility(View.VISIBLE);
                return false;
            }
            else
            {
                signupConfirmPass.setBackgroundResource(R.drawable.background_input);
                textErrorConfirmPassword.setVisibility(View.INVISIBLE);
                return true;
            }
        }
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
                Toast.makeText(getApplicationContext(), "Một vài điều gì đó đang không đúng", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                Intent intent = new Intent(SignUpActivity.this, MainUserActivity.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUp() {
        loading(true);
        Intent intent = new Intent(SignUpActivity.this, InputInfoActivity.class);
        intent.putExtra("email",signupEmail.getText().toString().trim());
        intent.putExtra("password",signupPassword.getText().toString().trim());
        startActivity(intent);
        loading(false);
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
            signupEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Vui lòng email để tiếp tục!");
            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(signupEmail.getText().toString()).matches())
        {
            signupEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Vui lòng nhập email hợp lệ!");
            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            signupEmail.setBackgroundResource(R.drawable.background_input);
            textErrorEmail.setVisibility(View.INVISIBLE);
            return true;
        }
    }
    @SuppressLint("SetTextI18n")
    private Boolean IsValidSignUpPassword()
    {
        if (signupPassword.getText().toString().trim().isEmpty())
        {
            signupPassword.setBackgroundResource(R.drawable.background_input_error);
            textErrorPassword.setText("Vui lòng nhập mật khẩu để tiếp tục!");
            textErrorPassword.setTextColor(Color.parseColor("#E10000"));
            textErrorPassword.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            if (signupPassword.getText().toString().trim().length() < 8)
            {
                signupPassword.setBackgroundResource(R.drawable.background_input_error);
                textErrorPassword.setText("Mật khẩu phải có độ dài từ 8 ký tự!");
                textErrorPassword.setTextColor(Color.parseColor("#E10000"));
                textErrorPassword.setVisibility(View.VISIBLE);
                return false;
            }
            else
            {
                signupPassword.setBackgroundResource(R.drawable.background_input);
                textErrorPassword.setVisibility(View.INVISIBLE);
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
