package com.example.ecommerceshop.qui.payment;

public class ItemOrder {
    private String pid;
    private String pAvatar;
    private String pBrand;
    private String pName;
    private long pPrice;
    private long pQuantity;
    private String pCategory;

    public String getpCategory() {
        return pCategory;
    }

    public void setpCategory(String pCategory) {
        this.pCategory = pCategory;
    }

    public ItemOrder() {
    }

    public ItemOrder(String pid, String pAvatar, String pBrand, String pName, long pPrice, long pQuantity, String pCategory) {
        this.pid = pid;
        this.pAvatar = pAvatar;
        this.pBrand = pBrand;
        this.pName = pName;
        this.pPrice = pPrice;
        this.pQuantity = pQuantity;
        this.pCategory = pCategory;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public long getpQuantity() {
        return pQuantity;
    }

    public void setpQuantity(long pQuantity) {
        this.pQuantity = pQuantity;
    }
}
