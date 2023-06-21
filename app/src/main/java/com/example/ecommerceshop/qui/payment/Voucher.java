package com.example.ecommerceshop.qui.payment;

public class Voucher {
    private String voucherid;
    private String vouchercode;
    private String voucherdes;
    private int quantity;
    private int minimumPrice;
    private int discountPrice;
    private String expiredDate;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    private String shopId;

    private boolean isCanUse;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    private boolean isCheck;



    public Voucher() {
    }

    public Voucher(String voucherid, String vouchercode, String voucherdes, int quantity, int minimumPrice, int discountPrice, String expiredDate) {
        this.voucherid = voucherid;
        this.vouchercode = vouchercode;
        this.voucherdes = voucherdes;
        this.quantity = quantity;
        this.minimumPrice = minimumPrice;
        this.discountPrice = discountPrice;
        this.expiredDate = expiredDate;
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
