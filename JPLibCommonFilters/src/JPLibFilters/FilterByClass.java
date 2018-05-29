/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPLibFilters;

import JPWord.Data.Filter.FilterByInteger;
import JPWord.Data.Filter.IIntegerChecker;
import JPWord.Data.Filter.IItemFilter;

import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class FilterByClass extends FilterTemplate {

    public FilterByClass(IWordDictionary dict) {
        super.name_ = "Filter by class";
        super.shortname_ = "CLS";

        CandidateParams clsParam = new CandidateParams(ParamType.Mandatory, null);

        for (IWord word : dict.getWords()) {
            String clsString = Integer.toString(word.getCls());
            if (!clsParam.contains(clsString)) {
                clsParam.add(clsString);
            }
        }
        super.candidateParams_ = clsParam;
    }

    @Override
    public IItemFilter createFilter(String params) throws Exception{
        List<String> paramItemsString = checkAndSplitParams(params);
        List<Integer> cls = new LinkedList<>();
        for (String string : paramItemsString) {
            cls.add(Integer.parseInt(string));
        }

        IItemFilter filter = new FilterByInteger(new IIntegerChecker() {
            @Override
            public boolean checkInteger(IWord word, int value) {
                return word.getCls() == value;
            }
        }, cls);
        return filter;
    }
}
