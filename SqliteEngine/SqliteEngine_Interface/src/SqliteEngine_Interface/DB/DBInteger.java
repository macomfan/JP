/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqliteEngine_Interface.DB;

import SqliteEngine_Interface.ISQLResult;

/**
 *
 * @author u0151316
 */
public class DBInteger extends DBEntity {

    public DBInteger(DBDef.Type type, int size, DBDef.Attr attr) {
        super(type, size, attr);
    }

    public int getValueFromRS(ISQLResult rs) throws Exception {
        return rs.getInteger(name_);
    }

    public OP_Update updateRaw(int value) {
        return new OP_Update(this, Integer.toString(value));
    }

    public OP_Update update(int value) {
        return new OP_Update(this, Integer.toString(value));
    }

    public OP_Where where(int value) {
        return new OP_Where(this, Integer.toString(value));
    }
}
