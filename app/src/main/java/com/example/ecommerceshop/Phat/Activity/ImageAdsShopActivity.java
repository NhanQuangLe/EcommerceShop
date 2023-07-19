package com.example.ecommerceshop.Phat.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerceshop.Phat.Adapter.AdapterImgAdsShop;
import com.example.ecommerceshop.Phat.Model.Photo;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ImageAdsShopActivity extends AppCompatActivity {
    ImageView btnBack;
    Button uploadImg;
    AppCompatButton addImg;
    RecyclerView imgAdsList;
    ArrayList<Photo> photoList=new ArrayList<>();;
    AdapterImgAdsShop adapterImgAdsShop;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    ArrayList<String> uriList;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(10), uris -> {
                if (!uris.isEmpty()) {
                    for (Uri uri:uris) {
                        photoList.add(new Photo(uri, 1));
                    }
                    adapterImgAdsShop.notifyDataSetChanged();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_ads_shop);
        initUI();

        adapterImgAdsShop=new AdapterImgAdsShop(ImageAdsShopActivity.this, photoList);
        imgAdsList.setAdapter(adapterImgAdsShop);
        loadPhotoList();
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());
            }
        });
        adapterImgAdsShop.setOnItemClickListener(new AdapterImgAdsShop.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                photoList.remove(position);
                adapterImgAdsShop.notifyItemRemoved(position);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputdate();
            }
        });
    }

    private void inputdate() {
        if(photoList.size()==0){
            CustomToast.makeText(getApplicationContext(),"Photo is required!",CustomToast.SHORT,CustomToast.ERROR).show();

            return;
        }
        else{
            uploadImage();
        }
    }

    private void uploadImage() {
        progressDialog.setMessage("Uploading....");
        progressDialog.show();
        uriList=new ArrayList<>();

        for (int i=0; i<photoList.size(); i++) {
            int finalI = i;
            if(photoList.get(i).getTypeImg()==0){
                uriList.add(photoList.get(i).getUri().toString());
                if(finalI==photoList.size()-1) {
                    uploadData();
                }
            }
            if(photoList.get(i).getTypeImg()==1) {
                String timestamp = ""+System.currentTimeMillis();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("ImageAdsShop/" + firebaseAuth.getUid() + "/" + timestamp);

                storageReference.putFile(photoList.get(i).getUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadUri = uriTask.getResult();
                        if (uriTask.isSuccessful()) uriList.add(downloadUri.toString());
                        if (finalI == photoList.size() - 1) uploadData();
                    }
                });
            }
        }
    }

    private void loadPhotoList() {
        photoList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Shop").child("ImageAds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String uri = ds.getValue(String.class);
                    photoList.add(new Photo(Uri.parse(uri),0));
                }
                adapterImgAdsShop.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                CustomToast.makeText(ImageAdsShopActivity.this,error.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();


            }
        });

    }

    private void uploadData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Shop").child("ImageAds").setValue(uriList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        loadPhotoList();
                        CustomToast.makeText(ImageAdsShopActivity.this,"Upload image ads successfully",CustomToast.SHORT,CustomToast.SUCCESS).show();
                    }
                });
    }

    private void initUI() {
        btnBack=findViewById(R.id.btnBack);
        uploadImg=findViewById(R.id.uploadImg);
        addImg=findViewById(R.id.addImg);
        imgAdsList=findViewById(R.id.imgList);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
    }
}