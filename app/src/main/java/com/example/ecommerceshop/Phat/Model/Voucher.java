package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Voucher implements Serializable {
    private String voucherid, vouchercode, voucherdes, expiredDate;
    private int discountPrice, quantity, minimumPrice;


    public Voucher(String voucherid, String vouchercode, String voucherdes, String expiredDate, int discountPrice, int quantity, int minimumPrice) {
        this.voucherid = voucherid;
        this.vouchercode = vouchercode;
        this.voucherdes = voucherdes;
        this.expiredDate = expiredDate;
        this.discountPrice = discountPrice;
        this.quantity = quantity;
        this.minimumPrice = minimumPrice;

    }

    public Voucher() {
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

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
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
}
