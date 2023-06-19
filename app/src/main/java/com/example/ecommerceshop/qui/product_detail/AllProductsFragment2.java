package com.example.ecommerceshop.qui.product_detail;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerceshop.databinding.FragmentAllProducts2Binding;
import com.example.ecommerceshop.qui.homeuser.IClickProductItemListener;
import com.example.ecommerceshop.qui.homeuser.Product;
import com.example.ecommerceshop.qui.homeuser.ProductAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AllProductsFragment2 extends Fragment {

    private FragmentAllProducts2Binding mFragmentAllProducts2Binding;
    private ProductDetailActivity mProductDetailActivity;
    private View viewFragment;
    private List<Product> mListProduct;
    private ProductAdapter productAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentAllProducts2Binding = FragmentAllProducts2Binding.inflate(inflater,container,false);
        viewFragment = mFragmentAllProducts2Binding.getRoot();

        mProductDetailActivity = (ProductDetailActivity) getActivity();
        receiveDataFromDetailFragment();

        mListProduct = new ArrayList<>();
        productAdapter = new ProductAdapter(mListProduct, new IClickProductItemListener() {
            @Override
            public void sentDataProduct(Product product) {
                onClickGoToProductDetail(product);
            }


        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        mFragmentAllProducts2Binding.rcvProduct.setLayoutManager(gridLayoutManager);
        mFragmentAllProducts2Binding.rcvProduct.setAdapter(productAdapter);
        setCart();
        setListSearchProductFromFireBase();

        mFragmentAllProducts2Binding.btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductDetailActivity.backToDetail();
            }
        });
        return viewFragment;
    }

    private void setListSearchProductFromFireBase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        String res = mFragmentAllProducts2Binding.editTextSearch.getText().toString();
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

    public void receiveDataFromDetailFragment(){
        Bundle bundle = getArguments();
        String textSearch = bundle.getString("text");
        mFragmentAllProducts2Binding.editTextSearch.setText(textSearch);
    }
    private void onClickGoToProductDetail(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product",product);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }
    private void setCart() {
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/Cart");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long quantity = snapshot.getChildrenCount();
                if (quantity==0) mFragmentAllProducts2Binding.iconCartQuantity.setVisibility(View.GONE);
                else {
                    mFragmentAllProducts2Binding.iconCartQuantity.setVisibility(View.VISIBLE);
                    mFragmentAllProducts2Binding.currentCartQuantity.setText(String.valueOf(quantity));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}