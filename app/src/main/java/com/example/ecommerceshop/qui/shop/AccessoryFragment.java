package com.example.ecommerceshop.qui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.FragmentAccessoryBinding;
import com.example.ecommerceshop.databinding.FragmentLaptopBinding;
import com.example.ecommerceshop.databinding.FragmentShopCustomerBinding;
import com.example.ecommerceshop.qui.homeuser.IClickProductItemListener;
import com.example.ecommerceshop.qui.homeuser.Product;
import com.example.ecommerceshop.qui.homeuser.ProductAdapter;
import com.example.ecommerceshop.qui.payment.Voucher;
import com.example.ecommerceshop.qui.product_detail.ProductDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AccessoryFragment extends Fragment  {

    private FirebaseUser mCurrentUser;
    private ShopActivityCustomer mActivity;
    private String shopId;
    private String brand;
    private int pos;

    public FragmentAccessoryBinding getmFragmentAccessoryBinding() {
        return mFragmentAccessoryBinding;
    }

    private FragmentAccessoryBinding mFragmentAccessoryBinding;
    private View mView;
    private List<String> mListTradeMark;
    private List<String> mListPrice;
    ItemTradeMarkAdapter adapter1;
    ItemTradeMarkAdapter adapter2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentAccessoryBinding = FragmentAccessoryBinding.inflate(inflater,container,false);
        mView = mFragmentAccessoryBinding.getRoot();
        init();
        iListener();
        return mView;
    }

    public void init(){
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mActivity = (ShopActivityCustomer) getActivity();
        this.shopId = mActivity.getShopId();
        mListTradeMark = new ArrayList<>();
        adapter1 = new ItemTradeMarkAdapter(getContext(), R.layout.adapter_item_trademark,mListTradeMark);
        mFragmentAccessoryBinding.autoCompleteTv.setAdapter(adapter1);
        mListPrice = new ArrayList<>();
        adapter2 = new ItemTradeMarkAdapter(getContext(), R.layout.adapter_item_trademark,mListPrice);
        mFragmentAccessoryBinding.autoCompleteTv2.setAdapter(adapter2);
        this.shopId = mActivity.getShopId();
        brand="";
        pos=-1;
        setListTradeMark();
        setListPrice();
    }
    public void iListener(){
        mFragmentAccessoryBinding.autoCompleteTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                brand = adapterView.getItemAtPosition(i).toString();
            }
        });
        mFragmentAccessoryBinding.autoCompleteTv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
            }
        });
        mFragmentAccessoryBinding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity,AllFilterProductActivity.class);
                intent.putExtra("clickType",2);
                intent.putExtra("brand",brand);
                intent.putExtra("pos",pos);
                intent.putExtra("tag",mActivity.getTag());
                intent.putExtra("shopId",shopId);
                mActivity.startActivity(intent);
            }
        });
    }
    public void setListPrice() {
        mListPrice.add("Dưới 1 triệu");
        mListPrice.add("Từ 1 đến 3 triệu");
        mListPrice.add("Trên 3 triệu");
        adapter2.notifyDataSetChanged();


    }

    public void setListTradeMark() {

        Query ref = FirebaseDatabase.getInstance().getReference("Users/"+this.shopId+"/Shop/Products")
                .orderByChild("productCategory")
                .equalTo("Accessory");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListTradeMark!=null) mListTradeMark.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product!=null){
                        if (!mListTradeMark.contains(product.getProductBrand())){
                            mListTradeMark.add(product.getProductBrand());
                        }
                    }
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}