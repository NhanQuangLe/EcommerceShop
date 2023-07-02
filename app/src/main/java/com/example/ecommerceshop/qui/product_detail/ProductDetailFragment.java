package com.example.ecommerceshop.qui.product_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerceshop.MainUserActivity;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.FragmentProductDetailBinding;
import com.example.ecommerceshop.qui.homeuser.IClickProductItemListener;
import com.example.ecommerceshop.qui.homeuser.Product;
import com.example.ecommerceshop.qui.homeuser.ProductAdapter;
import com.example.ecommerceshop.qui.shop.ShopActivityCustomer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ProductDetailFragment extends Fragment {
    public boolean isShowNavCart =false;
    private String path;
    private long cur_quantity_firebase;
    private  ISenDataListener iSenDataListener;
    public interface ISenDataListener{
        void senDataAndReplaceFragment(String textSearch);
    }
    private FragmentProductDetailBinding mFragmentProductDetailBinding;
    private ProductDetailActivity mProductDetailActivity;
    private View mView;

    private ProductAdapter productAdapter;
    private List<Product> mListProduct;
    private int followers;
    private Product product;
    private FirebaseUser mCurrentUser;
    private String keyHeart;
    private boolean isChecked;
    private float rate;

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
        iListener();


        return mView;
    }


    private void unit() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        product = (Product) getArguments().get("product");
        setInfoProduct(product);
        setInfoShop(product.getUid());
        setListShopProductFromFireBase(product.getUid());
        setHeart();
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

        setRate();



    }

    private void setRate() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int[] temp = {0};
                final int[] i = {0};
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DatabaseReference ref2 = dataSnapshot.getRef().child("Customer/Reviews");
                    ref2.orderByChild("productId").equalTo(product.getProductId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                if (dataSnapshot1.exists()){
                                    int rating = dataSnapshot1.child("rating").getValue(Integer.class);
                                    temp[0] +=rating;
                                    i[0]++;
                                    rate = (float)temp[0]/i[0];
                                    mFragmentProductDetailBinding.ratingBar.setRating(rate);
                                    mFragmentProductDetailBinding.productRate.setText(rate+"");
                                }
                            }
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

    private void iListener() {
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

        mFragmentProductDetailBinding.checkBoxHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/FavouriteProducts");
                if (isChecked==false){
                    ((CompoundButton) view).setChecked(true);
                    String key = String.valueOf((int) new Date().getTime());
                    ref.child(key).setValue(product.getProductId());
                }
                else {
                    String keytemp = keyHeart;
                    keyHeart=null;
                    ((CompoundButton) view).setChecked(false);
                    ref.child(keytemp).removeValue();
                }
                isChecked=!isChecked;
            }
        });
        mFragmentProductDetailBinding.navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mProductDetailActivity,MainUserActivity.class);
                startActivity(i);
                mProductDetailActivity.finish();
            }
        });
        mFragmentProductDetailBinding.navCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNavCarDetail();
                path="";
                cur_quantity_firebase=0;


            }
        });
        mFragmentProductDetailBinding.layoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentProductDetailBinding.navCartDetail.setVisibility(View.INVISIBLE);
            }
        });
        mFragmentProductDetailBinding.icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideNavCarDetail();
            }
        });
        mFragmentProductDetailBinding.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityStr = mFragmentProductDetailBinding.cardQuantity.getText().toString();
                int quantity = Integer.parseInt(quantityStr);
                if (quantity==1) return;
                quantity--;
                mFragmentProductDetailBinding.cardQuantity.setText(String.valueOf(quantity));
            }
        });
        mFragmentProductDetailBinding.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityStr = mFragmentProductDetailBinding.cardQuantity.getText().toString();
                int quantity = Integer.parseInt(quantityStr);
                if (quantity==product.getProductQuantity()){
                    Toast.makeText(mProductDetailActivity, "Số lượng có sẵn không đủ đáp ứng!", Toast.LENGTH_SHORT).show();
                    return;
                }
                quantity++;
                mFragmentProductDetailBinding.cardQuantity.setText(String.valueOf(quantity));
            }
        });
        mFragmentProductDetailBinding.btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path = "";
                cur_quantity_firebase=0;
                String quantityStr = mFragmentProductDetailBinding.cardQuantity.getText().toString();
                int quantity = Integer.parseInt(quantityStr);
                String key = String.valueOf((int) new Date().getTime());
                Cart cart = new Cart(key,product.getProductId(),quantity,product.getUid());
                DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/Cart");
                ref0.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if (product.getProductId().equals(dataSnapshot.child("productId").getValue(String.class))){
                                path = dataSnapshot.getKey();
                                 cur_quantity_firebase = dataSnapshot.child("productQuantity").getValue(Long.class);
                                break;
                            }
                        }
                        if (!path.equals("")){
                            long newQuantity = quantity+cur_quantity_firebase;
                            ref0.child(path).child("productQuantity").setValue(newQuantity, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                                    HideNavCarDetail();
                                }
                            });
                            return;
                        }
                        else {

                            ref0.child(cart.getCartId()).setValue(cart, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                                    HideNavCarDetail();
                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });
        mFragmentProductDetailBinding.viewAllProductShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllProductsFragment2 allProductsFragment2 = new AllProductsFragment2();
                allProductsFragment2.receiveShopIdFromActivity(product.getUid());
                mProductDetailActivity.replaceFragment(allProductsFragment2);

            }
        });
        mFragmentProductDetailBinding.btnViewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShopActivityCustomer.class);
                String shopId = product.getUid();
                intent.putExtra("shopId",shopId);
                getContext().startActivity(intent);
            }
        });


    }

    private void showNavCarDetail() {
        Glide.with(getContext()).load(product.getUriList().get(0)).into(mFragmentProductDetailBinding.imgProductAddCart);
        mFragmentProductDetailBinding.subLayout.setVisibility(View.VISIBLE);
        isShowNavCart=true;
        mFragmentProductDetailBinding.cardQuantity.setText("1");
        mFragmentProductDetailBinding.navCartDetail.startAnimation(AnimationUtils.loadAnimation(
                getContext(),
                R.anim.move_up
        ));


        mFragmentProductDetailBinding.subLayout.setClickable(true);
    }
    public void HideNavCarDetail() {
        mFragmentProductDetailBinding.navCartDetail.startAnimation(AnimationUtils.loadAnimation(
                getContext(),
                R.anim.move_down
        ));
        isShowNavCart=false;
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentProductDetailBinding.subLayout.setVisibility(View.GONE);
            }
        }, 400);



        mFragmentProductDetailBinding.subLayout.setClickable(false);
    }


    private void setHeart() {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/FavouriteProducts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String productId = dataSnapshot.getValue(String.class);
                    if (productId==null) continue;
                    if (productId.equals(product.getProductId())){
                        keyHeart = dataSnapshot.getKey();
                        break;
                    }
                }
                if (keyHeart!=null){
                    mFragmentProductDetailBinding.checkBoxHeart.setChecked(true);
                    isChecked=true;
                }
                else {

                    mFragmentProductDetailBinding.checkBoxHeart.setChecked(false);
                    isChecked=false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setInfoProduct(Product product) {
        setSlideProductImage(product.getUriList());
        mFragmentProductDetailBinding.productName.setText(product.getProductName());
        mFragmentProductDetailBinding.productBrand.setText(product.getProductBrand());

        if (product.getProductDiscountPrice()==0){
            mFragmentProductDetailBinding.productPrice.setText(product.getPriceStr());
            mFragmentProductDetailBinding.productDiscountPrice.setVisibility(View.GONE);
            mFragmentProductDetailBinding.frameDiscount.setVisibility(View.GONE);
        }
        else {
            mFragmentProductDetailBinding.productPrice.setText(product.getPriceAfterDiscountStr());
            mFragmentProductDetailBinding.productDiscountPrice.setText(product.getPriceStr());
            mFragmentProductDetailBinding.productDiscountPrice.setPaintFlags(mFragmentProductDetailBinding.productDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mFragmentProductDetailBinding.productDiscountPercent.setText(product.getPercentDiscountStr());
        }
        mFragmentProductDetailBinding.productDesc.setText(product.getProductDescription());
        mFragmentProductDetailBinding.productQuantity.setText(String.valueOf(product.getProductQuantity()));
        mFragmentProductDetailBinding.psoldQuantity.setText(String.valueOf(product.getPsoldQuantity()));

    }

    private void setInfoShop(String shopId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/"+shopId+"/Shop");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("ShopInfos").child("shopName").getValue(String.class);
                mFragmentProductDetailBinding.shopName.setText(name);
                String address = snapshot.child("ShopInfos").child("shopAddress").getValue(String.class);
                mFragmentProductDetailBinding.shopAddress.setText(address);
                String uri = snapshot.child("ShopInfos").child("shopAvt").getValue(String.class);
                Glide.with(getActivity()).load(uri).into((ImageView) mFragmentProductDetailBinding.shopAvatar);
                mFragmentProductDetailBinding.shopProductQuantity.setText(String.valueOf(snapshot.child("Products").getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference myRef2 = database.getReference("Users");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (followers!=0) followers=0;
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DatabaseReference myRef3 = dataSnapshot.getRef().child("Customer").child("Followers");
                    myRef3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                           for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                               String shopIdFollower = dataSnapshot1.getValue(String.class);
                               if (shopIdFollower.equals(shopId)) {
                                   followers++;
                                   break;
                                }
                           }
                            DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(1);
                            String followersStr;
                            Log.e("follow",""+followers);
                            if (followers<1000) followersStr = String.valueOf(followers);
                            else followersStr= df.format(followers*1.0/1000);

                            mFragmentProductDetailBinding.shopFollower.setText(followersStr);
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

    private void setSlideProductImage(List<String> listUri) {
        List<SlideModel> list = new ArrayList<>();
        for (String uri : listUri){
            list.add(new SlideModel(uri,ScaleTypes.CENTER_INSIDE));
        }
        mFragmentProductDetailBinding.slideProductImage.setImageList(list,ScaleTypes.CENTER_INSIDE);
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