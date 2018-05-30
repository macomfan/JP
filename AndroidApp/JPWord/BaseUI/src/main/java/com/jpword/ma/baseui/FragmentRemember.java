package com.jpword.ma.baseui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by u0151316 on 1/4/2018.
 */

public class FragmentRemember extends Fragment {

    protected TextView txtRememberMainText_ = null;
    protected TextView txtCount_ = null;
    protected TextView txtSkill_ = null;
    protected TextView txtRD_ = null;
    protected TextView mTxtCls = null;
    protected ProgressBar progbar_ = null;
    protected ImageButton btnHint_ = null;
    protected ImageButton btnPass_ = null;
    protected ImageButton btnFail_ = null;
    protected ImageButton btnNext_ = null;
    protected ImageButton btnPrev_ = null;
    protected ImageButton btnRefresh_ = null;
    protected ImageViewClickedEffect mImgMyWord = null;

    protected TextView mTxtFilter1 = null;
    protected TextView mTxtFilter2 = null;
    protected TextView mTxtFilter3 = null;
    protected TextView mTxtFilter4 = null;
    protected TextView mTxtFilter5 = null;
    protected TextView mTxtFilter6 = null;

    protected List<TextView> mTxtFilterList = new LinkedList<>();

    protected void onbtnNextClicked(View v) {
    }

    protected void onbtnPrevClicked(View v) {
    }

    protected void onbtnHintClicked(View v) {
    }

    protected void onbtnPassClicked(View v) {
    }

    protected void onbtnFailClicked(View v) {
    }

    protected void onbtRefreshClicked(View v) {
    }

    protected void onImgMyWordClicked(View v) {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_remember, container, false);
        setHasOptionsMenu(true);

        txtCount_ = (TextView) v.findViewById(R.id.txtCount);
        txtSkill_ = (TextView) v.findViewById(R.id.txtSkill);
        txtRD_ = (TextView) v.findViewById(R.id.txtRD);
        txtRememberMainText_ = (TextView) v.findViewById(R.id.txtRememberMainText);
        mTxtCls = (TextView) v.findViewById(R.id.txtCls);
        progbar_ = (ProgressBar) v.findViewById(R.id.progBar);

        btnNext_ = (ImageButton) v.findViewById(R.id.btnNext);
        btnNext_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRemember.this.onbtnNextClicked(v);
            }
        });
        btnPrev_ = (ImageButton) v.findViewById(R.id.btnPrev);
        btnPrev_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRemember.this.onbtnPrevClicked(v);
            }
        });
        btnHint_ = (ImageButton) v.findViewById(R.id.btnHint);
        btnHint_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRemember.this.onbtnHintClicked(v);
            }
        });
        btnPass_ = (ImageButton) v.findViewById(R.id.btnPass);
        btnPass_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRemember.this.onbtnPassClicked(v);
            }
        });
        btnFail_ = (ImageButton) v.findViewById(R.id.btnFail);
        btnFail_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRemember.this.onbtnFailClicked(v);
            }
        });

        btnRefresh_ = (ImageButton) v.findViewById(R.id.btnRefresh);
        btnRefresh_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRemember.this.onbtRefreshClicked(v);
            }
        });

        mImgMyWord = (ImageViewClickedEffect) v.findViewById(R.id.btnMyWord);
        mImgMyWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRemember.this.onImgMyWordClicked(v);
            }
        });

        mTxtFilter1 = (TextView) v.findViewById(R.id.txtFilter1);
        mTxtFilter2 = (TextView) v.findViewById(R.id.txtFilter2);
        mTxtFilter3 = (TextView) v.findViewById(R.id.txtFilter3);
        mTxtFilter4 = (TextView) v.findViewById(R.id.txtFilter4);
        mTxtFilter5 = (TextView) v.findViewById(R.id.txtFilter5);
        mTxtFilter6 = (TextView) v.findViewById(R.id.txtFilter6);
        mTxtFilterList.add(mTxtFilter1);
        mTxtFilterList.add(mTxtFilter2);
        mTxtFilterList.add(mTxtFilter3);
        mTxtFilterList.add(mTxtFilter4);
        mTxtFilterList.add(mTxtFilter5);
        mTxtFilterList.add(mTxtFilter6);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.setting, menu);
    }
}
