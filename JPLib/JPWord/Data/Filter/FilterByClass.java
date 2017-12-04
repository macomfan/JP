/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data.Filter;

import JPWord.Data.IWord;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class FilterByClass implements IItemFilter {

    private List<String> classes_ = new LinkedList<>();

    public FilterByClass(List<String> classes) {
        classes_ = classes;
    }

    public FilterByClass(String... classes) {
        for (int i = 0; i < classes.length; i++) {
            classes_.add(classes[i]);
        }
    }

    @Override
    public int buildChildGroup(List<Object> items) {
        return classes_.size();
    }

    @Override
    public List<Integer> distributeItem(Object item) {
        List<Integer> objGroup = new LinkedList<>();
        IWord w = (IWord) item;
        if (w == null) {
            return objGroup;
        }
        for (int i = 0; i < classes_.size(); i++) {
            if (classes_.get(i).equals(w.getTagValue("Cls"))) {
                objGroup.add(i);
            }
        }
        return objGroup;
    }
}
