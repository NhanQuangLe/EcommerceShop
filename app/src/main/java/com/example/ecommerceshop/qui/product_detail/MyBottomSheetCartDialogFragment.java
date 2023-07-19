package com.example.ecommerceshop.qui.product_detail;

import android.app.Dialog;
import android.os.Bundle;
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
import com.example.ecommerceshop.toast.CustomToast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class MyBottomSheetCartDialogFragment extends BottomSheetDialogFragment {
    private Product mProduct;
    private TextView tvQuantity;
    private ImageButton btnMinus, btnPlus;
    private Button btnAddCart;
    private ImageView icClose;
    private ImageView img;
    private String path;
    private long cur_quantity_firebase;
    private FirebaseUser mCurrentUser;

    public static MyBottomSheetCartDialogFragment newInstance(Product product){
        MyBottomSheetCartDialogFragment myBottomSheetCartDialogFragment = new MyBottomSheetCartDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product",product);
        myBottomSheetCartDialogFragment.setArguments(bundle);
        return myBottomSheetCartDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceive = getArguments();
        if (bundleReceive!=null){
            mProduct = (Product) bundleReceive.get("product");
        }
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet_cart_fragment,null);
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
                    CustomToast.makeText(getContext(),"Số lượng có sẵn không đủ đáp ứng!",CustomToast.SHORT,CustomToast.ERROR).show();

                    return;
                }
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path = "";
                cur_quantity_firebase=0;
                String quantityStr = tvQuantity.getText().toString();
                int quantity = Integer.parseInt(quantityStr);
                String key = String.valueOf((int) new Date().getTime());
                Cart cart = new Cart(key,mProduct.getProductId(),quantity,mProduct.getUid());
                DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/Cart");
                ref0.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if (mProduct.getProductId().equals(dataSnapshot.child("productId").getValue(String.class))){
                                path = dataSnapshot.getKey();
                                cur_quantity_firebase = dataSnapshot.child("productQuantity").getValue(Long.class);
                                break;
                            }
                        }
                        if (!path.equals("")){
                            long newQuantity = quantity+cur_quantity_firebase;
                            ref0.child(path).child("productQuantity").setValue(newQuantity, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();
                                }
                            });
                            return;
                        }
                        else {

                            ref0.child(cart.getCartId()).setValue(cart, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();
                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });

        return bottomSheetDialog;
    }



    private void initDialogView(View viewDialog) {
        tvQuantity = viewDialog.findViewById(R.id.cart_quantity);
        btnMinus = viewDialog.findViewById(R.id.btn_minus);
        btnPlus = viewDialog.findViewById(R.id.btn_plus);
        icClose = viewDialog.findViewById(R.id.ic_close);
        btnAddCart = viewDialog.findViewById(R.id.btn_add_cart);
        img = viewDialog.findViewById(R.id.img_product_add_cart);
    }
    private void setData() {
        if (mProduct==null) return;
        Glide.with(getContext()).load(mProduct.getUriList().get(0)).into(img);
        tvQuantity.setText("1");
    }
}
