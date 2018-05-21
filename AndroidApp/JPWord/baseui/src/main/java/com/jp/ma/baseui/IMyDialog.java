package com.jp.ma.baseui;

import android.content.Context;

/**
 * Created by u0151316 on 1/10/2018.
 */

public interface IMyDialog {
    void show(Context context, CallBack callback);

    String getResult();

    interface CallBack {
        void confirmed();
    }
}