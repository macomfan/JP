/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPLibAssist;

import JPWord.Data.IWordDictionary;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class FilterGenerator {

    private static FilterGenerator instance_ = null;

    private List<FilterTemplate> templates_ = new LinkedList<>();

    private FilterGenerator() {

    }

    public void initialize(IWordDictionary dict) {
        templates_.clear();
        templates_.add(new SortBySkill());
        templates_.add(new SortByReviewdata());
        templates_.add(new FilterByType(dict));
        templates_.add(new FilterByClass(dict));
    }

    public List<FilterTemplate> getTemplates() {
        return templates_;
    }

    public FilterTemplate getTemplateByShortname(String shortname) {
        for (FilterTemplate filterTemplate : templates_) {
            if (filterTemplate.shortname_.equals(shortname)) {
                return filterTemplate;
            }
        }
        return null;
    }

    public FilterTemplate getTemplateByName(String name) {
        for (FilterTemplate filterTemplate : templates_) {
            if (filterTemplate.name_.equals(name)) {
                return filterTemplate;
            }
        }
        return null;
    }

    public static FilterGenerator getInstance() {
        if (instance_ == null) {
            instance_ = new FilterGenerator();
        }
        return instance_;
    }

}
