/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPLibAssist;

import JPLibAssist.FilterEntity;
import JPLibAssist.FilterGenerator;
import JPLibAssist.FilterTemplate;
import JPWord.Data.IWordDictionary;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class Filters {

    private static final String FILTER_LIST = "FILTER_LIST";
    private static final String FILTER_PARAM = "_PARAM";

    private List<FilterEntity> currentFilters_ = new LinkedList<>();
    private IWordDictionary dict_ = null;

    public boolean readFromSetting() {
        List<String> filterList = dict_.getSetting().getList(FILTER_LIST);
        if (filterList == null || filterList.isEmpty()) {
            return false;
        }

        for (String filterShortname : filterList) {
            String param = dict_.getSetting().getString(filterShortname + FILTER_PARAM);
            addFilterByShortname(filterShortname, param);
        }

        return true;
    }

    public void saveToSetting() throws Exception {
        List<String> filterList = new LinkedList<>();
        for (FilterEntity entity : currentFilters_) {
            String shortname = entity.filterTemplate_.shortname_;
            filterList.add(shortname);
            dict_.getSetting().setString(shortname + FILTER_PARAM, entity.param_);
        }
        dict_.getSetting().setList(FILTER_LIST, filterList);
    }

    public Filters(IWordDictionary dict) {
        dict_ = dict;
    }

    public void addFilterByShortname(String shortname, String param) {
        FilterTemplate template = FilterGenerator.getInstance().getTemplateByShortname(shortname);
        if (template == null) {
            return;
        }
        addFilterByFilterTemplate(template, param);
    }

    public void addFilterByFilterTemplate(FilterTemplate template, String param) {
        addFilterByFilterTemplate(currentFilters_.size(), template, param);
    }

    public void addFilterByFilterTemplate(int index, FilterTemplate template, String param) {
        for (FilterEntity entity : currentFilters_) {
            if (template.shortname_.equals(entity.filterTemplate_.shortname_)) {
                return;
            }
        }
        try {
            FilterEntity entity = new FilterEntity(template, param);
            currentFilters_.add(index, entity);
        } catch (Exception e) {

        }
    }

    public void removeFilterByShortname(String shortname) {
        int index = 0;
        for (; index < currentFilters_.size(); index++) {
            if (currentFilters_.get(index).filterTemplate_.shortname_.equals(shortname)) {
                break;
            }
        }
        if (index != currentFilters_.size()) {
            currentFilters_.remove(index);
        }
    }

    public void removeFilter(int index) {
        for (int i = 0; i < currentFilters_.size(); i++) {
            if (index == i) {
                removeFilterByShortname(currentFilters_.get(i).filterTemplate_.shortname_);
                break;
            }
        }
    }

    public FilterEntity getAt(int index) {
        if (index < 0 || index >= currentFilters_.size()) {
            return null;
        }
        return currentFilters_.get(index);
    }

    public List<FilterEntity> getCurrentFilters() {
        return currentFilters_;
    }
}
