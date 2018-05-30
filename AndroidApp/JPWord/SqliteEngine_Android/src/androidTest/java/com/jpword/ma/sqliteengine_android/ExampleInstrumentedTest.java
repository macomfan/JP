package com.jpword.ma.sqliteengine_android;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;

import SqliteEngine_Interface.DB.DBDef;
import SqliteEngine_Interface.DB.DBInteger;
import SqliteEngine_Interface.DB.DBString;
import SqliteEngine_Interface.DB.DBTable;
import SqliteEngine_Interface.ISQLResult;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExampleInstrumentedTest {


    static class DB_Test extends DBTable {

        public DBString KEY = new DBString(DBDef.Type.TEXT, 0, DBDef.Attr.combinAttr(DBDef.Attr.NOT_NULL, DBDef.Attr.UNIQUE));
        public DBInteger TYPE = new DBInteger(DBDef.Type.INT, 0, DBDef.Attr.NONE);
        public DBString VALUE = new DBString(DBDef.Type.TEXT, 0, DBDef.Attr.NONE);

        public DB_Test() {
            super("test");
            addEntity(KEY, "KEY");
            addEntity(TYPE, "TYPE");
            addEntity(VALUE, "VALUE");
        }
    }

    private static DB_Test DBS = new DB_Test();

    @Test
    public void test_000_InitDB() throws Exception {
        File file = new File(InstrumentationRegistry.getTargetContext().getFilesDir().getCanonicalPath() + "//TEST.db");
        file.delete();

        SQLiteDatabase connection = SQLiteDatabase.openOrCreateDatabase(file.getCanonicalPath(), null);
        connection.beginTransaction();
        connection.endTransaction();
        connection.close();



        // Context of the app under test.
        Android_SQLEngine engine = new Android_SQLEngine();
        engine.connect(file.getCanonicalPath());
        assertEquals(true, engine.isConnected());

        assertEquals(false, engine.isTableExist(DBS.getName()));
        DBS.createNewTable(engine);
        engine.commit();
        assertEquals(true, engine.isTableExist(DBS.getName()));
        engine.close();
    }

    @Test
    public void test_010_InsertToDB() throws Exception {
        File file = new File(InstrumentationRegistry.getTargetContext().getFilesDir().getCanonicalPath() + "//TEST.db");
        // Context of the app under test.
        Android_SQLEngine engine = new Android_SQLEngine();
        engine.connect(file.getCanonicalPath());
        assertEquals(true, engine.isConnected());

        DBS.insert(DBS.KEY.update("1"), DBS.TYPE.update(1), DBS.VALUE.update("1.1"));
        DBS.executeChange(engine);
        engine.executeBatch();

        DBS.queryAll();
        ISQLResult rs = DBS.executeQuery(engine);
        while (rs.next()) {
            String key = DBS.KEY.getValueFromRS(rs);
            int type = DBS.TYPE.getValueFromRS(rs);
            String value = DBS.VALUE.getValueFromRS(rs);
            assertEquals("1", key);
            assertEquals(1, type);
            assertEquals("1.1", value);
        }
        engine.commit();
        engine.close();
}

    @Test
    public void test_020_MissingCommitDB() throws Exception {
        File file = new File(InstrumentationRegistry.getTargetContext().getFilesDir().getCanonicalPath() + "//TEST.db");
        // Context of the app under test.
        Android_SQLEngine engine = new Android_SQLEngine();
        engine.connect(file.getCanonicalPath());
        assertEquals(true, engine.isConnected());

        DBS.insert(DBS.KEY.update("2"), DBS.TYPE.update(2), DBS.VALUE.update("2.2"));
        DBS.executeChange(engine);
        engine.executeBatch();

        DBS.queryAll();
        ISQLResult rs = DBS.executeQuery(engine);
        rs.next();
        rs.next();
        String key = DBS.KEY.getValueFromRS(rs);
        int type = DBS.TYPE.getValueFromRS(rs);
        String value = DBS.VALUE.getValueFromRS(rs);
        assertEquals("2", key);
        assertEquals(2, type);
        assertEquals("2.2", value);
        engine.close();
    }

    @Test
    public void test_030_CommitDB() throws Exception {
        File file = new File(InstrumentationRegistry.getTargetContext().getFilesDir().getCanonicalPath() + "//TEST.db");
        // Context of the app under test.
        Android_SQLEngine engine = new Android_SQLEngine();
        engine.connect(file.getCanonicalPath());
        assertEquals(true, engine.isConnected());

        DBS.queryAll();
        ISQLResult rs = DBS.executeQuery(engine);
        assertEquals(true, rs.next());
        assertEquals(false, rs.next());

        DBS.insert(DBS.KEY.update("2"), DBS.TYPE.update(2), DBS.VALUE.update("2.2"));
        DBS.executeChange(engine);
        engine.executeBatch();
        engine.commit();
        engine.close();
    }

    @Test
    public void test_040_ConfirmCommitDB() throws Exception {
        File file = new File(InstrumentationRegistry.getTargetContext().getFilesDir().getCanonicalPath() + "//TEST.db");
        // Context of the app under test.
        Android_SQLEngine engine = new Android_SQLEngine();
        engine.connect(file.getCanonicalPath());
        assertEquals(true, engine.isConnected());

        DBS.queryAll();
        ISQLResult rs = DBS.executeQuery(engine);
        assertEquals(true, rs.next());
        assertEquals(true, rs.next());
        String key = DBS.KEY.getValueFromRS(rs);
        int type = DBS.TYPE.getValueFromRS(rs);
        String value = DBS.VALUE.getValueFromRS(rs);
        assertEquals("2", key);
        assertEquals(2, type);
        assertEquals("2.2", value);
        engine.close();
    }
}
