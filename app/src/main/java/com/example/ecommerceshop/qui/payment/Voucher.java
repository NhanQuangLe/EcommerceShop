package com.example.ecommerceshop.qui.payment;

public class Voucher {
    private String voucherid;
    private String vouchercode;
    private String voucherdes;
    private int quantity;
    private int minimumPrice;
    private int discountPrice;
    private String expiredDate;

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    private boolean used;

    private String shopId;

    private boolean isCanUse;



    private boolean isCheck;



    public Voucher() {
    }

    public Voucher(String voucherid, boolean used, String shopId) {
        this.voucherid = voucherid;
        this.used = used;
        this.shopId = shopId;
    }

    public Voucher(String voucherid, String vouchercode, String voucherdes, int quantity, int minimumPrice
            , int discountPrice, String expiredDate, boolean used, String shopId) {
        this.voucherid = voucherid;
        this.vouchercode = vouchercode;
        this.voucherdes = voucherdes;
        this.quantity = quantity;
        this.minimumPrice = minimumPrice;
        this.discountPrice = discountPrice;
        this.expiredDate = expiredDate;
        this.used = used;
        this.shopId = shopId;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getVoucherid() {
        return voucherid;
    }

    public void setVoucherid(String voucherid) {
        this.voucherid = voucherid;
    }

    public String getVouchercode() {
        return vouchercode;
    }

    public void setVouchercode(String vouchercode) {
        this.vouchercode = vouchercode;
    }

    public String getVoucherdes() {
        return voucherdes;
    }

    public void setVoucherdes(String voucherdes) {
        this.voucherdes = voucherdes;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(int minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }
    public boolean isCanUse() {
        return isCanUse;
    }

    public void setCanUse(boolean canUse) {
        isCanUse = canUse;
    }
}
