package com.example.ecommerceshop.qui.payment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.FragmentVoucherBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class VoucherFragment extends Fragment implements VoucherCustomerAdapter.ICheckedChangeListener {
    public static final String TAG = VoucherFragment.class.getName();
    private PaymentActivity mPaymentActivity;
    private FragmentVoucherBinding mFragmentVoucherBinding;
    private View mView;
    private List<Voucher> mListVoucher;
    private VoucherCustomerAdapter mVoucherCustomerAdapter;
    private FirebaseUser mCurrentUser;
    private List<Voucher> mListSelectedVoucher;
    private List<Voucher> listVoucherFromAdapter;
    private long money;
    private String shopId;
    private ISenData iSenData;

    public void setiSenData(ISenData iSenData) {
        this.iSenData = iSenData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentVoucherBinding = FragmentVoucherBinding.inflate(inflater, container, false);
        mView = mFragmentVoucherBinding.getRoot();
        init();
        iListener();
        return mView;
    }

    private void init() {
        mPaymentActivity = (PaymentActivity) getActivity();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mListVoucher = new ArrayList<>();
        mListSelectedVoucher = new ArrayList<>();
//        listVoucherFromAdapter = new ArrayList<>();
        mVoucherCustomerAdapter = new VoucherCustomerAdapter(this);
        mVoucherCustomerAdapter.setData(mListVoucher);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mFragmentVoucherBinding.rcvVoucher.setLayoutManager(linearLayoutManager);
        mFragmentVoucherBinding.rcvVoucher.setAdapter(mVoucherCustomerAdapter);
        mPaymentActivity = (PaymentActivity) getActivity();
        // Lấy voucher
        setListVoucher();


    }

    private void setListVoucher() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + mCurrentUser.getUid() + "/Customer/Vouchers");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListVoucher != null) mListVoucher.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Voucher voucher = dataSnapshot.getValue(Voucher.class);
                    if (voucher != null) {

                        //check minimum
                        if (voucher.getMinimumPrice() <= money && voucher.getShopId().equals(shopId) && !voucher.getShopId().equals(mCurrentUser.getUid()))
                            voucher.setCanUse(true);
                        else voucher.setCanUse(false);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date dateExpired = new Date();
                        try {
                            dateExpired = dateFormat.parse(voucher.getExpiredDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date now = new Date();
                        if (checkExpired(dateExpired, now) && voucher.getQuantity() > 0) {
                            Log.e("size",listVoucherFromAdapter.size()+"");
                            List<String> voucherIds = new ArrayList<>();
                            for (Voucher voucherTemp : listVoucherFromAdapter){
                                voucherIds.add(voucherTemp.getVoucherid());
                            }
                            if (voucherIds.contains(voucher.getVoucherid())){
                                voucher.setCheck(true);
                                sendStatus(voucher.isCheck(),voucher);
                            }
                            mListVoucher.add(voucher);

                        }

                    }
                }
                mVoucherCustomerAdapter.notifyDataSetChanged();
                if (mListVoucher == null || mListVoucher.size() == 0) {
                    mFragmentVoucherBinding.tvEmpty.setVisibility(View.VISIBLE);
                    mFragmentVoucherBinding.listVoucher.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void receiveDataFromAdapter(long money, String shopId, List<Voucher> vouchers) {
        listVoucherFromAdapter = vouchers;
        this.money = money;
        this.shopId = shopId;


    }

    private void setCheckBox(List<Voucher> vouchers) {


    }

    @Override
    public void sendStatus(boolean b, Voucher voucher) {
        if (b) {
            mListSelectedVoucher.add(voucher);
            mFragmentVoucherBinding.tvQuantityChoose.setText(mListSelectedVoucher.size() + "");
        } else {
            mListSelectedVoucher.remove(voucher);
            mFragmentVoucherBinding.tvQuantityChoose.setText(mListSelectedVoucher.size() + "");
        }
    }

    private void iListener() {
        mFragmentVoucherBinding.buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSenData.senDataToAdapter(mListSelectedVoucher);
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }

            }
        });
        mFragmentVoucherBinding.btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });
    }

    public boolean checkExpired(Date d1, Date d2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(d1);
        calendar2.setTime(d2);
        int year1 = calendar1.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);
        int day1 = calendar1.get(Calendar.DAY_OF_MONTH);

        int year2 = calendar2.get(Calendar.YEAR);
        int month2 = calendar2.get(Calendar.MONTH);
        int day2 = calendar2.get(Calendar.DAY_OF_MONTH);

        if (year1 < year2) {
            return false;
        } else if (year1 > year2) {
            return true;
        } else {
            if (month1 < month2) {
                return false;
            } else if (month1 > month2) {
                return true;
            } else {
                if (day1 < day2) {
                    return false;
                } else if (day1 > day2) {
                    return true;
                } else {
                    return true;
                }
            }
        }
    }
}