package com.example.ecommerceshop.Phat.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
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
import com.example.ecommerceshop.Phat.Adapter.PhotoAdapter;
import com.example.ecommerceshop.Phat.Model.Photo;
import com.example.ecommerceshop.Phat.Model.RequestShop;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.EditAddressActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address.ChooseAddressActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address.choose_location_gg_map.GoogleMapLocationActivity;
import com.example.ecommerceshop.toast.CustomToast;
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

import java.util.Calendar;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestToShopActivity extends AppCompatActivity {

    ImageView backbtn;
    CircleImageView avatarShop;
    FrameLayout addImgbtn;
    TextInputEditText shopName, shopDescription, shopEmail,shopPhone;
    Button btnAddRequest;
    Uri uriImage;
    TextView shopAddress;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
        if (!uris.isEmpty()) {
            uriImage=uris.get(0);
            Glide.with(getApplicationContext()).load(uris.get(0)).into(avatarShop);
        }
    });
    private ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (result.getResultCode() == 1) {
                        shopAddress.setText(intent.getStringExtra("province"));
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_to_shop);
        initUI();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestToShopActivity.this);
                builder.setTitle("Back...").setMessage("Are you sure you want to cancel the request?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onBackPressed();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .show();
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
        btnAddRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });

        shopAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestToShopActivity.this, ProvinceActivity.class);
                mActivityLauncher.launch(i);
            }
        });
    }


    String shopavt, shopname, shopdes, shopemail, shopphone, shopaddress;
    private void inputData() {
        shopname=shopName.getText().toString().trim();
        shopdes=shopDescription.getText().toString().trim();
        shopemail=shopEmail.getText().toString().trim();
        shopphone=shopPhone.getText().toString().trim();
        shopaddress=shopAddress.getText().toString().trim();
        if(TextUtils.isEmpty(shopname)){
            Toast.makeText(RequestToShopActivity.this, "Shop Name is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(shopdes)){
            Toast.makeText(RequestToShopActivity.this, "Shop Description is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(shopemail)){
            Toast.makeText(RequestToShopActivity.this, "Shop Email is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(shopphone)){
            Toast.makeText(RequestToShopActivity.this, "Shop PhoneNumber is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkPhone(shopphone)){
            CustomToast.makeText(RequestToShopActivity.this,"Shop PhoneNumber is not valid...",CustomToast.SHORT,CustomToast.ERROR).show();
            return;
        }
        if(TextUtils.isEmpty(shopaddress)){
            Toast.makeText(RequestToShopActivity.this, "Shop Address is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(uriImage.toString())){
            Toast.makeText(RequestToShopActivity.this, "Shop Avatar is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        uploadImage();
    }

    private void uploadImage() {
        progressDialog.setMessage("UPLOADING....");
        progressDialog.show();
        String t = ""+System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Vì Calendar.MONTH bắt đầu từ 0
        int year = calendar.get(Calendar.YEAR);
        String date =day+"/"+month+"/"+year;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("ImageShop/"+t);
        storageReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();
                if(uriTask.isSuccessful()) shopavt=downloadUri.toString();
                uploadRequest(date);
            }
        });

    }

    private void uploadRequest(String timestamp) {
        RequestShop requestShop = new RequestShop(firebaseAuth.getUid(),shopavt, shopname, shopdes, shopemail, shopphone, shopaddress, timestamp);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference.child(firebaseAuth.getUid()).setValue(requestShop).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(RequestToShopActivity.this, "Registrating to be Seller successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RequestToShopActivity.this, SuccessRegisActivity.class));
                finish();
            }
        });

    }
    boolean checkPhone(String phone){
        Pattern p = Pattern.compile("^[0-9]{10}$");
        Pattern p1 = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}$");
        Pattern p2 = Pattern.compile("^[0-9]{3}.[0-9]{3}.[0-9]{4}$");
        Pattern p3 = Pattern.compile("^[0-9]{3} [0-9]{3} [0-9]{4}$");
        Pattern p4 = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4} (x|ext)[0-9]{4}$");
        Pattern p5 = Pattern.compile("^\\([0-9]{3}\\)-[0-9]{3}-[0-9]{4}$");
        if (p.matcher(phone).find() || p1.matcher(phone).find() || p2.matcher(phone).find()
                || p3.matcher(phone).find() || p4.matcher(phone).find() || p5.matcher(phone).find())
        {

            return true;
        }
        else return false;
    }
    private void initUI(){
        backbtn=findViewById(R.id.backbtn);
        avatarShop=findViewById(R.id.avatarShop);
        addImgbtn=findViewById(R.id.addImgbtn);
        shopName=findViewById(R.id.shopName);
        shopDescription=findViewById(R.id.shopDescription);
        shopEmail=findViewById(R.id.shopEmail);
        shopPhone=findViewById(R.id.shopPhone);
        shopAddress=findViewById(R.id.shopAddress);
        btnAddRequest=findViewById(R.id.btnAddRequest);
        progressDialog=new ProgressDialog(RequestToShopActivity.this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();

    }
}