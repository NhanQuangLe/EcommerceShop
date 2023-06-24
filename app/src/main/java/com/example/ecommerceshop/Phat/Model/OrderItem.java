package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;

public class OrderItem implements Serializable {
    String pName, pid, pAvatar, pBrand;
    int pQuantity;
    long pPrice;

    public OrderItem() {
    }

    public OrderItem(String pName, long pPrice, String pid, String pAvatar, String pBrand, int pQuantity) {
        this.pName = pName;
        this.pPrice = pPrice;
        this.pid = pid;
        this.pAvatar = pAvatar;
        this.pBrand = pBrand;
        this.pQuantity = pQuantity;
    }

    public String getpAvatar() {
        return pAvatar;
    }

    public void setpAvatar(String pAvatar) {
        this.pAvatar = pAvatar;
    }

    public String getpBrand() {
        return pBrand;
    }

    public void setpBrand(String pBrand) {
        this.pBrand = pBrand;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public long getpPrice() {
        return pPrice;
    }

    public void setpPrice(long pPrice) {
        this.pPrice = pPrice;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getpQuantity() {
        return pQuantity;
    }

    public void setpQuantity(int pQuantity) {
        this.pQuantity = pQuantity;
    }
}
