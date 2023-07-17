package com.example.ecommerceshop.nhan.Model;

import java.io.Serializable;

public class Address implements Serializable {
    private String addressId, detail, district, fullName, phoneNumber, province, ward;
    private boolean isDefault;
    private Double latitude, longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Address(){}

    public Address(String addressId, String detail, String district, String fullName, String phoneNumber, String province, String ward, boolean isDefault) {
        this.addressId = addressId;
        this.detail = detail;
        this.district = district;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.ward = ward;
        this.isDefault = isDefault;
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String GetAddressString()
    {
        return ward + ", " + district + ", " + province;
    }
    public String GetWardRemoveHeader(){
        String result = "";
        String[] a = ward.split(" ");
        for(int i = 1; i < a.length; i++){
            result += a[i];
            if(i != a.length - 1)
                result += " ";
        }
        return result;
    }
    public String GetDistrictRemoveHeader(){
        String result = "";
        String[] a = district.split(" ");
        for(int i = 1; i < a.length; i++){
            result += a[i];
            if(i != a.length - 1)
                result += " ";
        }
        return result;
    }
}
