package com.jpword.ma.jpword;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.jpword.ma.baseui.WidgetMessage;

import java.util.LinkedList;
import java.util.List;

import DataEngine.AppLogging;
import DataEngine.DB;
import DataEngine.DBEntity;
import DataEngine.DatabaseServiceConnection;
import DataEngine.IDatabaseOperator;
import JPLibAssist.ICurrentWordChangeListener;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;

/**
 * Created by u0151316 on 1/3/2018.
 */

public class DatabaseService extends Service implements ICurrentWordChangeListener, IDatabaseOperator {

    class DBBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WidgetMessage.TO_DB_SERVICE.equals(action)) {
                if (!intent.hasExtra(WidgetMessage.USER_ACTION)) {
                    return;
                }
                int userAction = intent.getIntExtra(WidgetMessage.USER_ACTION, -1);
                AppLogging.showDebug(DatabaseService.class, "Receive: " + WidgetMessage.Action.messageToString(userAction));
                if (userAction == WidgetMessage.Action.CLOSE) {
                }
                if (DB.getInstance().getDatabase() == null) {
                    sendNotReadyToWidget(context);
                    return;
                }
                needNotifyWidget_ = true;
                if (userAction == WidgetMessage.Action.NEXT) {
                    DB.getInstance().getWordSequence().next();
                } else if (userAction == WidgetMessage.Action.PREV) {
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
                } else if (userAction == WidgetMessage.Action.READY) {
                    sendWordToWidget(context, DB.getInstance().getWordSequence().current());
                } else {
                    needNotifyWidget_ = false;
                }
            }
        }
    }

    public class BinderForDatabaseService extends Binder {
        public IDatabaseOperator getDatabaseOperator() {
            return DatabaseService.this;
        }

        public Context getContext() {
            return DatabaseService.this.getApplicationContext();
        }
    }

    class MonitorThread extends Thread {
        @Override
        public void run() {
            AppLogging.showDebug(DatabaseService.class, "DatabaseService monitor started");
            while (!monitorReadyForStop_) {
                try {
                    Thread.sleep(10000);
                    AppLogging.showDebug(DatabaseService.class, "PERSIST DATABASE");
                    DB.getInstance().persist(DatabaseService.this.getApplicationContext());
                } catch (Exception e) {
                    AppLogging.showDebug(DatabaseService.class, "Persist error: " + e.getMessage());
                }
            }
            try {
                DB.getInstance().persist(DatabaseService.this.getApplicationContext());
            } catch (Exception e) {
                AppLogging.showDebug(DatabaseService.class, "Persist error: " + e.getMessage());
            }
            AppLogging.showDebug(DatabaseService.class, "DatabaseService monitor stop");
        }
    }


    private BinderForDatabaseService binder_ = new BinderForDatabaseService();
    private MonitorThread thread_ = null;
    private DBBroadcastReceiver receiver_ = null;
    private boolean needNotifyWidget_ = true;
    private boolean monitorReadyForStop_ = false;

    private static List<ServiceConnection> bindedConnection_ = new LinkedList<>();

    public static void bind(Context context, DatabaseServiceConnection connection) {
        startAndBind(context, connection);
    }

    private static void startAndBind(Context context, DatabaseServiceConnection connection) {
        Intent serviceIntent = new Intent(context, DatabaseService.class);
        context.startService(serviceIntent);
        if (context.bindService(serviceIntent, connection, BIND_AUTO_CREATE)) {
            bindedConnection_.add(connection);
        }
    }

    public static void unbind(Context context, ServiceConnection connection) {
        if (bindedConnection_.contains(connection)) {
            context.unbindService(connection);
        }
    }

    @Override
    public DBEntity getDBEntity() {
        IWordDictionary dict = DB.getInstance().getDatabase();
        if (dict == null) {
            return null;
        }
        return new DBEntity(DB.getInstance().getDatabase(),
                DB.getInstance().getWordSequence(),
                DB.getInstance().getFilters(),
                DB.getInstance().getDisplaySetting());
    }

    @Override
    public List<String> getDatabaseList() {
        return DB.getInstance().getDatabaseList();
    }

    @Override
    public void onCurrentWordChange() {
        if (!needNotifyWidget_) {
            return;
        }
        IWord word = DB.getInstance().getWordSequence().current();
        sendWordToWidget(this.getApplicationContext(), word);
    }

    private void sendWordToWidget(Context context, IWord word) {
        AppLogging.showDebug(DatabaseService.class, "DatabaseService send data");
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
            if (DB.getInstance().getDisplaySetting().isDisplayKanJi() == true) {
                actionIntent.putExtra(WidgetMessage.DATA_WORD_DISP_SETTING, "KanJi");
            } else {
                actionIntent.putExtra(WidgetMessage.DATA_WORD_DISP_SETTING, "Kana");
            }
            actionIntent.putExtra(WidgetMessage.DATA_WORD_DISP_SETTING, "");
            actionIntent.putExtra(WidgetMessage.DATA_WORD_KANA, kana);
            actionIntent.putExtra(WidgetMessage.DATA_WORD_ROMA, word.getRoma().getString());
            String meaning = DataEngine.MeaningUtil.meaningToString(word.getMeanings());
            actionIntent.putExtra(WidgetMessage.DATA_WORD_IMI, meaning);
        }
        actionIntent.putExtra(WidgetMessage.DATA_WORD_COUNT, DB.getInstance().getWordSequence().count());
        actionIntent.putExtra(WidgetMessage.DATA_CURRENT_INDEX, DB.getInstance().getWordSequence().getCurrentIndex());
        AppLogging.showDebug(DatabaseService.class, "Send: " + WidgetMessage.Action.messageToString(WidgetMessage.Action.DATA));
        context.sendBroadcast(actionIntent);
    }

    private void sendNotReadyToWidget(Context context) {
        Intent actionIntent = new Intent(WidgetMessage.FROM_DB_SERVICE);
        actionIntent.putExtra(WidgetMessage.USER_ACTION, WidgetMessage.Action.INVALID);
        AppLogging.showDebug(DatabaseService.class, "Send: " + WidgetMessage.Action.messageToString(WidgetMessage.Action.INVALID));
        context.sendBroadcast(actionIntent);
    }

    private void tryDetectWidget(Context context) {
        Intent actionIntent = new Intent(WidgetMessage.FROM_DB_SERVICE);
        actionIntent.putExtra(WidgetMessage.USER_ACTION, WidgetMessage.Action.READY);
        AppLogging.showDebug(DatabaseService.class, "Send: " + WidgetMessage.Action.messageToString(WidgetMessage.Action.READY));
        context.sendBroadcast(actionIntent);
    }

    @Override
    public void loadDatabase(String dbName, boolean createIfNotExist) {
        try {
            DB.getInstance().loadDatabase(dbName, createIfNotExist);
            if (DB.getInstance().getDatabase() != null) {
                DB.getInstance().getWordSequence().setCurrentWordChangeListener(this);
            }
            tryDetectWidget(this.getApplicationContext());
        } catch (Exception e) {
            AppLogging.showDebug(DatabaseService.class, "LoadDatabase Error: " + e.getMessage());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (thread_ == null) {
            thread_ = new MonitorThread();
            thread_.start();
            try {
                DB.getInstance().initialize(this);
            } catch (Exception e) {
                AppLogging.showDebug(DatabaseService.class, "DatabaseService init Error: " + e.getMessage());
            }
            loadDatabase("", false);
        }
        if (receiver_ == null) {
            receiver_ = new DBBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WidgetMessage.TO_DB_SERVICE);
            registerReceiver(receiver_, intentFilter);
        }
        AppLogging.showDebug(DatabaseService.class, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppLogging.showDebug(DatabaseService.class, "onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver_ != null) {
            unregisterReceiver(receiver_);
        }
        monitorReadyForStop_ = true;
        AppLogging.showDebug(DatabaseService.class, "Destroy service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder_;
    }
}
