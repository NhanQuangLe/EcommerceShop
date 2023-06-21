package com.example.ecommerceshop.qui.payment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    private Address myAddress;
    private static FirebaseUser mCurrentUser;
    ProgressDialog TempDialog;
    CountDownTimer countDownTimer;
    Calendar calendar;
    int i=0;
    int numItemPayment=0;
    int numCart=0;
    private static final int THREAD_POOL_SIZE = 10;
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
                final Dialog dialog = new Dialog(PaymentActivity.this);
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
                TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
                TextView tvOk = dialog.findViewById(R.id.tv_ok);
                tvContent.setText("Nhấn đặt hàng đồng nghĩa với việc bạn đã đồng ý những điều khoản của chúng tôi!");
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        dialog.dismiss();
                        numItemPayment=0;
                        long orderId = calendar.getTimeInMillis();
                        createOrderFirebase(orderId);





                    }
                });
                dialog.show();


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
        calendar = Calendar.getInstance();
        TempDialog = new ProgressDialog(PaymentActivity.this);
        TempDialog.setMessage("Đơn hàng của bạn đang được tạo");
        TempDialog.setCancelable(false);
        TempDialog.setProgress(i);
        TempDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        TempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mActivityPaymentBinding.rcvItemPayment.setLayoutManager(linearLayoutManager);
        itemPaymentAdapter = new ItemPaymentAdapter(this);
        itemPaymentAdapter.setiSendData(new ISenData() {
            @Override
            public void senDataToAdapter(Voucher voucher) {

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
        myAddress = address;
        mActivityPaymentBinding.addressDetail.setText(address.getDetail());
        mActivityPaymentBinding.addressName.setText(address.getFullName());
        mActivityPaymentBinding.addressPhone.setText(address.getPhoneNumber());
        String addressMain = address.getWard()+", "+address.getDistrict()+", "+address.getProvince();
        mActivityPaymentBinding.addressMain.setText(addressMain);
    }
    public void createOrderFirebase(long id)  {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/Orders");
        if (numItemPayment==mListItemPayment.size()) {
            navigateToOrderSuccess();
            return;
        }
        ItemPayment itemPayment = mListItemPayment.get(numItemPayment);
            String orderId = id+"";
            String customerId = mCurrentUser.getUid();
            long discountPrice = itemPayment.getTienKhuyenMai();
            String orderStatus = "1";
            Date date = new Date();
            SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
            String orderedDate = sp.format(date);
            long shipPrice = 0;
            String shopId = itemPayment.getShopId();
            long totalPrice = itemPayment.getTongThanhToan();
            List<ItemOrder> Items = new ArrayList<>();
            for (ProductCart productCart:itemPayment.getListProductCart()){
                String pid = productCart.getProductId();
                String pAvatar = productCart.getUri();
                String pName = productCart.getProductName();
                String pBrand = productCart.getBrand();
                long pPrice = 0;
                if (productCart.getProductDiscountPrice()!=0){
                    pPrice = productCart.getProductDiscountPrice();
                }
                else pPrice = productCart.getProductPrice();
                long pQuantity = productCart.getProductQuantity();
                ItemOrder itemOrder = new ItemOrder(pid,pAvatar,pBrand,pName,pPrice,pQuantity);
                Items.add(itemOrder);
            }
            Address receiveAddress = myAddress;

            Order order = new Order(orderId,customerId,discountPrice,orderStatus,orderedDate,shipPrice, shopId,totalPrice,  Items, receiveAddress);
            ref.child(order.getOrderId()).setValue(order, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    numItemPayment++;
                    createOrderFirebase(id+1);

                }
            });



    }

    private void navigateToOrderSuccess() {
        Intent intent = new Intent(PaymentActivity.this,OrderSuccessActivity.class);
        startActivity(intent);
        finishAffinity();
    }





}