/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.util.List;
import java.util.LinkedList;

/**
 *
 * @author u0151316
 */
class Meaning implements IMeaning {

    private static final String TYPE_SEP = ":";
    private static final String MEAN_SEP = "\\";
    private static final String MEAN_EXAMPLE_SEP = "-";

    private String meaningInCHS_ = "";
    private String meaningInJP_ = "";
    private List<IExample> examples_ = new LinkedList<>();
    private String type_ = "";
    public Word parent_ = null;

    public Meaning(Word parent) {
        parent_ = parent;
    }

    public Meaning() {
    }

    @Override
    public boolean isEmpty() {
        if (meaningInCHS_.isEmpty() && meaningInJP_.isEmpty() && type_.isEmpty()) {
            for (IExample iExample : examples_) {
                if (!iExample.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String getInJP() {
        return meaningInJP_;
    }

    @Override
    public void setInJP(String value) {
        meaningInJP_ = value;
        parent_.updatedFlag();
    }

    @Override
    public String getInCHS() {
        return meaningInCHS_;
    }

    @Override
    public void setInCHS(String value) {
        meaningInCHS_ = value;
        parent_.updatedFlag();
    }

    @Override
    public String getType() {
        return type_;
    }

    @Override
    public void setType(String value) {
        type_ = value;
        parent_.updatedFlag();
    }

    @Override
    public void addExample(IExample example) {
        examples_.add(example);
        ((Example) example).parent_ = this;
        if (!((Example) example).isEmpty()) {
            if (parent_ != null) {
                parent_.updatedFlag();
            }
        }
    }

    @Override
    public List<IExample> getExamples() {
        List<IExample> temp = new LinkedList<>();
        for (IExample e : examples_) {
            temp.add(e);
        }
        return temp;
    }

    @Override
    public String encodeToString() {
        String line = "";
        if (type_.equals("") && meaningInCHS_.equals("") && meaningInJP_.equals("")) {
            return "";
        }
        line += type_ + ":";
        if (!meaningInCHS_.equals("")) {
            line += meaningInCHS_;
        }
        if (!meaningInJP_.equals("")) {
            line += "\\" + meaningInJP_;
        }

        for (IExample example : examples_) {
            line += "-";
            line += example.encodeToString();
        }
        return line;
    }

    @Override
    public boolean decodeFromString(String str) {
        String[] meaningItems = str.split("\\" + MEAN_EXAMPLE_SEP);
        IMeaning mean = null;
        if (meaningItems.length > 0) {
            String meaningString = meaningItems[0];
            if (meaningString.length() == 0) {
                return false;
            }
            meaningString = meaningString.replace('/', '\\');
            int typeIndex = meaningString.indexOf(TYPE_SEP);
            String typeString = meaningString.substring(0, typeIndex);
            meaningString = meaningString.substring(typeIndex + 1);
            type_ = typeString.trim();
            if (!type_.equals("")) {
                Constant.getInstance().addType(type_);
            }
            String[] meaningCHSAndJP = meaningString.split("\\" + MEAN_SEP);
            if (meaningCHSAndJP.length > 0) {
                meaningInCHS_ = meaningCHSAndJP[0].trim();
            }
            if (meaningCHSAndJP.length > 1) {
                meaningInJP_ = meaningCHSAndJP[1].trim();
            }
        }
        for (int i = 1; i < meaningItems.length; i++) {
            IExample example = new Example();
            if (example.decodeFromString(meaningItems[i])) {
                addExample(example);
            }
        }
        return true;
    }

}
