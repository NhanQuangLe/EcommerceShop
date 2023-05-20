package com.example.ecommerceshop.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ecommerceshop.Adapter.PhotoAdapter;
import com.example.ecommerceshop.Constants;
import com.example.ecommerceshop.Model.Photo;
import com.example.ecommerceshop.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class AddProductShopFragment extends Fragment {

    //UI VIEW

    private View mview;
    private ViewPager productPhoto;
    private CircleIndicator circleIndicator;
    private TextView CategoryProduct, deletebtn;
    private LinearLayout addProductbtn;
    private TextInputEditText productName, productDescription, productQuantity,productOriginalPrice,
            productCountry,productBrand,productDiscountPrice,productNoteDiscount;
    private SwitchCompat discountSwitch;
    private Button btnAddProduct;

    private TextInputLayout dispriceLayout,disnotelayout;

    List<Photo> photoList=new ArrayList<>();;
    private PhotoAdapter photoAdapter;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                if (!uris.isEmpty()) {
                    for (Uri uri:uris) {
                        photoList.add(new Photo(uri));
                    }
                    photoAdapter=new PhotoAdapter(getContext(), photoList);
                    productPhoto.setAdapter(photoAdapter);
                    circleIndicator.setViewPager(productPhoto);
                    photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
                }
            });
    public AddProductShopFragment() { }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //init ui
        mview=inflater.inflate(R.layout.fragment_add_product_shop, container, false);
        initUi();
        addProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());
            }
        });

        CategoryProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDialog();
            }

            private void categoryDialog() {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Product Category").setItems(Constants.categorys, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String category = Constants.categorys[i];
                        CategoryProduct.setText(category);
                    }
                }).show();
            }
        });
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoList.remove(productPhoto.getCurrentItem());
                photoAdapter=new PhotoAdapter(getContext(), photoList);
                productPhoto.setAdapter(photoAdapter);
                circleIndicator.setViewPager(productPhoto);
                photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
            }
        });
        discountSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    dispriceLayout.setVisibility(View.VISIBLE);
                    disnotelayout.setVisibility(View.VISIBLE);
                    productDiscountPrice.setVisibility(View.VISIBLE);
                    productNoteDiscount.setVisibility(View.VISIBLE);
                }
                else {
                    dispriceLayout.setVisibility(View.GONE);
                    disnotelayout.setVisibility(View.GONE);
                    productDiscountPrice.setVisibility(View.GONE);
                    productNoteDiscount.setVisibility(View.GONE);
                }
            }
        });
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return mview;
    }

    private void initUi(){
        deletebtn=mview.findViewById(R.id.deletebtn);
        circleIndicator=mview.findViewById(R.id.circleindicator);
        productPhoto=mview.findViewById(R.id.imgview);
        addProductbtn=mview.findViewById(R.id.addImage);
        productName=mview.findViewById(R.id.productName);
        productDescription=mview.findViewById(R.id.productDescription);
        productQuantity=mview.findViewById(R.id.productQuantity);
        productOriginalPrice=mview.findViewById(R.id.productOriginalPrice);
        productCountry=mview.findViewById(R.id.productCountry);
        productBrand=mview.findViewById(R.id.productBrand);
        discountSwitch=mview.findViewById(R.id.discountSwitch);
        productDiscountPrice=mview.findViewById(R.id.productDiscountPrice);
        productNoteDiscount=mview.findViewById(R.id.productNoteDiscount);
        btnAddProduct=mview.findViewById(R.id.btnAddProduct);
        CategoryProduct=mview.findViewById(R.id.CategoryProduct);
        dispriceLayout=mview.findViewById(R.id.dispriceLayout);
        disnotelayout=mview.findViewById(R.id.disnotelayout);
    }


}