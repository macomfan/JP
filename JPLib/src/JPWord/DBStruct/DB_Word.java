/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.DBStruct;

import SqliteEngine_Interface.DB.*;

/**
 *
 * @author u0151316
 */
public class DB_Word extends DBTable {

    public DBString GUID = new DBString(DBDef.Type.NCHAR, 36, DBDef.Attr.combinAttr(DBDef.Attr.NOT_NULL, DBDef.Attr.UNIQUE));
    public DBString CONTENT = new DBString(DBDef.Type.VARCHAR, 255, DBDef.Attr.NONE);
    public DBString KANA = new DBString(DBDef.Type.VARCHAR, 255, DBDef.Attr.NONE);
    public DBString TONE = new DBString(DBDef.Type.VARCHAR, 20, DBDef.Attr.NONE);
    public DBString NOTE = new DBString(DBDef.Type.TEXT, 0, DBDef.Attr.NONE);
    public DBString MEANS = new DBString(DBDef.Type.TEXT, 0, DBDef.Attr.NONE);
    public DBInteger CLASS = new DBInteger(DBDef.Type.INT, 0, DBDef.Attr.NONE);
    public DBInteger SKILL = new DBInteger(DBDef.Type.INT, 0, DBDef.Attr.NONE);
    public DBString REVIEWDATE = new DBString(DBDef.Type.VARCHAR, 8, DBDef.Attr.NONE);
    public DBString TAGS = new DBString(DBDef.Type.TEXT, 0, DBDef.Attr.NONE);

    public DB_Word() {
        super("WORD");
        addEntity(GUID, "GUID");
        addEntity(CONTENT, "CONTENT");
        addEntity(KANA, "KANA");
        addEntity(TONE, "TONE");
        addEntity(NOTE, "NOTE");
        addEntity(MEANS, "MEANS");
        addEntity(CLASS, "CLASS");
        addEntity(SKILL, "SKILL");
        addEntity(REVIEWDATE, "REVIEWDATE");
        addEntity(TAGS, "TAGS");
    }
}
