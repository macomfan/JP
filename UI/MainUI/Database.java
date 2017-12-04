/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainUI;

import JPWord.Data.IWordDictionary;
import java.io.File;

/**
 *
 * @author u0151316
 */
public class Database {

    private static Database instance_;
    private IWordDictionary dictionary_ = null;
    private String filename_ = "";

    private Database() {

    }

    public static Database getInstance() {
        if (instance_ == null) {
            instance_ = new Database();
        }
        return instance_;
    }

    public void initialize(String filename) {
        filename_ = filename;
        File file = new File(filename_);
        dictionary_ = JPWord.Data.Database.createWordDictionary(file);
    }

    public String getFilename() {
        return filename_;
    }

    public IWordDictionary getDatabase() {
        return dictionary_;
    }
}
