package com.example.ecommerceshop.qui.product_detail;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerceshop.databinding.FragmentAllProducts2Binding;
import com.example.ecommerceshop.qui.cart.CartActivity;
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
    private String shopId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentAllProducts2Binding = FragmentAllProducts2Binding.inflate(inflater,container,false);
        viewFragment = mFragmentAllProducts2Binding.getRoot();

        mProductDetailActivity = (ProductDetailActivity) getActivity();

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
        mFragmentAllProducts2Binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                getContext().startActivity(intent);
            }
        });
        mFragmentAllProducts2Binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    productAdapter.getFilter().filter(charSequence);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return viewFragment;
    }

    private void setListSearchProductFromFireBase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/"+this.shopId+"/Shop/Products");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListProduct!=null) mListProduct.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product!=null){
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


    public void receiveShopIdFromActivity(String shopId){
        this.shopId = shopId;
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