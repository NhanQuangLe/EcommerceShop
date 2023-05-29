package com.example.ecommerceshop.qui.homeuser;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerceshop.MainUserActivity;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.FragmentAllProductsBinding;
import com.example.ecommerceshop.qui.product_detail.ProductDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AllProductsFragment extends Fragment {
    private MainUserActivity mMainUserActivity;
    private FragmentAllProductsBinding mFragmentAllProductsBinding;
    private View viewFragment;
    private ProductAdapter productAdapter;
    private List<Product> mListProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentAllProductsBinding = FragmentAllProductsBinding.inflate(inflater,container,false);
        viewFragment = mFragmentAllProductsBinding.getRoot();

        mMainUserActivity = (MainUserActivity) getActivity();

        mFragmentAllProductsBinding.editTextSearch.setText(mMainUserActivity.getTextSearch());
        mListProduct = new ArrayList<>();
        productAdapter = new ProductAdapter(mListProduct, new IClickProductItemListener() {
            @Override
            public void sentDataProduct(Product product) {
                onClickGoToProductDetail(product);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        mFragmentAllProductsBinding.rcvProduct.setLayoutManager(gridLayoutManager);
        mFragmentAllProductsBinding.rcvProduct.setAdapter(productAdapter);

        if (mMainUserActivity.ACTION == mMainUserActivity.ACTION_SEARCH){
            setListSearchProductFromFireBase();
        }
        if (mMainUserActivity.ACTION == mMainUserActivity.ACTION_CATEGORY){
            if (mMainUserActivity.CATEGORY == R.id.category_laptop){
                setListProductCategory("Laptop");
            }
            else if (mMainUserActivity.CATEGORY == R.id.category_phone){
                setListProductCategory("Smartphone");
            }
            else {
                setListProductCategory("Accessory");
            }
            mFragmentAllProductsBinding.editTextSearch.setText("");
        }
        mFragmentAllProductsBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setListSearchProductFromFireBase();
            }
        });
        mFragmentAllProductsBinding.btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainUserActivity.mCurrentFragment = mMainUserActivity.FRAGMENT_HOME;
                mMainUserActivity.replaceFragment(new HomeFragmentUser());
            }
        });
        return viewFragment;
    }

    private void setListProductCategory(String brand) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DatabaseReference myRef2 = dataSnapshot.getRef().child("Shop").child("Products");
                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (mListProduct !=null) mListProduct.clear();
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                Product product = dataSnapshot1.getValue(Product.class);
                                if (product.getProductCategory().equals(brand)){
                                    mListProduct.add(product);
                                }
                            }
                            productAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setListSearchProductFromFireBase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        String res = mFragmentAllProductsBinding.editTextSearch.getText().toString().toLowerCase(Locale.ROOT);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DatabaseReference myRef2 = dataSnapshot.child("Shop").child("Products").getRef();
                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (mListProduct!=null) mListProduct.clear();
                            for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                Product product = dataSnapshot1.getValue(Product.class);
                                if (product!=null){

                                    if (product.getProductName().toLowerCase(Locale.ROOT).contains(res) || product.getProductBrand().toLowerCase(Locale.ROOT).contains(res)
                                        || product.getProductCategory().toLowerCase(Locale.ROOT).contains(res)){
                                        mListProduct.add(product);
                                    }

                                }
                            }
                            productAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void onClickGoToProductDetail(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product",product);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }

}