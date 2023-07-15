package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.review;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.nhan.Model.Review;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address.ChooseAddressActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import org.checkerframework.checker.units.qual.A;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG = 100;
    Uri imageSelection;
    Review rv;
    ImageReviewAdapter ivAdapter;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    private ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (result.getResultCode() == RESULT_LOAD_IMG) {
                        imageSelection = intent.getData();
                        rv.getUriList().add(imageSelection.toString());
                        ivAdapter.notifyDataSetChanged();
                    }
                }

            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && data != null && data.getData() != null) {
            imageSelection = data.getData();
            rv.getUriList().add(imageSelection.toString());
            ivAdapter.notifyDataSetChanged();
        }
    }

    RecyclerView rv_listProductInReview;
    ProductInReviewAdapter productInReviewAdapter;
    ArrayList<Review> productViewList;
    LinearLayout btn_Send;
    Button btnBackward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_rating_product);

        Intent intent = getIntent();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        HistoryOrder ho = (HistoryOrder) intent.getSerializableExtra("HistoryOrder");
        ArrayList<Product> productList = ho.getItems();
        productViewList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            Review review = new Review();
            review.setProductId(productList.get(i).getProductID());
            review.setShopId(productList.get(i).getShopID());
            review.setProductName(productList.get(i).getProductName());
            review.setProductAvatar(productList.get(i).getProductAvatar());
            review.setRating((double) 0);
            review.setUriList(new ArrayList<>());
            productViewList.add(review);
        }

        productInReviewAdapter = new ProductInReviewAdapter(this, productViewList, new IClickProductInReviewListener() {
            @Override
            public void RemoveReview(Review review) {
                if (productViewList.size() == 1)
                    finish();
                productViewList.remove(review);
                productInReviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void RatingBarChange(RatingBar ratingBar, double v, boolean b, Review review) {
                review.setRating(v);
            }

            @Override
            public void AddImage(ImageReviewAdapter imageReviewAdapter, Review review) {
                ivAdapter = imageReviewAdapter;
                rv = review;
                ReviewActivity.this.AddImage();
            }
        });

        InitUI();
        LoadData();
    }

    private void InitUI() {
        rv_listProductInReview = findViewById(R.id.rv_listProductInReview);
        btnBackward = findViewById(R.id.btnBackward);
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_Send = findViewById(R.id.btn_Send);
    }

    private void LoadData() {
        rv_listProductInReview.setAdapter(productInReviewAdapter);
        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendReviews();
            }
        });
    }

    private void SendReviews() {
        if (isHasEmptyReviews()) {
            Toast.makeText(ReviewActivity.this, "Vui lòng đánh giá sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getUid()).child("Customer");
        ref.child("CustomerInfos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                for (int i = 0; i < productViewList.size(); i++) {
                    ProductInReviewAdapter.ReviewViewholder holder = (ProductInReviewAdapter.ReviewViewholder) rv_listProductInReview.findViewHolderForAdapterPosition(i);
                    productViewList.get(i).setContent(holder.et_Comment.getText() + "");
                    productViewList.get(i).setAvatarCus(snapshot.child("avatar").getValue(String.class));
                    productViewList.get(i).setCustomerId(firebaseAuth.getUid());
                    productViewList.get(i).setCustomerName(snapshot.child("name").getValue(String.class));
                    String key = String.valueOf((int) new Date().getTime());
                    productViewList.get(i).setReviewDate(simpleDateFormat.format(calendar.getTime()) + "");
                    productViewList.get(i).setReviewId(key);
                }
                if(productViewList.size() != 0)
                    pushReviewToFirebase(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReviewActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AddImage() {
        Intent photoPickerIntent = new Intent();
        photoPickerIntent.setType("image/*");
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }
    private boolean isHasEmptyReviews() {
        for (int i = 0; i < productViewList.size(); i++) {
            if(productViewList.get(i).getRating() == 0)
                return true;
        }
        return false;
    }
    private void pushReviewToFirebase(int n){
        if(n == productViewList.size())
        {
            Toast.makeText(ReviewActivity.this, "Cảm ơn bạn đã đánh giá", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }
        pushUriList(productViewList.get(n).getUriList(),0 , n);
    }

    private void pushUriList(ArrayList<String> uriList, int currentUri, int currentProduct){
        if(currentUri == uriList.size())
        {
            pushData(currentProduct);
            return;
        }
        storageReference.child("ImageReview")
                .child(productViewList.get(currentProduct).getCustomerId())
                .child(productViewList.get(currentProduct).getReviewId())
                .child(String.valueOf((int) new Date().getTime()))
                .putFile(Uri.parse(uriList.get(currentUri)))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        if(uriTask.isSuccessful())
                        {
                            productViewList.get(currentProduct).getUriList().set(currentUri, downloadUri.toString());
                        }
                        pushUriList(uriList, currentUri + 1, currentProduct);
                    }
                });
    }
    private void pushData(int n)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getUid())
                .child("Customer")
                .child("Reviews");
        Review a = productViewList.get(n);
        String b = productViewList.get(n).getReviewId();
        ref.child(b).setValue(a, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                pushReviewToFirebase(n + 1);
            }
        });
    }
}