/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import SqliteEngine_Interface.DB.DBDef;
import SqliteEngine_Interface.DB.DBInteger;
import SqliteEngine_Interface.DB.DBString;
import SqliteEngine_Interface.DB.DBTable;
import SqliteEngine_JDBC.JDBC_SQLEngine;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author u0151316
 */
public class JDBCUT {

    public JDBCUT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

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
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void test_000_InitDB() throws Exception {
        File file = new File("TEST.db");
        file.delete();

        JDBC_SQLEngine engine = new JDBC_SQLEngine();
        engine.connect("TEST.db");

        assertEquals(true, engine.isConnected());

        assertEquals(false, engine.isTableExist(DBS.getName()));
        DBS.createNewTable(engine);
        engine.commit();
        assertEquals(true, engine.isTableExist(DBS.getName()));
        engine.close();
    }
}
