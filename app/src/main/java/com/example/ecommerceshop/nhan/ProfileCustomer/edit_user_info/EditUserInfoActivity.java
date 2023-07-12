package com.example.ecommerceshop.nhan.ProfileCustomer.edit_user_info;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Activity.ProfileSettingShopActivity;
import com.example.ecommerceshop.Phat.Model.Photo;
import com.example.ecommerceshop.Phat.Model.RequestShop;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Customer;
import com.example.ecommerceshop.nhan.Model.Shop;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_shops.FavouriteShopsActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_shops.FavouriteShopsAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_shops.IClickFavouriteShopListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserInfoActivity extends AppCompatActivity {
    private Customer currentCus;
    CircleImageView userAvatar;
    FrameLayout addImgbtn;
    TextInputEditText userName, phoneNumber, userGender;
    Button userDOB;
    Button btnUpdateProfile;
    Photo photo;
    ProgressDialog progressDialog;
    DatePickerDialog datePickerDialog;
    FirebaseAuth firebaseAuth;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
        if (!uris.isEmpty()) {
            photo=new Photo(uris.get(0),1);
            Glide.with(getApplicationContext()).load(uris.get(0)).into(userAvatar);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        Intent intent = getIntent();
        currentCus = (Customer) intent.getSerializableExtra("currentUser");
        firebaseAuth = FirebaseAuth.getInstance();
        initDatePicker();
        InitUI();
        LoadData();
    }
    public void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                userDOB.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.BUTTON_POSITIVE;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }
    private void InitUI(){
        userAvatar = findViewById(R.id.userAvatar);
        addImgbtn = findViewById(R.id.addImgbtn);
        userName = findViewById(R.id.userName);
        phoneNumber = findViewById(R.id.phoneNumber);
        userGender = findViewById(R.id.userGender);
        userDOB = findViewById(R.id.userDOB);
        userDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        progressDialog = new ProgressDialog(EditUserInfoActivity.this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
    }
    private void LoadData(){
        Picasso.get().load(currentCus.getAvatar()).into(userAvatar);
        userName.setText(currentCus.getName());
        phoneNumber.setText(currentCus.getPhoneNumber());
        userGender.setText(currentCus.getGender());
        userDOB.setText(currentCus.getDateOfBirth());
        addImgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());
            }
        });
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });
    }
    private void inputData() {
        if(TextUtils.isEmpty(userName.getText().toString().trim())){
            Toast.makeText(EditUserInfoActivity.this, "User Name is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phoneNumber.getText().toString().trim())){
            Toast.makeText(EditUserInfoActivity.this, "User phone number is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userGender.getText().toString().trim())){
            Toast.makeText(EditUserInfoActivity.this, "User Gender is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userDOB.getText().toString().trim())){
            Toast.makeText(EditUserInfoActivity.this, "User Date of birth is required...", Toast.LENGTH_SHORT).show();
            return;
        }

        currentCus.setName(userName.getText().toString());
        currentCus.setPhoneNumber(phoneNumber.getText().toString());
        currentCus.setGender(userGender.getText().toString());
        currentCus.setDateOfBirth(userDOB.getText().toString());
        if(photo == null)
            updateProfile();
        else
        {
            currentCus.setAvatar(photo.getUri().toString());
            uploadImage();
        }
    }
    private void uploadImage() {
        progressDialog.setMessage("UPLOADING....");
        progressDialog.show();
        if(photo.getTypeImg()==1){
            String t = ""+System.currentTimeMillis();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("ImageCustomer/"+ firebaseAuth.getUid() + "/" + t);

            storageReference.putFile(photo.getUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadUri = uriTask.getResult();
                    if(uriTask.isSuccessful()) currentCus.setAvatar(downloadUri.toString());
                    updateProfile();
                }
            });
        }
        else{
            updateProfile();
        }
    }
    private void updateProfile() {
        Map<String, String> curentUserMap = new HashMap<>();
        curentUserMap.put("avatar", currentCus.getAvatar());
        curentUserMap.put("dateOfBirth", currentCus.getDateOfBirth());
        curentUserMap.put("email", currentCus.getEmail());
        curentUserMap.put("gender", currentCus.getGender());
        curentUserMap.put("name", currentCus.getName());
        curentUserMap.put("phoneNumber", currentCus.getPhoneNumber());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference
                .child(firebaseAuth.getUid())
                .child("Customer")
                .child("CustomerInfos")
                .setValue(currentCus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(EditUserInfoActivity.this, "Update profile successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}
