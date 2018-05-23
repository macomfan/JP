/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqliteEngine_Interface.DB;

/**
 *
 * @author u0151316
 */
public class DBEntity {
    
    public DBDef.Attr attr_ = null;
    public DBDef.Type type_ = null;
    public int size_ = 0;
    
    public int index_ = 0;
    public String name_ = "";
    
    
    public DBEntity(DBDef.Type type, int size, DBDef.Attr attr) {
        type_ = type;
        attr_ = attr;
        size_ = size;
    }
}
