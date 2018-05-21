/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

/**
 *
 * @author u0151316
 */
class Example implements IExample {

    private static final String EXAMPLE_SEP = "\\";

    private String exampleInJP_;
    private String exampleInCHS_;
    public Meaning parent_ = null;

    public Example() {
        exampleInCHS_ = "";
        exampleInJP_ = "";
    }

    @Override
    public boolean isEmpty() {
        if (exampleInCHS_.isEmpty() && exampleInJP_.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public String encodeToString() {
        if (this.isEmpty()) {
            return "";
        }
        return this.exampleInJP_ + EXAMPLE_SEP + this.exampleInCHS_;
    }

    @Override
    public boolean decodeFromString(String str) {
        if (str.length() == 0) {
            return false;
        }

        String exampleItem = str.replace('/', '\\');
        String[] exampleCHSAndJP = exampleItem.split("\\" + EXAMPLE_SEP);
        if (exampleCHSAndJP.length > 0) {
            this.exampleInJP_ = exampleCHSAndJP[0].trim();
        }
        if (exampleCHSAndJP.length > 1) {
            this.exampleInCHS_ = exampleCHSAndJP[1].trim();
        }
        return true;
    }

    public void setExampleInJP(String value) {
        exampleInJP_ = value;
        if (parent_ != null && parent_.parent_ != null) {
        }
    }

    public String getExampleInJP() {
        return exampleInJP_;
    }

    public void setExampleInCHS(String value) {
        exampleInCHS_ = value;
        if (parent_ != null && parent_.parent_ != null) {
        }
    }

    public String getExampleInCHS() {
        return exampleInCHS_;
    }
}
