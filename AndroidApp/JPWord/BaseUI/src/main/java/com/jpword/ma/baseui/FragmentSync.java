package com.jpword.ma.baseui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by u0151316 on 1/4/2018.
 */

public class FragmentSync extends Fragment {
    protected Button btnStart_;
    protected Button btnSave_;
    protected TextView txtTotalCount_ = null;
    protected TextView mTvDatabase = null;
    protected ViewLogViewer mLogView = null;

    protected void onbtnStartClick(View v) {}
    protected void onbtnSaveClick(View v) {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sync, container, false);
        btnStart_ = (Button) v.findViewById(R.id.btnStart);
        btnSave_ = (Button) v.findViewById(R.id.btnSave);
        btnStart_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSync.this.onbtnStartClick(v);
            }
        });
        btnSave_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSync.this.onbtnSaveClick(v);
            }
        });
        txtTotalCount_ = (TextView) v.findViewById(R.id.tvTotalCount);
        mTvDatabase= (TextView) v.findViewById(R.id.tvDatabase);
        mLogView = (ViewLogViewer) v.findViewById(R.id.logViewer);
        return v;
    }
}
