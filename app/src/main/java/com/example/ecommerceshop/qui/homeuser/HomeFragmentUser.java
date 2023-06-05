package com.example.ecommerceshop.qui.homeuser;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerceshop.MainUserActivity;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.FragmentHomeUserBinding;
import com.example.ecommerceshop.qui.product_detail.ProductDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HomeFragmentUser extends Fragment {

    private MainUserActivity mMainUserActivity;
    private FragmentHomeUserBinding mFragmentHomeUserBinding;


    private View viewFragment;
    private ProductAdapter productAdapterLaptop;
    private ProductAdapter productAdapterPhone;
    private ProductAdapter productAdapterAccessories;
    private List<Product> mListLaptop;
    private List<Product> mListPhone;
    private List<Product> mListAccessories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentHomeUserBinding = FragmentHomeUserBinding.inflate(inflater,container,false);
        viewFragment = mFragmentHomeUserBinding.getRoot();
        mMainUserActivity = (MainUserActivity) getActivity();

        mMainUserActivity.setSupportActionBar(mFragmentHomeUserBinding.toolbarHomeUser);
        mFragmentHomeUserBinding.navView.setItemIconTintList(null);

        mFragmentHomeUserBinding.buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mFragmentHomeUserBinding.drawer.isDrawerOpen(GravityCompat.START)){
                    mFragmentHomeUserBinding.drawer.openDrawer(GravityCompat.START);
                }

            }
        });

        View headerView = mFragmentHomeUserBinding.navView.inflateHeaderView(R.layout.header_view);

        Button btnBackward = headerView.findViewById(R.id.btnBackward);
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragmentHomeUserBinding.drawer.isDrawerOpen(GravityCompat.START)){
                    mFragmentHomeUserBinding.drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        TextView textViewHome = headerView.findViewById(R.id.text_view_home);
        textViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragmentHomeUserBinding.drawer.isDrawerOpen(GravityCompat.START)){
                    mFragmentHomeUserBinding.drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        mFragmentHomeUserBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainUserActivity.ACTION = mMainUserActivity.ACTION_SEARCH;
                mMainUserActivity.mCurrentFragment = mMainUserActivity.FRAGMENT_ALL_PRODUCT;
                mMainUserActivity.textSearch = mFragmentHomeUserBinding.editTextSearch.getText().toString();
                mMainUserActivity.replaceFragment(new AllProductsFragment());
            }
        });



        setImageSlide();
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mFragmentHomeUserBinding.rcvProductLaptop.setLayoutManager(linearLayoutManager1);
        mFragmentHomeUserBinding.rcvProductPhone.setLayoutManager(linearLayoutManager2);
        mFragmentHomeUserBinding.rcvProductPhone.setLayoutManager(linearLayoutManager3);
        mListLaptop= new ArrayList<>();
        mListPhone = new ArrayList<>();
        mListAccessories = new ArrayList<>();
        productAdapterLaptop = new ProductAdapter(mListLaptop, new IClickProductItemListener() {
            @Override
            public void sentDataProduct(Product product) {
                onClickGoToProductDetail(product);
            }


        });
        productAdapterPhone = new ProductAdapter(mListPhone, new IClickProductItemListener() {
            @Override
            public void sentDataProduct(Product product) {
                onClickGoToProductDetail(product);
            }
        });
        productAdapterAccessories = new ProductAdapter(mListAccessories, new IClickProductItemListener() {
            @Override
            public void sentDataProduct(Product product) {
                onClickGoToProductDetail(product);
            }
        });
        mFragmentHomeUserBinding.rcvProductLaptop.setAdapter(productAdapterLaptop);
        mFragmentHomeUserBinding.rcvProductPhone.setAdapter(productAdapterPhone);
        mFragmentHomeUserBinding.rcvProductAccessories.setAdapter(productAdapterAccessories);
        setListProductFromFireBase();
        setCart();

        mFragmentHomeUserBinding.categoryLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mMainUserActivity.ACTION = mMainUserActivity.ACTION_CATEGORY;
            mMainUserActivity.CATEGORY = R.id.category_laptop;
            mMainUserActivity.mCurrentFragment = MainUserActivity.FRAGMENT_ALL_PRODUCT;
            mMainUserActivity.replaceFragment(new AllProductsFragment());
            }
        });
        mFragmentHomeUserBinding.categoryPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainUserActivity.ACTION = mMainUserActivity.ACTION_CATEGORY;
                mMainUserActivity.CATEGORY = R.id.category_phone;
                mMainUserActivity.mCurrentFragment = MainUserActivity.FRAGMENT_ALL_PRODUCT;
                mMainUserActivity.replaceFragment(new AllProductsFragment());
            }
        });
        mFragmentHomeUserBinding.categoryAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainUserActivity.ACTION = mMainUserActivity.ACTION_CATEGORY;
                mMainUserActivity.CATEGORY = R.id.category_accessories;
                mMainUserActivity.mCurrentFragment = MainUserActivity.FRAGMENT_ALL_PRODUCT;
                mMainUserActivity.replaceFragment(new AllProductsFragment());
            }
        });
        return viewFragment;
    }

    private void setCart() {
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/Cart");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long quantity = snapshot.getChildrenCount();
                if (quantity==0) mFragmentHomeUserBinding.iconCartQuantity.setVisibility(View.GONE);
                else {
                    mFragmentHomeUserBinding.iconCartQuantity.setVisibility(View.VISIBLE);
                    mFragmentHomeUserBinding.currentCartQuantity.setText(String.valueOf(quantity));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setListProductFromFireBase() {

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
                                if (product.getProductCategory().equals("Laptop")){
                                    mListLaptop.add(product);
                                }
                                else if (product.getProductCategory().equals("Smartphone")){
                                    mListPhone.add(product);
                                }
                                else {
                                    mListAccessories.add(product);
                                }

                            }
                            productAdapterLaptop.notifyDataSetChanged();
                            productAdapterPhone.notifyDataSetChanged();
                            productAdapterAccessories.notifyDataSetChanged();

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setImageSlide() {
        List<SlideModel> list = new ArrayList<>();
        List<String> uriList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (uriList!=null) uriList.clear();
                if (list!=null) list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    DatabaseReference myRef = dataSnapshot.child("Shop").child("ImageAds").getRef();
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                String value = dataSnapshot1.getValue(String.class);
                                if (value!=null) uriList.add(value);
                            }

                            List<String> stringList = new ArrayList<>();
                           Random random = new Random();
                           int size = uriList.size();
                           int num = size>=5? 5 : 3;
                            while (stringList.size() < num){

                                if (!uriList.isEmpty()){
                                    int index = random.nextInt(size);
                                   if (!stringList.contains(uriList.get(index))){
                                       stringList.add(uriList.get(index));
                                   }
                               }
                                else break;

                            }
                           for (String str : stringList){
                               list.add(new SlideModel(str,ScaleTypes.FIT));
                            }

                           mFragmentHomeUserBinding.slide.setImageList(list,ScaleTypes.FIT);

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
    public FragmentHomeUserBinding getmFragmentHomeUserBinding() {
        return mFragmentHomeUserBinding;
    }

}