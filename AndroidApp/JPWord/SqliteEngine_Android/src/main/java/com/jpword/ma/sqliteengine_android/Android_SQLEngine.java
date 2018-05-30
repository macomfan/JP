package com.jpword.ma.sqliteengine_android;

/**
 * Created by u0151316 on 5/29/2018.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import SqliteEngine_Interface.*;

public class Android_SQLEngine implements ISQLEngine {
    private SQLiteDatabase connection_ = null;
    private List<String> batchSQL_ = new LinkedList<>();
    private boolean penddingTransaction_ = false;

    @Override
    public void connect(String dbname) throws Exception {
        connection_ = SQLiteDatabase.openOrCreateDatabase(dbname, null);
    }

    @Override
    public boolean isTableExist(String tablename) throws Exception {
        ISQLResult rs = executeQuery("SELECT * FROM sqlite_master WHERE type = 'table' and name ='" + tablename + "'");
        if (rs.next() != false) {
            return true;
        }
        return false;
    }

    @Override
    public ISQLResult executeQuery(String sql) throws Exception {
        Cursor c = connection_.rawQuery(sql, null);
        return new Android_SQLResult(c);
    }

    @Override
    public void addBatch(String sql) throws Exception {
        batchSQL_.add(sql);
    }

    @Override
    public void executeBatch() throws Exception {
        if (!batchSQL_.isEmpty()) {
            if (!penddingTransaction_) {
                connection_.beginTransaction();
            }
            for (String sql : batchSQL_) {
                connection_.execSQL(sql);
            }
            batchSQL_.clear();
            penddingTransaction_ = true;
        }
    }

    @Override
    public void close() throws Exception {
        if (penddingTransaction_) {
            connection_.endTransaction();
        }
        penddingTransaction_ = false;
        connection_.close();
        connection_ = null;
    }

    @Override
    public boolean isConnected() {
        return connection_ != null;
    }

    @Override
    public ISQLEngine clone() {
        return new Android_SQLEngine();
    }

    @Override
    public void commit() throws Exception {
        if (penddingTransaction_) {
            connection_.setTransactionSuccessful();
            connection_.endTransaction();
            penddingTransaction_ = false;
        }
    }
}
