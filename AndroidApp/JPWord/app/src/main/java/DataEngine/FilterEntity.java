package DataEngine;

import JPLibFilters.FilterTemplate;
import JPWord.Data.Filter.IItemFilter;

/**
 * Created by u0151316 on 5/30/2018.
 */

public class FilterEntity {
    public String param_;
    public FilterTemplate filterTemplate_;
    public IItemFilter filter_;
    public String displayName_;

    public FilterEntity(FilterTemplate filterTemplate, String param) throws Exception {
        param_ = param;
        filterTemplate_ = filterTemplate;
        filter_ = filterTemplate_.createFilter(param_);
        displayName_ = filterTemplate_.name_;
        if (param != null && !param.equals("")) {
            displayName_ += ": " + param_;
        }
    }
}
