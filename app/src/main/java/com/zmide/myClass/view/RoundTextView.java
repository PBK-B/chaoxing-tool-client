package com.zmide.myClass.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatTextView;

public class RoundTextView extends AppCompatTextView {

    public RoundTextView(Context context) {
        super(context);
    }

    public RoundTextView(Context context, int radius, @ColorInt int bkColor){
        super(context);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setColor(bkColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(gradientDrawable);
        }
    }



}
