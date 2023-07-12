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
    private Voucher voucherFromItemPayment;
    private Voucher voucherSelected;
    private Voucher voucherPrevious;
    private long money;
    private String shopId;
    private ISenData iSenData;
    boolean isCheck=false;

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
                        Log.e("shopId",shopId);
                        //check minimum
                        if (voucher.getMinimumPrice() <= money
                                && voucher.getShopId().equals(shopId)
                                && !voucher.getShopId().equals(mCurrentUser.getUid())
                                && voucher.isUsed()==false)
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

                            if (voucherFromItemPayment!=null){
                                if (voucher.getVoucherid().equals(voucherFromItemPayment.getVoucherid())){
                                    voucher.setCheck(true);
                                    mFragmentVoucherBinding.tvQuantityChoose.setText("1");
                                }
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

    public void receiveDataFromAdapter(long money, String shopId, Voucher voucher) {
        this.voucherFromItemPayment = voucher;
        this.voucherPrevious = voucher;
        this.money = money;
        this.shopId = shopId;


    }

    private void setCheckBox(List<Voucher> vouchers) {


    }

    @Override
    public void sendStatus(boolean b, Voucher voucher) {
        if (b) {
            isCheck = true;
            voucherSelected = voucher;
            voucherPrevious = voucherSelected;
            mFragmentVoucherBinding.tvQuantityChoose.setText("1");
            setUnCheckOtherVoucher(voucher);
        } else {
            if (voucherPrevious!=null){
                if (voucher.getVoucherid().equals(voucherPrevious.getVoucherid())){
                    voucherSelected = null;
                    mFragmentVoucherBinding.tvQuantityChoose.setText("0");
                }
            }

        }
    }

    private void setUnCheckOtherVoucher(Voucher voucher) {
        List<Voucher> vouchersTemp = new ArrayList<>(mListVoucher);
        mListVoucher.clear();
        for (Voucher voucher1:vouchersTemp){
            if (voucher1.getVoucherid().equals(voucher.getVoucherid())) {
                voucher1.setCheck(true);
            }
            else {
                voucher1.setCheck(false);
            }
            mListVoucher.add(voucher1);

        }
    mVoucherCustomerAdapter.notifyDataSetChanged();


    }

    private void iListener() {
        mFragmentVoucherBinding.buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSenData.senDataToAdapter(voucherSelected);
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