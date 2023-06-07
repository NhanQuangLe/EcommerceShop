package com.example.ecommerceshop.nhan;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.fonts.FontFamily;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.example.ecommerceshop.R;

public class Account_Setting_Item extends androidx.appcompat.widget.AppCompatTextView {

    private String title;
    //private float titleSize;
    private Boolean style;
    private int titleColor;


    public Account_Setting_Item(Context context) {
        super(context);
        setAccountSettingItemTitle();
    }

    public Account_Setting_Item(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyText);
        int count = typedArray.getIndexCount();
        try{
            for(int i = 0; i < count; i++){
                int attr = typedArray.getIndex(i);
                if(attr == R.styleable.MyText_title)
                    title = typedArray.getString(attr);
                else if (attr == R.styleable.MyText_style)
                {
                    style = typedArray.getBoolean(attr, false);
                    setMyTitleStyle();
                }
            }
        } finally {
            typedArray.recycle();
        }
    }

    public Account_Setting_Item(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setAccountSettingItemTitle(){
        if(this.title != null)
            setText(this.title);
    }

    private void setMyTitleStyle()
    {
        if(this.style != null)
        {
            setTextColor(Color.parseColor("#FF444444"));
            setTextSize(14f);
        }
    }
}
