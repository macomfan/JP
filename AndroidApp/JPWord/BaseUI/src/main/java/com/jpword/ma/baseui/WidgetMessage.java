package com.jpword.ma.baseui;

/**
 * Created by u0151316 on 1/31/2018.
 */

public class WidgetMessage {
    public static final String FROM_DB_SERVICE = "JPWord.FROM_DB_SERVICE";
    public static final String TO_DB_SERVICE = "JPWord.TO_DB_SERVICE";
    public static final String USER_ACTION = "USER_ACTION";
    public static final String DATA_WORD_CONTENT = "Content";
    public static final String DATA_WORD_KANA = "Kana";
    public static final String DATA_WORD_DISP_SETTING = "Disp";
    public static final String DATA_WORD_ROMA = "Roma";
    public static final String DATA_WORD_IMI = "IMI";
    public static final String DATA_WORD_COUNT = "Count";
    public static final String DATA_CURRENT_INDEX = "CurrentIndex";


    public static class Action {
        public static final int NEXT = 0x01;
        public static final int PREV = 0x02;
        public static final int PASS = 0x03;
        public static final int HINT = 0x04;
        public static final int FAIL = 0x05;

        public static final int DATA = 0x07;

        public static final int READY = 0xFD;
        public static final int CLOSE = 0xFE;
        public static final int INVALID = 0xFB;
        //public static int HEARTBEAT = 0xFF;

        public static String messageToString(int message) {
            switch (message) {
                case NEXT:
                    return "NEXT";
                case PREV:
                    return "PREV";
                case PASS:
                    return "PASS";
                case HINT:
                    return "HINT";
                case FAIL:
                    return "FAIL";
                case DATA:
                    return "DATA";
                case READY:
                    return "READY";
                case CLOSE:
                    return "CLOSE";
                case INVALID:
                    return "INVALID";
            }
            return "";
        }

    }


}
