package com.example.ecommerceshop.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.R;

public class  CustomToast extends Toast {
    public static int SUCCESS = 1;
    public static int ERROR = 2;

    public static int SHORT = 2000;
    public static int LONG = 7000;
    public CustomToast(Context context) {
        super(context);
    }
    public static Toast makeText(Context context, String message, int duration, int type) {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.BOTTOM, 0, 0);
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_toast_custom, null, false);
        TextView l1 = (TextView) layout.findViewById(R.id.content);
        LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.layout_toast);
        l1.setText(message);
        if (type == 1) {
            linearLayout.setBackgroundResource(R.drawable.success_shape);
        } else if (type == 2) {
            linearLayout.setBackgroundResource(R.drawable.error_shape);
        }
        toast.setView(layout);
        return toast;
    }


}
