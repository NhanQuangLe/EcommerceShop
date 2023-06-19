package com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.Phat.Utils.FilterProduct;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.AddressItem;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> implements Filterable {
        private Context context;
        public ArrayList<AddressItem> listAddress, listAddressFilter;
        private FilterAddressItem AddressFilter;
        private IClickAddressItemListener mClickAddressItemListener;
        public AddressAdapter(Context context, ArrayList<AddressItem> addresses, IClickAddressItemListener listener) {
                this.context = context;
                this.listAddress = addresses;
                this.listAddressFilter = addresses;
                this.mClickAddressItemListener = listener;
        }

        @NonNull
        @Override
        public AddressAdapter.AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context).inflate(R.layout.adapter_province_item, parent,false);
                return new AddressAdapter.AddressViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AddressAdapter.AddressViewHolder holder, int position) {
                AddressItem address = listAddress.get(position);
                holder.tv_AddressName.setText(address.getFullName());
                holder.tv_HeaderAddress.setText(address.getHeadName());
                holder.ll_BoundaryAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                mClickAddressItemListener.ClickItem(address);
                        }
                });
        }
        @Override
        public int getItemCount() {
                return listAddress.size();
        }

        @Override
        public Filter getFilter() {
                if(AddressFilter == null){
                        AddressFilter = new FilterAddressItem(this, listAddressFilter);
                }
                return AddressFilter;
        }
        public static class AddressViewHolder extends RecyclerView.ViewHolder{
                TextView tv_HeaderAddress, tv_AddressName;
                LinearLayout ll_BoundaryAddress;
                public AddressViewHolder(@NonNull View itemView) {
                        super(itemView);
                        tv_HeaderAddress = itemView.findViewById(R.id.tv_HeaderAddress);
                        tv_AddressName = itemView.findViewById(R.id.tv_AddressName);
                        ll_BoundaryAddress =  itemView.findViewById(R.id.ll_BoundaryAddress);
                }
        }
}
