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

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.FragmentVoucherBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class VoucherFragment extends Fragment implements VoucherCustomerAdapter.ICheckedChangeListener {
    public static final String TAG =VoucherFragment.class.getName();
    private PaymentActivity mPaymentActivity;
    private FragmentVoucherBinding mFragmentVoucherBinding;
    private View mView;
    private List<Voucher> mListVoucher;
    private VoucherCustomerAdapter mVoucherCustomerAdapter;
    private FirebaseUser mCurrentUser;
    private List<Voucher> mListSelectedVoucher;
    private long money;
    private ISenData iSenData;
    public void setiSenData(ISenData iSenData) {
        this.iSenData = iSenData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentVoucherBinding = FragmentVoucherBinding.inflate(inflater,container,false);
        mView= mFragmentVoucherBinding.getRoot();
        init();
        iListener();
        return mView;
    }

    private void init() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mListVoucher = new ArrayList<>();
        mListSelectedVoucher = new ArrayList<>();
        mVoucherCustomerAdapter = new VoucherCustomerAdapter(this);
        mVoucherCustomerAdapter.setData(mListVoucher);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        mFragmentVoucherBinding.rcvVoucher.setLayoutManager(linearLayoutManager);
        mFragmentVoucherBinding.rcvVoucher.setAdapter(mVoucherCustomerAdapter);
        mPaymentActivity = (PaymentActivity) getActivity();
        // Lấy voucher
        setListVoucher();


    }
    private void setListVoucher() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/Vouchers");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListVoucher!=null) mListVoucher.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Voucher voucher = dataSnapshot.getValue(Voucher.class);
                    if (voucher!=null){
                        //check minimum
                        if (voucher.getMinimumPrice()<=money) voucher.setCanUse(true);
                        else voucher.setCanUse(false);
                        mListVoucher.add(voucher);

                    }
                }
                mVoucherCustomerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

   public void receiveDataFromAdapter(long money){
        this.money=money;
   }

    @Override
    public void sendStatus(boolean b, Voucher voucher) {
        if (b){
            mListSelectedVoucher.add(voucher);
            mFragmentVoucherBinding.tvQuantityChoose.setText(mListSelectedVoucher.size()+"");
        }
        else {
            mListSelectedVoucher.remove(voucher);
            mFragmentVoucherBinding.tvQuantityChoose.setText(mListSelectedVoucher.size()+"");
        }
    }
    private void iListener() {
         mFragmentVoucherBinding.buttonAccept.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 iSenData.senDataToAdapter(mListSelectedVoucher);
                 if (getParentFragmentManager()!=null){
                     getParentFragmentManager().popBackStack();
                 }

              }
         });
    }
}