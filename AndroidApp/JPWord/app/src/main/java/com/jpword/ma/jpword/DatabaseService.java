package com.jpword.ma.jpword;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.jpword.ma.baseui.WidgetMessage;

import DataEngine.AppLogging;
import DataEngine.DB;
import JPLibAssist.ICurrentWordChangeListener;
import JPWord.Data.Database;
import JPWord.Data.IRoma;
import JPWord.Data.IWord;

/**
 * Created by u0151316 on 1/3/2018.
 */

public class DatabaseService extends Service implements Runnable, ICurrentWordChangeListener {

    class DBBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WidgetMessage.TO_DB_SERVICE.equals(action)) {
                if (!intent.hasExtra(WidgetMessage.USER_ACTION)) {
                    return;
                }
                mNeedNotifyWidget = true;
                int userAction = intent.getIntExtra(WidgetMessage.USER_ACTION, -1);
                if (userAction == WidgetMessage.Action.NEXT) {
                    AppLogging.showDebug("DatabaseService onRecevie NEXT");
                    DB.getInstance().getWordSequence().next();
                } else if (userAction == WidgetMessage.Action.PREV) {
                    AppLogging.showDebug("DatabaseService onRecevie PREV");
                    DB.getInstance().getWordSequence().prev();
                } else if (userAction == WidgetMessage.Action.PASS) {
                    IWord word = DB.getInstance().getWordSequence().current();
                    word.increaseSkill();
                    DB.getInstance().getWordSequence().next();
                } else if (userAction == WidgetMessage.Action.FAIL) {
                    IWord word = DB.getInstance().getWordSequence().current();
                    if (word.getSkill() >= -5) {
                        word.updateSkill(-5);
                    }
                    DB.getInstance().getWordSequence().next();
                } else if (userAction == WidgetMessage.Action.HINT) {
                } else if (userAction == WidgetMessage.Action.HEARTBEAT) {
                    AppLogging.showDebug("DatabaseService onRecevie HEARTBEAT");
                    sendWordToWidget(context, DB.getInstance().getWordSequence().current());
                } else if (userAction == WidgetMessage.Action.CLOSE) {
                    AppLogging.showDebug("DatabaseService onRecevie CLOSE");
                }
            }
        }
    }

    public class MyBinder extends Binder {
        public DatabaseService getService() {
            return DatabaseService.this;
        }
    }

    private MyBinder mBinder = new MyBinder();
    private DBBroadcastReceiver mReceiver = new DBBroadcastReceiver();
    private boolean mNeedNotifyWidget = true;
    private boolean mMonitorReadyForStop = false;

    @Override
    public void onCurrentWordChange() {
        if (!mNeedNotifyWidget) {
            return;
        }
        IWord word = DB.getInstance().getWordSequence().current();
        sendWordToWidget(this.getApplicationContext(), word);
    }

    private void sendWordToWidget(Context context, IWord word) {
        AppLogging.showDebug("DatabaseService send data");
        Intent actionIntent = new Intent(WidgetMessage.FROM_DB_SERVICE);
        actionIntent.putExtra(WidgetMessage.USER_ACTION, WidgetMessage.Action.DATA);
        if (word != null) {
            actionIntent.putExtra(WidgetMessage.DATA_WORD_CONTENT, word.getContent());
            String kana = word.getKana();
            if (!word.getTone().isEmpty()) {
                kana += " (";
                kana += word.getTone();
                kana += ")";
            }
            actionIntent.putExtra(WidgetMessage.DATA_WORD_KANA, kana);
            actionIntent.putExtra(WidgetMessage.DATA_WORD_ROMA, word.getRoma().getString());
            String meaning = DataEngine.MeaningUtil.meaningToString(word.getMeanings());
            actionIntent.putExtra(WidgetMessage.DATA_WORD_IMI, meaning);
        }
        actionIntent.putExtra(WidgetMessage.DATA_WORD_COUNT, DB.getInstance().getWordSequence().count());
        actionIntent.putExtra(WidgetMessage.DATA_CURRENT_INDEX, DB.getInstance().getWordSequence().getCurrentIndex());
        context.sendBroadcast(actionIntent);
    }

    private void detectWidget(Context context) {
        Intent actionIntent = new Intent(WidgetMessage.FROM_DB_SERVICE);
        actionIntent.putExtra(WidgetMessage.USER_ACTION, WidgetMessage.Action.TEST);
        context.sendBroadcast(actionIntent);
    }

    @Override
    public void run() {
        AppLogging.showDebug("DatabaseService monitor started");
        detectWidget(this.getApplicationContext());
        while (!mMonitorReadyForStop) {
            try {
                Thread.sleep(5000);
                DB.getInstance().persist(this.getApplicationContext());
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WidgetMessage.TO_DB_SERVICE);
        registerReceiver(mReceiver, intentFilter);

        AppLogging.showDebug("Start Service");
        try {
            DB.getInstance().initialize(this);
            DB.getInstance().getWordSequence().setCurrentWordChangeListener(this);
        } catch (Exception e) {
            AppLogging.showDebug("DatabaseService init Error !!!!");
            e.printStackTrace();
        }

        AppLogging.showDebug("Service start complete");
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppLogging.showDebug("onStartCommand() executed");
        detectWidget(this.getApplicationContext());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMonitorReadyForStop = true;
        AppLogging.showLog("Destroy service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
