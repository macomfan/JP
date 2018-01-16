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
public class FilterByTextTag implements IItemFilter {

    private List<String> tagvalues_ = new LinkedList<>();
    private String tagname_ = "";

    public FilterByTextTag() {
        
    }
    
    public FilterByTextTag(String tagname, List<String> tagvalues) {
        tagvalues_ = tagvalues;
        tagname_ = tagname;
    }

    public FilterByTextTag(String tagname, String... tagvalues) {
        tagvalues_.addAll(Arrays.asList(tagvalues));
        tagname_ = tagname;
    }

    @Override
    public IItemFilter createSelf(String mainParam, List<String> params) {
        return new FilterByTextTag(mainParam, params);
    }

    @Override
    public int buildChildGroup(List<Object> items) {
        return tagvalues_.size();
    }

    @Override
    public List<Integer> distributeItem(Object item) {
        List<Integer> objGroup = new LinkedList<>();
        IWord w = (IWord) item;
        if (w == null) {
            return objGroup;
        }
        for (int i = 0; i < tagvalues_.size(); i++) {
            if (tagvalues_.get(i).equals(w.getTagValue(tagname_)) || tagvalues_.get(i).equals("*")) {
                objGroup.add(i);
            }
        }
        return objGroup;
    }
}
