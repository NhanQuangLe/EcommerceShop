package com.example.ecommerceshop.qui.product_detail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.qui.homeuser.Product;
import com.example.ecommerceshop.qui.payment.PaymentActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MyBottmSheetBuySingleProductDialogFragment extends BottomSheetDialogFragment {
    private Product mProduct;
    private TextView tvQuantity;
    private ImageButton btnMinus, btnPlus;
    private Button btnPayment;
    private ImageView icClose;
    private ImageView img;
    private String path;
    private long cur_quantity_firebase;
    private FirebaseUser mCurrentUser;
    private ProductDetailActivity mActivity;

    public static MyBottmSheetBuySingleProductDialogFragment newInstance(Product product){
        MyBottmSheetBuySingleProductDialogFragment myBottmSheetBuySingleProductDialogFragment = new MyBottmSheetBuySingleProductDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product",product);
        myBottmSheetBuySingleProductDialogFragment.setArguments(bundle);
        return myBottmSheetBuySingleProductDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceive = getArguments();
        if (bundleReceive!=null){
            mProduct = (Product) bundleReceive.get("product");
        }
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mActivity = (ProductDetailActivity) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottm_sheet_buy_single_product_fragment,null);
        bottomSheetDialog.setContentView(viewDialog);
        initDialogView(viewDialog);
        setData();
        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityStr = tvQuantity.getText().toString();
                int quantity = Integer.parseInt(quantityStr);
                if (quantity==1) return;
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityStr = tvQuantity.getText().toString();
                int quantity = Integer.parseInt(quantityStr);
                if (quantity==mProduct.getProductQuantity()){
                    Toast.makeText(getContext(), "Số lượng có sẵn không đủ đáp ứng!", Toast.LENGTH_SHORT).show();
                    return;
                }
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path = "";
                cur_quantity_firebase=0;
                String quantityStr = tvQuantity.getText().toString();
                int quantity = Integer.parseInt(quantityStr);
                Intent intent = new Intent(mActivity, PaymentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("clickType","fromProductDetail");
                bundle.putInt("quantity",quantity);
                bundle.putSerializable("product",mProduct);
                intent.putExtras(bundle);
                mActivity.startActivity(intent);




            }
        });

        return bottomSheetDialog;
    }



    private void initDialogView(View viewDialog) {
        tvQuantity = viewDialog.findViewById(R.id.cart_quantity);
        btnMinus = viewDialog.findViewById(R.id.btn_minus);
        btnPlus = viewDialog.findViewById(R.id.btn_plus);
        icClose = viewDialog.findViewById(R.id.ic_close);
        btnPayment = viewDialog.findViewById(R.id.btn_payment);
        img = viewDialog.findViewById(R.id.img_product_add_cart);
    }
    private void setData() {
        if (mProduct==null) return;
        Glide.with(getContext()).load(mProduct.getUriList().get(0)).into(img);
        tvQuantity.setText("1");
    }
}
