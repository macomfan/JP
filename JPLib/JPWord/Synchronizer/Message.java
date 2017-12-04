/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;

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

    public String getValue() {
        return value_;
    }

    public void setValue(String value) {
        value_ = value;
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update(value.getBytes("UTF-8"));
//            byte[] ss = md.();
//            int a = 0;
//        } catch (Exception e) {
//        }
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
        int size = buffer.getInt();
        if (buffer.remaining() < size) {
            return 0;
        }
        byte[] valueArr = new byte[size];
        buffer.get(valueArr);
        try {
            value_ = new String(valueArr, "UTF-8");
        } catch (Exception e) {
        }
        return basicLength() + size;
    }

//    public int Parse(byte[] buf, int length) {
//        if (length < basicLength() || buf.length < basicLength()) {
//            return 0;
//        }
//        int offset = 0;
//        String header = new String(buf, offset + length, header_.length(), Charset.forName("US-ASCII"));
//        offset += header.length();
//        type_ = buf[offset + length];
//        offset++;
//        int size = buf[offset + length + 3] & 0xFF
//                | (buf[offset + length + 2] & 0xFF) << 8
//                | (buf[offset + length + 1] & 0xFF) << 16
//                | (buf[offset + length] & 0xFF) << 24;
//        offset += Integer.SIZE / 8;
//        value_ = new String(buf, offset + length, size, Charset.forName("UTF-8"));
//        offset += size;
//        return offset;
//    }
    public byte[] toByteArray() {
        int size = header_.length();
        size += Byte.SIZE / 8;
        size += Integer.SIZE / 8;
        try {
            byte[] valueBytes = value_.getBytes("UTF-8");
            int valueSize = valueBytes.length;
            size += valueSize;
            ByteBuffer buffer = ByteBuffer.allocate(size);
            buffer.put(header_.getBytes(Charset.forName("US-ASCII")));
            buffer.put(type_);
            buffer.putInt(valueSize);
            buffer.put(valueBytes);
            return buffer.array();
        } catch (Exception e) {
        }
        return null;
    }

}
