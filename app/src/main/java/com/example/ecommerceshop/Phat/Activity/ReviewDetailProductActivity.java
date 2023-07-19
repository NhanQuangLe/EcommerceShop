package com.example.ecommerceshop.Phat.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerceshop.Phat.Adapter.AdapterRatedProducts;
import com.example.ecommerceshop.Phat.Adapter.AdapterReviews;
import com.example.ecommerceshop.Phat.Model.Product;
import com.example.ecommerceshop.Phat.Model.RatedProduct;
import com.example.ecommerceshop.Phat.Model.Review;
import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewDetailProductActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ImageView backbtn;
    TextView productName, productBrand, productRate, psoldQuantity, productDiscountPrice, productPrice, productDiscountPercent,rvNums;
    ImageSlider slideProductImage;
    RecyclerView listRv;
    String pid,rvNum;
    RatingBar ratingBar;
    FrameLayout frameDiscount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail_products_shop);
        initUI();
        pid = getIntent().getStringExtra("pid");
        rvNum = getIntent().getStringExtra("rvNums");
        LoadDetail();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void LoadDetail() {
        List<SlideModel> slideModels=new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Shop").child("Products").child(pid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Product product = new Product();
                        product=snapshot.getValue(Product.class);
                        productName.setText(product.getProductName());
                        productBrand.setText(product.getProductBrand());
                        productRate.setText(rvNum);
                        rvNums.setText(rvNum);
                        psoldQuantity.setText(String.valueOf(product.getPsoldQuantity()));
                        if(product.getProductDiscountPrice()==0){
                            productDiscountPrice.setVisibility(View.GONE);
                            productPrice.setText(Constants.convertToVND(product.getProductPrice()));
                            frameDiscount.setVisibility(View.GONE);
                        }
                        else{
                            productDiscountPrice.setText(Constants.convertToVND(product.getProductPrice()));
                            productPrice.setText(Constants.convertToVND(product.getProductPrice()-product.getProductDiscountPrice()));
                            productDiscountPercent.setText(product.getProductDiscountNote());
                        }
                        for(String str : product.getUriList()){
                            slideModels.add(new SlideModel(str, ScaleTypes.CENTER_INSIDE));
                        }
                        slideProductImage.setImageList(slideModels, ScaleTypes.CENTER_INSIDE);
                        loadListReview();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        CustomToast.makeText(getApplicationContext(),error.getMessage()+"",CustomToast.SHORT,CustomToast.ERROR).show();

                    }
                });
    }

    private void loadListReview() {
        ArrayList<Review> reviews = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviews.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String uid = ""+ds.getRef().getKey();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(uid).child("Customer").child("Reviews").orderByChild("shopId")
                            .equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for (DataSnapshot ds: snapshot.getChildren()) {
                                            Review review = ds.getValue(Review.class);
                                            if(review!=null){
                                                if(review.getProductId().equals(pid)){
                                                    reviews.add(review);
                                                }
                                            }
                                        }
                                        float ratingProduct=0;
                                        for (Review rv:reviews) {
                                            ratingProduct=ratingProduct+rv.getRating();
                                        }
                                        ratingProduct=ratingProduct/reviews.size();
                                        ratingBar.setRating(ratingProduct);
                                        AdapterReviews adapterReviews = new AdapterReviews(getApplicationContext(), reviews);
                                        listRv.setAdapter(adapterReviews);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    CustomToast.makeText(getApplicationContext(),error.getMessage()+"",CustomToast.SHORT,CustomToast.ERROR).show();

                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                CustomToast.makeText(getApplicationContext(),error.getMessage()+"",CustomToast.SHORT,CustomToast.ERROR).show();

            }
        });
    }


    private void initUI() {
        firebaseAuth=FirebaseAuth.getInstance();
        backbtn=findViewById(R.id.backbtn);
        productName=findViewById(R.id.productName);
        productBrand=findViewById(R.id.productBrand);
        productRate=findViewById(R.id.productRate);
        psoldQuantity=findViewById(R.id.psoldQuantity);
        productDiscountPrice=findViewById(R.id.productDiscountPrice);
        productPrice=findViewById(R.id.productPrice);
        productDiscountPercent=findViewById(R.id.productDiscountPercent);
        rvNums=findViewById(R.id.rvNums);
        slideProductImage=findViewById(R.id.slideProductImage);
        frameDiscount=findViewById(R.id.frameDiscount);
        listRv=findViewById(R.id.listRv);
        ratingBar=findViewById(R.id.ratingBar);
    }
}
