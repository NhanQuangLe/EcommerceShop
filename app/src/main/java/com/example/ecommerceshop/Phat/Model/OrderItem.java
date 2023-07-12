package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;

public class OrderItem implements Serializable {
    String pName, pid, pAvatar, pBrand, pCategory;
    int pQuantity;
    long pPrice;

    public OrderItem() {
    }

    public OrderItem(String pName, String pid, String pAvatar, String pBrand, String pCategory, int pQuantity, long pPrice) {
        this.pName = pName;
        this.pid = pid;
        this.pAvatar = pAvatar;
        this.pBrand = pBrand;
        this.pCategory = pCategory;
        this.pQuantity = pQuantity;
        this.pPrice = pPrice;
    }

    public String getpCategory() {
        return pCategory;
    }

    public void setpCategory(String pCategory) {
        this.pCategory = pCategory;
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
