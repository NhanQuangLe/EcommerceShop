package com.example.ecommerceshop.Phat.Utils;

import com.example.ecommerceshop.Phat.Model.SpinnerItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static String[] orderStatus = { "Processing","Completed","Cancelled"};
    public static String[] orderStatus1 = { "Completed","Cancelled"};

    public static String convertToVND(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
        return decimalFormat.format(number);
    }
}
