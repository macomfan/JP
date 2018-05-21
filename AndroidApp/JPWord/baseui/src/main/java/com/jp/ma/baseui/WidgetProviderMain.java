package com.jp.ma.baseui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by u0151316 on 1/17/2018.
 */

public class WidgetProviderMain extends AppWidgetProvider {

    interface OnSelfActionListener {
        boolean onAction();
    }

    class SelfActionEngine {

        public static final String ACTION = "JPWord.WidgetProviderMain.Click";
        private static final String ToService = "ToService";
        private static final String SelfMethod = "SelfMethod";

        public SelfActionEngine() {
        }

        private RemoteViews mRemoteView = null;

        public void beginBindAction(Context context, AppWidgetManager appWidgetManager) {
            mRemoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        }

        public void completeBindAction(Context context, AppWidgetManager appWidgetManager) {
            ComponentName componentName = new ComponentName(context, WidgetProviderMain.class);
            appWidgetManager.updateAppWidget(componentName, mRemoteView);
            mRemoteView = null;
        }

        public void bindServiceAction(Context context, int itemId, int actionIdToService) {
            Intent intent = new Intent(ACTION);
            intent.setClass(context, WidgetProviderMain.class);
            intent.setData(Uri.parse("Main://"
                    + ToService
                    + "?"
                    + "value="
                    + Integer.toString(actionIdToService)));
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
            mRemoteView.setOnClickPendingIntent(itemId, pi);
        }

        public void bindSelfAction(Context context, int itemId, String listener) {
            Intent intent = new Intent(ACTION);
            intent.setClass(context, WidgetProviderMain.class);
            intent.setData(Uri.parse("Main://"
                    + SelfMethod
                    + "?"
                    + "value="
                    + listener));
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
            mRemoteView.setOnClickPendingIntent(itemId, pi);
        }

        public void doAction(Context context, Intent intent, WidgetProviderMain widget) {
            if (intent.getAction().equals(ACTION)) {
                Uri data = intent.getData();
                String type = data.getAuthority();
                if (ToService.equals(type)) {
                    String value = data.getQueryParameter("value");
                    pushActionToService(context, Integer.parseInt(value));

                } else if (SelfMethod.equals(type)) {
                    String value = data.getQueryParameter("value");
                    try {
                        Method m = widget.getClass().getMethod(value);
                        m.invoke(widget, intent);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

            }
        }

        public void pushActionToService(Context context, int action) {
            Intent actionIntent = new Intent(WidgetMessage.TO_DB_SERVICE);
            actionIntent.putExtra(WidgetMessage.USER_ACTION, action);
            context.sendBroadcast(actionIntent);
        }
    }

    private SelfActionEngine mActionEngine = new SelfActionEngine();


    protected void onHintClicked(Intent intent) {
        int a = 0;
        a++;
    }

    protected void onReceiveSendData(RemoteViews remoteViews, Intent intent) {
        String text = intent.getStringExtra("Word");
        if (text != null) {
            remoteViews.setTextViewText(R.id.txtRememberMainText, text);
        }
        else {
            remoteViews.setTextViewText(R.id.txtRememberMainText, "");
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        mActionEngine.beginBindAction(context, appWidgetManager);
        mActionEngine.bindServiceAction(context, R.id.btnNext, WidgetMessage.Action.NEXT);
        mActionEngine.bindServiceAction(context, R.id.btnPrev, WidgetMessage.Action.PREV);
        mActionEngine.bindServiceAction(context, R.id.btnPass, WidgetMessage.Action.PASS);
        mActionEngine.bindServiceAction(context, R.id.btnHint, WidgetMessage.Action.HINT);
        mActionEngine.bindServiceAction(context, R.id.btnFail, WidgetMessage.Action.FAIL);
        mActionEngine.bindSelfAction(context, R.id.btnHint, "onHintClicked");
        mActionEngine.completeBindAction(context, appWidgetManager);
        mActionEngine.pushActionToService(context, WidgetMessage.Action.INIT);
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
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                onReceiveSendData(remoteViews, intent);
                ComponentName componentName = new ComponentName(context, WidgetProviderMain.class);
                AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
            }
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
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
