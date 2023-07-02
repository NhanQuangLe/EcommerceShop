package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.review;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.nhan.Model.Review;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrder;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                }
            });
    RecyclerView rv_listProductInReview;
    ProductInReviewAdapter productInReviewAdapter;
    ArrayList<Product> productList;
    ArrayList<Review> productViewList;
    LinearLayout btn_Send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_rating_product);

        Intent intent = getIntent();

        HistoryOrder ho = (HistoryOrder) intent.getSerializableExtra("HistoryOrder");
        productList = ho.getItems();
        productViewList = new ArrayList<>();
        for(int i = 0; i < productList.size(); i++)
        {
            Review review = new Review();
            review.setProductId(productList.get(i).getProductID());
            productViewList.add(review);
        }

        productInReviewAdapter = new ProductInReviewAdapter(this, productList, new IClickProductInReviewListener() {
            @Override
            public void RemoveReview(Product product) {
                if(productList.size() == 1)
                    finish();
                for(int i = 0; i < productViewList.size(); i++)
                    if(productViewList.get(i).getProductId().equals(product.getProductID()))
                    {
                        productViewList.remove(i);
                        break;
                    }
                productList.remove(product);
                productInReviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void RatingBarChange(RatingBar ratingBar, float v, boolean b, Product product) {
                if(v > 0)
                {
                    Review review = new Review();
                    review.setProductId(product.getProductID());
                    review.setRating(v);
                    productViewList.add(review);
                }
            }
        });

        InitUI();
        LoadData();
    }
    private void InitUI()
    {
        rv_listProductInReview = findViewById(R.id.rv_listProductInReview);
        btn_Send = findViewById(R.id.btn_Send);
    }
    private void LoadData()
    {
        rv_listProductInReview.setAdapter(productInReviewAdapter);
        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendReviews();
            }
        });
    }
    private void SendReviews(){
        if(productViewList.size() == 0)
        {
            Toast.makeText(this, "Vui Lòng đánh giá sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }
        if(productViewList.size() < productList.size())
        {
            Toast.makeText(this, "Vui lòng đánh giá sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }
        for(int i = 0; i < productViewList.size(); i++)
        {
            ProductInReviewAdapter.ReviewViewholder holder = (ProductInReviewAdapter.ReviewViewholder)rv_listProductInReview.findViewHolderForAdapterPosition(i);
            productViewList.get(i).setContent(holder.et_Comment.getText() + "");
        }
    }
}
