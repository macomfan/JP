/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import JPWord.File.IJPFileReader;
import JPWord.File.IJPFileWriter;
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
    private List<String> fileNameList_ = new LinkedList<>();
    private IJPFileReader reader_ = null;
    private IJPFileWriter writer_ = null;
    private Map<String, String> dictFilenameMap_ = new HashMap<>();

    public static Database getInstance() {
        if (instance_ == null) {
            instance_ = new Database();
        }
        return instance_;
    }

    public void initialize(IJPFileReader reader, IJPFileWriter writer) {
        reader_ = reader;
        writer_ = writer;
    }

    public List<String> getDictionaryNameList() {
        List<String> res = new LinkedList<>();
        for (Map.Entry<String, String> entry : dictFilenameMap_.entrySet()) {
            res.add(entry.getKey());
        }
        return res;
    }

    public boolean addFile(String filename) {
        final String NAME = "#NAME=";
        IJPFileReader reader = reader_.clone(filename);
        try {
            reader.open();
            String line = reader.readline();

            if (line.indexOf(NAME) != 0) {
                return false;
            }
            String name = line.substring(NAME.length(), line.length());
            dictFilenameMap_.put(name, filename);
            reader.close();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public IWordDictionary loadDictionary(String dictname) {
        if (!dictFilenameMap_.containsKey(dictname)) {
            return null;
        }
        IJPFileReader reader = reader_.clone(dictFilenameMap_.get(dictname));
        IJPFileWriter writer = writer_.clone(dictFilenameMap_.get(dictname));
        IWordDictionary dict = new WordDictionary(reader, writer);
        try {
            dict.load();
        } catch (Exception e) {
        }

        return dict;
    }

//    public static IWordDictionary createWordDictionary(IJPFileReader reader, IJPFileWriter writer) {
//        return (IWordDictionary) (new WordDictionary(reader, writer));
//    }
}
