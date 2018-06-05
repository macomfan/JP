package com.jpword.ma.jpword;


import android.app.ActivityManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import com.jpword.ma.baseui.WidgetMessage;
import com.jpword.ma.jpword.DatabaseService;
import com.jpword.ma.jpword.MainActivity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import DataEngine.AppLogging;

/**
 * Created by u0151316 on 2/2/2018.
 */

public class WidgetProviderMain extends AppWidgetProvider {
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            AppLogging.showDebug("Widget onServiceDisconnected !!!!!!");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AppLogging.showDebug("Widget onServiceConnected");
        }
    };

    public interface OnItemClickListener {
        void onItemClick(Context context, RemoteViews remoteViews, Intent intent);
    }

    class SelfActionEngine {

        public static final String ACTION = "JPWord.WidgetProviderMain.Click";
        private static final String ToService = "ToService";
        private static final String SelfMethod = "SelfMethod";

        private Map<Integer, OnItemClickListener> mMapSelfEventAction = new HashMap<>();
        private Map<Integer, Integer> mMapSimpleServiceAction = new HashMap<>();

        public SelfActionEngine() {
            AppLogging.showDebug("SelfActionEngine NEW");
        }

        private RemoteViews mRemoteView = null;
        private int mRemoteViewReferenceCount = 0;

        private void beginBindAction(Context context, AppWidgetManager appWidgetManager) {
            String packageName = context.getPackageName();
            mRemoteView = new RemoteViews(packageName, R.layout.widget_layout);
        }

        private void completeBindAction(Context context, AppWidgetManager appWidgetManager) {
            ComponentName componentName = new ComponentName(context, WidgetProviderMain.class);
            appWidgetManager.updateAppWidget(componentName, mRemoteView);
            mRemoteView = null;
        }

        private void beforeUpdateRemoteView(Context context) {
            mRemoteViewReferenceCount++;
            AppLogging.showDebug(String.format("Widget beforeUpdateRemoteView %d", mRemoteViewReferenceCount));
            if (mRemoteView == null) {
                mRemoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            }
        }

        private void finishRemoteView(Context context) {
            AppLogging.showDebug(String.format("Widget finishRemoteView %d", mRemoteViewReferenceCount));
            mRemoteViewReferenceCount--;
            if (mRemoteViewReferenceCount == 0) {
                ComponentName componentName = new ComponentName(context, WidgetProviderMain.class);
                AppWidgetManager.getInstance(context).updateAppWidget(componentName, mRemoteView);
                mRemoteView = null;
                mRemoteViewReferenceCount = 0;
            }
        }

        public void doAction(Context context, Intent intent, WidgetProviderMain widget) {
            if (intent.getAction().equals(ACTION)) {
                beforeUpdateRemoteView(context);
                Uri data = intent.getData();
                String type = data.getAuthority();
                if (ToService.equals(type)) {
                    String value = data.getQueryParameter("value");
                    AppLogging.showDebug("selfaction " + value);
                    pushActionToService(context, Integer.parseInt(value));

                } else if (SelfMethod.equals(type)) {
                    String value = data.getQueryParameter("value");
                    Integer id = Integer.parseInt(value);
                    OnItemClickListener listener = mMapSelfEventAction.get(id);
                    if (mMapSelfEventAction.containsKey(id)) {
                        listener.onItemClick(context, mRemoteView, intent);
                    }
                }
                finishRemoteView(context);
            }
        }

        public void refreshPendingIntent(Context context, AppWidgetManager appWidgetManager) {
            this.beginBindAction(context, appWidgetManager);
            for (Map.Entry<Integer, Integer> entry : mMapSimpleServiceAction.entrySet()) {
                Intent intent = new Intent(ACTION);
                intent.setClass(context, WidgetProviderMain.class);
                intent.setData(Uri.parse("Main://"
                        + ToService
                        + "?"
                        + "value="
                        + entry.getValue()));
                PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
                mRemoteView.setOnClickPendingIntent(entry.getKey(), pi);
            }
            for (Map.Entry<Integer, OnItemClickListener> entry : mMapSelfEventAction.entrySet()) {
                Intent intent = new Intent(ACTION);
                intent.setClass(context, WidgetProviderMain.class);
                intent.setData(Uri.parse("Main://"
                        + SelfMethod
                        + "?"
                        + "value="
                        + entry.getKey()));
                PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
                mRemoteView.setOnClickPendingIntent(entry.getKey(), pi);
            }
            this.completeBindAction(context, appWidgetManager);
        }

        public void bindSimpleServiceAction(int itemID, int actionIdToService) {
            mMapSimpleServiceAction.put(itemID, actionIdToService);
        }

        public void bindSelfEventAction(int itemID, OnItemClickListener listener) {
            mMapSelfEventAction.put(itemID, listener);
        }

        public synchronized void pushActionToService(Context context, int action) {
            boolean serviceRunning = false;
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (DatabaseService.class.getName().equals(service.service.getClassName())) {
                    serviceRunning = true;
                }
            }
            if (!serviceRunning) {
                beforeUpdateRemoteView(context);
                AppLogging.showDebug("Widget DatabaseService is not running !!!!!");
                mRemoteView.setViewVisibility(R.id.widgetCheckService, View.VISIBLE);
                mRemoteView.setViewVisibility(R.id.widgetHint, View.GONE);
                mRemoteView.setViewVisibility(R.id.widgetMain, View.GONE);
                finishRemoteView(context);
                Intent startIntent = new Intent(context, DatabaseService.class);
                context.getApplicationContext().bindService(startIntent, mConnection, Context.BIND_AUTO_CREATE);
            }
            else {
                Intent actionIntent = new Intent(WidgetMessage.TO_DB_SERVICE);
                actionIntent.putExtra(WidgetMessage.USER_ACTION, action);
                context.sendBroadcast(actionIntent);
            }
        }
    }

    private SelfActionEngine mActionEngine = new SelfActionEngine();

    public void onHintClicked(Context context, RemoteViews remoteViews, Intent intent) {
        remoteViews.setViewVisibility(R.id.widgetMain, View.GONE);
        remoteViews.setViewVisibility(R.id.widgetHint, View.VISIBLE);
    }

    public void onBackClicked(Context context, RemoteViews remoteViews, Intent intent) {
        remoteViews.setViewVisibility(R.id.widgetHint, View.GONE);
        remoteViews.setViewVisibility(R.id.widgetMain, View.VISIBLE);
    }

    public void onMaintextClicked(Context context, RemoteViews remoteViews, Intent intent) {
        Intent newIntent = new Intent(context, MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }

    public WidgetProviderMain() {
        mActionEngine.bindSimpleServiceAction(R.id.btnNext, WidgetMessage.Action.NEXT);
        mActionEngine.bindSimpleServiceAction(R.id.btnPrev, WidgetMessage.Action.PREV);
        mActionEngine.bindSimpleServiceAction(R.id.btnPass, WidgetMessage.Action.PASS);
        mActionEngine.bindSimpleServiceAction(R.id.btnFail, WidgetMessage.Action.FAIL);
        mActionEngine.bindSimpleServiceAction(R.id.btnHint, WidgetMessage.Action.HINT);
        mActionEngine.bindSelfEventAction(R.id.btnHint, new OnItemClickListener() {
            @Override
            public void onItemClick(Context context, RemoteViews remoteViews, Intent intent) {
                WidgetProviderMain.this.onHintClicked(context, remoteViews, intent);
            }
        });
        mActionEngine.bindSelfEventAction(R.id.btnBack, new OnItemClickListener() {
            @Override
            public void onItemClick(Context context, RemoteViews remoteViews, Intent intent) {
                WidgetProviderMain.this.onBackClicked(context, remoteViews, intent);
            }
        });
        mActionEngine.bindSelfEventAction(R.id.txtRememberMainText, new OnItemClickListener() {
            @Override
            public void onItemClick(Context context, RemoteViews remoteViews, Intent intent) {
                WidgetProviderMain.this.onMaintextClicked(context, remoteViews, intent);
            }
        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent startIntent = new Intent(context, DatabaseService.class);
        context.getApplicationContext().bindService(startIntent, mConnection, Context.BIND_AUTO_CREATE);
        mActionEngine.refreshPendingIntent(context, appWidgetManager);
        mActionEngine.pushActionToService(context, WidgetMessage.Action.HEARTBEAT);
    }


    protected void onReceiveSendData(RemoteViews remoteViews, Intent intent) {
        remoteViews.setViewVisibility(R.id.widgetCheckService, View.GONE);
        remoteViews.setViewVisibility(R.id.widgetHint, View.GONE);
        remoteViews.setViewVisibility(R.id.widgetMain, View.VISIBLE);

        int count = intent.getIntExtra(WidgetMessage.DATA_WORD_COUNT, 0);
        int index = intent.getIntExtra(WidgetMessage.DATA_CURRENT_INDEX, 0);
        if (count != 0 && intent.hasExtra(WidgetMessage.DATA_WORD_CONTENT)) {
            String context = intent.getStringExtra(WidgetMessage.DATA_WORD_CONTENT);
            String kana = intent.getStringExtra(WidgetMessage.DATA_WORD_KANA);
            String dispSetting = intent.getStringExtra(WidgetMessage.DATA_WORD_DISP_SETTING);
            if (dispSetting.equals("KanJi")) {
                remoteViews.setTextViewText(R.id.txtRememberMainText, context);
            } else {
                remoteViews.setTextViewText(R.id.txtRememberMainText, kana);
            }
            remoteViews.setTextViewText(R.id.txtContent, context);
            remoteViews.setTextViewText(R.id.txtKana, kana);
            remoteViews.setTextViewText(R.id.txtImi, intent.getStringExtra(WidgetMessage.DATA_WORD_IMI));
            remoteViews.setProgressBar(R.id.progBar,
                    count,
                    index + 1,
                    false);
            remoteViews.setBoolean(R.id.btnPass, "setEnabled", true);
            remoteViews.setBoolean(R.id.btnFail, "setEnabled", true);
            remoteViews.setBoolean(R.id.btnHint, "setEnabled", true);
        } else {
            remoteViews.setTextViewText(R.id.txtRememberMainText, "");
            remoteViews.setProgressBar(R.id.progBar, 0, 0, false);
            remoteViews.setBoolean(R.id.btnPass, "setEnabled", false);
            remoteViews.setBoolean(R.id.btnFail, "setEnabled", false);
            remoteViews.setBoolean(R.id.btnHint, "setEnabled", false);
        }

        if (index == -1) {
            remoteViews.setBoolean(R.id.btnPrev, "setEnabled", false);
        } else {
            remoteViews.setBoolean(R.id.btnPrev, "setEnabled", true);
        }
        if (index == count) {
            remoteViews.setBoolean(R.id.btnNext, "setEnabled", false);
        } else {
            remoteViews.setBoolean(R.id.btnNext, "setEnabled", true);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mActionEngine.doAction(context, intent, this);
        if (intent.getAction().equals(WidgetMessage.FROM_DB_SERVICE)) {
            if (!intent.hasExtra(WidgetMessage.USER_ACTION)) {
                return;
            }
            int userAction = intent.getIntExtra(WidgetMessage.USER_ACTION, -1);
            if (userAction == WidgetMessage.Action.DATA) {
                AppLogging.showDebug("Widget onRecevie DATA");
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                onReceiveSendData(remoteViews, intent);
                ComponentName componentName = new ComponentName(context, WidgetProviderMain.class);
                AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
            } else if (userAction == WidgetMessage.Action.TEST) {
                AppLogging.showDebug("Widget onRecevie TEST");
                mActionEngine.pushActionToService(context, WidgetMessage.Action.HEARTBEAT);
            }
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        mActionEngine.pushActionToService(context, WidgetMessage.Action.CLOSE);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
