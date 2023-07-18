package com.example.ecommerceshop.Phat.Utils;

import com.example.ecommerceshop.Phat.Model.SpinnerItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static String[] orderStatus = { "Processing","Cancelled"};
    public static String[] orderStatus1 = { "Completed"};
    public static final String FCM_KEY ="AAAA3n9da1c:APA91bFiKpUQAunEUVuCbl1GZvDQVS0y0XZedv_bVsMAHVq9YIiZhaFc6kK65mRie-PHcf18vLakRlzCcLB2Fwph5w-cJgpgcjbjPD7OBRRXfWMJol-aiyC3fyutC2-ZYHnTCpa_R1ei";
    public  static  final String FCM_TOPIC ="PUSH_NOTIFICATIONS";

    public static String convertToVND(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
        return decimalFormat.format(number);
    }
}
