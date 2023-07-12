package com.example.ecommerceshop.qui.shop;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.databinding.AdapterItemVoucherShopBinding;
import com.example.ecommerceshop.qui.payment.Voucher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class VoucherShopAdapter extends RecyclerView.Adapter<VoucherShopAdapter.VoucherShopViewHolder> {

    private List<Voucher> mList;

    public void setData(List<Voucher> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public interface ICickListener {
        void clickSaveVoucher(Voucher voucher);
    }

    private ICickListener iCickListener;

    public void setiCickListener(ICickListener iCickListener) {
        this.iCickListener = iCickListener;
    }

    @NonNull
    @Override
    public VoucherShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterItemVoucherShopBinding mBinding = AdapterItemVoucherShopBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VoucherShopViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherShopViewHolder holder, int position) {
        Voucher voucher = mList.get(position);
        if (voucher != null) {
            holder.mBinding.codeVoucherShop.setText(voucher.getVouchercode());
            holder.mBinding.voucherDes.setText(voucher.getVoucherdes());
            holder.mBinding.voucherExpired.setText(voucher.getExpiredDate());
            FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + mCurrentUser.getUid() + "/Customer/Vouchers");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Voucher voucher1 = dataSnapshot.getValue(Voucher.class);
                        String voucherId = voucher1.getVoucherid();

                        if (voucher.getVoucherid().equals(voucherId) && voucher1.isUsed()==false) {
                            holder.mBinding.btnSave.setVisibility(View.GONE);
                            holder.mBinding.tvFollowed.setVisibility(View.VISIBLE);
                        }
                        else {
                            holder.mBinding.tvFollowed.setVisibility(View.GONE);
                            holder.mBinding.btnSave.setVisibility(View.VISIBLE);

                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            holder.mBinding.btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iCickListener.clickSaveVoucher(voucher);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public class VoucherShopViewHolder extends RecyclerView.ViewHolder {

        AdapterItemVoucherShopBinding mBinding;

        public VoucherShopViewHolder(@NonNull AdapterItemVoucherShopBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
