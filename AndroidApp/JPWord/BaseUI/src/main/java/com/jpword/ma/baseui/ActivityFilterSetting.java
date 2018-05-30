package com.jpword.ma.baseui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by u0151316 on 1/8/2018.
 */

public class ActivityFilterSetting extends AppCompatActivity {

    protected ListView lvFilterListView_ = null;
    protected RadioButton mRBDisplayKanJi = null;
    protected RadioButton mRBDisplayKana = null;
    protected RadioGroup mRBGroup = null;

    protected void onmainListViewClick(AdapterView<?> parent, View view, int position, long id) {
    }

    protected void onRBGroupCheckedChange(RadioGroup group, @IdRes int checkedId) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        setSupportActionBar(toolbar);

        lvFilterListView_ = (ListView) findViewById(R.id.lvFilterListView);
        lvFilterListView_.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityFilterSetting.this.onmainListViewClick(parent, view, position, id);
            }
        });

        mRBDisplayKana = (RadioButton) findViewById(R.id.rbDispKana);
        mRBDisplayKanJi = (RadioButton) findViewById(R.id.rbDispKanji);
        mRBGroup = (RadioGroup) findViewById(R.id.rbDispSetting);
        mRBGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                ActivityFilterSetting.this.onRBGroupCheckedChange(group, checkedId);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
