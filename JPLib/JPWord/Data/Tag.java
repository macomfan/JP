/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

/**
 *
 * @author u0151316
 */
class Tag implements ITag {

    private String name_;
    private String value_;

    public Tag() {
        name_ = "";
        value_ = "";
    }

    @Override
    public String getName() {
        return name_;
    }

    @Override
    public void setName(String name_) {
        this.name_ = name_;
    }

    @Override
    public String getValue() {
        return value_;
    }

    @Override
    public void setValue(String value_) {
        this.value_ = value_;
    }
}
