/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import JPWord.DBStruct.DB_Setting;
import SqliteEngine_Interface.ISQLEngine;
import SqliteEngine_Interface.ISQLResult;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author u0151316
 */
public class Setting implements ISetting {

    enum Type {
        DB,
        OTF,
    }

    abstract class Tag {

        public String key_ = "";
        public int type_ = TYPE_UNKNOWN;
        public Type db_Type = Type.OTF;

        private Tag() {

        }

        private Tag(ISQLResult rs) throws Exception {
            key_ = DBS.KEY.getValueFromRS(rs);
            type_ = DBS.TYPE.getValueFromRS(rs);
            db_Type = Type.DB;
        }

        public abstract void encode() throws Exception;

        public void saveToDB(String value) throws Exception {
            if (db_Type == Type.DB) {
                DBS.update(DBS.KEY.where(key_), DBS.TYPE.update(type_), DBS.VALUE.update(value));
            } else if (db_Type == Type.OTF) {
                DBS.insert(DBS.KEY.update(key_), DBS.TYPE.update(type_), DBS.VALUE.update(value));
            }
        }
    }

    class StringTag extends Tag {

        public String value_ = null;

        public StringTag() {

        }

        public StringTag(ISQLResult rs) throws Exception {
            super(rs);
            value_ = DBS.VALUE.getValueFromRS(rs);
        }

        @Override
        public void encode() throws Exception {
            saveToDB(value_);
        }
    }

    class ListTag extends Tag {

        public List<String> value_ = new LinkedList<>();

        public ListTag() {

        }

        public ListTag(ISQLResult rs) throws Exception {
            super(rs);
            String value = DBS.VALUE.getValueFromRS(rs);
            String lines[] = value.split("\\" + "\n");
            for (String line : lines) {
                if (!line.equals("")) {
                    value_.add(line);
                }
            }
        }

        @Override
        public void encode() throws Exception {
            String value = "";
            for (String string : value_) {
                value += string;
                value += "\n";
            }
            saveToDB(value);
        }
    }

    public static DB_Setting DBS = new DB_Setting();

    private final static int TYPE_UNKNOWN = 0;
    private final static int TYPE_STRING = 1;
    private final static int TYPE_LIST = 2;

    private Map<String, Tag> tags_ = new HashMap<>();

    @Override
    public List<String> getList(String key) {
        if (tags_.containsKey(key)) {
            Tag value = tags_.get(key);
            if (value.type_ == TYPE_LIST) {
                return ((ListTag) value).value_;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        return tags_.containsKey(key);
    }

    @Override
    public String getString(String key) {
        if (tags_.containsKey(key)) {
            Tag value = tags_.get(key);
            if (value.type_ == TYPE_STRING) {
                return ((StringTag) value).value_;
            }
        }
        return "";
    }

    @Override
    public void setString(String key, String value) throws Exception {
        StringTag tag = null;
        if (containsKey(key)) {
            tag = (StringTag) tags_.get(key);
            if (tag == null) {
                throw new Exception("[JPLIB] The setting key name is existed as different type");
            }
        } else {
            tag = new StringTag();
        }
        tag.key_ = key;
        tag.type_ = TYPE_STRING;
        tag.value_ = value;
        tags_.put(key, tag);
    }

    @Override
    public void setList(String key, List<String> value) throws Exception {
        ListTag tag = null;
        if (containsKey(key)) {
            tag = (ListTag) tags_.get(key);
            if (tag == null) {
                throw new Exception("[JPLIB] The setting key name is existed as different type");
            }
        } else {
            tag = new ListTag();
        }
        tag.key_ = key;
        tag.type_ = TYPE_LIST;
        tag.value_ = value;
        tags_.put(key, tag);
    }

    public void loadFromDB(ISQLEngine engine) throws Exception {
        if (!engine.isConnected()) {
            throw new Exception("SQL is not connected");
        }
        if (!engine.isTableExist(DBS.getName())) {
            DBS.createNewTable(engine);
            engine.commit();
            return;
        }
        DBS.queryAll();
        ISQLResult rs = DBS.executeQuery(engine);

        while (rs.next()) {
            int type = DBS.TYPE.getValueFromRS(rs);
            switch (type) {
                case TYPE_STRING: {
                    StringTag tag = new StringTag(rs);
                    tags_.put(tag.key_, tag);
                    break;
                }
                case TYPE_LIST: {
                    ListTag tag = new ListTag(rs);
                    tags_.put(tag.key_, tag);
                    break;
                }
                default:
                    throw new Exception("[JPLIB] unknown setting type");
            }
        }
    }

    public void saveToDB(ISQLEngine engine) throws Exception {
        for (Map.Entry<String, Tag> entry : tags_.entrySet()) {
            Tag tag = entry.getValue();
            tag.encode();
        }
        DBS.executeChange(engine);
    }

}
