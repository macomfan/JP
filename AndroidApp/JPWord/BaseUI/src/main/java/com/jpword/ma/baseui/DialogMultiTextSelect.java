package com.jpword.ma.baseui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by u0151316 on 1/10/2018.
 */

public class DialogMultiTextSelect implements IMyDialog {

    private List<String> mItems = new LinkedList<>();
    private List<String> mResult = new LinkedList<>();
    private String mTitle = "";
    private ImageButton mBtnAdd = null;
    private ImageButton mBtnRemove = null;
    private TextView mTxtResultView = null;
    private String mSelectedItem = "";

    public DialogMultiTextSelect(String mTitle, List<String> mItems, String current) {
        this.mItems = mItems;
        this.mTitle = mTitle;
        String[] split = current.split("\\,");
        for (String c : split) {
            mResult.add(c);
        }
    }

    protected void onBtnAddClick(View v) {
        if (!mSelectedItem.equals("")) {
            if (!mResult.contains(mSelectedItem)) {
                mResult.add(mSelectedItem);
                mTxtResultView.setText(getResult());
            }
        }
    }

    protected void onBtnRemoveClick(View v) {
        if (!mSelectedItem.equals("")) {
            if (mResult.contains(mSelectedItem)) {
                mResult.remove(mSelectedItem);
                mTxtResultView.setText(getResult());
            }
        }
    }

    @Override
    public void show(Context context, final CallBack callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater factory = LayoutInflater.from(context);
        View dialogView = factory.inflate(R.layout.dialog_multitext_select, null);

        Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedItem = mItems.get(position);
                mSelectedItem = mSelectedItem.trim();
                DialogMultiTextSelect.this.mBtnAdd.setEnabled(true);
                DialogMultiTextSelect.this.mBtnRemove.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSelectedItem = "";
                DialogMultiTextSelect.this.mBtnAdd.setEnabled(false);
                DialogMultiTextSelect.this.mBtnRemove.setEnabled(false);
            }
        });

        String[] items = mItems.toArray(new String[mItems.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mBtnAdd = (ImageButton) dialogView.findViewById(R.id.btnAdd);
        mBtnRemove = (ImageButton) dialogView.findViewById(R.id.btnRemove);

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMultiTextSelect.this.onBtnAddClick(v);
            }
        });

        mBtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMultiTextSelect.this.onBtnRemoveClick(v);
            }
        });

        mTxtResultView = (TextView) dialogView.findViewById(R.id.txtResultView);
        mTxtResultView.setText("");
        mBtnAdd.setEnabled(false);
        mBtnRemove.setEnabled(false);
        mTxtResultView.setText(getResult());

        builder.setTitle(mTitle);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                callback.confirmed();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    @Override
    public String getResult() {
        String res = "";
        boolean isFirst = true;
        for (String r : mResult) {
            if (!isFirst) {
                res += ",";
            }
            isFirst = false;
            res += r;
        }
        return res;
    }
}
