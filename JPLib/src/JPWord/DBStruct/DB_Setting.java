/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.DBStruct;

import SqliteEngine_Interface.DB.DBDef;
import SqliteEngine_Interface.DB.DBInteger;
import SqliteEngine_Interface.DB.DBString;
import SqliteEngine_Interface.DB.DBTable;

/**
 *
 * @author u0151316
 */
public class DB_Setting extends DBTable {

    public DBString KEY = new DBString(DBDef.Type.TEXT, 0, DBDef.Attr.combinAttr(DBDef.Attr.NOT_NULL, DBDef.Attr.UNIQUE));
    public DBInteger TYPE = new DBInteger(DBDef.Type.INT, 0, DBDef.Attr.NONE);
    public DBString VALUE = new DBString(DBDef.Type.TEXT, 0, DBDef.Attr.NONE);

    public DB_Setting() {
        super("SETTING");
        addEntity(KEY, "KEY");
        addEntity(TYPE, "TYPE");
        addEntity(VALUE, "VALUE");
    }
}
