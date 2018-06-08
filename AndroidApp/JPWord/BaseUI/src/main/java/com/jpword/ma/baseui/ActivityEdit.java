package com.jpword.ma.baseui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by u0151316 on 1/4/2018.
 */

public class ActivityEdit extends AppCompatActivity {
    protected EditText txtContent_ = null;
    protected EditText txtKana_ = null;
    protected EditText txtRoma_ = null;
    protected EditText txtNote_ = null;
    protected EditText txtTone_ = null;
    protected ListView lvMeaning = null;
    protected LinearLayout barMenu_ = null;
    protected LinearLayout barImi_ = null;
    protected ImageButton btnSave_ = null;

    protected boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    protected void beforetxtKanaTextChanged(CharSequence s, int start, int count, int after) {

    }

    protected void ontxtKanaTextChanged(CharSequence s, int start, int before, int count) {

    }

    protected void aftertxtKanaTextChanged(Editable s) {

    }

    protected void onbtnSaveClick(View v) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return ActivityEdit.this.onMenuItemClick(item);
            }
        });

        lvMeaning = (ListView) this.findViewById(R.id.lvMeaningListView);
        txtContent_ = (EditText) this.findViewById(R.id.txtContent);
        txtKana_ = (EditText) this.findViewById(R.id.txtKana);
        txtRoma_ = (EditText) this.findViewById(R.id.txtRoma);
        txtNote_ = (EditText) this.findViewById(R.id.txtNote);
        txtTone_ = (EditText) this.findViewById(R.id.txtTone);
        txtKana_.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ActivityEdit.this.beforetxtKanaTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ActivityEdit.this.ontxtKanaTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                ActivityEdit.this.aftertxtKanaTextChanged(s);
            }
        });
        barImi_ = (LinearLayout) this.findViewById(R.id.barImi);
        barMenu_ = (LinearLayout) this.findViewById(R.id.barMenu);


        btnSave_ = (ImageButton) this.findViewById(R.id.btnSave);
        btnSave_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityEdit.this.onbtnSaveClick(v);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
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
