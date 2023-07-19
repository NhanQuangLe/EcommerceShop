package com.example.ecommerceshop.Phat.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ecommerceshop.Phat.Adapter.PhotoAdapter;
import com.example.ecommerceshop.Phat.Model.Photo;
import com.example.ecommerceshop.Phat.Model.Product;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class AddProductShopFragment extends Fragment  {
    //UI VIEW
    private View mview;
    private ViewPager productPhoto;
    private CircleIndicator circleIndicator;
    private TextView CategoryProduct, deletebtn,productBrand;
    private LinearLayout addProductbtn;
    private TextInputEditText productName, productDescription, productQuantity,productOriginalPrice,
            productCountry,productDiscountPrice;
    private SwitchCompat discountSwitch;
    private Button btnAddProduct;
    private TextInputLayout dispriceLayout;
    private List<Photo> photoList = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private List<String> uriList, producttypeList, trademarkList ;
    String ProductName, ProductDescription, productCategory, ProductBrand, ProductSite, ProductDiscountNote;
    int ProductQuantity, productPrice, ProductDiscountPrice;
    boolean isDiscount;
    String[] categorys, trademarks;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(10), uris -> {
                if (!uris.isEmpty()) {
                    for (Uri uri:uris) {
                        photoList.add(new Photo(uri, 1));
                    }
                    photoAdapter=new PhotoAdapter(getContext(), photoList);
                    productPhoto.setAdapter(photoAdapter);
                    circleIndicator.setViewPager(productPhoto);
                    photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
                }
            });
    public AddProductShopFragment() { }
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //init
        mview=inflater.inflate(R.layout.fragment_add_product_shop, container, false);
        initUi();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth=FirebaseAuth.getInstance();
        LoadData();

        //function
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
            public void onClick(View view) {categoryDialog();}
            private void categoryDialog() {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Product Category").setItems(categorys, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String category = categorys[i];
                        CategoryProduct.setText(category);
                    }
                }).show();
            }
        });
        productBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {categoryDialog();}
            private void categoryDialog() {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Product brand").setItems(trademarks, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String trademark = trademarks[i];
                        productBrand.setText(trademark);
                    }
                }).show();
            }
        });
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photoList.size()==0){
                    return;
                }
                else{
                    photoList.remove(productPhoto.getCurrentItem());
                    photoAdapter=new PhotoAdapter(getContext(), photoList);
                    productPhoto.setAdapter(photoAdapter);
                    circleIndicator.setViewPager(productPhoto);
                    photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
                }
            }
        });
        discountSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    dispriceLayout.setVisibility(View.VISIBLE);
                    productDiscountPrice.setVisibility(View.VISIBLE);

                }
                else {
                    dispriceLayout.setVisibility(View.GONE);
                    productDiscountPrice.setVisibility(View.GONE);
                }
            }
        });
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Inputdata();
            }
        });
        return mview;
    }
    private void LoadData() {
        producttypeList = new ArrayList<>();
        trademarkList = new ArrayList<>();
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ProductType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                producttypeList.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String str = ds.getValue(String.class);
                    producttypeList.add(str);
                }
                categorys = producttypeList.toArray(new String[producttypeList.size()]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                CustomToast.makeText(getContext(),""+ error.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();

            }
        });
        databaseReference.child("Trademark").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trademarkList.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String str = ds.getValue(String.class);
                    trademarkList.add(str);
                }
                trademarks= trademarkList.toArray(new String[trademarkList.size()]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                CustomToast.makeText(getContext(),""+ error.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();

            }
        });
    }
    private void uploadData( String timestamp) {

        Product product = new Product(timestamp,ProductName, ProductDescription,productCategory,ProductBrand,ProductSite,ProductDiscountNote
        ,  ProductQuantity, productPrice, ProductDiscountPrice, uriList,firebaseAuth.getUid(),0, true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Shop").child("Products").child(timestamp).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                ResetData();
                CustomToast.makeText(getContext(),"Add product successfully",CustomToast.SHORT,CustomToast.SUCCESS).show();

            }
        });
    }
    private void ResetData() {
        productName.setText("");
        productDescription.setText("");
        CategoryProduct.setText("Loại sản phẩm");
        productBrand.setText("Thương hiệu");
        productCountry.setText("");
        productDiscountPrice.setText("");
        productQuantity.setText("");
        productOriginalPrice.setText("");
        discountSwitch.setChecked(false);
        photoList= new ArrayList<>();
        photoAdapter=new PhotoAdapter(getContext(), photoList);
        productPhoto.setAdapter(photoAdapter);
        circleIndicator.setViewPager(productPhoto);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }
    private void Inputdata(){
        ProductName=productName.getText().toString().trim();
        ProductDescription = productDescription.getText().toString().trim();
        productCategory = CategoryProduct.getText().toString().trim();
        ProductBrand = productBrand.getText().toString().trim();
        ProductSite = productCountry.getText().toString().trim();
        isDiscount=discountSwitch.isChecked();
        if(isDiscount){
            if(TextUtils.isEmpty(productDiscountPrice.getText().toString().trim())){
                CustomToast.makeText(getContext(),"Product Discount Price is required...",CustomToast.SHORT,CustomToast.ERROR).show();
                return;
            } else ProductDiscountPrice= Integer.parseInt(productDiscountPrice.getText().toString().trim());
            int x = (int) (( Float.parseFloat(productDiscountPrice.getText().toString().trim())/
                                Float.parseFloat(productOriginalPrice.getText().toString().trim()))*100);
            if(ProductDiscountPrice>= Integer.parseInt(productOriginalPrice.getText().toString().trim())) {
                CustomToast.makeText(getContext(),"Product Discount Price is invalid...",CustomToast.SHORT,CustomToast.ERROR).show();
                return;
            }
            ProductDiscountNote = "- " + x + "%";
        }
        else{
            ProductDiscountNote="";
            ProductDiscountPrice=0;
        }
        if(TextUtils.isEmpty(ProductName)){
            CustomToast.makeText(getContext(),"Product Name is required...",CustomToast.SHORT,CustomToast.ERROR).show();

            return;
        }
        if(TextUtils.isEmpty(ProductDescription)){
            CustomToast.makeText(getContext(),"Product Description is required...",CustomToast.SHORT,CustomToast.ERROR).show();

            return;
        }
        if(productCategory.equals("Loại sản phẩm")){
            CustomToast.makeText(getContext(),"Product Category is required...",CustomToast.SHORT,CustomToast.ERROR).show();

            return;
        }
        if(TextUtils.isEmpty("Thương hiệu")){
            CustomToast.makeText(getContext(),"Product Brand is required...",CustomToast.SHORT,CustomToast.ERROR).show();

            return;
        }
        if(TextUtils.isEmpty(ProductSite)){
            CustomToast.makeText(getContext(),"Product Site is required...",CustomToast.SHORT,CustomToast.ERROR).show();

            return;
        }
        if(TextUtils.isEmpty(productQuantity.getText().toString().trim())) {
            CustomToast.makeText(getContext(),"Product Quantity is required...",CustomToast.SHORT,CustomToast.ERROR).show();

            return;
        } else ProductQuantity = Integer.parseInt(productQuantity.getText().toString().trim());
        if(TextUtils.isEmpty(productOriginalPrice.getText().toString().trim())){
            CustomToast.makeText(getContext(),"Product Price is required...",CustomToast.SHORT,CustomToast.ERROR).show();

            return;
        } else productPrice = Integer.parseInt(productOriginalPrice.getText().toString().trim());
        if(photoList.size()==0) {
            CustomToast.makeText(getContext(),"Product Image is required...",CustomToast.SHORT,CustomToast.ERROR).show();
            return;
        }
        UploadImageFireStore();
    }
    private void UploadImageFireStore() {
        progressDialog.setMessage("Uploading....");
        progressDialog.show();
        uriList=new ArrayList<>();
        String timestamp = ""+System.currentTimeMillis();
        for (int i=0; i<photoList.size(); i++) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("ImageProduct/"+timestamp+"/"+i);
            int finalI = i;
            storageReference.putFile(photoList.get(i).getUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadUri = uriTask.getResult();
                    if(uriTask.isSuccessful()) uriList.add(downloadUri.toString());
                    if(finalI==photoList.size()-1) uploadData(timestamp);
                }
            });
        }
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
        btnAddProduct=mview.findViewById(R.id.btnAddProduct);
        CategoryProduct=mview.findViewById(R.id.CategoryProduct);
        dispriceLayout=mview.findViewById(R.id.dispriceLayout);
    }
}