/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqliteEngine_JDBC;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import SqliteEngine_Interface.*;

/**
 *
 * @author u0151316
 */
public class JDBC_SQLEngine implements ISQLEngine {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
        }

    }
    private Connection connection_ = null;
    private List<String> batchSQL_ = new LinkedList<>();
    private Statement queryStatement = null;
    private Statement batchStatement = null;

    public JDBC_SQLEngine() {

    }

    @Override
    public ISQLEngine clone() {
        return new JDBC_SQLEngine();
    }

    @Override
    public void connect(String dbFilename) throws Exception {
        try {
            connection_ = DriverManager.getConnection("jdbc:sqlite:" + dbFilename);
            connection_.setAutoCommit(false);
        } catch (Exception e) {
            connection_ = null;
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isTableExist(String tablename) throws Exception {
        ISQLResult rs = executeQuery("SELECT * FROM sqlite_master WHERE type = 'table' and name ='" + tablename + "'");
        if (rs.next() != false) {
            return true;
        }
        return false;
    }

    public ISQLResult executeQuery(String sql) throws Exception {
        if (queryStatement != null) {
            queryStatement.close();
        }
        queryStatement = connection_.createStatement();
        ResultSet rs = queryStatement.executeQuery(sql);
        return new JDBC_SQLResult(rs);
    }

    public void addBatch(String sql) throws Exception {
        batchSQL_.add(sql);
    }

    @Override
    public void executeBatch() throws Exception {
        if (!batchSQL_.isEmpty()) {
            Statement s = connection_.createStatement();
            for (String sql : batchSQL_) {
                s.addBatch(sql);
            }
            s.executeBatch();
            batchSQL_.clear();
        }
    }

    @Override
    public void close() throws Exception {
        batchSQL_.clear();
        if (queryStatement != null) {
            queryStatement.close();
            queryStatement = null;
        }
        if (connection_ != null) {
            connection_.close();
        }
    }

    @Override
    public boolean isConnected() {
        return connection_ != null;
    }

    @Override
    public void commit() throws Exception {
        connection_.commit();
    }

}
