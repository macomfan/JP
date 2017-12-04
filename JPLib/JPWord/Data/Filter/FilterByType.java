/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data.Filter;

import JPWord.Data.IMeaning;
import JPWord.Data.IWord;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class FilterByType implements IItemFilter {

    private List<String> types_ = new LinkedList<>();

    public FilterByType(List<String> types) {
        types_ = types;
    }

    public FilterByType(String... types) {
        for (int i = 0; i < types.length; i++) {
            types_.add(types[i]);
        }
    }

    @Override
    public int buildChildGroup(List<Object> items) {
        return types_.size();
    }

    @Override
    public List<Integer> distributeItem(Object item) {
        List<Integer> objGroup = new LinkedList<>();
        IWord w = (IWord) item;
        if (w == null) {
            return objGroup;
        }
        for (int i = 0; i < types_.size(); i++) {
            for (IMeaning m : w.getMeanings()) {
                if (m.getType().equals(types_.get(i))) {
                    objGroup.add(i);
                }
            }
        }
        return objGroup;
    }
}
