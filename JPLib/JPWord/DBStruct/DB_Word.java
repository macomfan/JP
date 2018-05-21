/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.DBStruct;

import sqliteEngine_interface.DB.*;

/**
 *
 * @author u0151316
 */
public class DB_Word extends DBTable {

    public DBString GUID = new DBString();
    public DBString CONTENT = new DBString();
    public DBString KANA = new DBString();
    public DBString TONE = new DBString();
    public DBString NOTE = new DBString();
    public DBString MEANS = new DBString();
    public DBInteger CLASS = new DBInteger();
    public DBInteger SKILL = new DBInteger();
    public DBString REVIEWDATE = new DBString();
    public DBString TAGS = new DBString();

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
