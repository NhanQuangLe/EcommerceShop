package com.example.ecommerceshop.tinh.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
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
import com.example.ecommerceshop.Phat.Activity.AdminActivity;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.toast.CustomToast;
import com.example.ecommerceshop.utilities.Constants;
import com.example.ecommerceshop.utilities.PreferenceManagement;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser mCurrentUser;
    private String avt;
    private GoogleSignInAccount mGoogleAccount;
    private EditText loginEmail, loginPass;
    private TextView textForgotPass, textSignUp, textErrorEmail, textErrorPassword;
    private Button loginButton;
    private ProgressBar loginProgressBar;
    private ImageView eyeImagePass;
    private LinearLayout googleButton;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private PreferenceManagement preferenceManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferenceManagement = new PreferenceManagement(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        InitUI();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            navigateToSecondActivity();
            return;
        }
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getEmail().equals("admin@gmail.com")) {
                finish();
                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent i = getIntent();
                if (i.getBooleanExtra("signUp", false) == false){
                    finish();
                    Intent intent = new Intent(LoginActivity.this, MainUserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    preferenceManagement.putString(Constants.KEY_COLLECTION_USER,auth.getCurrentUser().getUid());
                    startActivity(intent);
                }

            }

        }
        setListener();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);
    }

    void navigateToSecondActivity() {
        finish();
        Intent intent = new Intent(LoginActivity.this, MainUserActivity.class);
        preferenceManagement.putString(Constants.KEY_COLLECTION_USER,auth.getCurrentUser().getUid());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setListener() {
        textSignUp.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
        textForgotPass.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        eyeImagePass.setImageResource(R.drawable.ic_eye);
        eyeImagePass.setOnClickListener(view -> {
            HandleEyePassword();
        });
        loginButton.setOnClickListener(view -> {
            hideKeyboard(view);
            Boolean checkEmail = IsValidLoginEmail();
            Boolean checkPassword = IsValidLoginPassword();
            if (checkEmail && checkPassword) {
                Login();
            }
        });
        googleButton.setOnClickListener(v -> LoginWithGoogle());
        loginEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        loginEmail.setOnClickListener(v -> {
            if (loginEmail.getText().toString().trim().isEmpty()) {
                loginEmail.setBackgroundResource(R.drawable.background_input_error);
                textErrorEmail.setText("Vui lòng nhập email để đăng nhập!");
                textErrorEmail.setTextColor(Color.parseColor("#E10000"));
                textErrorEmail.setVisibility(View.VISIBLE);
            } else if (!Patterns.EMAIL_ADDRESS.matcher(loginEmail.getText().toString()).matches()) {
                loginEmail.setBackgroundResource(R.drawable.background_input_error);
                textErrorEmail.setText("Vui lòng nhập email hợp lệ!");
                textErrorEmail.setTextColor(Color.parseColor("#E10000"));
                textErrorEmail.setVisibility(View.VISIBLE);
            } else {
                loginEmail.setBackgroundResource(R.drawable.background_input);
                textErrorEmail.setVisibility(View.INVISIBLE);
            }
        });
        loginPass.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        loginPass.setOnClickListener(v -> {
            if (loginPass.getText().toString().trim().isEmpty()) {
                loginPass.setBackgroundResource(R.drawable.background_input_error);
                textErrorPassword.setText("Vui lòng nhập mật khẩu để đăng nhập!");
                textErrorPassword.setTextColor(Color.parseColor("#E10000"));
                textErrorPassword.setVisibility(View.VISIBLE);
            } else {
                if (!loginPass.getText().toString().trim().isEmpty() && loginPass.getText().toString().trim().length() < 8) {
                    loginPass.setBackgroundResource(R.drawable.background_input_error);
                    textErrorPassword.setText("Mật khẩu phải có độ dài từ 8 ký tự!");
                    textErrorPassword.setTextColor(Color.parseColor("#E10000"));
                    textErrorPassword.setVisibility(View.VISIBLE);
                } else {
                    loginPass.setBackgroundResource(R.drawable.background_input);
                    textErrorEmail.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void LoginWithGoogle() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                mGoogleAccount = account;
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
            if (task.isSuccessful()) {
                FirebaseUser currentUser = auth.getCurrentUser();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + currentUser.getUid());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(getApplicationContext(), "SignIn Successful!", Toast.LENGTH_SHORT).show();
                            Intent i2 = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i2);
                        } else {
                            Started(currentUser);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "Failed...........", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void Started(FirebaseUser currentUser) {
        loading(true);

        String email = mGoogleAccount.getEmail();
        CreateAccountChat(email, currentUser);

        loading(false);
    }

    private void CreateAccountChat(String email, FirebaseUser currentUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> userChat = new HashMap<>();
        userChat.put(Constants.KEY_EMAIL, email);
        db.collection(Constants.KEY_COLLECTION_USER).document(currentUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                        } else {
                            db.collection(Constants.KEY_COLLECTION_USER).document(currentUser.getUid())
                                    .set(userChat)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            loading(false);
                                            preferenceManagement.putString(Constants.KEY_USER_ID, currentUser.getUid());
                                            SaveImgInFirebaseStorage(email, currentUser);

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            loading(false);
                                            showToast(e.getMessage());
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void SaveImgInFirebaseStorage(String email, FirebaseUser currentUser) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + currentUser.getUid() + "/Customer/CustomerInfos");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    loading(true);
                    Intent i = new Intent(getApplicationContext(), InputInfoActivity.class);
                    i.putExtra("withGoogle", true);
                    i.putExtra("userId", currentUser.getUid());
                    i.putExtra("email", currentUser.getEmail());
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "SignUp Successful!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void Login() {
        String email = loginEmail.getText().toString().trim();
        String pass = loginPass.getText().toString().trim();
        loading(true);
        if (email.equals("admin@gmail.com") && pass.equals("16032003")) {
            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(authResult -> {
                loading(false);
                Toast.makeText(LoginActivity.this, "Login Successful !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                finish();
            }).addOnFailureListener(e -> {
                loading(false);
                Toast.makeText(LoginActivity.this, "Login Failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(authResult -> {
                loading(false);
                CustomToast.makeText(getApplicationContext(), "Login Successful!", CustomToast.SHORT,CustomToast.SUCCESS).show();
                preferenceManagement.putString(Constants.KEY_COLLECTION_USER,auth.getCurrentUser().getUid());
                startActivity(new Intent(LoginActivity.this, MainUserActivity.class));
                finish();
            }).addOnFailureListener(e -> {
                loading(false);
                Toast.makeText(LoginActivity.this, "Login Failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void InitUI() {
        loginEmail = findViewById(R.id.editTextEmail);
        loginPass = findViewById(R.id.editTextPassword);
        textErrorEmail = findViewById(R.id.textErrorEmail);
        textErrorPassword = findViewById(R.id.textErrorPassword);
        textForgotPass = findViewById(R.id.textForgotPass);
        textSignUp = findViewById(R.id.textSignUpAccount);
        loginButton = findViewById(R.id.buttonLogin);
        eyeImagePass = findViewById(R.id.eyePassword);
        loginProgressBar = findViewById(R.id.progressBar);
        googleButton = findViewById(R.id.buttonGoogle);
    }

    private void HandleEyePassword() {
        if (loginPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
            loginPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            eyeImagePass.setImageResource(R.drawable.ic_eye);
        } else {
            loginPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            eyeImagePass.setImageResource(R.drawable.ic_not_eye);
        }
    }

    private Boolean IsValidLoginEmail() {
        if (loginEmail.getText().toString().trim().isEmpty()) {
            loginEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Vui lòng nhập email để đăng nhập!");
            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(loginEmail.getText().toString()).matches()) {
            loginEmail.setBackgroundResource(R.drawable.background_input_error);
            textErrorEmail.setText("Vui lòng nhập email hợp lệ!");
            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
            textErrorEmail.setVisibility(View.VISIBLE);
            return false;
        } else {
            loginEmail.setBackgroundResource(R.drawable.background_input);
            textErrorEmail.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private Boolean IsValidLoginPassword() {
        if (loginPass.getText().toString().trim().isEmpty()) {
            loginPass.setBackgroundResource(R.drawable.background_input_error);
            textErrorPassword.setText("Vui lòng nhập mật khẩu để đăng nhập!");
            textErrorPassword.setTextColor(Color.parseColor("#E10000"));
            textErrorPassword.setVisibility(View.VISIBLE);
            return false;
        } else {
            if (!loginPass.getText().toString().trim().isEmpty() && loginPass.getText().toString().trim().length() < 8) {
                loginPass.setBackgroundResource(R.drawable.background_input_error);
                textErrorPassword.setText("Mật khẩu phải có độ dài từ 8 ký tự!");
                textErrorPassword.setTextColor(Color.parseColor("#E10000"));
                textErrorPassword.setVisibility(View.VISIBLE);
                return false;
            } else {
                loginPass.setBackgroundResource(R.drawable.background_input);
                textErrorEmail.setVisibility(View.INVISIBLE);
                return true;
            }
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            loginButton.setVisibility(View.INVISIBLE);
            loginProgressBar.setVisibility(View.VISIBLE);
        } else {
            loginButton.setVisibility(View.VISIBLE);
            loginProgressBar.setVisibility(View.INVISIBLE);
        }
    }

}