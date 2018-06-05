/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPLibAssist;

import JPWord.Data.Filter.FilterByString;
import JPWord.Data.Filter.IItemFilter;
import JPWord.Data.Filter.IStringChecker;
import JPWord.Data.IMeaning;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class FilterByType extends FilterTemplate {

    public FilterByType(IWordDictionary dict) {
        super.name_ = "Filter by type";
        super.shortname_ = "TYPE";
        CandidateParams typeParam = new CandidateParams(ParamType.Mandatory, null);

        for (IWord word : dict.getWords()) {
            for (IMeaning meaning : word.getMeanings()) {
                if (meaning.getType().equals("")) {
                    continue;
                }
                if (!typeParam.contains(meaning.getType())) {
                    typeParam.add(meaning.getType());
                }
            }
        }
        super.candidateParams_ = typeParam;
    }

    @Override
    public IItemFilter createFilter(String params) throws Exception {
        List<String> paramItemsString = checkAndSplitParams(params);
        IItemFilter filter = new FilterByString(new IStringChecker() {
            @Override
            public boolean checkString(IWord word, String value) {
                for (IMeaning meaning : word.getMeanings()) {
                    if (meaning.getType().equals(value)) {
                        return true;
                    }
                }
                return false;
            }
        }, paramItemsString);
        return filter;
    }
}
