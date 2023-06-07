package com.example.ecommerceshop.qui.product_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.FragmentProductDetailBinding;
import com.example.ecommerceshop.qui.homeuser.IClickProductItemListener;
import com.example.ecommerceshop.qui.homeuser.Product;
import com.example.ecommerceshop.qui.homeuser.ProductAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ProductDetailFragment extends Fragment {

    private  ISenDataListener iSenDataListener;
    public interface ISenDataListener{
        void senDataAndReplaceFragment(String textSearch);
    }
    private FragmentProductDetailBinding mFragmentProductDetailBinding;
    private ProductDetailActivity mProductDetailActivity;
    private View mView;

    private ProductAdapter productAdapter;
    private List<Product> mListProduct;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iSenDataListener = (ISenDataListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentProductDetailBinding = FragmentProductDetailBinding.inflate(inflater,container,false);
        mView = mFragmentProductDetailBinding.getRoot();

        mProductDetailActivity = (ProductDetailActivity) getActivity();

        unit();
        mFragmentProductDetailBinding.btnDetailDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragmentProductDetailBinding.btnDetailDesc.getText().toString().equals("Xem thêm") ){
                    ViewGroup.LayoutParams layoutParams = mFragmentProductDetailBinding.productDesc.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    mFragmentProductDetailBinding.productDesc.setLayoutParams(layoutParams);
                    mFragmentProductDetailBinding.btnDetailDesc.setText("Thu gọn");
                    mFragmentProductDetailBinding.btnDetailDesc.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_chervon_up,0);
                }
                else {
                    ViewGroup.LayoutParams layoutParams = mFragmentProductDetailBinding.productDesc.getLayoutParams();
                    layoutParams.height = 50;
                    mFragmentProductDetailBinding.productDesc.setLayoutParams(layoutParams);
                    mFragmentProductDetailBinding.btnDetailDesc.setText("Xem thêm");
                    mFragmentProductDetailBinding.btnDetailDesc.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_chevron_down,0);
                }
            }
        });
        mFragmentProductDetailBinding.btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        mFragmentProductDetailBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textSearch = mFragmentProductDetailBinding.editTextSearch.getText().toString().trim().toLowerCase(Locale.ROOT);
                iSenDataListener.senDataAndReplaceFragment(textSearch);
            }
        });

        return mView;
    }

    private void unit() {
        Product product = (Product) getArguments().get("product");
        setSlideProductImage(product.getUriList());
        mFragmentProductDetailBinding.productName.setText(product.getProductName());
        mFragmentProductDetailBinding.productBrand.setText(product.getProductBrand());
        mFragmentProductDetailBinding.productPrice.setText(product.getPrice());
        if (product.getProductDiscountPrice()==0){
            mFragmentProductDetailBinding.productDiscountPrice.setVisibility(View.GONE);
            mFragmentProductDetailBinding.frameDiscount.setVisibility(View.GONE);
        }
        else {
            mFragmentProductDetailBinding.productDiscountPrice.setText(product.getDiscountPrice());
            mFragmentProductDetailBinding.productDiscountPrice.setPaintFlags(mFragmentProductDetailBinding.productDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mFragmentProductDetailBinding.productDiscountPercent.setText(product.getPercentDiscount());
        }
        mFragmentProductDetailBinding.productDesc.setText(product.getProductDescription());
        mListProduct = new ArrayList<>();
        productAdapter = new ProductAdapter(mListProduct, new IClickProductItemListener() {
            @Override
            public void sentDataProduct(Product product) {
                onClickGoToProductDetail(product);
            }


        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mFragmentProductDetailBinding.rcvProduct.setLayoutManager(linearLayoutManager);
        mFragmentProductDetailBinding.rcvProduct.setAdapter(productAdapter);
        setListShopProductFromFireBase(product.getUid());

    }

    private void setSlideProductImage(List<String> listUri) {
        List<SlideModel> list = new ArrayList<>();
        for (String uri : listUri){
            list.add(new SlideModel(uri, ScaleTypes.FIT));
        }
        mFragmentProductDetailBinding.slideProductImage.setImageList(list,ScaleTypes.FIT);
    }

    private void setListShopProductFromFireBase(String shopId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/"+shopId+"/Shop/Products");
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

    private void onClickGoToProductDetail(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product",product);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }
}