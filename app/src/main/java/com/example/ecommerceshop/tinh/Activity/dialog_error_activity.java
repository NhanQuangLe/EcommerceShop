package com.example.ecommerceshop.tinh.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.ecommerceshop.R;

public class dialog_error_activity extends Dialog {

    public Activity a;
    public String  contentDialog;
    public TextView content, button;

    public dialog_error_activity(Activity a, String contentDialog) {
        super(a);
        this.contentDialog = contentDialog;;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog_error);


        content = findViewById(R.id.content_dialog);
        button = findViewById(R.id.button_dong_dialog);

        content.setText(contentDialog);
        button.setOnClickListener(view -> dismiss());


        Window window = this.getWindow();
        if (window == null)
        {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = Gravity.CENTER;
        window.setAttributes(windowAttribute);
    }
}