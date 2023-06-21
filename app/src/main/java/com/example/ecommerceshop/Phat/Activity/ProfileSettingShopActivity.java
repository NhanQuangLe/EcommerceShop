package com.example.ecommerceshop.Phat.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Model.Photo;
import com.example.ecommerceshop.Phat.Model.RequestShop;
import com.example.ecommerceshop.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingShopActivity extends AppCompatActivity {
    ImageView backbtn;
    FrameLayout addImgbtn;
    TextInputEditText shopName, shopDescription, shopEmail, shopPhone, shopAddress;
    Button btnUpdateProfile;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    CircleImageView avatarShop;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
        if (!uris.isEmpty()) {
            photo=new Photo(uris.get(0),1);
            Glide.with(getApplicationContext()).load(uris.get(0)).into(avatarShop);
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting_shop);
        initUI();
        LoadProfileDetail();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
        name=shopName.getText().toString().trim();
        des=shopDescription.getText().toString().trim();
        email=shopEmail.getText().toString().trim();
        phone=shopPhone.getText().toString().trim();
        address=shopAddress.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(ProfileSettingShopActivity.this, "Shop Name is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(des)){
            Toast.makeText(ProfileSettingShopActivity.this, "Shop Description is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(ProfileSettingShopActivity.this, "Shop Email is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(ProfileSettingShopActivity.this, "Shop PhoneNumber is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(address)){
            Toast.makeText(ProfileSettingShopActivity.this, "Shop Address is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(photo.getUri().toString())){
            Toast.makeText(ProfileSettingShopActivity.this, "Shop Avatar is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        uploadImage();
    }

    private void uploadImage() {
        progressDialog.setMessage("UPLOADING....");
        progressDialog.show();
        if(photo.getTypeImg()==1){
            String t = ""+System.currentTimeMillis();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("ImageShop/"+t);

            storageReference.putFile(photo.getUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadUri = uriTask.getResult();
                    if(uriTask.isSuccessful()) avt=downloadUri.toString();
                    updateProfile();
                }
            });
        }
        else{
            updateProfile();
        }
    }

    private void updateProfile() {
        RequestShop requestShop = new RequestShop(firebaseAuth.getUid(),avt, name, des, email, phone, address, tt);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Shop").child("ShopInfos").setValue(requestShop).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                LoadProfileDetail();
                Toast.makeText(ProfileSettingShopActivity.this, "Update profile successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    String name, des, email, phone, address, avt, tt;
    Photo photo;
    private void LoadProfileDetail() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Shop").child("ShopInfos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RequestShop requestShop = snapshot.getValue(RequestShop.class);
                if(requestShop!= null){
                    name=requestShop.getShopName();
                    des=requestShop.getShopDescription();
                    email=requestShop.getShopEmail();
                    phone=requestShop.getShopPhone();
                    address=requestShop.getShopAddress();
                    tt= requestShop.getTimestamp();
                    avt=requestShop.getShopAvt();

                    shopPhone.setText(phone);
                    shopName.setText(name);
                    shopEmail.setText(email);
                    shopDescription.setText(des);
                    shopAddress.setText(address);
                    photo = new Photo(Uri.parse(avt), 0);
                    Glide.with(getApplicationContext()).load(Uri.parse(avt)).into(avatarShop);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileSettingShopActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        backbtn=findViewById(R.id.backbtn);
        addImgbtn=findViewById(R.id.addImgbtn);
        shopName=findViewById(R.id.shopName);
        shopDescription=findViewById(R.id.shopDescription);
        shopEmail=findViewById(R.id.shopEmail);
        shopPhone=findViewById(R.id.shopPhone);
        avatarShop=findViewById(R.id.avatarShop);
        shopAddress=findViewById(R.id.shopAddress);
        btnUpdateProfile=findViewById(R.id.btnUpdateProfile);
        progressDialog=new ProgressDialog(ProfileSettingShopActivity.this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth= FirebaseAuth.getInstance();
    }
}