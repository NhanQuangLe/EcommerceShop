package com.example.ecommerceshop.nhan.Model;

public class AddressItem {
    public String Code, FullName, HeadName, Name;

    public AddressItem() {
        HeadName = "";
    }

    public AddressItem(String code, String fullName, String headName) {
        Code = code;
        FullName = fullName;
        HeadName = headName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getHeadName() {
        return HeadName;
    }

    public void setHeadName(String headName) {
        HeadName = headName;
    }
}
