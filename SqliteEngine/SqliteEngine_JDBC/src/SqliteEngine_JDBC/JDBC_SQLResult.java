/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqliteEngine_JDBC;

import SqliteEngine_Interface.*;
import java.sql.*;

/**
 *
 * @author u0151316
 */
public class JDBC_SQLResult implements ISQLResult {
    private ResultSet resultSet_ = null;
    
    public JDBC_SQLResult(ResultSet rs){
        resultSet_ = rs;
    }

    @Override
    public boolean next() throws Exception {
        return resultSet_.next();
    }

    @Override
    public String getString(int index) throws Exception {
        return resultSet_.getString(index);
    }

    @Override
    public String getString(String colname) throws Exception {
        return resultSet_.getString(colname);
    }

    @Override
    public int getInteger(int index) throws Exception {
        return resultSet_.getInt(index);
    }

    @Override
    public int getInteger(String colname) throws Exception {
        return resultSet_.getInt(colname);
    }
    
    
}
