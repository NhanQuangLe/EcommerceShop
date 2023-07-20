package com.example.ecommerceshop.nhan.ProfileCustomer.orders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Address;
import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrdersFragment;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryProductsInOrderAdapter;
import com.example.ecommerceshop.qui.cart.CartActivity;
import com.example.ecommerceshop.qui.shop.ShopActivityCustomer;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {
    TextView address_name, address_phone, address_detail, address_main;

    ImageView iv_ShopAvatar;
    Button btnBackward;
    RecyclerView rv_ProductList;
    TextView tv_ShopName, tv_statusOrder, tv_DateSuccess, btn_Rate;
    TextView tv_SumMoney, tv_DiscountPrice, tv_DeliveryPrice, tv_TotalPrice;
    LinearLayout btn_buy;
    ConstraintLayout item_order_shop;
    private ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail_read_only);
        Intent intent = getIntent();
        Order ho = new Order();
        InitUI();
        if(intent.getBooleanExtra("isNoti", false)){
            String orderId = intent.getStringExtra("orderId");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(FirebaseAuth.getInstance().getUid())
                    .child("Customer")
                    .child("Orders")
                    .child(orderId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {
                            Order ho = new Order();
                            ho.setOrderId(ds.child("orderId").getValue(String.class));
                            ho.setCustomerId(ds.child("customerId").getValue(String.class));
                            ref.child(ds.child("shopId").getValue(String.class))
                                    .child("Shop")
                                    .child("ShopInfos").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            ho.setShopAvt(snapshot.child("shopAvt").getValue(String.class));
                                            ho.setShopName(snapshot.child("shopName").getValue(String.class));
                                            ho.setShopId(ds.child("shopId").getValue(String.class));
                                            ho.setShipPrice(ds.child("shipPrice").getValue(int.class));
                                            ho.setDiscountPrice(ds.child("discountPrice").getValue(int.class));
                                            ho.setOrderStatus(ds.child("orderStatus").getValue(String.class));
                                            ho.setTotalPrice(ds.child("totalPrice").getValue(int.class));
                                            ho.setOrderedDate(ds.child("orderDate").getValue(String.class));
                                            ho.setReceiveAddress(ds.child("receiveAddress").getValue(Address.class));
                                            ho.setShipPrice(ds.child("shipPrice").getValue(int.class));
                                            ArrayList<Product> products = new ArrayList<>();
                                            for(DataSnapshot product : ds.child("items").getChildren())
                                            {
                                                Product pd = new Product();
                                                pd.setProductID(product.child("pid").getValue(String.class));
                                                pd.setShopID(ds.child("shopId").getValue(String.class));
                                                pd.setProductAvatar(product.child("pAvatar").getValue(String.class));
                                                pd.setProductBrand(product.child("pBrand").getValue(String.class));
                                                pd.setProductName(product.child("pName").getValue(String.class));
                                                pd.setProductCategory(product.child("pCategory").getValue(String.class));
                                                pd.setProductDiscountPrice(product.child("pPrice").getValue(int.class));
                                                pd.setPurchaseQuantity(product.child("pQuantity").getValue(int.class));
                                                products.add(pd);
                                            }
                                            ho.setItems(products);
                                            LoadData(ho);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            CustomToast.makeText(getApplicationContext(),error.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();
                                        }
                                    });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        else{
            ho = (Order) intent.getSerializableExtra("HistoryOrder");
            LoadData(ho);
        }
    }
    private void InitUI()
    {
        address_name = findViewById(R.id.address_name);
        address_phone = findViewById(R.id.address_phone);
        address_detail = findViewById(R.id.address_detail);
        address_main = findViewById(R.id.address_main);

        iv_ShopAvatar = findViewById(R.id.iv_ShopAvatar);
        rv_ProductList = findViewById(R.id.rv_ProductList);
        tv_ShopName = findViewById(R.id.tv_ShopName);
        tv_statusOrder = findViewById(R.id.tv_statusOrder);
        tv_SumMoney = findViewById(R.id.tv_SumMoney);
        tv_DiscountPrice = findViewById(R.id.tv_DiscountPrice);
        tv_DeliveryPrice = findViewById(R.id.tv_DeliveryPrice);
        tv_TotalPrice = findViewById(R.id.tv_TotalPrice);
        tv_DateSuccess = findViewById(R.id.tv_DateSuccess);
        item_order_shop = findViewById(R.id.item_order_shop);
        btn_buy = findViewById(R.id.btn_buy);
        btn_Rate = findViewById(R.id.btn_Rate);
        btn_Rate.setVisibility(View.GONE);

        btnBackward = findViewById(R.id.btnBackward);
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    private void LoadData(Order ho)
    {
        switch (ho.getOrderStatus()){
            case "Completed":
                tv_statusOrder.setText("Đơn hàng đã được giao thành công");
                break;
            case "UnProcessed":
                tv_statusOrder.setText("Đơn hàng đang chờ được xử lý");
                break;
            case "Processing":
                tv_statusOrder.setText("Đơn hàng đang được vận chuyển");
                break;
            case "Cancelled":
                tv_statusOrder.setText("Đơn hàng đã hủy");
                break;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(FirebaseAuth.getInstance().getUid())
                .child("Customer")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("Notifications").exists()){
                            for(DataSnapshot noti : snapshot.child("Notifications").getChildren()){
                                if(noti.child("orderId").getValue(String.class).equals(ho.getOrderId())){
                                    tv_DateSuccess.setText(noti.child("dateNotifi").getValue(String.class));
                                }
                            }
                        }
                        else{
                            tv_DateSuccess.setText(ho.getOrderedDate());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        item_order_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailActivity.this, ShopActivityCustomer.class);
                intent.putExtra("shopId", ho.getShopId());
                mActivityLauncher.launch(intent);
            }
        });
        address_name.setText(ho.getReceiveAddress().getFullName());
        address_phone.setText(ho.getReceiveAddress().getPhoneNumber());
        address_detail.setText(ho.getReceiveAddress().getDetail());
        address_main.setText(ho.getReceiveAddress().GetAddressString());

        Glide.with(getApplicationContext()).load(ho.getShopAvt()).into(iv_ShopAvatar);
        HistoryProductsInOrderAdapter historyOrdersAdapter = new HistoryProductsInOrderAdapter(this, ho.getItems());
        rv_ProductList.setAdapter(historyOrdersAdapter);
        tv_ShopName.setText(ho.getShopName());
        String price;
         price = Constants.convertToVND(ho.getTotalPrice() - ho.getDiscountPrice() - ho.getShipPrice());
        tv_SumMoney.setText(price);
        price = Constants.convertToVND(ho.getDiscountPrice());
        tv_DiscountPrice.setText(price);
        price = Constants.convertToVND(ho.getShipPrice());
        tv_DeliveryPrice.setText(price);
        price = Constants.convertToVND(ho.getTotalPrice());
        tv_TotalPrice.setText(price);

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailActivity.this, CartActivity.class);
                intent.putExtra("HistoryOrder", ho);
                intent.putExtra("Key", HistoryOrdersFragment.HISTORY_ORDER);
                mActivityLauncher.launch(intent);
            }
        });
    }
}
