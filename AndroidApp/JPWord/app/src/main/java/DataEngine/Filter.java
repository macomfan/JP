package DataEngine;

import java.util.LinkedList;
import java.util.List;

import JPLibFilters.FilterTemplate;
import JPLibFilters.Filters;
import JPWord.Data.IWordDictionary;

/**
 * Created by u0151316 on 1/9/2018.
 */

public class Filter {
    private static final String FILTER_LIST = "FILTER_LIST";
    private static final String FILTER_PARAM = "_PARAM";
    private static final String FILTER_DISP_KANJI = "FILTER_DISP_KANJI";

    private List<FilterEntity> currentFilters_ = new LinkedList<>();
    private boolean mDisplayKanJi = true;


    public boolean readConfig(IWordDictionary dict) {
        List<String> filterList = dict.getSetting().getList(FILTER_LIST);
        if (filterList == null || filterList.size() == 0) {
            return false;
        }

        for (String filterShortname : filterList) {
            String param = dict.getSetting().getString(filterShortname + FILTER_PARAM);
            addFilterByShortname(filterShortname, param);
    }

        String disp = dict.getSetting().getString(FILTER_DISP_KANJI);
        if (disp.equals("") || disp.equals("1")) {
            mDisplayKanJi = true;
        } else {
            mDisplayKanJi = false;
        }
        return true;
    }

    public void saveConfig(IWordDictionary dict) throws Exception {
        List<String> filterList = new LinkedList<>();
        List<String> filterParamList = new LinkedList<>();
        for (FilterEntity entity : currentFilters_) {
            String shortname = entity.filterTemplate_.shortname_;
            filterList.add(shortname);
            dict.getSetting().setString(shortname + FILTER_PARAM, entity.param_);
        }
        dict.getSetting().setList(FILTER_LIST, filterList);

        if (mDisplayKanJi == true) {
            dict.getSetting().setString(FILTER_DISP_KANJI, "1");
        } else {
            dict.getSetting().setString(FILTER_DISP_KANJI, "0");
        }
    }

    public Filter() {
    }

    public void addFilterByShortname(String shortname, String param) {
        FilterTemplate template = Filters.getInstance().getTemplateByShortname(shortname);
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
        try
        {
            FilterEntity entity = new FilterEntity(template, param);
            currentFilters_.add(index, entity);
        }
        catch (Exception e) {

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

    public boolean isDisplayKanJi() {
        return mDisplayKanJi;
    }

    public void setDisplayKanJi(boolean DisplayKanJi) {
        this.mDisplayKanJi = DisplayKanJi;
    }

    public List<FilterEntity> getCurrentFilter() {
        return currentFilters_;
    }
}
