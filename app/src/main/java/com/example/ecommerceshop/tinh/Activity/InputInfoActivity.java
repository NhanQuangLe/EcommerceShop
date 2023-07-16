package com.example.ecommerceshop.tinh.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.tinh.models.User;
import com.example.ecommerceshop.utilities.Constants;
import com.example.ecommerceshop.utilities.PreferenceManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class InputInfoActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseUser mCurrentUser;
    private Uri mUri;
    private String avt;
    private String gender;
    private PreferenceManagement preferenceManagement;
    private RoundedImageView avatar;
    private FrameLayout layoutImage;
    private TextView textAddImage, textError, textErrorGender, textErrorBirthdate, textErrorImage, textErrorPhone;
    private EditText edittextName, edittextBirthdate, editTextPhone;
    private ImageView buttonShowDatePicker;
    private AppCompatImageView buttonBack;
    private CheckBox cbNam, cbNu;
    private Button buttonStart;
    private ProgressBar progressBar;
    private String encodedImage;
    final Calendar myCalendar = Calendar.getInstance();
    private Boolean isWithGoogle;
    private String currentUserId;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);
        InitUI();
        Intent i = getIntent();
        isWithGoogle = i.getBooleanExtra("withGoogle",false);
        auth = FirebaseAuth.getInstance();
        preferenceManagement = new PreferenceManagement(getApplicationContext());
        buttonStart.setOnClickListener(v -> {
            hideKeyboard(v);
            Boolean checkName = IsValidName();
            Boolean checkImage = IsValidImage();
            Boolean checkDate = IsValidDate();
            Boolean checkCheckBox = IsValidGender();
            Boolean checkPhone = IsValidPhone();
            if (checkName && checkDate && checkCheckBox && checkImage && checkPhone) {
                if (!isWithGoogle){
                    Started();
                }
                else{
                    currentUserId = i.getStringExtra("userId");
                    Started2();
                }

            }
        });
        buttonBack.setOnClickListener(view -> startActivity(new Intent(InputInfoActivity.this, SignUpActivity.class)));
        cbNam.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbNam.isChecked()) {
                cbNam.setChecked(true);
                gender = cbNam.getText().toString();
                cbNu.setChecked(false);
                textErrorGender.setVisibility(View.INVISIBLE);
            } else {
                cbNam.setChecked(false);
            }
        });

        cbNu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbNu.isChecked()) {
                cbNu.setChecked(true);
                gender = cbNu.getText().toString();
                cbNam.setChecked(false);
                textErrorGender.setVisibility(View.INVISIBLE);
            } else {
                cbNu.setChecked(false);
            }
        });
        edittextName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        edittextName.setOnClickListener(v -> {
            if (!edittextName.getText().toString().trim().isEmpty()) {
                edittextName.setBackgroundResource(R.drawable.background_input);
                textError.setVisibility(View.INVISIBLE);
            } else {
                edittextName.setBackgroundResource(R.drawable.background_input_error);
                textError.setText("Vui lòng nhập họ tên để bắt đầu!");
                textError.setTextColor(Color.parseColor("#E10000"));
                textError.setVisibility(View.VISIBLE);
            }
        });
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };
        buttonShowDatePicker.setOnClickListener(v -> new DatePickerDialog(InputInfoActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        layoutImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
        editTextPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        editTextPhone.setOnClickListener(v -> {
            if (editTextPhone.getText().toString().trim().isEmpty()) {
                editTextPhone.setBackgroundResource(R.drawable.background_input_error);
                textErrorPhone.setText("Vui lòng nhập số điện thoại để đăng ký!");
                textErrorPhone.setTextColor(Color.parseColor("#E10000"));
                textErrorPhone.setVisibility(View.VISIBLE);
            }
            else
            {
                String phone = editTextPhone.getText().toString().trim();
                Pattern p = Pattern.compile("^[0-9]{10}$");
                Pattern p1 = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}$");
                Pattern p2 = Pattern.compile("^[0-9]{3}.[0-9]{3}.[0-9]{4}$");
                Pattern p3 = Pattern.compile("^[0-9]{3} [0-9]{3} [0-9]{4}$");
                Pattern p4 = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4} (x|ext)[0-9]{4}$");
                Pattern p5 = Pattern.compile("^\\([0-9]{3}\\)-[0-9]{3}-[0-9]{4}$");
                if (p.matcher(phone).find() || p1.matcher(phone).find() || p2.matcher(phone).find()
                        || p3.matcher(phone).find() || p4.matcher(phone).find() || p5.matcher(phone).find())
                {
                    editTextPhone.setBackgroundResource(R.drawable.background_input);
                    textErrorPhone.setVisibility(View.INVISIBLE);
                }
                else
                {
                    editTextPhone.setBackgroundResource(R.drawable.background_input_error);
                    textErrorPhone.setText("Số điện thoại không hợp lệ. Vui lòng nhập lại!");
                    textErrorPhone.setTextColor(Color.parseColor("#E10000"));
                    textErrorPhone.setVisibility(View.VISIBLE);
                }
            }
        });
        edittextBirthdate.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        edittextBirthdate.setOnClickListener(v -> new DatePickerDialog(InputInfoActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }
    private Boolean IsValidPhone() {
        if (editTextPhone.getText().toString().trim().isEmpty()) {
            editTextPhone.setBackgroundResource(R.drawable.background_input_error);
            textErrorPhone.setText("Vui lòng nhập số điện thoại để đăng ký!");
            textErrorPhone.setTextColor(Color.parseColor("#E10000"));
            textErrorPhone.setVisibility(View.VISIBLE);
            return false;
        }
        else
        {
            String phone = editTextPhone.getText().toString().trim();
            Pattern p = Pattern.compile("^[0-9]{10}$");
            Pattern p1 = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}$");
            Pattern p2 = Pattern.compile("^[0-9]{3}.[0-9]{3}.[0-9]{4}$");
            Pattern p3 = Pattern.compile("^[0-9]{3} [0-9]{3} [0-9]{4}$");
            Pattern p4 = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4} (x|ext)[0-9]{4}$");
            Pattern p5 = Pattern.compile("^\\([0-9]{3}\\)-[0-9]{3}-[0-9]{4}$");
            if (p.matcher(phone).find() || p1.matcher(phone).find() || p2.matcher(phone).find()
                    || p3.matcher(phone).find() || p4.matcher(phone).find() || p5.matcher(phone).find())
            {
                editTextPhone.setBackgroundResource(R.drawable.background_input);
                textErrorPhone.setVisibility(View.INVISIBLE);
                return true;
            }
            else
            {
                editTextPhone.setBackgroundResource(R.drawable.background_input_error);
                textErrorPhone.setText("Số điện thoại không hợp lệ. Vui lòng nhập lại!");
                textErrorPhone.setTextColor(Color.parseColor("#E10000"));
                textErrorPhone.setVisibility(View.VISIBLE);
                return false;
            }
        }
    }
    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), previewBitmap, "Title", null);
        mUri = Uri.parse(path);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            avatar.setImageBitmap(bitmap);
                            textAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        edittextBirthdate.setText(dateFormat.format(myCalendar.getTime()));
        if (edittextBirthdate.getText().toString().trim().isEmpty()) {
            edittextBirthdate.setBackgroundResource(R.drawable.background_input_error);
            textErrorBirthdate.setText("Vui lòng chọn ngày tháng năm sinh để bắt đầu!");
            textErrorBirthdate.setTextColor(Color.parseColor("#E10000"));
            textErrorBirthdate.setVisibility(View.VISIBLE);
        } else {
            SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
            Date birthdate = new Date();
            try {
                birthdate = date_format.parse(edittextBirthdate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date now = new Date();

            if (birthdate.compareTo(now) >= 0) {
                edittextBirthdate.setBackgroundResource(R.drawable.background_input_error);
                textErrorBirthdate.setText("Ngày sinh không hợp lệ! Vui lòng chọn ngày sinh trước ngày hiện tại!");
                textErrorBirthdate.setTextColor(Color.parseColor("#E10000"));
                textErrorBirthdate.setVisibility(View.VISIBLE);
            } else {
                edittextBirthdate.setBackgroundResource(R.drawable.background_input);
                textErrorBirthdate.setVisibility(View.INVISIBLE);
            }
        }
    }

    private Boolean IsValidGender() {

        if (!cbNam.isChecked() && !cbNu.isChecked()) {
            textErrorGender.setText("Vui lòng chọn giới tính để bắt đầu!");
            textErrorGender.setTextColor(Color.parseColor("#E10000"));
            textErrorGender.setVisibility(View.VISIBLE);
            return false;
        } else {
            textErrorGender.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Boolean IsValidDate() {
        if (edittextBirthdate.getText().toString().trim().isEmpty()) {
            edittextBirthdate.setBackgroundResource(R.drawable.background_input_error);
            textErrorBirthdate.setText("Vui lòng chọn ngày sinh để bắt đầu!");
            textErrorBirthdate.setTextColor(Color.parseColor("#E10000"));
            textErrorBirthdate.setVisibility(View.VISIBLE);
            return false;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date birthdate = new Date();
            try {
                birthdate = dateFormat.parse(edittextBirthdate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date now = new Date();

            if (birthdate.compareTo(now) >= 0) {
                edittextBirthdate.setBackgroundResource(R.drawable.background_input_error);
                textErrorBirthdate.setText("Ngày sinh không hợp lệ! Vui lòng chọn ngày sinh trước ngày hiện tại!");
                textErrorBirthdate.setTextColor(Color.parseColor("#E10000"));
                textErrorBirthdate.setVisibility(View.VISIBLE);
                return false;
            } else {
                edittextBirthdate.setBackgroundResource(R.drawable.background_input);
                textErrorBirthdate.setVisibility(View.INVISIBLE);
                return true;
            }
        }
    }

    private Boolean IsValidImage() {
        if (encodedImage == null) {
            textErrorImage.setText("Vui lòng chọn ảnh đại diện!");
            textErrorImage.setTextColor(Color.parseColor("#E10000"));
            textErrorImage.setVisibility(View.VISIBLE);
            return false;
        } else {
            textErrorImage.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private Boolean IsValidName() {
        if (edittextName.getText().toString().trim().isEmpty()) {
            edittextName.setBackgroundResource(R.drawable.background_input_error);
            textError.setText("Vui lòng nhập họ tên để bắt đầu");
            textError.setTextColor(Color.parseColor("#E10000"));
            textError.setVisibility(View.VISIBLE);
            return false;
        } else {
            edittextName.setBackgroundResource(R.drawable.background_input);
            textError.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private void Started() {
        loading(true);
        // đăng ký
        Intent i = getIntent();
        String email = i.getStringExtra("email");
        String password = i.getStringExtra("password");
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                CreateAccountChat(email);

            } else {
                loading(false);
                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage() + "Try signing up with a new email account or login with this one!", Toast.LENGTH_SHORT).show();
            }
        });
        loading(false);
    }
    private void Started2() {
        loading(true);
        String email = getIntent().getStringExtra("email");
        SaveImgInFirebaseStorage(email);

    }

    private void CreateAccountChat(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> userChat = new HashMap<>();
        userChat.put(Constants.KEY_EMAIL, email);
        db.collection(Constants.KEY_COLLECTION_USER).document(mCurrentUser.getUid())
                .set(userChat)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        loading(false);
                        preferenceManagement.putString(Constants.KEY_USER_ID, mCurrentUser.getUid());
                        SaveImgInFirebaseStorage(email);

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

    private void SaveImgInFirebaseStorage(String email) {
        String userId;
        if (isWithGoogle) userId = currentUserId;
        else userId = mCurrentUser.getUid();
        StorageReference storage = FirebaseStorage.getInstance().getReference("ImageCustomer/"+userId+"/"+userId+"Avt");
        storage.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();
                if(uriTask.isSuccessful()) avt=downloadUri.toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + userId + "/Customer/CustomerInfos");
                Map<String,String> user = new HashMap<>();
                user.put("avatar",avt);
                user.put("dateOfBirth",edittextBirthdate.getText().toString().trim());
                user.put("email",email);
                user.put("name",edittextName.getText().toString().trim());
                user.put("gender",gender);
                user.put("phoneNumber",editTextPhone.getText().toString().trim());
                ref.setValue(user);
                if (isWithGoogle){
                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent i2 = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i2);
                }
                else {
                    Intent i2 = new Intent(getApplicationContext(), LoginActivity.class);
                    i2.putExtra("signUp",true);
                    startActivity(i2);
                }
            }
        });



    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void InitUI() {
        avatar = findViewById(R.id.imageProfile);
        textAddImage = findViewById(R.id.textAddImage);
        edittextBirthdate = findViewById(R.id.editTextBirthDate);
        edittextName = findViewById(R.id.editTextName);
        cbNam = findViewById(R.id.checkbox_nam);
        cbNu = findViewById(R.id.checkbox_nu);
        buttonStart = findViewById(R.id.buttonStart);
        progressBar = findViewById(R.id.progressBar);
        textError = findViewById(R.id.textErrorName);
        buttonShowDatePicker = findViewById(R.id.buttonShowDatePicker);
        textErrorGender = findViewById(R.id.textErrorGender);
        textErrorBirthdate = findViewById(R.id.textErrorBirthdate);
        layoutImage = findViewById(R.id.layoutImage);
        textErrorImage = findViewById(R.id.textErrorImage);
        editTextPhone = findViewById(R.id.editTextPhone);
        textErrorPhone = findViewById(R.id.textErrorPhone);
        buttonBack = findViewById(R.id.buttonBack);
    }


    private void loading(Boolean isLoading) {
        if (isLoading) {
            buttonStart.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            buttonStart.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}