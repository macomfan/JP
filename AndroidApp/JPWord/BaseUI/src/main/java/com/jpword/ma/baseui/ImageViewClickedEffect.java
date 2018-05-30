package com.jpword.ma.baseui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

/**
 * Created by u0151316 on 1/15/2018.
 */

public class ImageViewClickedEffect extends android.support.v7.widget.AppCompatImageView {
    private int normalColor_ = Color.rgb(0xFF,0xFF,0xFF);
    private int disabledColor_ = Color.rgb(0x88,0x88,0x88);
    private boolean mIsClicked = false;

    public ImageViewClickedEffect(Context context) {
        super(context);
    }

    public ImageViewClickedEffect(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.imageButtonStyle);
    }

    public ImageViewClickedEffect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.ImageButtonDisabledEffect);
        disabledColor_ = ta.getColor(R.styleable.ImageButtonDisabledEffect_disabled_color, Color.rgb(0x88,0x88,0x88));
        //this.setColorFilter(disabledColor_, PorterDuff.Mode.MULTIPLY);
        ta.recycle();
    }

    public boolean isClicked() {
        return mIsClicked;
    }

    public void setClicked(boolean clicked) {
        mIsClicked = clicked;
        if (mIsClicked == true) {
            this.setColorFilter(normalColor_, PorterDuff.Mode.MULTIPLY);
        }
        else {
            this.setColorFilter(disabledColor_, PorterDuff.Mode.MULTIPLY);
        }
    }
}