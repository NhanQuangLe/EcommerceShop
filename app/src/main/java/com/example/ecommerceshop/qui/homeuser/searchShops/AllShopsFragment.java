package com.example.ecommerceshop.qui.homeuser.searchShops;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import com.example.ecommerceshop.MainUserActivity;
import com.example.ecommerceshop.Phat.Model.RequestShop;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.FragmentAllShopsBinding;
import com.example.ecommerceshop.qui.homeuser.HomeFragmentUser;
import com.example.ecommerceshop.qui.homeuser.ProductAdapter;
import com.example.ecommerceshop.qui.homeuser.searchProducts.ShopAdapter;
import com.example.ecommerceshop.qui.product_detail.ProductDetailActivity;
import com.example.ecommerceshop.qui.shop.ShopActivityCustomer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AllShopsFragment extends Fragment implements ShopAdapter.IClickShopItemListener {

    private FragmentAllShopsBinding mFragmentAllShopsBinding;
    private View mView;
    private MainUserActivity mMainUserActivity;
    private ShopAdapter shopAdapter;
    private List<RequestShop> mListShop;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentAllShopsBinding = FragmentAllShopsBinding.inflate(inflater,container,false);
        mView = mFragmentAllShopsBinding.getRoot();

        init();
        iListener();

        return mView;
    }

    private void iListener() {
        mFragmentAllShopsBinding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    shopAdapter.getFilter().filter(charSequence);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mFragmentAllShopsBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setListSearchShop();
            }
        });
        mFragmentAllShopsBinding.btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainUserActivity.mCurrentFragment = mMainUserActivity.FRAGMENT_HOME;
                mMainUserActivity.replaceFragment(new HomeFragmentUser());
            }
        });
    }

    private void init(){
        mMainUserActivity = (MainUserActivity) getActivity();
        mFragmentAllShopsBinding.editTextSearch.setText(mMainUserActivity.getTextSearch());
        mListShop = new ArrayList<>();
        shopAdapter = new ShopAdapter();
        shopAdapter.setiClickShopItemListener(this);
        shopAdapter.setData(mListShop);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        mFragmentAllShopsBinding.rcvShop.setLayoutManager(linearLayoutManager);
        mFragmentAllShopsBinding.rcvShop.setAdapter(shopAdapter);

        setListSearchShop();
    }

    private void setListSearchShop() {
        Log.e("name","Đã vào");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        String res = mFragmentAllShopsBinding.editTextSearch.getText().toString().toLowerCase(Locale.ROOT);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListShop!=null) mListShop.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DatabaseReference ref2 = dataSnapshot.getRef().child("Shop").child("ShopInfos");
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                RequestShop shop = snapshot.getValue(RequestShop.class);
                                Log.e("name",shop.getShopName());
                                if (shop!=null){
                                    if (shop.getShopName().toLowerCase(Locale.ROOT).contains(res) || shop.getShopAddress().toLowerCase(Locale.ROOT).contains(res)){
                                        mListShop.add(shop);
                                    }

                                }
                            }
                            shopAdapter.notifyDataSetChanged();
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

    @Override
    public void sendDataShop(RequestShop shop) {
        Intent intent = new Intent(getContext(), ShopActivityCustomer.class);
        String shopId = shop.getUid();
        intent.putExtra("shopId",shopId);
        getContext().startActivity(intent);
    }
}