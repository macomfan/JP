package com.jp.ma.jpword;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.jp.ma.baseui.WidgetMessage;

import DataEngine.DB;
import JPWord.Data.Database;
import JPWord.Data.IWord;

/**
 * Created by u0151316 on 1/3/2018.
 */

public class DatabaseService extends Service {

    class DBBroadcastReceiver extends BroadcastReceiver {
        private void SendWordToWidget(Context context, IWord word) {
            Intent actionIntent = new Intent(WidgetMessage.FROM_DB_SERVICE);
            actionIntent.putExtra(WidgetMessage.USER_ACTION, WidgetMessage.Action.DATA);
            if (word != null) {
                actionIntent.putExtra("Word", word.getContent());
            }
            context.sendBroadcast(actionIntent);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WidgetMessage.TO_DB_SERVICE.equals(action)) {
                if (!intent.hasExtra(WidgetMessage.USER_ACTION)) {
                    return;
                }
                int userAction = intent.getIntExtra(WidgetMessage.USER_ACTION, -1);
                if (userAction == WidgetMessage.Action.NEXT) {
                    IWord word = DB.getInstance().getWordSequence().next();
                    SendWordToWidget(context, word);
                } else if (userAction == WidgetMessage.Action.INIT) {
                    IWord word = DB.getInstance().getWordSequence().current();
                    SendWordToWidget(context, word);
                }
            }
        }
    }

    public class MyBinder extends Binder {
    }

    public DatabaseService getService() {
        return DatabaseService.this;
    }


    private MyBinder mBinder = new MyBinder();
    private DBBroadcastReceiver mReceiver = new DBBroadcastReceiver();



    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WidgetMessage.TO_DB_SERVICE);
        registerReceiver(mReceiver, intentFilter);

        System.out.println("+++++  Start service");
        try {
            DB.getInstance().initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("+++++  Start service finish");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("+++++  onStartCommand() executed");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("+++++  Destroy service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
