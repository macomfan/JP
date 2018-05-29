/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqliteEngine_Interface.DB;

/**
 *
 * @author u0151316
 */
public class DBDef {

    public static class Type {

        public static Type TEXT = new Type("TEXT");
        public static Type INT = new Type("INT");
        public static Type VARCHAR = new Type("VARCHER");
        public static Type NCHAR = new Type("NCHAR");

        private Type(String typeString) {
            tyString_ = typeString;
        }

        private String tyString_ = "";

        public String toTypeString() {
            return tyString_;
        }
    }

    public static class Attr {

        public static Attr NONE = new Attr(0, "");
        public static Attr NOT_NULL = new Attr(0x01, "NOT NULL");
        public static Attr UNIQUE = new Attr(0x02, "UNIQUE");

        private int value_ = 0;
        private String stringValue_ = "";
        
        private Attr(int value, String stringValue) {
            value_ = value;
            stringValue_ = stringValue;
        }
        
        public static Attr combinAttr(Attr... attrs) {
            Attr res = new Attr(0, "");
            for (Attr attr : attrs) {
                res.value_ = res.value_ | attr.value_;
                res.stringValue_ += attr.stringValue_;
                res.stringValue_ += " ";
            }
            return res;
        }
        
        public String toAttrString() {
            return stringValue_;
        } 
    }
}
