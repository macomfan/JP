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
public class DBString extends DBEntity {

    private int size_ = 0;

    public DBString(DBDef.Type type, int size, DBDef.Attr attr) {
        super(type, size, attr);
    }

    public String getValueFromRS(ISQLResult rs) throws Exception {
        String ret = rs.getString(name_);
        if (ret == null) {
            return "";
        }
        return ret;
    }

    public OP_Update updateRaw(String value) {
        return new OP_Update(this, value);
    }

    public OP_Update update(String value) {
        value = value.replace("'", "''");
        return new OP_Update(this, "'" + value + "'");
    }

    public OP_Where where(String value) {
        return new OP_Where(this, "'" + value + "'");
    }
}
