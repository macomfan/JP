/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

/**
 *
 * @author U0151316
 */
public class Log {

    public enum Type {
        HARMLESS,
        WARNING,
        SUCCESS,
        UNKNOWN,
        FAILURE,
    }

    private String what_ = "";
    private Type type_ = Type.UNKNOWN;

    public Log(Type type, String what) {
        type_ = type;
        what_ = what;
    }

    public Type type() {
        return type_;
    }

    public String what() {
        return what_;
    }
}
