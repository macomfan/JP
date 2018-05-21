package SqliteEngine_Interface;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author u0151316
 */
public interface ISQLSegment {
    void bindInt(int index, int value);
    void bindString(int index, String value);
    void bindLong(int index, long value);
    void bindBlob(int index, byte[] value);
    
    void execute();
    
}
