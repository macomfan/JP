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

    public static class Codec_V1 implements ICodec {

        private static final String TYPE_SEP = ":";
        private static final String MEAN_SEP = "\\";
        private static final String MEAN_EXAMPLE_SEP = "-";

        public String encodeToString(Object obj) {
            Meaning mean = (Meaning)obj;
            String line = "";
            if (mean.type_.equals("") && mean.meaningInCHS_.equals("") && mean.meaningInJP_.equals("")) {
                return "";
            }
            line += mean.type_ + ":";
            if (!mean.meaningInCHS_.equals("")) {
                line += mean.meaningInCHS_;
            }
            if (!mean.meaningInJP_.equals("")) {
                line += "\\" + mean.meaningInJP_;
            }

            for (IExample example : mean.examples_) {
                line += "-";
                line += example.encodeToString();
            }
            return line;
        }

        public boolean decodeFromString(Object obj, String str) {
            Meaning mean = (Meaning)obj;
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
                mean.type_ = typeString.trim();

                String[] meaningCHSAndJP = meaningString.split("\\" + MEAN_SEP);
                if (meaningCHSAndJP.length > 0) {
                    mean.meaningInCHS_ = meaningCHSAndJP[0].trim();
                }
                if (meaningCHSAndJP.length > 1) {
                    mean.meaningInJP_ = meaningCHSAndJP[1].trim();
                }
            }
            for (int i = 1; i < meaningItems.length; i++) {
                Example example = new Example();
                if (example.decodeFromString(meaningItems[i])) {
                    mean.examples_.add(example);
                    example.parent_ = mean;
                }
            }
            return true;
        }
    }

    public static class Codec_V2 implements ICodec {

        private static final String TYPE_SEP = ":";
        private static final String MEAN_SEP = "\\";
        private static final String MEAN_EXAMPLE_SEP = "<E>";

        public String encodeToString(Object obj) {
            Meaning mean = (Meaning)obj;
            String line = "";
            if (mean.type_.equals("") && mean.meaningInCHS_.equals("") && mean.meaningInJP_.equals("")) {
                return "";
            }
            line += mean.type_ + ":";
            if (!mean.meaningInCHS_.equals("")) {
                line += mean.meaningInCHS_;
            }
            if (!mean.meaningInJP_.equals("")) {
                line += MEAN_SEP + mean.meaningInJP_;
            }

            for (IExample example : mean.examples_) {
                line += MEAN_EXAMPLE_SEP;
                line += example.encodeToString();
            }
            return line;
        }

        public boolean decodeFromString(Object obj, String str) {
            Meaning mean = (Meaning)obj;
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
                mean.type_ = typeString.trim();

                String[] meaningCHSAndJP = meaningString.split("\\" + MEAN_SEP);
                if (meaningCHSAndJP.length > 0) {
                    mean.meaningInCHS_ = meaningCHSAndJP[0].trim();
                }
                if (meaningCHSAndJP.length > 1) {
                    mean.meaningInJP_ = meaningCHSAndJP[1].trim();
                }
            }
            for (int i = 1; i < meaningItems.length; i++) {
                Example example = new Example();
                if (example.decodeFromString(meaningItems[i])) {
                    mean.examples_.add(example);
                    example.parent_ = mean;
                }
            }
            return true;
        }
    }

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
        return Persistence.getInstance().getCurrentMeanCodec().encodeToString(this);
    }

    @Override
    public boolean decodeFromString(String str) {
        return Persistence.getInstance().getCurrentMeanCodec().decodeFromString(this, str);
    }

}
