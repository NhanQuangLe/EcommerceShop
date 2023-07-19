package com.example.ecommerceshop.nhan.ProfileCustomer.vouchers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.qui.payment.Voucher;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class VoucherCustomerAdapter extends RecyclerView.Adapter<VoucherCustomerAdapter.VoucherCustomerViewHolder> {
    private Context context;
    private ArrayList<Voucher> listVouchers;

    public VoucherCustomerAdapter(Context context, ArrayList<Voucher> vouchers) {
        this.context = context;
        this.listVouchers = vouchers;
    }

    @NonNull
    @Override
    public VoucherCustomerAdapter.VoucherCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_voucher_customer, parent,false);
        return new VoucherCustomerAdapter.VoucherCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherCustomerAdapter.VoucherCustomerViewHolder holder, int position) {
            Voucher voucher = listVouchers.get(position);
            if(voucher.isUsed())
            {
                holder.ib_DeleteVoucher.setVisibility(View.GONE);
                holder.layout_voucher.setBackgroundColor(Color.parseColor("#D9D9D9"));
            }
            else
                holder.ib_DeleteVoucher.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.GONE);
            holder.ib_DeleteVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(FirebaseAuth.getInstance().getUid())
                            .child("Customer")
                            .child("Vouchers")
                            .child(voucher.getVoucherid())
                            .removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    CustomToast.makeText(context,"Remove voucher successfully!",CustomToast.SHORT,CustomToast.SUCCESS).show();

                                }
                            });
                }
            });
            holder.tv_voucher_code.setText(voucher.getVouchercode());
            holder.tv_voucher_des.setText(voucher.getVoucherdes());
            holder.tv_voucher_expired.setText(voucher.getExpiredDate());
            holder.discount_price.setText(getPrice(voucher.getDiscountPrice()));
    }
    public String getPrice(long money) {
        long res = money;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String str1 = currencyVN.format(res);
        return str1;
    }

    @Override
    public int getItemCount() {
            return listVouchers.size();
    }

    public static class VoucherCustomerViewHolder extends RecyclerView.ViewHolder{
        TextView tv_voucher_code, tv_voucher_des, tv_voucher_expired, discount_price;
        ImageButton ib_DeleteVoucher;
        LinearLayout layout_voucher;
        CheckBox checkbox;
        public VoucherCustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_voucher_code = itemView.findViewById(R.id.tv_voucher_code);
            tv_voucher_des = itemView.findViewById(R.id.tv_voucher_des);
            tv_voucher_expired = itemView.findViewById(R.id.tv_voucher_expired);
            ib_DeleteVoucher = itemView.findViewById(R.id.ib_DeleteVoucher);
            discount_price = itemView.findViewById(R.id.discount_price);
            layout_voucher = itemView.findViewById(R.id.layout_voucher);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
