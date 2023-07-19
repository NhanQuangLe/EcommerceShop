package com.example.ecommerceshop.qui.shop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerceshop.databinding.FragmentShopCustomerBinding;
import com.example.ecommerceshop.qui.homeuser.IClickProductItemListener;
import com.example.ecommerceshop.qui.homeuser.Product;
import com.example.ecommerceshop.qui.homeuser.ProductAdapter;
import com.example.ecommerceshop.qui.payment.Voucher;
import com.example.ecommerceshop.qui.product_detail.ProductDetailActivity;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ShopCustomerFragment extends Fragment implements VoucherShopAdapter.ICickListener {

   private FragmentShopCustomerBinding mFragmentShopCustomerBinding;
   private View mView;
   private List<Voucher> mListVoucher;
   private VoucherShopAdapter mVoucherShopAdapter;
   private List<String> mListAdsImages;
   private AdsImageAdapter mAdsImageAdapter;
   private FirebaseUser mCurrentUser;
   private ShopActivityCustomer  mActivity;
   private String shopId;
    private ProductAdapter productAdapterLaptop;
    private ProductAdapter productAdapterPhone;
    private ProductAdapter productAdapterAccessories;
    private List<Product> mListLaptop;
    private List<Product> mListPhone;
    private List<Product> mListAccessories;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentShopCustomerBinding = FragmentShopCustomerBinding.inflate(inflater,container,false);
        mView = mFragmentShopCustomerBinding.getRoot();

        init();
        iListener();

        return mView;
    }



    public void init(){
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mActivity = (ShopActivityCustomer) getActivity();
        this.shopId = mActivity.getShopId();
        setUIVoucher();
        setUIAds();
        setListRecycleView();

    }

    private void setListRecycleView() {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mFragmentShopCustomerBinding.rcvProductLaptop.setLayoutManager(linearLayoutManager1);
        mFragmentShopCustomerBinding.rcvProductPhone.setLayoutManager(linearLayoutManager2);
        mFragmentShopCustomerBinding.rcvProductAccessories.setLayoutManager(linearLayoutManager3);
        mListLaptop = new ArrayList<>();
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
        mFragmentShopCustomerBinding.rcvProductLaptop.setAdapter(productAdapterLaptop);
        mFragmentShopCustomerBinding.rcvProductPhone.setAdapter(productAdapterPhone);
        mFragmentShopCustomerBinding.rcvProductAccessories.setAdapter(productAdapterAccessories);
        setListProductFromFireBase();
    }

    private void setUIAds() {
        mListAdsImages = new ArrayList<>();
        mAdsImageAdapter = new AdsImageAdapter();
        mAdsImageAdapter.setData(mListAdsImages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        mFragmentShopCustomerBinding.rcvAdsImage.setLayoutManager(linearLayoutManager);
        mFragmentShopCustomerBinding.rcvAdsImage.setAdapter(mAdsImageAdapter);
        setListAdsImages();

    }

    private void setListAdsImages() {

        List<String> uriImages = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+this.shopId+"/Shop/ImageAds");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (uriImages!=null) uriImages.clear();
                if (mListAdsImages!=null) mListAdsImages.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String uri = dataSnapshot.getValue(String.class);
                    uriImages.add(uri);
                }

                Random random = new Random();
                int size = uriImages.size();
                int num = size >= 2 ? 2 : size;
                for (int i=1; i<=num; i++){
                    int index = random.nextInt(size);
                    if (!mListAdsImages.contains(uriImages.get(index))){
                        mListAdsImages.add(uriImages.get(index));
                    }
                }

                mAdsImageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUIVoucher() {
        mListVoucher = new ArrayList<>();
        mVoucherShopAdapter = new VoucherShopAdapter(getContext());
        mVoucherShopAdapter.setShopId(this.shopId);
        mVoucherShopAdapter.setData(mListVoucher);
        mVoucherShopAdapter.setiCickListener(this::clickSaveVoucher);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        mFragmentShopCustomerBinding.rcvVoucherShop.setLayoutManager(linearLayoutManager);
        mFragmentShopCustomerBinding.rcvVoucherShop.setAdapter(mVoucherShopAdapter);
        setListVoucher();
    }

    private void iListener() {
        mFragmentShopCustomerBinding.categoryLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity,AllFilterProductActivity.class);
                intent.putExtra("clickType",1);
                intent.putExtra("category","Laptop");
                intent.putExtra("shopId",shopId);
                mActivity.startActivity(intent);
            }
        });
        mFragmentShopCustomerBinding.categoryPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity,AllFilterProductActivity.class);
                intent.putExtra("clickType",1);
                intent.putExtra("category","Smartphone");
                intent.putExtra("shopId",shopId);
                mActivity.startActivity(intent);
            }
        });
        mFragmentShopCustomerBinding.categoryAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity,AllFilterProductActivity.class);
                intent.putExtra("clickType",1);
                intent.putExtra("category","Accessory");
                intent.putExtra("shopId",shopId);
                mActivity.startActivity(intent);
            }
        });
    }

    private void setListVoucher() {
        String shopId = ((ShopActivityCustomer) getActivity()).getShopId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+shopId+"/Shop/Vouchers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListVoucher!=null) mListVoucher.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Voucher voucher = dataSnapshot.getValue(Voucher.class);
                    if (voucher!=null){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date dateExpired = new Date();
                        try {
                            dateExpired = dateFormat.parse(voucher.getExpiredDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date now = new Date();
                        if (checkExpired(dateExpired, now) && voucher.getQuantity() > 0){
                            mListVoucher.add(voucher);
                        }

                    }
                }
                mVoucherShopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public boolean checkExpired(Date d1, Date d2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(d1);
        calendar2.setTime(d2);
        int year1 = calendar1.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);
        int day1 = calendar1.get(Calendar.DAY_OF_MONTH);

        int year2 = calendar2.get(Calendar.YEAR);
        int month2 = calendar2.get(Calendar.MONTH);
        int day2 = calendar2.get(Calendar.DAY_OF_MONTH);

        if (year1 < year2) {
            return false;
        } else if (year1 > year2) {
            return true;
        } else {
            if (month1 < month2) {
                return false;
            } else if (month1 > month2) {
                return true;
            } else {
                if (day1 < day2) {
                    return false;
                } else if (day1 > day2) {
                    return true;
                } else {
                    return true;
                }
            }
        }
    }
    private void setListProductFromFireBase() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/"+this.shopId+"/Shop/Products");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListLaptop!=null) mListLaptop.clear();
                if (mListAccessories!=null) mListAccessories.clear();
                if (mListPhone!=null) mListPhone.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product.getProductCategory().equals("Laptop")) {
                        mListLaptop.add(product);
                    } else if (product.getProductCategory().equals("Smartphone")) {
                        mListPhone.add(product);
                    } else if (product.getProductCategory().equals("Accessory")){
                        mListAccessories.add(product);
                    }
                }
                productAdapterLaptop.notifyDataSetChanged();
                productAdapterPhone.notifyDataSetChanged();
                productAdapterAccessories.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                CustomToast.makeText(getContext(),"Thất bại!",CustomToast.SHORT,CustomToast.ERROR).show();

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

    @Override
    public void clickSaveVoucher(Voucher voucher) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/Vouchers/"+voucher.getVoucherid());
        Voucher tmp = new Voucher(voucher.getVoucherid(),false,shopId);
        ref.setValue(tmp, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                CustomToast.makeText(getContext(),"Lưu voucher thành công",CustomToast.SHORT,CustomToast.SUCCESS).show();

            }
        });



    }
}