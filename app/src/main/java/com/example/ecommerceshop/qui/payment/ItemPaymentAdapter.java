package com.example.ecommerceshop.qui.payment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.AdapterShopListItemPaymentBinding;
import com.example.ecommerceshop.databinding.AdapterShopListProductPaymentBinding;
import com.example.ecommerceshop.qui.cart.ProductCart;

import java.text.NumberFormat;
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
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            holder.adapterShopListProductPaymentBinding.rcvProductCart.setLayoutManager(linearLayoutManager);
            ProductCartAdapter2 productCartAdapter2 = new ProductCartAdapter2();
            productCartAdapter2.setData(itemPayment.getListProductCart());
            holder.adapterShopListProductPaymentBinding.rcvProductCart.setAdapter(productCartAdapter2);
            long tongTienHang = 0;
            for (ProductCart productCart : itemPayment.getListProductCart()) {
                if (productCart.getProductDiscountPrice() == 0)
                    tongTienHang += productCart.getProductPrice() * productCart.getProductQuantity();
                else
                    tongTienHang += productCart.getProductDiscountPrice() * productCart.getProductQuantity();
            }
            holder.adapterShopListProductPaymentBinding.tvTongTienHang.setText(getPrice(tongTienHang));
            long finalTongTienHang = tongTienHang;
            holder.adapterShopListProductPaymentBinding.tvChooseVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final long[] tienKhuyenMai = {0};
                    VoucherFragment voucherFragment = new VoucherFragment();
                    voucherFragment.receiveDataFromAdapter(finalTongTienHang);
                    voucherFragment.setiSenData(new ISenData() {
                        @Override
                        public void senDataToAdapter(List<Voucher> vouchers) {
                            for (Voucher voucher : vouchers) {
                                tienKhuyenMai[0] += voucher.getDiscountPrice();
                            }
                            itemPayment.setTienKhuyenMai(tienKhuyenMai[0]);
                            notifyDataSetChanged();
                            holder.adapterShopListProductPaymentBinding.tvKhuyenmai.setText(getPrice(tienKhuyenMai[0]));
                            Log.e("Tha",holder.adapterShopListProductPaymentBinding.tvKhuyenmai.getText().toString());
                        }


                    });
                    ((PaymentActivity) mContext).replaceFragment(voucherFragment);
                }


            });
            holder.adapterShopListProductPaymentBinding.shopName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.adapterShopListProductPaymentBinding.shopName.setText("qqqq");
                }
            });
        }


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
