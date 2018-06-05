/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqliteEngine_Interface.DB;

import SqliteEngine_Interface.ISQLResult;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class Bytes_SQLResult implements ISQLResult {

    class StringEntity {

        public String key_;
        public String value_;
    }

    private List<StringEntity> entities_ = new LinkedList<>();
    private int index_ = -1;

    public Bytes_SQLResult(byte[] bytes) {
        if (bytes.length == 0) {
            return;
        }
        try {
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes, 0, bytes.length);
            buffer.rewind();
            while (buffer.remaining() != 0) {
                int keySize = buffer.getInt();
                int valueSize = buffer.getInt();

                byte[] key = new byte[keySize];
                buffer.get(key);
                String keyString = new String(key, "UTF-8");

                byte[] value = new byte[valueSize];
                buffer.get(value);
                String valueString = new String(value, "UTF-8");

                StringEntity entity = new StringEntity();
                entity.key_ = keyString;
                entity.value_ = valueString;
                entities_.add(entity);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean next() throws Exception {
        if (index_ == entities_.size()) {
            return false;
        }
        index_++;
        return true;
    }

    @Override
    public String getString(int index) throws Exception {
        if (index > entities_.size()) {
            throw new Exception("[SQLEngine] Index overload");
        }
        StringEntity entity = entities_.get(index);
        return entity.value_;
    }

    @Override
    public String getString(String colname) throws Exception {
        for (StringEntity stringEntity : entities_) {
            if (stringEntity.key_.equals(colname)) {
                return stringEntity.value_;
            }
        }
        throw new Exception("[SQLEngine] No column");
    }

    @Override
    public int getInteger(int index) throws Exception {
        if (index > entities_.size()) {
            throw new Exception("[SQLEngine] Index overload");
        }
        StringEntity entity = entities_.get(index);
        return Integer.parseInt(entity.value_);
    }

    @Override
    public int getInteger(String colname) throws Exception {
        for (StringEntity stringEntity : entities_) {
            if (stringEntity.key_.equals(colname)) {
                return Integer.parseInt(stringEntity.value_);
            }
        }
        throw new Exception("[SQLEngine] No column");
    }

}
