package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;

public class RequestShop implements Serializable {
    String uid, shopAvt, shopName, shopDescription, shopEmail, shopPhone, shopAddress, timestamp;

    public RequestShop() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public RequestShop(String uid, String shopAvt, String shopName, String shopDescription, String shopEmail, String shopPhone, String shopAddress, String timestamp) {
        this.uid=uid;
        this.shopAvt = shopAvt;
        this.shopName = shopName;
        this.shopDescription = shopDescription;
        this.shopEmail = shopEmail;
        this.shopPhone = shopPhone;
        this.shopAddress = shopAddress;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String getShopAvt() {
        return shopAvt;
    }

    public void setShopAvt(String shopAvt) {
        this.shopAvt = shopAvt;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }
}
