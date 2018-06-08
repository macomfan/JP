package com.jpword.ma.jpword;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import DataEngine.AppLogging;
import DataEngine.DatabaseServiceConnection;

/**
 * Created by u0151316 on 6/6/2018.
 */

public class LauncherActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 1;

    private DatabaseServiceConnection connection_ = new DatabaseServiceConnection() {
        @Override
        public void onServiceConnected() {
            AppLogging.showDebug(LauncherActivity.class, "onServiceConnected");
            if (getDatabaseOperator().getDBEntity() == null) {
                AppLogging.showDebug(LauncherActivity.class, "Check DB initialize failed");
                Intent intent = new Intent(LauncherActivity.this, ActivitySelectDB.class);
                intent.putExtra("Mode", "Init");
                LauncherActivity.this.startActivity(intent);
                LauncherActivity.this.finish();
            } else {
                AppLogging.showDebug(LauncherActivity.class, "Check DB initialize successfully");
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                LauncherActivity.this.startActivity(intent);
                LauncherActivity.this.finish();
            }
        }

        @Override
        public void onServiceDisconnected() {
            AppLogging.showDebug(LauncherActivity.class, "onServiceDisconnected");
        }
    };

    private void initialize() {
        AppLogging.showDebug(LauncherActivity.class, "Start DatabaseService");
        DatabaseService.bind(this, connection_);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {
        AppLogging.showDebug(LauncherActivity.class, "onRequestPermissionsResult");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initialize();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                    this.finish();
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppLogging.showDebug(LauncherActivity.class, "Start onCreate");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            AppLogging.showDebug(LauncherActivity.class, "Try request permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CODE);
        } else {
            initialize();
        }
        AppLogging.showDebug(LauncherActivity.class, "End onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            AppLogging.showDebug(LauncherActivity.class, "onDestroy");
            DatabaseService.unbind(this, connection_);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
