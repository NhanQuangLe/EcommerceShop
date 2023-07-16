package com.example.ecommerceshop.qui.cart;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.AdapterItemOnCartBinding;
import com.example.ecommerceshop.qui.homeuser.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.ProductCartViewHolder>{

    private List<ProductCart> mList;
    private Context mContext;

    private IClickProductCartItemListener iClickProductCartItemListener;
    public void setData(List<ProductCart> list){
        this.mList=list;
        notifyDataSetChanged();
    }

    public ProductCartAdapter(Context mContext, IClickProductCartItemListener iClickProductCartItemListener) {
        this.mContext = mContext;
        this.iClickProductCartItemListener = iClickProductCartItemListener;
    }

    @NonNull
    @Override
    public ProductCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         AdapterItemOnCartBinding adapterItemOnCartBinding = AdapterItemOnCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ProductCartViewHolder(adapterItemOnCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCartViewHolder holder, int position) {
        ProductCart productCart = mList.get(position);
        if (productCart!=null){
            Glide.with(holder.mBinding.getRoot()).load(productCart.getUri()).into(holder.mBinding.productImage);
            holder.mBinding.productName.setText(productCart.getProductName());

            if (productCart.getProductDiscountPrice()==0){
                holder.mBinding.productPrice.setVisibility(View.GONE);
                holder.mBinding.productDiscountPrice.setText(productCart.getProductPriceStr());
            }
            else {
                holder.mBinding.productPrice.setText(productCart.getProductPriceStr());
                holder.mBinding.productPrice.setPaintFlags(holder.mBinding.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.mBinding.productDiscountPrice.setText(productCart.getProductAfterDiscountStr());
            }
            holder.mBinding.productQuantity.setText(String.valueOf(productCart.getProductQuantity()));
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+productCart.getShopId()+"/Shop/Products/"+productCart.getProductId());

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean isSold = snapshot.child("sold").getValue(Boolean.class);
                    holder.mBinding.checkBox.setChecked(false);
                    if (!isSold){
                        holder.mBinding.mainLayout.setClickable(false);
                        holder.mBinding.layoutChangeQuantity.setVisibility(View.GONE);
                        holder.mBinding.layoutNgungkinhdoanh.setVisibility(View.VISIBLE);
                    }
                    else {
                        holder.mBinding.mainLayout.setClickable(true);
                        holder.mBinding.layoutChangeQuantity.setVisibility(View.VISIBLE);
                        holder.mBinding.layoutNgungkinhdoanh.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if (productCart.isChecked()) holder.mBinding.checkBox.setChecked(true);
            else holder.mBinding.checkBox.setChecked(false);
            holder.mBinding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    iClickProductCartItemListener.sendParentAdapter(b,productCart);
                    if (!b){
                        iClickProductCartItemListener.checkAllCheckbox();
                    }
                }
            });
            holder.mBinding.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickProductCartItemListener.sendInfoProduct(productCart);
                }
            });
            holder.mBinding.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    increaseQuantity(productCart);
                }
            });
            holder.mBinding.btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    decreaseQuantity(productCart);
                }
            });

        }
    }

    private void decreaseQuantity(ProductCart productCart) {

        int quantity = productCart.getProductQuantity();
        if (quantity==1) {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_ok_cancel);
            Window window = dialog.getWindow();
            if (window==null) return;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;

            window.setAttributes(windowAttributes);
            dialog.setCancelable(true);
            TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
            TextView tvOk = dialog.findViewById(R.id.tv_ok);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/Customer/Cart/"+productCart.getProductCardId());
                    ref.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(mContext, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            });
            dialog.show();
        }
        else {
            quantity--;
            productCart.setProductQuantity(quantity);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/Customer/Cart/"+productCart.getProductCardId()+"/productQuantity");
            ref.setValue(quantity);

        }


    }

    private void increaseQuantity(ProductCart productCart) {
        String shopId = productCart.getShopId();
        String productId = productCart.getProductId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+shopId+"/Shop/Products/"+productId+"/productQuantity");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("name",snapshot.getRef().toString());
                int quantity = snapshot.getValue(Integer.class);
                if (productCart.getProductQuantity()==quantity){
                    Toast.makeText(mContext, "Số lượng sản phẩm có sẵn không đủ để đáp ứng", Toast.LENGTH_SHORT).show();
                }
                else {
                    int newQuantity = productCart.getProductQuantity()+1;
                    productCart.setProductQuantity(newQuantity);
                    Log.e("quantity",""+quantity);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/Customer/Cart/"+productCart.getProductCardId()+"/productQuantity");
                    ref.setValue(newQuantity);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//
    }

    @Override
    public int getItemCount() {
        if (mList!=null) return mList.size();
        return 0;
    }

    public class ProductCartViewHolder extends RecyclerView.ViewHolder {
        private AdapterItemOnCartBinding mBinding;
        public ProductCartViewHolder(@NonNull AdapterItemOnCartBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }

}
