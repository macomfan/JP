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
public class OP_Where {
    public DBEntity entity_;
    public String value_;
    
    public OP_Where(DBEntity entity, String value) {
        entity_ = entity;
        value_ = value;
    }
}
