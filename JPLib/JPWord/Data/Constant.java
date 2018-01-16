/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class Constant {
    
    private static Constant instance_;
    private List<String> tones_ = new LinkedList<>();
    private List<String> types_ = new LinkedList<>();
    private List<String> classes_ = new LinkedList<>();

    private Constant() {

    }

    public List<String> getTypes() {
        return types_;
    }

    public List<String> getClasses() {
        return classes_;
    }

    public static Constant getInstance() {
        if (instance_ == null) {
            instance_ = new Constant();
        }
        return instance_;
    }

    public void addTone(String tone) {
        if (tone == null || tone.equals("")) {
            return;
        }
        if (!tones_.contains(tone)) {
            tones_.add(tone);
        }
        Collections.sort(tones_);
    }

    public void addType(String type) {
        if (type == null || type.equals("")) {
            return;
        }
        if (!types_.contains(type)) {
            types_.add(type);
        }
        Collections.sort(types_);
    }

    public void addClass(String cls) {
        if (cls == null || cls.equals("")) {
            return;
        }
        if (!classes_.contains(cls)) {
            classes_.add(cls);
        }
        Collections.sort(classes_);
    }
}
