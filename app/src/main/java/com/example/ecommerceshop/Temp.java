//package com.example.ecommerceshop;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatImageView;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.method.HideReturnsTransformationMethod;
//import android.text.method.PasswordTransformationMethod;
//import android.util.Base64;
//import android.util.Log;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.ecommerceshop.MainUserActivity;
//import com.example.ecommerceshop.R;
//import com.example.ecommerceshop.tinh.Activity.LoginActivity;
//import com.example.ecommerceshop.tinh.Activity.SignUpActivity;
//import com.example.ecommerceshop.utilities.Constants;
//import com.example.ecommerceshop.utilities.PreferenceManagement;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.GoogleAuthProvider;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.io.ByteArrayOutputStream;
//import java.util.HashMap;
//import java.util.Objects;
//
//public class Temp extends AppCompatActivity {
//
//    private FirebaseAuth auth;
//    private PreferenceManagement preferenceManagement;
//    private EditText signupEmail, signupPassword;
//    private Button buttonSignUp;
//    private TextView loginTextView, textErrorEmail, textErrorPassword;
//    private ProgressBar signupProgressBar;
//    private AppCompatImageView buttonBack;
//    private ImageView eyeImagePass;
//    private LinearLayout googleButton;
//    GoogleSignInOptions gso;
//    GoogleSignInClient gsc;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        preferenceManagement = new PreferenceManagement(getApplicationContext());
//        setContentView(R.layout.activity_sign_up);
//        auth = FirebaseAuth.getInstance();
//        InitUI();
//        setListeners();
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        gsc = GoogleSignIn.getClient(this,gso);
//    }
//
//    void InitUI()
//    {
//        signupEmail= findViewById(R.id.editTextEmail);
//        signupPassword = findViewById(R.id.editTextPassword);
//        textErrorEmail = findViewById(R.id.textErrorEmail);
//        textErrorPassword = findViewById(R.id.textErrorPassword);
//        eyeImagePass = findViewById(R.id.eyePassword);
//        loginTextView = findViewById(R.id.textHaveAccount);
//        buttonBack = findViewById(R.id.buttonBack);
//        signupProgressBar = findViewById(R.id.progressBar);
//        buttonSignUp = findViewById(R.id.buttonSignup);
//        googleButton = findViewById(R.id.buttonGoogle);
//    }
//    private void setListeners() {
//        buttonBack.setOnClickListener(view -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
//        loginTextView.setOnClickListener(view -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
//        eyeImagePass.setImageResource(R.drawable.ic_eye);
//        eyeImagePass.setOnClickListener(view -> HandleEyePassword());
//        buttonSignUp.setOnClickListener(view -> {
//            Boolean checkEmail = IsValidSignUpEmail();
//            Boolean checkPassword = IsValidSignUpPassword();
//            if (checkEmail && checkPassword)
//                signUp();
//        });
//        googleButton.setOnClickListener(v -> LoginWithGoogle());
//    }
//    private void LoginWithGoogle() {
//        Intent signInIntent = gsc.getSignInIntent();
//        startActivityForResult(signInIntent,100);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 100){
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account.getIdToken());
//            } catch (ApiException e) {
//                Log.e("e", Objects.requireNonNull(e.getMessage()).trim());
//                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
//            if (task.isSuccessful())
//            {
//                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//            else
//            {
//                Toast.makeText(getApplicationContext(), "Failed...........", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void signUp() {
//        loading(true);
//        String email = signupEmail.getText().toString().trim();
//        String pass = signupPassword.getText().toString().trim();
//        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
//            if (task.isSuccessful())
//            {
//                loading(false);
//                CreateAccountChat(email, pass);
//                Toast.makeText(SignUpActivity.this, "SignUp Successful!", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
//                i.putExtra("signUp",true);
//                startActivity(i);
//            }
//            else
//            {
//                loading(false);
//                Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage() + "Try signing up with a new email account or login with this one!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void CreateAccountChat(String email, String pass) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        HashMap<String, Object> userChat = new HashMap<>();
//        userChat.put(Constants.KEY_NAME, "Newbie");
//        userChat.put(Constants.KEY_EMAIL, email);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
//        String encodedImage = encodeImage(bitmap);
//        userChat.put(Constants.KEY_IMAGE, encodedImage);
//        db.collection(Constants.KEY_COLLECTION_USER).add(userChat)
//                .addOnSuccessListener(documentReference -> {
//                    loading(false);
//                    preferenceManagement.putString(Constants.KEY_USER_ID, documentReference.getId());
//                    preferenceManagement.putString(Constants.KEY_NAME, "Newbie");
//                    preferenceManagement.putString(Constants.KEY_IMAGE, encodedImage);
//                    String accountChatId = preferenceManagement.getString(Constants.KEY_USER_ID);
//                    String idCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+idCurrentUser+"/Customer/accountChatId");
//                    ref.setValue(accountChatId);
//                }).addOnFailureListener(exception -> {
//                    loading(false);
//                    showToast(exception.getMessage());
//                });
//    }
//
//    private String encodeImage(Bitmap bitmap)
//    {
//        int previewWidth = 150;
//        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
//        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(bytes, Base64.DEFAULT);
//    }
//    private void HandleEyePassword()
//    {
//        if (signupPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
//        {
//            signupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//            eyeImagePass.setImageResource(R.drawable.ic_eye);
//        }
//        else
//        {
//            signupPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//            eyeImagePass.setImageResource(R.drawable.ic_not_eye);
//        }
//    }
//    private Boolean IsValidSignUpEmail()
//    {
//        if (signupEmail.getText().toString().trim().isEmpty())
//        {
//            signupEmail.setBackgroundResource(R.drawable.background_input_error);
//            textErrorEmail.setText("Please enter email!");
//            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
//            textErrorEmail.setVisibility(View.VISIBLE);
//            return false;
//        }
//        else if (!Patterns.EMAIL_ADDRESS.matcher(signupEmail.getText().toString()).matches())
//        {
//            signupEmail.setBackgroundResource(R.drawable.background_input_error);
//            textErrorEmail.setText("Please enter valid email!");
//            textErrorEmail.setTextColor(Color.parseColor("#E10000"));
//            textErrorEmail.setVisibility(View.VISIBLE);
//            return false;
//        }
//        else
//        {
//            signupEmail.setBackgroundResource(R.drawable.background_input);
//            textErrorEmail.setText("Valid email account");
//            textErrorEmail.setTextColor(Color.parseColor("#08FF00"));
//            textErrorEmail.setVisibility(View.VISIBLE);
//            return true;
//        }
//    }
//    @SuppressLint("SetTextI18n")
//    private Boolean IsValidSignUpPassword()
//    {
//        if (signupPassword.getText().toString().trim().isEmpty())
//        {
//            signupPassword.setBackgroundResource(R.drawable.background_input_error);
//            textErrorPassword.setText("Please enter password!");
//            textErrorPassword.setTextColor(Color.parseColor("#E10000"));
//            textErrorPassword.setVisibility(View.VISIBLE);
//            return false;
//        }
//        else
//        {
//            if (signupPassword.getText().toString().trim().length() < 8)
//            {
//                signupPassword.setBackgroundResource(R.drawable.background_input_error);
//                textErrorPassword.setText("Please enter a password longer than 8 characters!");
//                textErrorPassword.setTextColor(Color.parseColor("#E10000"));
//                textErrorPassword.setVisibility(View.VISIBLE);
//                return false;
//            }
//            else
//            {
//                signupPassword.setBackgroundResource(R.drawable.background_input);
//                textErrorPassword.setText("Valid password");
//                textErrorPassword.setTextColor(Color.parseColor("#08FF00"));
//                textErrorPassword.setVisibility(View.VISIBLE);
//                return true;
//            }
//        }
//    }
//
//    private void showToast(String message)
//    {
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//    }
//    private void loading(Boolean isLoading)
//    {
//        if(isLoading)
//        {
//            buttonSignUp.setVisibility(View.INVISIBLE);
//            signupProgressBar.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            buttonSignUp.setVisibility(View.VISIBLE);
//            signupProgressBar.setVisibility(View.INVISIBLE);
//        }
//    }
//}
