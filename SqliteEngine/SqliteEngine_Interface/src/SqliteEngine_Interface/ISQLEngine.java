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
public interface ISQLEngine {    
    void connect(String dbFilename) throws Exception;
    ISQLResult executeQuery(String sql) throws Exception;
    void addBatch(String sql) throws Exception;
    void executeBatch() throws Exception;
    void close() throws Exception;
    boolean isConnected();
    
    ISQLEngine clone();
    void commit() throws Exception;
}
