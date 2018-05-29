/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data.Filter;

import JPWord.Data.IWord;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class FilterByInteger implements IItemFilter {

    private List<Integer> integerList_ = new LinkedList<>();
    private IIntegerChecker getter_ = null;

    public FilterByInteger(IIntegerChecker getter, List<Integer> values) {
        getter_ = getter;
        integerList_.addAll(values);
    }

    public FilterByInteger(IIntegerChecker getter, Integer... values) {
        getter_ = getter;
        integerList_.addAll(Arrays.asList(values));
    }

    public FilterByInteger(IIntegerChecker getter, String values) {
        getter_ = getter;
        String stringList[] = values.split("\\,");
        for (String string : stringList) {
            integerList_.add(Integer.parseInt(string, 10));
        }
    }

    @Override
    public int buildChildGroup(List<Object> items) {
        return 1;
    }

    @Override
    public List<Integer> distributeItem(Object item) {
        List<Integer> objGroup = new LinkedList<>();
        IWord word = (IWord) item;
        if (word == null) {
            return objGroup;
        }
        for (int i = 0; i < integerList_.size(); i++) {
            if (getter_.checkInteger(word, integerList_.get(i))) {
                objGroup.add(0);
            }
        }
        return objGroup;
    }
}
