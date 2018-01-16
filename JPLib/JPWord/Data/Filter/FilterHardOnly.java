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
public class FilterHardOnly implements IItemFilter {

    public FilterHardOnly() {
    }

    @Override
    public IItemFilter createSelf(String mainParam, List<String> params) {
        return new FilterHardOnly();
    }

    @Override
    public int buildChildGroup(List<Object> items) {
        return 1;
    }

    @Override
    public List<Integer> distributeItem(Object item) {
        List<Integer> objGroup = new LinkedList<>();
        IWord w = (IWord) item;
        if (w == null) {
            return objGroup;
        }
        if (w.getSkill() < 0) {
            objGroup.add(0);
        }
        return objGroup;
    }
}
