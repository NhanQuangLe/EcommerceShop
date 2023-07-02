package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.review;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
                        rv.getUriList().add(imageSelection);
                        ivAdapter.notifyDataSetChanged();
                    }

                }

            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && data != null && data.getData() != null) {
            imageSelection = data.getData();
            rv.getUriList().add(imageSelection);
            ivAdapter.notifyDataSetChanged();
        }
    }

    RecyclerView rv_listProductInReview;
    ProductInReviewAdapter productInReviewAdapter;
    ArrayList<Review> productViewList;
    LinearLayout btn_Send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_rating_product);

        Intent intent = getIntent();
        firebaseAuth = FirebaseAuth.getInstance();
        HistoryOrder ho = (HistoryOrder) intent.getSerializableExtra("HistoryOrder");
        ArrayList<Product> productList = ho.getItems();
        productViewList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            Review review = new Review();
            review.setProductId(productList.get(i).getProductID());
            review.setProductName(productList.get(i).getProductName());
            review.setProductAvatar(productList.get(i).getProductAvatar());
            review.setRating(0);
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
            public void RatingBarChange(RatingBar ratingBar, float v, boolean b, Review review) {
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
        ref.child("CustomerInfos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Date currentTime = Calendar.getInstance().getTime();
                for (int i = 0; i < productViewList.size(); i++) {
                    ProductInReviewAdapter.ReviewViewholder holder = (ProductInReviewAdapter.ReviewViewholder) rv_listProductInReview.findViewHolderForAdapterPosition(i);
                    productViewList.get(i).setContent(holder.et_Comment.getText() + "");
                    productViewList.get(i).setAvatarCus(task.getResult().child("avatar").getValue(String.class));
                    productViewList.get(i).setCustomerName(task.getResult().child("name").getValue(String.class));
                    ref.child("Reviews");
                    String key = String.valueOf((int) new Date().getTime());
                    productViewList.get(i).setReviewDate(currentTime + "");
                    productViewList.get(i).setReviewId(key);
                    storageReference = FirebaseStorage.getInstance().getReference();
                    pushUriList(productViewList.get(i).getUriList(), key, 0);
                }
                pushData(0);
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
    private void pushUriList(ArrayList<Uri> uriList, String key, int n){
        if(n >= uriList.size()) return;
        storageReference.child("ImageReview")
                .child(key + "")
                .putFile(uriList.get(n))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pushUriList(uriList, key, n + 1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReviewActivity.this, "Không đánh giá được, thử lại sau", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
    private void pushData(int n)
    {
        if(n >= productViewList.size()) return;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getUid()).child("Customer")
                .child("Reviews")
                .child(productViewList.get(n).getReviewId());
        ref.setValue(productViewList.get(n))
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pushData(n + 1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReviewActivity.this, "Không đánh giá được, thử lại sau", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
