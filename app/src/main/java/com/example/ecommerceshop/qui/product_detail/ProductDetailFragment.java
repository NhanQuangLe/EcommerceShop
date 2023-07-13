package com.example.ecommerceshop.qui.product_detail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerceshop.MainUserActivity;
import com.example.ecommerceshop.Phat.Adapter.AdapterReviews;
import com.example.ecommerceshop.Phat.Model.Review;
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
import com.example.ecommerceshop.Phat.Model.Review;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ProductDetailFragment extends Fragment {
    public boolean isShowNavCart = false;
    private String path;
    private long cur_quantity_firebase;
    private Parcelable recyclerViewState;
    private ISenDataListener iSenDataListener;

    public interface ISenDataListener {
        void senDataAndReplaceFragment(String textSearch);
    }

    private FragmentProductDetailBinding mFragmentProductDetailBinding;
    private ProductDetailActivity mProductDetailActivity;
    private View mView;

    private ProductAdapter productAdapter;
    private List<Product> mListProduct;
    ArrayList<Review> reviews ;
    AdapterReviewCustomer adapterReviews;
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
        mFragmentProductDetailBinding = FragmentProductDetailBinding.inflate(inflater, container, false);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mFragmentProductDetailBinding.rcvProduct.setLayoutManager(linearLayoutManager);
        mFragmentProductDetailBinding.rcvProduct.setAdapter(productAdapter);
        reviews = new ArrayList<>();
         adapterReviews = new AdapterReviewCustomer(getContext(), reviews);

        setRate();
        loadListReview();


    }

    private void setRate() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int[] temp = {0};
                final int[] i = {0};
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DatabaseReference ref2 = dataSnapshot.getRef().child("Customer/Reviews");
                    ref2.orderByChild("productId").equalTo(product.getProductId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                if (dataSnapshot1.exists()) {
                                    int rating = dataSnapshot1.child("rating").getValue(Integer.class);
                                    temp[0] += rating;
                                    i[0]++;
                                    rate = (float) temp[0] / i[0];
                                    mFragmentProductDetailBinding.ratingBar.setRating(rate);
                                    mFragmentProductDetailBinding.productRate.setText(rate + "");
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
                if (mFragmentProductDetailBinding.btnDetailDesc.getText().toString().equals("Xem thêm")) {
                    ViewGroup.LayoutParams layoutParams = mFragmentProductDetailBinding.productDesc.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    mFragmentProductDetailBinding.productDesc.setLayoutParams(layoutParams);
                    mFragmentProductDetailBinding.btnDetailDesc.setText("Thu gọn");
                    mFragmentProductDetailBinding.btnDetailDesc.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_chervon_up, 0);
                } else {
                    ViewGroup.LayoutParams layoutParams = mFragmentProductDetailBinding.productDesc.getLayoutParams();
                    layoutParams.height = 50;
                    mFragmentProductDetailBinding.productDesc.setLayoutParams(layoutParams);
                    mFragmentProductDetailBinding.btnDetailDesc.setText("Xem thêm");
                    mFragmentProductDetailBinding.btnDetailDesc.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_down, 0);
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

        mFragmentProductDetailBinding.checkBoxHeart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + mCurrentUser.getUid() + "/Customer/FavouriteProducts");
                if (b){
                    Map<String,String> map = new HashMap<>();
                    map.put("productId",product.getProductId());
                    map.put("shopId",product.getUid());
                    ref.child(product.getProductId()).setValue(map);
                }
                else {
                    ref.child(product.getProductId()).removeValue();
                }
            }
        });
        mFragmentProductDetailBinding.navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mProductDetailActivity, MainUserActivity.class);
                startActivity(i);
                mProductDetailActivity.finish();
            }
        });
        mFragmentProductDetailBinding.navCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNavCarDetail();
            }
        });
        mFragmentProductDetailBinding.navBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getUid().equals(mCurrentUser.getUid())){
                    noti();
                }
                else {
                    showNavBuyDetail();
                }
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
                intent.putExtra("shopId", shopId);
                getContext().startActivity(intent);
            }
        });


    }

    private void noti() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_ok_cancel);
        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;

        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        TextView tvContent = dialog.findViewById(R.id.tv_content);
        tvContent.setText("Không thể mua hàng thuộc Shop của bạn");
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvCancel.setVisibility(View.GONE);
        TextView tvOk = dialog.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void showNavCarDetail() {
        MyBottomSheetCartDialogFragment myBottomSheetCartDialogFragment = MyBottomSheetCartDialogFragment.newInstance(product);
        myBottomSheetCartDialogFragment.show(getParentFragmentManager(), myBottomSheetCartDialogFragment.getTag());
    }

    private void showNavBuyDetail() {
        MyBottmSheetBuySingleProductDialogFragment myBottmSheetBuySingleProductDialogFragment = MyBottmSheetBuySingleProductDialogFragment.newInstance(product);
        myBottmSheetBuySingleProductDialogFragment.show(getParentFragmentManager(), myBottmSheetBuySingleProductDialogFragment.getTag());
    }


    private void setHeart() {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users/" + mCurrentUser.getUid() + "/Customer/FavouriteProducts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(product.getProductId()).exists()){
                    mFragmentProductDetailBinding.checkBoxHeart.setChecked(true);
                }
                else {
                    mFragmentProductDetailBinding.checkBoxHeart.setChecked(false);
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

        if (product.getProductDiscountPrice() == 0) {
            mFragmentProductDetailBinding.productPrice.setText(product.getPriceStr());
            mFragmentProductDetailBinding.productDiscountPrice.setVisibility(View.GONE);
            mFragmentProductDetailBinding.frameDiscount.setVisibility(View.GONE);
        } else {
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
        DatabaseReference myRef = database.getReference("Users/" + shopId + "/Shop");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("ShopInfos").child("shopName").getValue(String.class);
                mFragmentProductDetailBinding.shopName.setText(name);
                String address = snapshot.child("ShopInfos").child("shopAddress").getValue(String.class);
                mFragmentProductDetailBinding.shopAddress.setText(address);
                String uri = snapshot.child("ShopInfos").child("shopAvt").getValue(String.class);
                Glide.with(getContext()).load(uri).into((ImageView) mFragmentProductDetailBinding.shopAvatar);
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
                if (followers != 0) followers = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DatabaseReference myRef3 = dataSnapshot.getRef().child("Customer").child("Followers");
                    myRef3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                String shopIdFollower = dataSnapshot1.getValue(String.class);
                                if (shopIdFollower.equals(shopId)) {
                                    followers++;
                                    break;
                                }
                            }
                            DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(1);
                            String followersStr;
                            if (followers < 1000) followersStr = String.valueOf(followers);
                            else followersStr = df.format(followers * 1.0 / 1000);

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
        for (String uri : listUri) {
            list.add(new SlideModel(uri, ScaleTypes.CENTER_INSIDE));
        }
        mFragmentProductDetailBinding.slideProductImage.setImageList(list, ScaleTypes.CENTER_INSIDE);
    }

    private void setListShopProductFromFireBase(String shopId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/" + shopId + "/Shop/Products");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListProduct != null) mListProduct.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
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

    private void loadListReview() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mFragmentProductDetailBinding.listRv.setLayoutManager(linearLayoutManager);
        mFragmentProductDetailBinding.listRv.setAdapter(adapterReviews);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (reviews!=null) reviews.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String uid = "" + ds.getRef().getKey();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(uid).child("Customer").child("Reviews").orderByChild("productId")
                            .equalTo(product.getProductId()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            Review review = ds.getValue(Review.class);
                                            if (review != null) {
                                                reviews.add(review);
                                            }
                                        }
                                        adapterReviews.notifyDataSetChanged();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickGoToProductDetail(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }
}