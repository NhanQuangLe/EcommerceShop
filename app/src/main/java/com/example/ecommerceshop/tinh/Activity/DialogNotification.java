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

public class DialogNotification extends Dialog {

    public Activity a;
    public String contentDialog;
    public TextView content, button_ok, button_cancel;
    public Boolean resultDialog;

    public DialogNotification(Activity a, String content) {
        super(a);
        this.contentDialog = content;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog_notification);


        content = findViewById(R.id.content_dialog);
        button_cancel = findViewById(R.id.btn_khong_dialog);
        button_ok = findViewById(R.id.btn_dongy_dialog);

        content.setText(contentDialog);
        button_cancel.setOnClickListener(view -> Huy());
        button_ok.setOnClickListener(view -> DongY());

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

    private void DongY() {
        resultDialog = true;
        dismiss();
    }

    private void Huy() {
        resultDialog = false;
        dismiss();
    }


}