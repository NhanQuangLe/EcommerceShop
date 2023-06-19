package com.example.ecommerceshop.qui.payment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.ActivityPaymentBinding;
import com.example.ecommerceshop.databinding.FragmentCartBinding;
import com.example.ecommerceshop.nhan.Model.Address;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.UserAddressActivity;
import com.example.ecommerceshop.qui.cart.CartFragment;
import com.example.ecommerceshop.qui.cart.ProductCart;
import com.example.ecommerceshop.qui.cart.ShopProductCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private ActivityPaymentBinding mActivityPaymentBinding;
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == UserAddressActivity.TRA_VE_TU_USER_ADDRESS_ACTIVITY){
                        Intent i = result.getData();
                        Address address = (Address) i.getSerializableExtra("address");
                        setAddress(address);

                    }
                }
            });



    private View mView;
    public static final int FRAGMENT_PAYMENT  = 0;
    public static final int FRAGMENT_ALL_PRODUCT2  = 1;
    public int mCurrentFragment = FRAGMENT_PAYMENT;
    public Fragment paymentFragment;

    private ArrayList<ProductCart> listSelectedCart;
    private ItemPaymentAdapter itemPaymentAdapter;
    private List<ItemPayment> mListItemPayment;
    private List<Voucher> listSelectedVoucher;
    private FirebaseUser mCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityPaymentBinding = ActivityPaymentBinding.inflate(getLayoutInflater());
        mView = mActivityPaymentBinding.getRoot();
        setContentView(mView);

        Bundle bundle = getIntent().getExtras();
       listSelectedCart = bundle.getParcelableArrayList("listSelectedCart");


        init();
        iListener();
    }

    private void iListener() {
        mActivityPaymentBinding.btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mActivityPaymentBinding.btnChooseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(),UserAddressActivity.class);
                mActivityResultLauncher.launch(i);
            }
        });

        mActivityPaymentBinding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ItemPayment itemPayment:mListItemPayment){
                    Log.e("khuyen mai",itemPayment.getTienKhuyenMai()+"");
                }
            }
        });
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view,fragment);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commitAllowingStateLoss();
    }

    private void init() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mActivityPaymentBinding.rcvItemPayment.setLayoutManager(linearLayoutManager);
        itemPaymentAdapter = new ItemPaymentAdapter(this);
        itemPaymentAdapter.setiSendData(new ISenData() {
            @Override
            public void senDataToAdapter(List<Voucher> vouchers) {

            }

            @Override
            public void senDataToPaymentActivity(List<ItemPayment> itemPaymentList) {
                setTotalPayment(itemPaymentList);

            }
        });
        mListItemPayment = new ArrayList<>();
        itemPaymentAdapter.setData(mListItemPayment);
        mActivityPaymentBinding.rcvItemPayment.setAdapter(itemPaymentAdapter);
        ArrayList<String> listShopId = new ArrayList<>();
        ArrayList<String> listShopName = new ArrayList<>();

        for (ProductCart productCart : listSelectedCart) {
            if (!listShopId.contains(productCart.getShopId())) {
                listShopId.add(productCart.getShopId());
                listShopName.add(productCart.getShopName());
            }
        }
        for (int i = 0; i < listShopId.size(); i++) {
            String shopId = listShopId.get(i);
            ItemPayment itemPayment = new ItemPayment();
            itemPayment.setShopId(shopId);
            itemPayment.setListProductCart(new ArrayList<>());
            itemPayment.setShopName(listShopName.get(i));
            for (ProductCart productCart : listSelectedCart) {
                if (productCart.getShopId().equals(shopId)) {
                    itemPayment.getListProductCart().add(productCart);
                }
            }
            mListItemPayment.add(itemPayment);
        }

        itemPaymentAdapter.notifyDataSetChanged();

        listSelectedCart = new ArrayList<>();

        setAddressDefault();
        setInitTotalPayment();
    }

    private void setInitTotalPayment() {
        long finalPayment = 0;
        for (ItemPayment itemPayment:mListItemPayment){
            finalPayment+=itemPayment.getTongTienHang();
        }
        mActivityPaymentBinding.tvTotalMoney.setText(itemPaymentAdapter.getPrice(finalPayment));
    }

    private void setTotalPayment(List<ItemPayment> itemPaymentList) {
        long finalPayment = 0;
        for (ItemPayment itemPayment:itemPaymentList){
            finalPayment+=itemPayment.getTongThanhToan();
        }
        mActivityPaymentBinding.tvTotalMoney.setText(itemPaymentAdapter.getPrice(finalPayment));
    }

    private void setAddressDefault() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/Addresses");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Address address = dataSnapshot.getValue(Address.class);
                    if (address!=null){
                        if (address.isDefault()){
                            setAddress(address);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setAddress(Address address) {
        mActivityPaymentBinding.addressDetail.setText(address.getDetail());
        mActivityPaymentBinding.addressName.setText(address.getFullName());
        mActivityPaymentBinding.addressPhone.setText(address.getPhoneNumber());
        String addressMain = address.getWard()+", "+address.getDistrict()+", "+address.getProvince();
        mActivityPaymentBinding.addressMain.setText(addressMain);
    }

    public void addToListSelectedVoucher(List<Voucher> vouchers){
        List<String> voucherIds = new ArrayList<>();
        for (Voucher voucher:listSelectedVoucher){
            voucherIds.add(voucher.getVoucherid());
        }
        for (Voucher voucher:vouchers){
            if (!voucherIds.contains(voucher.getVoucherid())){
                listSelectedVoucher.add(voucher);
            }
        }
    }

}