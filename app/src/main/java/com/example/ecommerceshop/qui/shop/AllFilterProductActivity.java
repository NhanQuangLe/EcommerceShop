package com.example.ecommerceshop.qui.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityAllFilterProductBinding;
import com.example.ecommerceshop.qui.homeuser.IClickProductItemListener;
import com.example.ecommerceshop.qui.homeuser.Product;
import com.example.ecommerceshop.qui.homeuser.ProductAdapter;
import com.example.ecommerceshop.qui.product_detail.ProductDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AllFilterProductActivity extends AppCompatActivity {

    private ActivityAllFilterProductBinding mActivityAllFilterProductBinding;
    private View mView;
    private List<Product> mListProduct;
    private ProductAdapter productAdapter;
    private String tag;
    private String brand;
    private String productType;
    private int pos;
    private String shopId;
    private String textSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAllFilterProductBinding = ActivityAllFilterProductBinding.inflate(getLayoutInflater());
        mView = mActivityAllFilterProductBinding.getRoot();
        setContentView(mView);
        init();
        iListener();

    }
    private void init(){
        mListProduct = new ArrayList<>();
        productAdapter = new ProductAdapter(mListProduct, new IClickProductItemListener() {
            @Override
            public void sentDataProduct(Product product) {
                onClickGoToProductDetail(product);
            }


        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        mActivityAllFilterProductBinding.rcvProduct.setLayoutManager(gridLayoutManager);
        mActivityAllFilterProductBinding.rcvProduct.setAdapter(productAdapter);
        Intent intent = getIntent();
        int clickType = intent.getIntExtra("clickType",-1);
        if (clickType==0){
            textSearch = intent.getStringExtra("textSearch");
            shopId = intent.getStringExtra("shopId");
            mActivityAllFilterProductBinding.editTextSearch.setText(textSearch);
            setListProductBySearch();
        }
        else if (clickType==1){
            productType = intent.getStringExtra("category");
            shopId = intent.getStringExtra("shopId");
            setListProductNoFilter();
        }
        else if (clickType==2){
            tag = intent.getStringExtra("tag");
            brand = intent.getStringExtra("brand");
            pos = intent.getIntExtra("pos",-1);
            shopId = intent.getStringExtra("shopId");
            switch (tag){
                case "Laptop":
                    productType = "Laptop";
                    break;
                case "Điện thoại":
                    productType = "Smartphone";
                    break;
                case "Phụ kiện":
                    productType = "Accessory";
                    break;
            }
            setListProductFilter();
        }


    }
    private void iListener(){
        mActivityAllFilterProductBinding.btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mActivityAllFilterProductBinding.editTextSearch.addTextChangedListener(new TextWatcher() {
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
    }
    private void setListProductNoFilter(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query ref = database.getReference("Users/"+this.shopId+"/Shop/Products").orderByChild("productCategory").equalTo(productType);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListProduct!=null) mListProduct.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product!=null && product.isSold()){
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
    private void setListProductFilter(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (brand.equals("")){
            Query ref = database.getReference("Users/"+this.shopId+"/Shop/Products").orderByChild("productCategory").equalTo(productType);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (mListProduct!=null) mListProduct.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        if (product!=null && product.isSold() && checkPrice(product)){
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
        else {
            Query myRef = database.getReference("Users/"+this.shopId+"/Shop/Products").orderByChild("productCategory").equalTo(productType);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (mListProduct!=null) mListProduct.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        if (product!=null && product.getProductBrand().equals(brand) && checkPrice(product)){
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
    private void setListProductBySearch(){
        String res = textSearch.toLowerCase(Locale.ROOT);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query ref = database.getReference("Users/"+this.shopId+"/Shop/Products");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListProduct!=null) mListProduct.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product!=null && product.isSold()){
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
    private boolean checkPrice(Product product){
        if (tag.equals("Laptop")) {
            switch (pos) {
                case 0:
                    return product.getPrice() < 15000000;
                case 1:
                    return (product.getPrice() >= 15000000 && product.getPrice() <= 25000000);
                case 2:
                    return product.getPrice() > 25000000;
                default:
                    return true;
            }
        }
        else if (tag.equals("Điện thoại")){
            switch (pos) {
                case 0:
                    return product.getPrice() < 10000000;
                case 1:
                    return (product.getPrice() >= 10000000 && product.getPrice() <= 20000000);
                case 2:
                    return product.getPrice() > 20000000;
                default:
                    return true;
            }
        }
        else {
            switch (pos) {
                case 0:
                    return product.getPrice() < 1000000;
                case 1:
                    return (product.getPrice() >= 1000000 && product.getPrice() <= 3000000);
                case 2:
                    return product.getPrice() > 3000000;
                default:
                    return true;
            }
        }
    }
    private void onClickGoToProductDetail(Product product) {
        Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product",product);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}