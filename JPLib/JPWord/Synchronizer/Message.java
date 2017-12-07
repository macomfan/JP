/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author u0151316
 */
public class Message {

    public final static byte MSG_DETECT = 0x01;
    public final static byte MSG_SYN = 0x02;
    public final static byte MSG_ACK = 0x03;
    public final static byte MSG_DAT = 0x04;
    public final static byte MSG_BYE = 0x05;
    public final static byte MSG_REP = 0x06;
    public final static byte MSG_UNKNOWN = 0x00;

    private final static String header_ = "#JP#";
    private String value_ = "";
    private byte type_ = MSG_UNKNOWN;
    private Map<String, String> tagMap_ = new HashMap<>();

    public void addTag(String key, String value) {
        tagMap_.put(key, value);
    }

    public String getTag(String key) {
        if (tagMap_.containsKey(key)) {
            return tagMap_.get(key);
        }
        return null;
    }

    public void setValue(String value) {
        value_ = value;
    }

    public String getValue() {
        return value_;
    }

    public int getType() {
        return type_;
    }

    public Message(byte type) {
        type_ = type;
    }

    public Message() {

    }

    public static String headerString() {
        return header_;
    }

    private int basicLength() {
        return header_.length() + Byte.SIZE / 8 + Integer.SIZE / 8;
    }

    public int Parse(ByteBuffer buffer) {
        if (buffer.remaining() < basicLength()) {
            return 0;
        }
        int offset = 0;
        byte[] headerArr = new byte[header_.length()];
        buffer.get(headerArr);
        String header = new String(headerArr);
        if (!header.equals(header_)) {
            return 0;
        }
        type_ = buffer.get();
        int valueSize = buffer.getInt();
        if (buffer.remaining() < valueSize) {
            return 0;
        }
        byte[] valueArr = new byte[valueSize];
        buffer.get(valueArr);
        try {
            value_ = new String(valueArr, "UTF-8");
        } catch (Exception e) {
        }

        int tagSize = buffer.getInt();
        if (buffer.remaining() < tagSize) {
            return 0;
        }
        byte[] tagArr = new byte[tagSize];
        buffer.get(tagArr);
        try {
            String tag = new String(tagArr, "UTF-8");
            // Parse tag
            parseTag(tag);
        } catch (Exception e) {
        }
        return basicLength() + valueSize + tagSize;
    }

    private void parseTag(String tag) {
        String tags[] = tag.split("\\|");
        for (String tag1 : tags) {
            int sp = tag1.indexOf('=');
            if (sp == -1) {
                //error
            } else {
                tagMap_.put(tag1.substring(0, sp), tag1.substring(sp + 1, tag1.length()));
            }
        }
    }

    public byte[] toByteArray() {
        int size = header_.length();
        size += Byte.SIZE / 8;
        size += Integer.SIZE / 8;
        size += Integer.SIZE / 8;
        try {
            byte[] valueBytes = value_.getBytes("UTF-8");
            int valueSize = valueBytes.length;

            String tagString = "";
            for (Map.Entry<String, String> entry : tagMap_.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                tagString += key;
                tagString += "=";
                tagString += value;
                tagString += "|";
            }
            byte[] tagBytes = tagString.getBytes("UTF-8");
            int tagSize = tagBytes.length;

            size += valueSize;
            size += tagSize;

            ByteBuffer buffer = ByteBuffer.allocate(size);
            buffer.put(header_.getBytes(Charset.forName("US-ASCII")));
            buffer.put(type_);
            buffer.putInt(valueSize);
            buffer.put(valueBytes);
            buffer.putInt(tagSize);
            buffer.put(tagBytes);
            return buffer.array();
        } catch (Exception e) {
        }
        return null;
    }

}
