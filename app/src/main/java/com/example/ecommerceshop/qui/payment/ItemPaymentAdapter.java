package com.example.ecommerceshop.qui.payment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.databinding.AdapterShopListProductPaymentBinding;
import com.example.ecommerceshop.nhan.Model.Address;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemPaymentAdapter extends RecyclerView.Adapter<ItemPaymentAdapter.ItemPaymentViewHolder> {

    private List<ItemPayment> mListItemPayment;

    private Context mContext;

    private ISenData iSendData;



    public void setiSendData(ISenData iSendData) {
        this.iSendData = iSendData;
    }

    public ItemPaymentAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ItemPayment> list) {
        this.mListItemPayment = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterShopListProductPaymentBinding adapterShopListProductPaymentBinding = AdapterShopListProductPaymentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemPaymentViewHolder(adapterShopListProductPaymentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPaymentViewHolder holder, int position) {
        ItemPayment itemPayment = mListItemPayment.get(position);
        if (itemPayment != null) {
            holder.adapterShopListProductPaymentBinding.shopName.setText(itemPayment.getShopName());
            holder.adapterShopListProductPaymentBinding.tvKhuyenmai.setText(getPrice(itemPayment.getTienKhuyenMai()));
            holder.adapterShopListProductPaymentBinding.tvTienVanChuyen.setText(getPrice(itemPayment.getTienVanChuyen()));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            holder.adapterShopListProductPaymentBinding.rcvProductCart.setLayoutManager(linearLayoutManager);
            ProductCartAdapter2 productCartAdapter2 = new ProductCartAdapter2();
            productCartAdapter2.setData(itemPayment.getListProductCart());
            holder.adapterShopListProductPaymentBinding.rcvProductCart.setAdapter(productCartAdapter2);
            holder.adapterShopListProductPaymentBinding.tvTongThanhToan.setText(getPrice(itemPayment.getTongThanhToan()));
            holder.adapterShopListProductPaymentBinding.tvTongTienHang.setText(getPrice(itemPayment.getTongTienHang()));
            holder.adapterShopListProductPaymentBinding.tvChooseVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    VoucherFragment voucherFragment = new VoucherFragment();
                    voucherFragment.receiveDataFromAdapter(itemPayment.getTongTienHang(), itemPayment.getShopId(), itemPayment.getVoucher());
                    ((PaymentActivity) mContext).replaceFragment(voucherFragment);
                    voucherFragment.setiSenData(new ISenData() {
                        @Override
                        public void senDataToAdapter(Voucher voucher) {
                            if (itemPayment.getTienKhuyenMai() != 0) {
                                itemPayment.setTienKhuyenMai(0);
                                itemPayment.setVoucher(null);
                                itemPayment.setTongThanhToan(itemPayment.getTongTienHang()+itemPayment.getTienVanChuyen());
                                holder.adapterShopListProductPaymentBinding.tvChooseVoucher.setText("Chọn hoặc nhập mã");
                                holder.adapterShopListProductPaymentBinding.tvKhuyenmai.setText(getPrice(itemPayment.getTienKhuyenMai()));
                                String tmp = getPrice(itemPayment.getTongThanhToan());
                                holder.adapterShopListProductPaymentBinding.tvTongThanhToan.setText(tmp);
                            }

                            if (voucher != null) {
                                itemPayment.setTienKhuyenMai(itemPayment.getTienKhuyenMai() + voucher.getDiscountPrice());
                                itemPayment.setVoucher(voucher);
                            }

                            if (itemPayment.getTienKhuyenMai() != 0) {
                                holder.adapterShopListProductPaymentBinding.tvChooseVoucher.setText("Đã chọn");
                                holder.adapterShopListProductPaymentBinding.tvKhuyenmai.setText(getPrice(itemPayment.getTienKhuyenMai()));
                                String tmp = getPrice(itemPayment.getTongThanhToan());
                                holder.adapterShopListProductPaymentBinding.tvTongThanhToan.setText(tmp);

                            }
                            iSendData.senDataToPaymentActivity(mListItemPayment);
                        }

                        @Override
                        public void senDataToPaymentActivity(List<ItemPayment> itemPaymentList) {

                        }
                    });
                }


            });
        }


    }

    private void resetValue(ItemPayment itemPayment) {

    }

    @Override
    public int getItemCount() {
        if (mListItemPayment != null) return mListItemPayment.size();
        return 0;
    }

    public class ItemPaymentViewHolder extends RecyclerView.ViewHolder {
        AdapterShopListProductPaymentBinding adapterShopListProductPaymentBinding;

        public ItemPaymentViewHolder(@NonNull AdapterShopListProductPaymentBinding adapterShopListProductPaymentBinding) {
            super(adapterShopListProductPaymentBinding.getRoot());
            this.adapterShopListProductPaymentBinding = adapterShopListProductPaymentBinding;
        }
    }

    public String getPrice(long money) {
        long res = money;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String str1 = currencyVN.format(res);
        return str1;
    }
}
