package com.jpword.ma.baseui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

/**
 * Created by u0151316 on 12/27/2017.
 */

public class ImageButtonDisabledEffect extends android.support.v7.widget.AppCompatImageButton {
    private int normalColor_ = Color.rgb(0xFF,0xFF,0xFF);
    private int disabledColor_ = Color.rgb(0x88,0x88,0x88);


    public ImageButtonDisabledEffect(Context context) {
        super(context);
    }

    public ImageButtonDisabledEffect(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.imageButtonStyle);
    }

    public ImageButtonDisabledEffect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.ImageButtonDisabledEffect);
        disabledColor_ = ta.getColor(R.styleable.ImageButtonDisabledEffect_disabled_color, Color.rgb(0x88,0x88,0x88));
        ta.recycle();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == true) {
            this.setColorFilter(normalColor_, PorterDuff.Mode.MULTIPLY);
        }
        else {
            this.setColorFilter(disabledColor_, PorterDuff.Mode.MULTIPLY);
        }
        super.setEnabled(enabled);
    }
}
