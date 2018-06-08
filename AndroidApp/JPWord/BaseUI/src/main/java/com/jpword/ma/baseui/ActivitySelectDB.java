package com.jpword.ma.baseui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivitySelectDB extends AppCompatActivity {

    protected ListView lvSelectDB_ = null;

    private List<String> dbNameList_ = new LinkedList<>();

    protected boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    protected boolean onBackItemClick(MenuItem item) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return ActivitySelectDB.this.onMenuItemClick(item);
            }
        });

        lvSelectDB_ = (ListView) findViewById(R.id.lvSelectDB);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_database, menu);
        return true;
    }

    protected void addDBNameList(List<String> dbNameList) {
        dbNameList_.addAll(dbNameList);
    }

    protected String getSelectedDatabase() {
        int index = lvSelectDB_.getCheckedItemPosition();
        if (index == AdapterView.INVALID_POSITION) {
            return "";
        }
        return dbNameList_.get(index);
    }

    protected void refreshMainListView() {
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.listview_entity_select_db);
        adapter.addAll(dbNameList_);
        lvSelectDB_.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return onBackItemClick(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
