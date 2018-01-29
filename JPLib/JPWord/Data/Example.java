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

    public static class Codec_V1 implements ICodec {

        private static final String EXAMPLE_SEP = "\\";

        public String encodeToString(Object obj) {
            Example example = (Example)obj;
            
            if (example.isEmpty()) {
                return "";
            }
            return example.exampleInJP_ + EXAMPLE_SEP + example.exampleInCHS_;
        }

        public boolean decodeFromString(Object obj, String str) {
            Example example = (Example)obj;
            if (str.length() == 0) {
                return false;
            }

            String exampleItem = str.replace('/', '\\');
            String[] exampleCHSAndJP = exampleItem.split("\\" + EXAMPLE_SEP);
            if (exampleCHSAndJP.length > 0) {
                example.exampleInJP_ = exampleCHSAndJP[0].trim();
            }
            if (exampleCHSAndJP.length > 1) {
                example.exampleInCHS_ = exampleCHSAndJP[1].trim();
            }
            return true;
        }
    }

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
        return Persistence.getInstance().getCurrentExampleCodec().encodeToString(this);
    }

    @Override
    public boolean decodeFromString(String str) {
        return Persistence.getInstance().getCurrentExampleCodec().decodeFromString(this, str);
    }

    public void setExampleInJP(String value) {
        exampleInJP_ = value;
        if (parent_ != null && parent_.parent_ != null) {
            parent_.parent_.updatedFlag();
        }
    }

    public String getExampleInJP() {
        return exampleInJP_;
    }

    public void setExampleInCHS(String value) {
        exampleInCHS_ = value;
        if (parent_ != null && parent_.parent_ != null) {
            parent_.parent_.updatedFlag();
        }
    }

    public String getExampleInCHS() {
        return exampleInCHS_;
    }
}
