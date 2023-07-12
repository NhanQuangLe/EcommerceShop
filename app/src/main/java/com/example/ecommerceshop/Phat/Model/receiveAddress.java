package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;

public class receiveAddress implements Serializable {
    String addressId, detail, district, fullName, phoneNumber, province, ward;

    public receiveAddress() {
    }

    public receiveAddress(String addressId, String detail, String district, String fullName, String phoneNumber, String province, String ward) {
        this.addressId = addressId;
        this.detail = detail;
        this.district = district;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.ward = ward;
    }
    public String getAddress(){
        return this.detail + " ," + this.ward + " ," + this.district + " ," + this.province+".";
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
}
