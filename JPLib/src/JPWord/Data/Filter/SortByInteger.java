/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data.Filter;

import JPWord.Data.IWord;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class SortByInteger implements IItemFilter {

    private List<Integer> integerList_ = new LinkedList<>();
    private IIntegerGetter getter_ = null;
    private boolean isDesc_ = false;

    public SortByInteger(IIntegerGetter getter) {
        getter_ = getter;
    }

    public SortByInteger(IIntegerGetter getter, boolean isDesc) {
        getter_ = getter;
        isDesc_ = isDesc;
    }

    private void insertNumber(int num) {
        if (integerList_.isEmpty()) {
            integerList_.add(num);
        }
        for (int value : integerList_) {
            if (value == num) {
                return;
            }
        }
        integerList_.add(num);
    }

    @Override
    public int buildChildGroup(List<Object> items) {
        for (Object item : items) {
            IWord word = (IWord) item;
            if (word == null) {
                continue;
            }
            int num = getter_.getInteger(word);
            insertNumber(num);
        }
        Collections.sort(integerList_, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1.intValue() == o2.intValue()) {
                    return 0;
                } else {
                    if (!isDesc_) {
                        return (o1 > o2) ? 1 : -1;
                    } else {
                        return (o1 > o2) ? -1 : 1;
                    }
                }
            }
        });
        return integerList_.size();
    }

    @Override
    public List<Integer> distributeItem(Object item) {
        List<Integer> objGroup = new LinkedList<>();
        IWord word = (IWord) item;
        if (word == null) {
            return objGroup;
        }
        for (int i = 0; i < integerList_.size(); i++) {
            if (getter_.getInteger(word) == integerList_.get(i)) {
                objGroup.add(i);
            }
        }
        return objGroup;
    }
}
