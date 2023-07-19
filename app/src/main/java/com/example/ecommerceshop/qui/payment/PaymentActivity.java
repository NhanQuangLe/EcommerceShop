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
import com.example.ecommerceshop.qui.homeuser.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PaymentActivity extends AppCompatActivity {

    private ActivityPaymentBinding mActivityPaymentBinding;
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == UserAddressActivity.TRA_VE_TU_USER_ADDRESS_ACTIVITY) {
                        Intent i = result.getData();
                        Address address = (Address) i.getSerializableExtra("address");
                        setAddress(address);
                        for (ItemPayment itemPayment : mListItemPayment) {
                            itemPayment.setAddress(address);
                        }
                        itemPaymentAdapter.notifyDataSetChanged();
                        setTotalPayment();


                    }
                }
            });


    private View mView;
    public static final int FRAGMENT_PAYMENT = 0;
    public static final int FRAGMENT_ALL_PRODUCT2 = 1;
    public int mCurrentFragment = FRAGMENT_PAYMENT;
    public Fragment paymentFragment;

    private ArrayList<ProductCart> listSelectedCart;
    private Product mProduct;
    private int mQuantity;
    private ItemPaymentAdapter itemPaymentAdapter;
    private List<ItemPayment> mListItemPayment;
    private List<Voucher> listSelectedVoucher;
    private Address myAddress;
    private static FirebaseUser mCurrentUser;
    ProgressDialog TempDialog;
    CountDownTimer countDownTimer;
    Calendar calendar;
    int i = 0;
    int numItemPayment = 0;
    int numCart = 0;
    private Map<String,String> IdProvinceShop = new HashMap<>();
    private Boolean isHasAddress = false;
    private static final int THREAD_POOL_SIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityPaymentBinding = ActivityPaymentBinding.inflate(getLayoutInflater());
        mView = mActivityPaymentBinding.getRoot();
        setContentView(mView);

        Bundle bundle = getIntent().getExtras();
        String clickType = bundle.getString("clickType");
        if (clickType.equals("fromCart")) {
            listSelectedCart = bundle.getParcelableArrayList("listSelectedCart");
            IdProvinceShop = (Map<String, String>) bundle.getSerializable("map");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("PricePerUnitDistance");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long priceUnitDistance = snapshot.getValue(Long.class);
                    initIfCart(priceUnitDistance);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else if (clickType.equals("fromProductDetail")) {
            mProduct = (Product) bundle.get("product");
            mQuantity = bundle.getInt("quantity");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("PricePerUnitDistance");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long priceUnitDistance = snapshot.getValue(Long.class);
                    initIfProductDetail(priceUnitDistance);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


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
                i.putExtra("isPayment", true);
                i.setClass(getApplicationContext(), UserAddressActivity.class);
                mActivityResultLauncher.launch(i);
            }
        });
        mActivityPaymentBinding.btnChooseNoAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), UserAddressActivity.class);
                mActivityResultLauncher.launch(i);
            }
        });

        mActivityPaymentBinding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isHasAddress){
                    noti("Vui lòng chọn địa chỉ để nhận hàng!");
                    return;
                }
                final Boolean[] isHasNoSold = {false};
                int index1 = -1;
                for (ItemPayment itemPayment : mListItemPayment) {
                    index1++;
                    int index2 = -1;
                    for (ProductCart productCart : itemPayment.getListProductCart()) {
                        index2++;
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + productCart.getShopId() + "/Shop/Products/" + productCart.getProductId());
                        int finalIndex = index1;
                        int finalIndex1 = index2;
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Boolean isSold = snapshot.child("sold").getValue(Boolean.class);
                                if (!isSold) {
                                    isHasNoSold[0] = true;
                                }
                                if (finalIndex == mListItemPayment.size() - 1 && finalIndex1 == itemPayment.getListProductCart().size() - 1) {
                                    if (isHasNoSold[0]) {
                                        noti("Xin lỗi, vì đơn hàng có sản phẩm đã ngừng kinh doanh");
                                    } else {
                                        // Nếu điều kiện thỏa
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
                                                numItemPayment = 0;
                                                long orderId = calendar.getTimeInMillis();
                                                createOrderFirebase(orderId);


                                            }
                                        });
                                        dialog.show();

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }


            }
        });
    }


    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view, fragment);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commitAllowingStateLoss();
    }

    private void initIfCart(long priceUnitDistance) {
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
            public void senDataToPaymentActivity() {
                setTotalPayment();

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
            itemPayment.setmContext(getApplicationContext());
            String shopProvince = IdProvinceShop.get(shopId);
            itemPayment.setShopProvince(shopProvince);
            itemPayment.priceUnitDistance = priceUnitDistance;
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

    }

    private void initIfProductDetail(long priceUnitDistance) {
        calendar = Calendar.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mActivityPaymentBinding.rcvItemPayment.setLayoutManager(linearLayoutManager);
        itemPaymentAdapter = new ItemPaymentAdapter(this);
        itemPaymentAdapter.setiSendData(new ISenData() {
            @Override
            public void senDataToAdapter(Voucher voucher) {

            }

            @Override
            public void senDataToPaymentActivity() {
                setTotalPayment();

            }
        });
        mListItemPayment = new ArrayList<>();
        itemPaymentAdapter.setData(mListItemPayment);
        mActivityPaymentBinding.rcvItemPayment.setAdapter(itemPaymentAdapter);
        ProductCart productCart = new ProductCart("null", mProduct.getProductId(), mProduct.getProductQuantity(), mProduct.getProductName(),
                mQuantity, mProduct.getProductPrice(), mProduct.getProductDiscountPrice(), mProduct.getUriList().get(0), mProduct.getUid(),
                mProduct.getShopName(), mProduct.getProductBrand(), mProduct.getProductCategory());
        String shopId = mProduct.getUid();
        ItemPayment itemPayment = new ItemPayment();
        itemPayment.setShopId(shopId);
        itemPayment.setListProductCart(new ArrayList<>());
        itemPayment.setShopName(mProduct.getShopName());
        itemPayment.setShopProvince(mProduct.getShopProvince());
        itemPayment.getListProductCart().add(productCart);
        itemPayment.setmContext(getApplicationContext());
        itemPayment.priceUnitDistance = priceUnitDistance;
        mListItemPayment.add(itemPayment);
        itemPaymentAdapter.notifyDataSetChanged();
        setAddressDefault();
    }



    private void setTotalPayment() {
        long finalPayment = 0;
        for (ItemPayment itemPayment : mListItemPayment) {
            finalPayment += itemPayment.getTongThanhToan();
        }
        mActivityPaymentBinding.tvTotalMoney.setText(itemPaymentAdapter.getPrice(finalPayment));
    }

    private void setAddressDefault() {
        mActivityPaymentBinding.layoutAddress.setVisibility(View.GONE);
        Query ref = FirebaseDatabase.getInstance().getReference("Users/" + mCurrentUser.getUid() + "/Customer/Addresses")
                .orderByChild("default").equalTo(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isHasAddress=true;
                    mActivityPaymentBinding.layoutAddress.setVisibility(View.VISIBLE);
                    mActivityPaymentBinding.btnChooseAddress.setVisibility(View.VISIBLE);
                    mActivityPaymentBinding.layoutNoAddress.setVisibility(View.GONE);
                    mActivityPaymentBinding.btnChooseNoAddress.setVisibility(View.GONE);
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Address address = dataSnapshot.getValue(Address.class);
                        setAddress(address);
                        for (ItemPayment itemPayment : mListItemPayment) {
                            itemPayment.setAddress(address);
                        }
                    }
                    itemPaymentAdapter.notifyDataSetChanged();
                }
                else {
                    isHasAddress=false;
                    mActivityPaymentBinding.layoutAddress.setVisibility(View.GONE);
                    mActivityPaymentBinding.btnChooseAddress.setVisibility(View.GONE);
                    mActivityPaymentBinding.layoutNoAddress.setVisibility(View.VISIBLE);
                    mActivityPaymentBinding.btnChooseNoAddress.setVisibility(View.VISIBLE);
                }
                setTotalPayment();
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
        String addressMain = address.getWard() + ", " + address.getDistrict() + ", " + address.getProvince();
        mActivityPaymentBinding.addressMain.setText(addressMain);
    }

    public void createOrderFirebase(long id) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + mCurrentUser.getUid() + "/Customer/Orders");
        if (numItemPayment == mListItemPayment.size()) {
            navigateToOrderSuccess();
            return;
        }
        ItemPayment itemPayment = mListItemPayment.get(numItemPayment);
        String orderId = id + "";
        String customerId = mCurrentUser.getUid();
        long discountPrice = itemPayment.getTienKhuyenMai();
        String orderStatus = "UnProcessed";
        Date date = new Date();
        SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
        String orderedDate = sp.format(date);
        long shipPrice = 0;
        String shopId = itemPayment.getShopId();


        String voucherUsedId = null;
        if (itemPayment.getVoucher() != null) {
            voucherUsedId = itemPayment.getVoucher().getVoucherid();
        }

        long totalPrice = itemPayment.getTongThanhToan();
        List<ItemOrder> Items = new ArrayList<>();
        for (ProductCart productCart : itemPayment.getListProductCart()) {
            String pid = productCart.getProductId();
            String pAvatar = productCart.getUri();
            String pName = productCart.getProductName();
            String pBrand = productCart.getBrand();
            String pCategory = productCart.getProductCategory();

            long pPrice = 0;
            if (productCart.getProductDiscountPrice() != 0) {
                pPrice = productCart.getProductPrice()-productCart.getProductDiscountPrice();
            } else pPrice = productCart.getProductPrice();
            long pQuantity = productCart.getProductQuantity();
            ItemOrder itemOrder = new ItemOrder(pid, pAvatar, pBrand, pName, pPrice, pQuantity, pCategory);
            Items.add(itemOrder);
        }
        Address receiveAddress = myAddress;

        Order order = new Order(orderId, customerId, discountPrice, orderStatus, orderedDate, shipPrice, shopId, totalPrice, Items, receiveAddress, voucherUsedId);
        String finalVoucherUsedId = voucherUsedId;
        ref.child(order.getOrderId()).setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (finalVoucherUsedId != null) {
                    updateVoucherOfItemOrder(finalVoucherUsedId, id);
                } else {
                    numItemPayment++;
                    createOrderFirebase(id + 1);
                }
            }
        });


    }

    private void updateVoucherOfItemOrder(String voucherUsedId, long id) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + mCurrentUser.getUid() + "/Customer/Vouchers");
        ref.child(voucherUsedId).child("used").setValue(true, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                numItemPayment++;
                createOrderFirebase(id + 1);
            }
        });
    }

    private void navigateToOrderSuccess() {
        Intent intent = new Intent(PaymentActivity.this, OrderSuccessActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void noti(String message) {
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
        tvContent.setText(message);
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


}