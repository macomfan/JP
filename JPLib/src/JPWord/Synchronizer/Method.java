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
public class Method {

    public final static Method REBASE_TO_MASTER = new Method("REBASE_TO_MASTER");
    public final static Method REBASE_FROM_MASTER = new Method("REBASE_FROM_MASTER");
    // public final static Method AUTO_SYNC = new Method("AUTO_SYNC"); // Not supported
    public final static Method OVERLAP = new Method("OVERLAP");

    private String value_;

    public Method(String value) {
        value_ = value;
    }

    public String getStringValue() {
        return value_;
    }

    public boolean is(String value) {
        if (value_.equals(value)) {
            return true;
        }
        return false;
    }

    public boolean is(Method method) {
        return is(method.getStringValue());
    }
}
