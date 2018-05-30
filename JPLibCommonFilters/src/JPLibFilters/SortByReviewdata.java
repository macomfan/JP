/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPLibFilters;

import JPWord.Data.Filter.IItemFilter;
import JPWord.Data.Filter.IStringGetter;
import JPWord.Data.Filter.SortByString;
import JPWord.Data.IWord;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class SortByReviewdata extends FilterTemplate {

    public SortByReviewdata() {
        super.name_ = "Sort by review date";
        super.shortname_ = "RD";
        CandidateParams order = new CandidateParams(ParamType.SignleSelect, "ASCE");
        order.add("ASCE");
        order.add("DESC");

        super.candidateParams_ = order;
    }

    @Override
    public IItemFilter createFilter(String params) throws Exception {
        List<String> paramItemsString = checkAndSplitParams(params);
        boolean isDesc = false;
        for (String string : paramItemsString) {
            if (string.equals("DESC")) {
                isDesc = true;
            }
        }
        IItemFilter filter = new SortByString(new IStringGetter() {
            @Override
            public String getString(IWord word) {
                return word.getReviewDate();
            }
        }, isDesc);
        return filter;
    }
}
