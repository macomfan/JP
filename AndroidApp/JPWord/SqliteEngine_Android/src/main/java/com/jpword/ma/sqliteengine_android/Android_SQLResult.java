package com.jpword.ma.sqliteengine_android;

import android.database.Cursor;
import SqliteEngine_Interface.ISQLResult;

/**
 * Created by u0151316 on 5/29/2018.
 */

public class Android_SQLResult implements ISQLResult {
    private Cursor cursor_ = null;

    public Android_SQLResult(Cursor c) {
        cursor_ = c;
    }

    @Override
    public boolean next() throws Exception {
        return cursor_.moveToNext();
    }

    @Override
    public String getString(int i) throws Exception {
        return cursor_.getString(i);
    }

    @Override
    public String getString(String s) throws Exception {
        int index = cursor_.getColumnIndex(s);
        return cursor_.getString(index);
    }

    @Override
    public int getInteger(int i) throws Exception {
        return cursor_.getInt(i);
    }

    @Override
    public int getInteger(String s) throws Exception {
        int index = cursor_.getColumnIndex(s);
        return cursor_.getInt(index);
    }
}
