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
public interface ISQLResult {
    boolean next() throws Exception;
    
    String getString(int index) throws Exception;
    String getString(String colname) throws Exception;
    
    int getInteger(int index) throws Exception;
    int getInteger(String colname) throws Exception;
    
}
