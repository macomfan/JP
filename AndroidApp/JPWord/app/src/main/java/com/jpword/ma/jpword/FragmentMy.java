package com.jpword.ma.jpword;

import DataEngine.DBEntity;

/**
 * Created by Ma on 2017/12/20.
 */

public class FragmentMy extends com.jpword.ma.baseui.FragmentMy {

    private DBEntity dbEntity_ = null;

    public void setDatabaseEntity(DBEntity dbEntity) {
        dbEntity_ = dbEntity;
    }
}
