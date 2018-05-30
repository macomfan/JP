package com.jpword.ma.baseui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by u0151316 on 1/10/2018.
 */

public class DialogSingleItemSelect implements IMyDialog {

    private List<String> mItems = new LinkedList<>();
    private int choice_ = 0;
    private String mResult = "";
    private String mTitle = "";

    public DialogSingleItemSelect(String title, List<String> items, String current) {
        mItems = items;
        mTitle = title;
    }

    @Override
    public void show(Context context, final CallBack callback) {
        String[] items = new String[mItems.size()];
        items = mItems.toArray(items);
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(context);
        singleChoiceDialog.setTitle(mTitle);
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice_ = which;
                    }
                });

        singleChoiceDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (choice_ != -1) {
                    mResult = mItems.get(choice_);
                    callback.confirmed();
                }
            }
        });
        singleChoiceDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        singleChoiceDialog.setCancelable(false);
        singleChoiceDialog.show();
    }

    @Override
    public String getResult() {
        return mResult;
    }
}
