/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPUI;

import JPWord.Data.Filter.IItemFilter;

/**
 *
 * @author u0151316
 */
class FilterStruct {

    public String name_;
    public IItemFilter filter_;

    public FilterStruct(String name, IItemFilter filter) {
        name_ = name;
        filter_ = filter;
    }
}
