/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqliteEngine_Interface.DB;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import SqliteEngine_Interface.ISQLEngine;
import SqliteEngine_Interface.ISQLResult;
import java.nio.ByteBuffer;

/**
 *
 * @author u0151316
 */
public class DBTable {

    final private List<DBEntity> entities_ = new LinkedList<>();
    private String name_ = "";

    private String queryString_ = "";
    final private Map<String, Map<String, String>> updateMap_ = new HashMap<>();
    final private List<String> insertList_ = new LinkedList<>();

    public DBTable(String name) {
        name_ = name;
    }

    final public void createNewTable(ISQLEngine engine) throws Exception {
        String createTableString = "CREATE TABLE " + name_ + " (";
        int i = 0;
        for (DBEntity dBEntity : entities_) {
            createTableString += dBEntity.name_ + " ";
            createTableString += dBEntity.type_.toTypeString() + " ";
            if (dBEntity.size_ != 0) {
                createTableString += "(" + Integer.toString(dBEntity.size_) + ") ";
            }
            createTableString += dBEntity.attr_.toAttrString();
            if (++i < entities_.size()) {
                createTableString += ", ";
            }
        }
        createTableString += ")";
        engine.addBatch(createTableString);
        engine.executeBatch();
    }

    final public void addEntity(DBEntity entity, String name) {
        entity.index_ = entities_.size();
        entity.name_ = name;
        entities_.add(entity);
    }

    public ISQLResult executeQuery(ISQLEngine engine) throws Exception {
        if ("".equals(queryString_)) {
            throw new Exception("[JPWORD] No query need executed");
        }
        ISQLResult rs = engine.executeQuery(queryString_);
        queryString_ = "";
        return rs;
    }

    private String parseUpdateForEachItem(Map<String, String> entityMap) {
        if (entityMap.isEmpty()) {
            return "";
        }
        String updateString = "UPDATE " + name_ + " SET ";
        int i = 0;
        for (Map.Entry<String, String> entry : entityMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            updateString += key + " = " + value;
            if (++i < entityMap.size()) {
                updateString += " , ";
            }
        }
        return updateString;
    }

    public void executeChange(ISQLEngine engine) throws Exception {
        for (Map.Entry<String, Map<String, String>> entry : updateMap_.entrySet()) {
            String whereString = entry.getKey();
            Map<String, String> value = entry.getValue();
            String updateString = parseUpdateForEachItem(value);
            if (updateString.equals("")) {
                throw new Exception("[JPWORD] No real update");
            }
            updateString += " ";
            updateString += whereString;
            engine.addBatch(updateString);
        }
        for (String insertString : insertList_) {
            engine.addBatch(insertString);
        }
        insertList_.clear();
        updateMap_.clear();
    }

    public String getName() {
        return name_;
    }

    public void queryAll() throws Exception {
        if (!"".equals(queryString_)) {
            throw new Exception("[JPWORD] A pending query is exist");
        }
        queryString_ = "SELECT * FROM " + name_ + " order by " + name_ + ".rowid";
    }

    public void insert(OP_Update... updates) {
        if (updates.length == 0) {
            return;
        }
        String insertString = "INSERT INTO " + name_ + " ";
        String columnString = "(";
        String valueString = "(";
        int i = 0;
        for (OP_Update update : updates) {
            columnString += update.entity_.name_;
            valueString += update.value_;
            if (++i < updates.length) {
                columnString += " , ";
                valueString += " , ";
            }
        }
        columnString += ")";
        valueString += ")";
        insertString = insertString + columnString + " VALUES " + valueString;
        insertList_.add(insertString);
    }

    public byte[] encodeToBytes(OP_Update... updates) {
        int size = 0;
        for (OP_Update update : updates) {
            size += 8;
            size += update.entity_.name_.length();
            size += update.value_.length();
        }
        try {
            ByteBuffer buffer = ByteBuffer.allocate(size);
            for (OP_Update update : updates) {
                buffer.putInt(update.entity_.name_.length());
                buffer.putInt(update.value_.length());
                buffer.put(update.entity_.name_.getBytes("UTF-8"));
                buffer.put(update.value_.getBytes("UTF-8"));
            }
            return buffer.array();
        } catch (Exception e) {
        }
        return null;
    }

    public void update(OP_Where where, OP_Update... updates) {
        if (updates.length == 0) {
            return;
        }
        Map<String, String> currentItemUpdateMap = null;
        String whereString = "WHERE " + where.entity_.name_ + " = " + where.value_;
        if (!updateMap_.containsKey(whereString)) {
            currentItemUpdateMap = new HashMap<>();
            updateMap_.put(whereString, currentItemUpdateMap);
        } else {
            currentItemUpdateMap = updateMap_.get(whereString);
        }
        for (OP_Update update : updates) {
            currentItemUpdateMap.put(update.entity_.name_, update.value_);
        }
    }
}
