package com.example.ecommerceshop.Phat.Utils;

import java.text.DecimalFormat;

public class Constants {
    public static String[] orderStatus = { "Processing","Completed","Cancelled"};
    public static String convertToVND(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
        return decimalFormat.format(number);
    }
}
