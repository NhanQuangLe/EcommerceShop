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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrdersFragment;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryProductsInOrderAdapter;
import com.example.ecommerceshop.qui.cart.CartActivity;
import com.squareup.picasso.Picasso;

public class OrderDetailActivity extends AppCompatActivity {
    TextView address_name, address_phone, address_detail, address_main;

    ImageView iv_ShopAvatar;
    Button btnBackward;
    RecyclerView rv_ProductList;
    TextView tv_ShopName, tv_statusOrder, tv_DateSuccess;
    TextView tv_SumMoney, tv_DiscountPrice, tv_DeliveryPrice, tv_TotalPrice;
    LinearLayout btn_buy;
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
        Order ho = (Order) intent.getSerializableExtra("HistoryOrder");
        InitUI();
        LoadData(ho);
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

        btn_buy = findViewById(R.id.btn_buy);
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

        tv_statusOrder.setText("Đơn hàng đã được giao thành công");
        tv_DateSuccess.setText(ho.getOrderedDate());

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
