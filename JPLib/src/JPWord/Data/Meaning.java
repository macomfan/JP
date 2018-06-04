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
    private static final String MEAN_EXAMPLE_SEP = "<E>";

    private String meaningInCHS_ = "";
    private String meaningInJP_ = "";
    private List<IExample> examples_ = new LinkedList<>();
    private String type_ = "";
    public Word parent_ = null;

    public Meaning(Word parent) {
        this();
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
    }

    @Override
    public String getInCHS() {
        return meaningInCHS_;
    }

    @Override
    public void setInCHS(String value) {
        meaningInCHS_ = value;
    }

    @Override
    public String getType() {
        return type_;
    }

    @Override
    public void setType(String value) {
        type_ = value;
    }

    @Override
    public void addExample(IExample example) {
        examples_.add(example);
        ((Example) example).parent_ = this;
        if (!((Example) example).isEmpty()) {
            if (parent_ != null) {
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
        if (this.type_.equals("") && this.meaningInCHS_.equals("") && this.meaningInJP_.equals("")) {
            return "";
        }
        line += this.type_ + ":";
        if (!this.meaningInCHS_.equals("")) {
            line += this.meaningInCHS_;
        }
        if (!this.meaningInJP_.equals("")) {
            line += MEAN_SEP + this.meaningInJP_;
        }

        for (IExample example : this.examples_) {
            line += MEAN_EXAMPLE_SEP;
            line += example.encodeToString();
        }
        return line;
    }

    @Override
    public boolean decodeFromString(String str) {
        String[] meaningItems = str.split("\\" + MEAN_EXAMPLE_SEP);
        if (meaningItems.length > 0) {
            String meaningString = meaningItems[0];
            if (meaningString.length() == 0) {
                return false;
            }
            meaningString = meaningString.replace('/', '\\');
            int typeIndex = meaningString.indexOf(TYPE_SEP);
            String typeString = meaningString.substring(0, typeIndex);
            meaningString = meaningString.substring(typeIndex + 1);
            this.type_ = typeString.trim();

            String[] meaningCHSAndJP = meaningString.split("\\" + MEAN_SEP);
            if (meaningCHSAndJP.length > 0) {
                this.meaningInCHS_ = meaningCHSAndJP[0].trim();
            }
            if (meaningCHSAndJP.length > 1) {
                this.meaningInJP_ = meaningCHSAndJP[1].trim();
            }
        }
        for (int i = 1; i < meaningItems.length; i++) {
            Example example = new Example();
            if (example.decodeFromString(meaningItems[i])) {
                if (!example.isEmpty()) {
                    this.examples_.add(example);
                    example.parent_ = this;
                }
            }
        }
        return true;
    }

}
