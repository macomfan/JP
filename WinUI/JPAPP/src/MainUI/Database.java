/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainUI;

import JPWord.Data.IWordDictionary;
import java.io.File;
import SqliteEngine_JDBC.JDBC_SQLEngine;

/**
 *
 * @author u0151316
 */
public class Database {

    private static Database instance_ = null;
    private IWordDictionary dictionary_ = null;
    private String rootFolder_ = "";
    private String currentDictname_ = "Standard_JP_Junior";

    private Database() {

    }

    public static Database getInstance() {
        if (instance_ == null) {
            instance_ = new Database();
            instance_.initialize();
        }
        return instance_;
    }

    private void initialize() {
        rootFolder_ = Setting.getInstance().getRootFolder();
        JPWord.Data.Database.getInstance().initialize(rootFolder_, new JDBC_SQLEngine());
        dictionary_ = JPWord.Data.Database.getInstance().loadDictionary(currentDictname_);
        try {
            //dictionary_.load();
        } catch (Exception e) {
        }

    }

    public String getCurrentDictName() {
        return currentDictname_;
    }

    public IWordDictionary getDatabase() {
        return dictionary_;
    }
}
