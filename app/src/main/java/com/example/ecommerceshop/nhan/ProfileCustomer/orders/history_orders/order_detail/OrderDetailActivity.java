package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.order_detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Address;
import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address.ChooseAddressActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrder;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrdersAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrdersFragment;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryProductsInOrderAdapter;
import com.example.ecommerceshop.qui.cart.CartActivity;
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
    RecyclerView rv_ProductList;
    TextView tv_ShopName;
    TextView tv_SumMoney, tv_DiscountPrice, tv_DeliveryPrice, tv_TotalPrice;
    LinearLayout btn_buy;
    HistoryOrder ho = new HistoryOrder();
    FirebaseAuth firebaseAuth;
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
        InitUI();
        if(intent.getStringExtra("orderId")!=null){
            loadHO(intent.getStringExtra("orderId"));
        }
        else{
            ho = (HistoryOrder) intent.getSerializableExtra("HistoryOrder");
            LoadData(ho);
        }


    }

    private void loadHO(String orderId) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");

        dbReference.child(firebaseAuth.getUid())
                .child("Customer")
                .child("Orders")
                .child(orderId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ho = new HistoryOrder();
                        ho.setOrderId(snapshot.child("orderId").getValue(String.class));
                        ho.setCustomerId(snapshot.child("customerId").getValue(String.class));
                        dbReference.child(snapshot.child("shopId").getValue(String.class))
                                .child("Shop")
                                .child("ShopInfos").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                        ho.setShopAvt(snapshot1.child("shopAvt").getValue(String.class));
                                        ho.setShopName(snapshot1.child("shopName").getValue(String.class));
                                        ho.setShopId(snapshot.child("shopId").getValue(String.class));
                                        ho.setShipPrice(snapshot.child("shipPrice").getValue(int.class));
                                        ho.setDiscountPrice(snapshot.child("discountPrice").getValue(int.class));
                                        ho.setOrderStatus(snapshot.child("orderStatus").getValue(String.class));
                                        ho.setTotalPrice(snapshot.child("totalPrice").getValue(int.class));
                                        ho.setOrderedDate(snapshot.child("orderDate").getValue(String.class));
                                        ho.setReceiveAddress(snapshot.child("receiveAddress").getValue(Address.class));
                                        ho.setShipPrice(snapshot.child("shipPrice").getValue(int.class));
                                        ArrayList<Product> products = new ArrayList<>();
                                        for(DataSnapshot product : snapshot.child("items").getChildren())
                                        {
                                            Product pd = new Product();
                                            pd.setProductID(product.child("pid").getValue(String.class));
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
                                        Toast.makeText(getApplicationContext(), error.getMessage() + "", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage() + "", Toast.LENGTH_SHORT).show();
                    }
                });
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

        tv_SumMoney = findViewById(R.id.tv_SumMoney);
        tv_DiscountPrice = findViewById(R.id.tv_DiscountPrice);
        tv_DeliveryPrice = findViewById(R.id.tv_DeliveryPrice);
        tv_TotalPrice = findViewById(R.id.tv_TotalPrice);

        btn_buy = findViewById(R.id.btn_buy);
        firebaseAuth=FirebaseAuth.getInstance();
    }
    private void LoadData(HistoryOrder ho)
    {
        address_name.setText(ho.getReceiveAddress().getFullName());
        address_phone.setText(ho.getReceiveAddress().getPhoneNumber());
        address_detail.setText(ho.getReceiveAddress().getDetail());
        address_main.setText(ho.getReceiveAddress().GetAddressString());

        Picasso.get().load(Uri.parse(ho.getShopAvt())).into(iv_ShopAvatar);
        HistoryProductsInOrderAdapter historyOrdersAdapter = new HistoryProductsInOrderAdapter(this, ho.getItems());
        rv_ProductList.setAdapter(historyOrdersAdapter);
        tv_ShopName.setText(ho.getShopName());

        tv_SumMoney.setText(String.valueOf(ho.getTotalPrice() - ho.getDiscountPrice() - ho.getShipPrice()));
        tv_DiscountPrice.setText(String.valueOf(ho.getDiscountPrice()));
        tv_DeliveryPrice.setText(String.valueOf(ho.getShipPrice()));
        tv_TotalPrice.setText(String.valueOf(ho.getTotalPrice()));

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
