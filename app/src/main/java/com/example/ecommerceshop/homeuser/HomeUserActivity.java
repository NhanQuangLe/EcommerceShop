package com.example.ecommerceshop.homeuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityHomeUserBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeUserActivity extends AppCompatActivity {
    private ProductAdapter productAdapter;
    private ActivityHomeUserBinding mActivityHomeUserBinding;
    private List<Product> mListLaptop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHomeUserBinding = ActivityHomeUserBinding.inflate(getLayoutInflater());
        setContentView(mActivityHomeUserBinding.getRoot());
        setImageSlide();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mActivityHomeUserBinding.rcvProductLaptop.setLayoutManager(linearLayoutManager);
        mListLaptop= new ArrayList<>();
        productAdapter = new ProductAdapter(mListLaptop);
        mActivityHomeUserBinding.rcvProductLaptop.setAdapter(productAdapter);
        setListLapTopFromFireBase();
    }

    private void setListLapTopFromFireBase() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DatabaseReference myRef2 = dataSnapshot.child("Shop").child("Products").getRef();
                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (mListLaptop != null) mListLaptop.clear();
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                Product product = dataSnapshot1.getValue(Product.class);
                                if (product.getProductCategory().equals("Smartphone")){
                                    mListLaptop.add(product);
                                    mListLaptop.add(product);
                                    mListLaptop.add(product);
                                    mListLaptop.add(product);
                                    mListLaptop.add(product);
                                    mListLaptop.add(product);
                                    mListLaptop.add(product);

                                }

                            }
                            productAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(HomeUserActivity.this, "Thất bại rồi!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeUserActivity.this, "Thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void setImageSlide() {
        List<SlideModel> list = new ArrayList<>();
        list.add(new SlideModel(R.drawable.image1, ScaleTypes.FIT));
        list.add(new SlideModel(R.drawable.image2, ScaleTypes.FIT));
        list.add(new SlideModel(R.drawable.image3, ScaleTypes.FIT));
        list.add(new SlideModel(R.drawable.image4, ScaleTypes.FIT));
        mActivityHomeUserBinding.slide.setImageList(list);
    }
}