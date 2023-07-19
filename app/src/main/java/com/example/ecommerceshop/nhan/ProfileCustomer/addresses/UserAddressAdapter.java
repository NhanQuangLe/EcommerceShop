package com.example.ecommerceshop.nhan.ProfileCustomer.addresses;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Address;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserAddressAdapter extends RecyclerView.Adapter<UserAddressAdapter.UserAddressViewHolder> {

    private Context context;
    private ArrayList<Address> listAddress;
    private FirebaseAuth firebaseAuth;
    private IClickAddressListener addressListener;
    public UserAddressAdapter(Context context, ArrayList<Address> addresses, IClickAddressListener listener) {
        this.context = context;
        this.listAddress = addresses;
        this.addressListener = listener;
    }
    public void setFirebaseAuth(FirebaseAuth fbA)
    {
        this.firebaseAuth = fbA;
    }

    @NonNull
    @Override
    public UserAddressAdapter.UserAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_address_customer, parent,false);
        return new UserAddressAdapter.UserAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAddressAdapter.UserAddressViewHolder holder, int position) {
        Address address = listAddress.get(position);

        holder.tv_FullName.setText(address.getFullName());
        holder.tv_PhoneNumber.setText(address.getPhoneNumber());
        holder.tv_InfoDetail.setText(address.getDetail());
        holder.tv_MainAddress.setText(address.GetAddressString());
        if(address.isDefault())
            holder.tv_DeFaultAddresss.setHeight(80);
        else
            holder.tv_DeFaultAddresss.setHeight(0);
        holder.btn_EditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressListener.EditAddress(address);
            }
        });
        holder.layout_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressListener.ReturnAddressForPayment(address);
            }
        });
    }
    @Override
    public int getItemCount() {
        return listAddress.size();
    }

    public static class UserAddressViewHolder extends RecyclerView.ViewHolder{

        TextView tv_FullName, tv_PhoneNumber, tv_InfoDetail, tv_MainAddress, tv_DeFaultAddresss;
        TextView btn_EditAddress;
        LinearLayout layout_address;
        public UserAddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_FullName = itemView.findViewById(R.id.tv_FullName);
            tv_PhoneNumber = itemView.findViewById(R.id.tv_PhoneNumber);
            tv_InfoDetail = itemView.findViewById(R.id.tv_InfoDetail);
            tv_MainAddress = itemView.findViewById(R.id.tv_MainAddress);
            tv_DeFaultAddresss = itemView.findViewById(R.id.tv_DeFaultAddresss);
            btn_EditAddress = itemView.findViewById(R.id.btn_EditAddress);
            layout_address = itemView.findViewById(R.id.layout_address);
        }
    }
}
