/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data.Filter;

import JPWord.Data.IMeaning;
import JPWord.Data.IWord;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
class Getter {

    static String getString(IWord word) {
        return word.getReviewDate();
    }
}

public class FilterByString implements IItemFilter {

    private List<String> stringList_ = new LinkedList<>();
    private IStringChecker getter_ = null;

    public FilterByString(IStringChecker getter, List<String> values) {
        getter_ = getter;
        stringList_.addAll(values);
    }

    public FilterByString(IStringChecker getter, String... tagvalues) {
        getter_ = getter;
        stringList_.addAll(Arrays.asList(tagvalues));
    }

    @Override
    public int buildChildGroup(List<Object> items) {
        return stringList_.size();
    }

    @Override
    public List<Integer> distributeItem(Object item) {
        List<Integer> objGroup = new LinkedList<>();
        IWord word = (IWord) item;
        if (word == null) {
            return objGroup;
        }
        for (int i = 0; i < stringList_.size(); i++) {
            if (getter_.checkString(word, stringList_.get(i))) {
                objGroup.add(0);
            }
        }
        return objGroup;
    }
}
