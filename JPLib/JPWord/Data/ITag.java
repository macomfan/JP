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
public interface ITag {

    static final String TAG_MY = "MY";
    static final String TAG_Skill = "Skill";
    static final String TAG_RD = "RD";
    static final String TAG_HD = "HD";
    static final String TAG_Cls = "Cls";
    
    String getName();

    String getValue();

    void setName(String name_);

    void setValue(String value_);
}
