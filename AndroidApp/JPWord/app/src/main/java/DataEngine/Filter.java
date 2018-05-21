package DataEngine;

import java.util.LinkedList;
import java.util.List;

import JPWord.Data.Filter.FilterByTextTag;
import JPWord.Data.Filter.FilterByType;
import JPWord.Data.Filter.IItemFilter;
import JPWord.Data.Filter.SoftByNumberTag;

/**
 * Created by u0151316 on 1/9/2018.
 */

public class Filter {
    private static final String FILTER_LIST = "FILTER_LIST";
    private static final String FILTER_PARAM = "_PARAM";
    private static final String FILTER_DISP_KANJI = "FILTER_DISP_KANJI";

    private List<FilterStruct> currentFilters_ = new LinkedList<>();
    private boolean mDisplayKanJi = true;

    public class FilterStruct {
        public String name_;
        public String param_;

        private FilterStruct(String name, String param) {
            name_ = name;
            param_ = param;
        }
    }

    public boolean readConfig(SettingFile setting) {
        List<String> filterList = setting.getList(FILTER_LIST);
        if (filterList == null || filterList.size() == 0) {
            return false;
        }

        for (String f : filterList) {
            String param = setting.getString(f + FILTER_PARAM);
            addFilter(f, param);
    }

        String disp = setting.getString(FILTER_DISP_KANJI);
        if (disp.equals("") || disp.equals("1")) {
            mDisplayKanJi = true;
        } else {
            mDisplayKanJi = false;
        }
        return true;
    }

    public void saveConfig(SettingFile setting) {
        List<String> filterList = new LinkedList<>();
        List<String> filterParamList = new LinkedList<>();
        for (FilterStruct f : currentFilters_) {
            filterList.add(f.name_);
            setting.setString(f.name_ + FILTER_PARAM, f.param_);
        }
        setting.setList(FILTER_LIST, filterList);

        if (mDisplayKanJi == true) {
            setting.setString(FILTER_DISP_KANJI, "1");
        } else {
            setting.setString(FILTER_DISP_KANJI, "0");
        }
    }

    public Filter() {
    }

    public void addFilter(String name, String param) {
        addFilter(currentFilters_.size(), name, param);
    }

    public void addFilter(int index, String name, String param) {
        for (FilterStruct f : currentFilters_) {
            if (name.equals(f.name_)) {
                return;
            }
        }
        boolean isFound = false;
        for (FilterTemplate.TemplateEntity entity : FilterTemplate.getInstance().getEntities()) {
            if (name.equals(entity.mName)) {
                isFound = true;
            }
        }
        if (!isFound) {
            return;
        }

        FilterStruct newFilter = new FilterStruct(name, param);
        currentFilters_.add(index, newFilter);
    }

    public void removeFilter(String name) {
        int index = 0;
        for (; index < currentFilters_.size(); index++) {
            if (currentFilters_.get(index).name_.equals(name)) {
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
                removeFilter(currentFilters_.get(i).name_);
                break;
            }
        }
    }

    public FilterStruct getAt(int index) {
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

    public List<FilterStruct> getCurrentFilter() {
        return currentFilters_;
    }
}
