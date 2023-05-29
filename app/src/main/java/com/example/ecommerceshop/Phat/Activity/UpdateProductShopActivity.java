package com.example.ecommerceshop.Phat.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.Phat.Adapter.PhotoAdapter;
import com.example.ecommerceshop.Phat.Model.Photo;
import com.example.ecommerceshop.Phat.Model.Product;
import com.example.ecommerceshop.R;
import com.google.android.gms.tasks.OnFailureListener;
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

public class UpdateProductShopActivity extends AppCompatActivity {

    private ViewPager productPhoto;
    private CircleIndicator circleIndicator;
    private TextView CategoryProduct, deletebtn,productBrand;
    private LinearLayout addProductbtn;
    private TextInputEditText productName, productDescription, productQuantity,productOriginalPrice,
            productCountry,productDiscountPrice,productNoteDiscount;
    private SwitchCompat discountSwitch;
    private Button btnUpdateProduct;
    private ImageView backbtn, deleteBtn;
    private TextInputLayout dispriceLayout,disnotelayout;
    private List<Photo> photoList ;
    private PhotoAdapter photoAdapter;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private List<String> uriList , producttypeList, trademarkList ;
    String ProductName, ProductDescription, productCategory, ProductBrand, ProductSite, ProductDiscountNote, Productid;
    int ProductQuantity, productPrice, ProductDiscountPrice;
    boolean isDiscount;
    String[] categorys, trademarks;
    boolean isdeleted=false;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                if (!uris.isEmpty()) {
                    for (Uri uri:uris) {
                        photoList.add(new Photo(uri,1));
                    }
                    photoAdapter=new PhotoAdapter(UpdateProductShopActivity.this, photoList);
                    productPhoto.setAdapter(photoAdapter);
                    circleIndicator.setViewPager(productPhoto);
                    photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updelete_product_shop);
        initUi();
        LoadData();
        progressDialog=new ProgressDialog(UpdateProductShopActivity.this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth=FirebaseAuth.getInstance();
        Productid = getIntent().getStringExtra("productid");
        if (!isdeleted) loadProductDetail();
        addProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());
            }
        });
        CategoryProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {categoryDialog();}
            private void categoryDialog() {
                AlertDialog.Builder builder= new AlertDialog.Builder(UpdateProductShopActivity.this);
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
                AlertDialog.Builder builder= new AlertDialog.Builder(UpdateProductShopActivity.this);
                builder.setTitle("Product brand").setItems(trademarks, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String trademark = trademarks[i];
                        productBrand.setText(trademark);
                    }
                }).show();
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
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photoList.size()==0){
                    return;
                }
                else{
                    photoList.remove(productPhoto.getCurrentItem());
                    photoAdapter=new PhotoAdapter(UpdateProductShopActivity.this, photoList);
                    productPhoto.setAdapter(photoAdapter);
                    circleIndicator.setViewPager(productPhoto);
                    photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
                }
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductShopActivity.this);
                builder.setTitle("Delete...").setMessage("Are you sure you want to delete product "+ productName.getText().toString()+" ?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isdeleted=true;
                                deleteproduct(Productid);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .show();
            }
        });
        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Inputdata();
            }
        });
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
                Toast.makeText(UpdateProductShopActivity.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UpdateProductShopActivity.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteproduct(String productid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Shop").child("Products").child(productid).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onBackPressed();
                        Toast.makeText(getApplicationContext(), "Delete product successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProductShopActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loadProductDetail() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Shop").child("Products").child(Productid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = new Product();
                product = snapshot.getValue(Product.class);
                if(product!=null){
                    productName.setText(product.getProductName());
                    productDescription.setText(product.getProductDescription());
                    CategoryProduct.setText(product.getProductCategory());
                    productQuantity.setText(String.valueOf(product.getProductQuantity()));
                    productOriginalPrice.setText(String.valueOf(product.getProductPrice()));
                    productBrand.setText(product.getProductBrand());
                    productCountry.setText(product.getProductSite());
                    if(product.getProductDiscountPrice()==0){
                        discountSwitch.setChecked(false);
                        dispriceLayout.setVisibility(View.GONE);
                        disnotelayout.setVisibility(View.GONE);
                        productDiscountPrice.setVisibility(View.GONE);
                        productNoteDiscount.setVisibility(View.GONE);
                    }
                    else {
                        discountSwitch.setChecked(true);
                        dispriceLayout.setVisibility(View.VISIBLE);
                        disnotelayout.setVisibility(View.VISIBLE);
                        productDiscountPrice.setVisibility(View.VISIBLE);
                        productNoteDiscount.setVisibility(View.VISIBLE);
                        productDiscountPrice.setText(String.valueOf(product.getProductDiscountPrice()));
                        productNoteDiscount.setText(product.getProductDiscountNote());
                    }
                    photoList = new ArrayList<>();
                    uriList=product.getUriList();
                    for(String str:uriList){
                        photoList.add(new Photo(Uri.parse(str), 0));
                    }
                    photoAdapter=new PhotoAdapter(UpdateProductShopActivity.this, photoList);
                    productPhoto.setAdapter(photoAdapter);
                    circleIndicator.setViewPager(productPhoto);
                    photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProductShopActivity.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void initUi(){
        deletebtn=findViewById(R.id.deletebtn);
        circleIndicator=findViewById(R.id.circleindicator);
        productPhoto=findViewById(R.id.imgview);
        addProductbtn=findViewById(R.id.addImage);
        productName=findViewById(R.id.productName);
        productDescription=findViewById(R.id.productDescription);
        productQuantity=findViewById(R.id.productQuantity);
        productOriginalPrice=findViewById(R.id.productOriginalPrice);
        productCountry=findViewById(R.id.productCountry);
        productBrand=findViewById(R.id.productBrand);
        discountSwitch=findViewById(R.id.discountSwitch);
        productDiscountPrice=findViewById(R.id.productDiscountPrice);
        productNoteDiscount=findViewById(R.id.productNoteDiscount);
        btnUpdateProduct=findViewById(R.id.btnUpdateProduct);
        CategoryProduct=findViewById(R.id.CategoryProduct);
        dispriceLayout=findViewById(R.id.dispriceLayout);
        disnotelayout=findViewById(R.id.disnotelayout);
        deleteBtn=findViewById(R.id.deleteBtn);
        backbtn=findViewById(R.id.backbtn);
    }
    private void Inputdata(){
        ProductName=productName.getText().toString().trim();
        ProductDescription = productDescription.getText().toString().trim();
        productCategory = CategoryProduct.getText().toString().trim();
        ProductBrand = productBrand.getText().toString().trim();
        ProductSite = productCountry.getText().toString().trim();
        isDiscount=discountSwitch.isChecked();
        if(isDiscount){
            ProductDiscountNote = productNoteDiscount.getText().toString().trim();

            if(TextUtils.isEmpty(ProductDiscountNote)){
                Toast.makeText(UpdateProductShopActivity.this, "Product Discount Note is required...", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(productDiscountPrice.getText().toString().trim())){
                Toast.makeText(UpdateProductShopActivity.this, "Product Discount Price is required...", Toast.LENGTH_SHORT).show();
                return;
            } else ProductDiscountPrice= Integer.parseInt(productDiscountPrice.getText().toString().trim());
        }
        else{
            ProductDiscountNote="";
            ProductDiscountPrice=0;
        }
        if(TextUtils.isEmpty(ProductName)){
            Toast.makeText(UpdateProductShopActivity.this, "Product Name is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(ProductDescription)){
            Toast.makeText(UpdateProductShopActivity.this, "Product Description is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(productCategory.equals("Loại sản phẩm")){
            Toast.makeText(UpdateProductShopActivity.this, "Product Category is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(ProductBrand)){
            Toast.makeText(UpdateProductShopActivity.this, "Product Brand is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(ProductSite)){
            Toast.makeText(UpdateProductShopActivity.this, "Product Site is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(productQuantity.getText().toString().trim())) {
            Toast.makeText(UpdateProductShopActivity.this, "Product Quantity is required...", Toast.LENGTH_SHORT).show();
            return;
        } else ProductQuantity = Integer.parseInt(productQuantity.getText().toString().trim());
        if(TextUtils.isEmpty(productOriginalPrice.getText().toString().trim())){
            Toast.makeText(UpdateProductShopActivity.this, "Product Price is required...", Toast.LENGTH_SHORT).show();
            return;
        } else productPrice = Integer.parseInt(productOriginalPrice.getText().toString().trim());
        if(photoList.size()==0) {
            Toast.makeText(UpdateProductShopActivity.this, "Product Image is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        UploadImageFireStore();
    }
    private void UploadImageFireStore() {
        progressDialog.setMessage("UPLOADING....");
        progressDialog.show();
        uriList=new ArrayList<>();
        for (int i=0; i<photoList.size(); i++) {
            int finalI = i;
            if(photoList.get(i).getTypeImg()==0){
                uriList.add(photoList.get(i).getUri().toString());
                if(finalI==photoList.size()-1) {
                    updateProduct(Productid);
                }
            }
            if(photoList.get(i).getTypeImg()==1) {
                String t = ""+System.currentTimeMillis();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("ImageProduct/"+Productid+"/"+t);

                storageReference.putFile(photoList.get(i).getUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        if(uriTask.isSuccessful()) uriList.add(downloadUri.toString());
                        if(finalI==photoList.size()-1) {
                            updateProduct(Productid);
                        }
                    }
                });
            }
        }
    }
    private void updateProduct(String productid) {
        Product product = new Product(productid,ProductName, ProductDescription,productCategory,ProductBrand,ProductSite,ProductDiscountNote
                ,  ProductQuantity, productPrice, ProductDiscountPrice, uriList,firebaseAuth.getUid() );
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Products").child(productid)
                .setValue(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                loadProductDetail();
                Toast.makeText(UpdateProductShopActivity.this, "Update product successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}