package com.jpword.ma.baseui;

/**
 * Created by u0151316 on 1/31/2018.
 */

public class WidgetMessage {
    public static final String FROM_DB_SERVICE = "JPWord.FROM_DB_SERVICE";
    public static final String TO_DB_SERVICE = "JPWord.TO_DB_SERVICE";
    public static final String USER_ACTION = "USER_ACTION";

    public static class Action {
        public static int NEXT = 0x01;
        public static int PREV = 0x02;
        public static int PASS = 0x03;
        public static int HINT = 0x04;
        public static int FAIL = 0x05;

        public static int INIT = 0x06;
        public static int DATA = 0x07;
    }


}
