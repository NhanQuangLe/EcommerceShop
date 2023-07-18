package com.example.ecommerceshop.nhan.ProfileCustomer.orders.rating_products_orders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.databinding.FragmentHistoryOrdersBinding;
import com.example.ecommerceshop.databinding.FragmentRatedProductBinding;
import com.example.ecommerceshop.nhan.Model.Address;
import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.nhan.Model.Review;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.Order;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.OrderDetailActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrdersAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrdersFragment;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.IClickHistoryOrderListener;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.review.ReviewActivity;
import com.example.ecommerceshop.qui.cart.CartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RatedProductFragment extends Fragment {
    public RatedProductFragment() {
    }
    FirebaseAuth firebaseAuth;
    FragmentRatedProductBinding fragmentRatedProductBinding;
    UserReviewAdapter mHistoryAdapter;
    RecyclerView mRatedProductView;
    ArrayList<Review> listReviews;
    UserReviewAdapter userReviewAdapter;
    View mViewFragment;
    private ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                }
            });

    public static RatedProductFragment newInstance() {
        RatedProductFragment fragment = new RatedProductFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRatedProductBinding = FragmentRatedProductBinding.inflate(inflater, container, false);
        mViewFragment = fragmentRatedProductBinding.getRoot();
        listReviews = new ArrayList<>();
        mRatedProductView = fragmentRatedProductBinding.rvRatedProduct;
        firebaseAuth = FirebaseAuth.getInstance();

        mRatedProductView.setAdapter(mHistoryAdapter);
        userReviewAdapter = new UserReviewAdapter(getContext(), listReviews);
        mRatedProductView.setAdapter(userReviewAdapter);
        LoadData();
        return mViewFragment;
    }
    void LoadData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .child("Customer")
                .child("Reviews")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot rv : snapshot.getChildren()){
                            Review review = rv.getValue(Review.class);
                            listReviews.add(review);
                            userReviewAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Lỗi khi lấy đánh giá của bạn", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}