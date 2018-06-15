/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainUI;

import JPWord.Data.IWordDictionary;
import SqliteEngine_JDBC.JDBC_SQLEngine;
import JPLibAssist.Filters;
import JPLibAssist.WordSequence;
import JPLibAssist.WordStatistics;

/**
 *
 * @author u0151316
 */
public class Database {

    private static Database instance_ = null;
    private IWordDictionary dictionary_ = null;
    private String rootFolder_ = "";
    private String currentDictname_ = "JP_TEST";
    private WordSequence wordSequence_ = null;
    private WordStatistics wordStatistics_ = null;
    private Filters filters_ = null;

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
        rootFolder_ = SettingFile.getInstance().getRootFolder();
        JPWord.Data.Database.getInstance().initialize(rootFolder_, new JDBC_SQLEngine());
        try {
            dictionary_ = JPWord.Data.Database.getInstance().loadDictionary(currentDictname_);
        } catch (Exception e) {
        }
        
        try {
            //dictionary_.load();
        } catch (Exception e) {
        }
        wordSequence_ = new WordSequence(dictionary_);
        wordStatistics_ = new WordStatistics(dictionary_);
        filters_ = new Filters(dictionary_);
        filters_.readFromSetting();
        wordSequence_.readFromSetting();
        
    }

    public WordSequence getWordSequence() {
        return wordSequence_;
    }

    public Filters getFilters() {
        return filters_;
    }

    public String getCurrentDictName() {
        return currentDictname_;
    }

    public IWordDictionary getDatabase() {
        return dictionary_;
    }

    public void close() {
        try {
            if (dictionary_ != null) {
                JPWord.Data.Database.getInstance().closeDictionary(dictionary_);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
