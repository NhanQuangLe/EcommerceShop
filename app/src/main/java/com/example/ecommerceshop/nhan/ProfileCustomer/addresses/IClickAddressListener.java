package com.example.ecommerceshop.nhan.ProfileCustomer.addresses;

import com.example.ecommerceshop.nhan.Model.Address;

public interface IClickAddressListener {
    void EditAddress(Address address);
    void ReturnAddressForPayment(Address address);
}
