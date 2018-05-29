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
public class SortByString implements IItemFilter {

    private List<String> stringList = new LinkedList<>();
    private IStringGetter getter_ = null;
    private boolean isDesc_ = false;

    public SortByString(IStringGetter getter) {
        getter_ = getter;
    }

    public SortByString(IStringGetter getter, boolean isDesc) {
        getter_ = getter;
        isDesc_ = isDesc;
    }

    private void insertString(String string) {
        if (string == null) {
            return;
        }
        if (stringList.isEmpty()) {
            stringList.add(string);
        }
        for (String value : stringList) {
            if (value.equals(string)) {
                return;
            }
        }
        stringList.add(string);
    }

    @Override
    public int buildChildGroup(List<Object> items) {
        for (Object item : items) {
            IWord word = (IWord) item;
            if (word == null) {
                continue;
            }
            String string = getter_.getString(word);
            insertString(string);
        }
        Collections.sort(stringList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (isDesc_) {
                    return o2.compareTo(o1);
                } else {
                    return o1.compareTo(o2);
                }

            }
        });
        return stringList.size();
    }

    @Override
    public List<Integer> distributeItem(Object item) {
        List<Integer> objGroup = new LinkedList<>();
        IWord word = (IWord) item;
        if (word == null) {
            return objGroup;
        }
        for (int i = 0; i < stringList.size(); i++) {
            if (getter_.getString(word).equals(stringList.get(i))) {
                objGroup.add(i);
            }
        }
        return objGroup;
    }
}
