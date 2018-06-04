/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import SqliteEngine_Interface.*;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author u0151316
 */
public class Database {

    private static Database instance_ = null;
    private String rootFolder_ = "";
    private List<String> dictList_ = new LinkedList<>();
    private ISQLEngine sqlEngine_ = null;
    private final Map<String, WordDictionary> openedDictMap_ = new HashMap<>();

    public static Database getInstance() {
        if (instance_ == null) {
            instance_ = new Database();
        }
        return instance_;
    }

    private Database() {

    }

    public void initialize(String footFolder, ISQLEngine engine) {
        dictList_.clear();
        sqlEngine_ = engine;
        rootFolder_ = footFolder.replace('\\', '/');
        if (rootFolder_.charAt(footFolder.length() - 1) != '/') {
            rootFolder_ += "/";
        }

        File folder = new File(rootFolder_);
        File[] subFile = folder.listFiles();
        for (File file : subFile) {
            if (!file.isDirectory()) {
                String filename = file.getName();
                if (filename.endsWith(".db")) {
                    String dictname = filename.substring(0, filename.length() - 3);
                    dictList_.add(dictname);
                }
            }
        }
    }

    public List<String> getDictList() {
        return dictList_;
    }

    public IWordDictionary loadDictionary(String dictname) {
        if (!dictList_.contains(dictname)) {
            return null;
        }
        if (openedDictMap_.containsKey(dictname)) {
            return openedDictMap_.get(dictname);
        }
        try {
            ISQLEngine engine = sqlEngine_.clone();
            WordDictionary dict = new WordDictionary(dictname, engine);
            engine.connect(rootFolder_ + dictname + ".db");
            dict.loadFromDB();
            openedDictMap_.put(dictname, dict);
            return dict;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void deleteDictionary(String dictname) throws Exception {
        if (openedDictMap_.containsKey(dictname)) {
            throw new Exception("[JPLib] The opened Database must be closed");
        }
        File file = new File(rootFolder_ + dictname + ".db");
        if (file.exists()) {
            file.delete();
            dictList_.remove(dictname);
        }
    }

    public void deleteDictionary(IWordDictionary dict) throws Exception {
        String dictName = dict.getName();
        closeDictionary(dict);
        deleteDictionary(dictName);
    }

    public void closeDictionary(IWordDictionary dict) throws Exception {
        if (dict == null) {
            throw new Exception("[JPLib] Check the null param in closeDictionary");
        }
        WordDictionary d = (WordDictionary) dict;
        d.close();
        if (openedDictMap_.containsKey(dict.getName())) {
            openedDictMap_.remove(dict.getName());
        }
    }

    public IWordDictionary createDictionary(String dictname) {
        try {
            ISQLEngine engine = sqlEngine_.clone();
            engine.connect(rootFolder_ + dictname + ".db");
            Word.DBS.createNewTable(engine);
            engine.commit();
            dictList_.add(dictname);
            IWordDictionary dict = new WordDictionary(rootFolder_ + dictname + ".db", engine);
            return dict;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
