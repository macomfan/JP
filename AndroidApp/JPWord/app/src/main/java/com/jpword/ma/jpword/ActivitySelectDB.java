package com.jpword.ma.jpword;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import DataEngine.AppLogging;
import DataEngine.DatabaseServiceConnection;
import DataEngine.IDatabaseOperator;


/**
 * Created by u0151316 on 6/6/2018.
 */

public class ActivitySelectDB extends com.jpword.ma.baseui.ActivitySelectDB {

    private String mode_ = "";
    private IDatabaseOperator databaseOperator_ = null;

    private DatabaseServiceConnection connection_ = new DatabaseServiceConnection() {
        @Override
        public void onServiceConnected() {
            AppLogging.showDebug(ActivitySelectDB.class, "onServiceConnected");
            databaseOperator_ = getDatabaseOperator();
            List<String> list = databaseOperator_.getDatabaseList();
            addDBNameList(list);
            refreshMainListView();
        }

        @Override
        public void onServiceDisconnected() {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLogging.showDebug(ActivitySelectDB.class, "onCreate");
        mode_ = this.getIntent().getStringExtra("Mode");
        DatabaseService.bind(this, connection_);
    }

    @Override
    protected boolean onBackItemClick(MenuItem item) {
        String selectedDatabase = getSelectedDatabase();
        if (!selectedDatabase.equals("")) {
            try {
                databaseOperator_.loadDatabase(selectedDatabase, false);
            } catch (Exception e) {
                return true;
            }
            if (mode_ != null && mode_.equals("Init")) {
                Intent intent = new Intent(ActivitySelectDB.this, LauncherActivity.class);
                intent.putExtra("DBName", selectedDatabase);
                this.startActivity(intent);
            }
            this.finish();
            return true;
        }
        this.finish();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            AppLogging.showDebug(ActivityEdit.class, "onDestroy");
            DatabaseService.unbind(this, connection_);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
