/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data.Filter;
import JPWord.Data.IWord;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SoftByNumberTag implements IItemFilter {

    private List<String> dateList_ = new LinkedList<>();
    private String tagName_ = "";
    
    public SoftByNumberTag() {
        
    }
    
    public SoftByNumberTag(String tagName, List<String> params) {
        tagName_ = tagName;
    }

    @Override
    public IItemFilter createSelf(String mainParam, List<String> params) {
        return new SoftByNumberTag(mainParam, params);
    }
    
    private void insertNumber(String num) {
        if (dateList_.isEmpty()) {
            dateList_.add(num);
        }
        for (String value : dateList_) {
            if (value.equals(num)) {
                return;
            }
        }
        dateList_.add(num);
    }

    @Override
    public int buildChildGroup(List<Object> items) {
        for (Object item : items) {
            IWord w = (IWord) item;
            if (w == null) {
                continue;
            }
            String num = w.getTagValue(tagName_);
            insertNumber(num);
        }
        Collections.sort(dateList_, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.equals(o2)) {
                    return 0;
                } else {
                    if (o1.equals("")) {
                        return -1;
                    } else if (o2.equals("")) {
                        return 1;
                    } else {
                        int ir = Integer.parseInt(o1, 10);
                        int il = Integer.parseInt(o2, 10);
                        return (ir > il) ? 1 : -1;
                    }
                }
            }
        });
        return dateList_.size();
    }

    @Override
    public List<Integer> distributeItem(Object item) {
        List<Integer> objGroup = new LinkedList<>();
        IWord w = (IWord) item;
        if (w == null) {
            return objGroup;
        }
        for (int i = 0; i < dateList_.size(); i++) {
            if (w.getTagValue(tagName_).equals(dateList_.get(i))) {
                objGroup.add(i);
            }
        }
        return objGroup;
    }

}

/**
 *
 * @author u0151316
 */
//public class SoftByNumberTag implements IItemFilter {
//
//    
//    
//
//
//    @Override
//    public int buildChildGroup(ItemGroup group) {

//    }
//
//
//    @Override
//    public List<Integer> distributeItem(Object item) {

//    }
//}
