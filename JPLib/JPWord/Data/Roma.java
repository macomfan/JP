/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.util.Vector;

/**
 *
 * @author u0151316
 */
class Roma implements IRoma {

    private String roma_ = "";
    Vector<String> vector_ = null;

    public Roma() {

    }

    public void SetVector(Vector<String> vector) {
        vector_ = vector;
        for (int i = 0; i < vector_.size(); i++) {
            roma_ += vector_.get(i);
        }
    }

    @Override
    public String getString() {
        return roma_;
    }

    @Override
    public boolean hitTest(String testroma) {
        String roma = testroma.trim();
        int findIndex = roma_.indexOf(roma);
        if (findIndex == -1) {
            return false;
        }
        boolean hint = false;
        int cursor = 0;
        for (int i = 0; i < vector_.size(); i++) {
            if (findIndex < cursor) {
                return hint;
            }
            if (findIndex == cursor) {
                hint = true;
            }
            cursor += vector_.get(i).length();
        }
        return hint;
    }

}
