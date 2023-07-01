package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.order_detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address.ChooseAddressActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrder;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryOrdersAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryProductsInOrderAdapter;
import com.squareup.picasso.Picasso;

public class OrderDetailActivity extends AppCompatActivity {
    TextView address_name, address_phone, address_detail, address_main;

    ImageView iv_ShopAvatar;
    RecyclerView rv_ProductList;
    TextView tv_ShopName;
    TextView tv_SumMoney, tv_DiscountPrice, tv_DeliveryPrice, tv_TotalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail_read_only);
        Intent intent = getIntent();

        HistoryOrder ho = (HistoryOrder) intent.getSerializableExtra("HistoryOrder");
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

        tv_SumMoney = findViewById(R.id.tv_SumMoney);
        tv_DiscountPrice = findViewById(R.id.tv_DiscountPrice);
        tv_DeliveryPrice = findViewById(R.id.tv_DeliveryPrice);
        tv_TotalPrice = findViewById(R.id.tv_TotalPrice);
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
    }
}
