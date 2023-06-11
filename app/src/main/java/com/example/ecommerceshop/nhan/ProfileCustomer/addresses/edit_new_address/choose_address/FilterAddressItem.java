package com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address;

import android.widget.Filter;

import com.example.ecommerceshop.nhan.Model.AddressItem;

import java.util.ArrayList;

public class FilterAddressItem extends Filter{
    private AddressAdapter adapterAddress;
    public ArrayList<AddressItem> addressItems;

    public FilterAddressItem (AddressAdapter adapterAddress, ArrayList<AddressItem> addresses) {
        this.adapterAddress = adapterAddress;
        this.addressItems = addresses;
    }

    @Override
    protected Filter.FilterResults performFiltering(CharSequence charSequence) {
        Filter.FilterResults filterResults = new Filter.FilterResults();
        if(charSequence !=null && charSequence.length()>0){
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<AddressItem> filterAddresses = new ArrayList<>();
            for(int i=0; i<addressItems.size();i++){
                if(addressItems.get(i).getFullName().toUpperCase().contains(charSequence)){
                    filterAddresses.add(addressItems.get(i));
                }
            }
            filterResults.count=filterAddresses.size();
            filterResults.values=filterAddresses;
        }
        else {
            filterResults.count=addressItems.size();
            filterResults.values=addressItems;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
        adapterAddress.listAddress = (ArrayList<AddressItem>)filterResults.values;
        adapterAddress.notifyDataSetChanged();
    }
}

