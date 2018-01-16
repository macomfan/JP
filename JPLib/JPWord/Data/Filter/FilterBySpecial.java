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
public class FilterBySpecial implements IItemFilter {

    private List<String> special_ = new LinkedList<>();

    public FilterBySpecial() {

    }

    public FilterBySpecial(List<String> values) {
        special_ = values;
    }

    public FilterBySpecial(String tagname, String... tagvalues) {
        special_.addAll(Arrays.asList(tagvalues));
    }

    @Override
    public IItemFilter createSelf(String mainParam, List<String> params) {
        return new FilterBySpecial(params);
    }

    @Override
    public int buildChildGroup(List<Object> items) {
        return special_.size();
    }

    @Override
    public List<Integer> distributeItem(Object item) {
        List<Integer> objGroup = new LinkedList<>();
        IWord w = (IWord) item;
        if (w == null) {
            return objGroup;
        }
        for (int i = 0; i < special_.size(); i++) {
            if (w.getContent().indexOf(special_.get(i)) != -1
                    || w.getKana().indexOf(special_.get(i)) != -1) {
                objGroup.add(i);
            }
        }
        return objGroup;
    }
}
