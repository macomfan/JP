package com.jpword.ma.jpword;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.jpword.ma.baseui.ViewLogViewer;

import DataEngine.DB;
import JPWord.Synchronizer.ILogging;
import JPWord.Synchronizer.Log;
import JPWord.Synchronizer.Method;
import JPWord.Synchronizer.Sync;

/**
 * Created by u0151316 on 12/14/2017.
 */

public class FragmentSync extends com.jpword.ma.baseui.FragmentSync {
    private ILogging logging_ = null;
    private Handler mHandler = null;

    class LogListener extends Thread {
        private Handler mHandler = null;

        public LogListener(Handler handler) {
            mHandler = handler;
        }

//        public enum Type {
//            HARMLESS,
//            WARNING,
//            SUCCESS,
//            FAILURE,
//        }

        @Override
        public void run() {
            ILogging logging = Sync.getInstance().getDefaultLogging();
            while (true) {
                try {
                    Log log = logging.pop();
                    if (log != null) {
                        Message msg = new Message();
                        Bundle b = new Bundle();
                        b.putString("What", log.what());
                        switch (log.type()) {
                            case HARMLESS:
                                b.putInt("Type", 1);
                                break;
                            case WARNING:
                                b.putInt("Type", 2);
                                break;
                            case SUCCESS:
                                b.putInt("Type", 3);
                                break;
                            case UNKNOWN:
                                b.putInt("Type", 4);
                                break;
                            case FAILURE:
                                b.putInt("Type", 4);
                                break;
                        }
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                    }
                    if (log == null && logging.isJobDone()) {
                        break;
                    }
                    Thread.sleep(1);
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    protected void onbtnStartClick(View v) {
        //Sync.getInstance().startAsSlave(DB.getInstance().getDatabase().getName(), Method.OVERLAP);
        LogListener l = new LogListener(mHandler);
        l.start();
    }

    @Override
    protected void onbtnSaveClick(View v) {
        try {
            DB.getInstance().getDatabase().saveToDB();
            Toast.makeText(this.getActivity(), "Save completed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this.getActivity(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        int count = DB.getInstance().getDatabase().getWords().size();
        txtTotalCount_.setText(Integer.toString(count, 10));
        mTvDatabase.setText(DB.getInstance().getDatabase().getName());
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                int type = b.getInt("Type");
                switch (type) {
                    case 1:
                        mLogView.addLog(ViewLogViewer.Type.HARMLESS, b.getString("What"));
                        break;
                    case 2:
                        mLogView.addLog(ViewLogViewer.Type.WARNING, b.getString("What"));
                        break;
                    case 3:
                        mLogView.addLog(ViewLogViewer.Type.SUCCESS, b.getString("What"));
                        break;
                    case 4:
                        mLogView.addLog(ViewLogViewer.Type.FAILURE, b.getString("What"));
                        break;
                }

            }
        };
//        com.jp.ma.baseui.LogViewer lv = (com.jp.ma.baseui.LogViewer) v.findViewById(R.id.txtLogViewer);
//
//        String temp = "";
//        for (int i = 0; i < 100; i++) {
//            lv.addLog(String.format("This is a loooooooooooooooooo00000000000000000000ooooooooooooog at line %d...... \r\n", i));
//        }

        return v;
    }
}
