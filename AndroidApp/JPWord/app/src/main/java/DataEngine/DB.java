package DataEngine;

import android.content.Context;
import android.os.Environment;

import com.jpword.ma.sqliteengine_android.Android_SQLEngine;

import java.io.File;

import JPLibFilters.Filters;
import JPWord.Data.IWordDictionary;

/**
 * Created by u0151316 on 1/8/2018.
 */

public class DB {
    private static final String DICTNAME = "DICTNAME";

    private static final DB ourInstance = new DB();

    public static DB getInstance() {
        return ourInstance;
    }

    private DB() {

    }

    private IWordDictionary dict_ = null;

    private WordSequence wordSequence_ = null;

    public void initialize(Context context) throws Exception {
        if (dict_ != null) {
            return;
        }

        SettingFile settingFile = new SettingFile();
        settingFile.load(context);

        File path = Environment.getExternalStorageDirectory();
        File jpPath = new File(path.getCanonicalFile() + "/JP/");

        if (!jpPath.exists()) {
            jpPath.mkdir();
        }
        JPWord.Data.Database.getInstance().initialize(jpPath.getCanonicalPath(), new Android_SQLEngine());

        System.out.println("+++++  start read db");
        String defaultDictname = settingFile.getString(DICTNAME);
        if (defaultDictname.equals("")) {
            defaultDictname = "Dictionary";
        }
        IWordDictionary dict = JPWord.Data.Database.getInstance().loadDictionary(defaultDictname);
        Filters.getInstance().initialize(dict_);
        if (dict == null) {
            return;
        }
        dict_ = dict;

        System.out.println("+++++  start seq");
        wordSequence_ = new WordSequence(dict_);

        if (!wordSequence_.readConfig()) {
            wordSequence_.reSort();
        }
        System.out.println("+++++  finish seq");
    }

    public IWordDictionary changeDatabase(String dictname, boolean createdIfNotExist) {
        dict_ = null;
        IWordDictionary dict = JPWord.Data.Database.getInstance().loadDictionary(dictname);
        if (dict == null) {
            if (createdIfNotExist) {
                dict = JPWord.Data.Database.getInstance().createDictionary(dictname);
            }
        }
        dict_ = dict;
        Filters.getInstance().initialize(dict_);
        if (dict_ != null) {
            wordSequence_ = new WordSequence(dict_);
            wordSequence_.reSort();
        }

        return dict;
    }

    public IWordDictionary getDatabase() {
        return dict_;
    }

    public WordSequence getWordSequence() {
        return wordSequence_;
    }

    public void persist(Context context) {
        SettingFile settingFile = new SettingFile();
        if (dict_ != null) {
            settingFile.setString(DICTNAME, dict_.getName());
        }
        settingFile.save(context);
        wordSequence_.saveConfig();
    }
}
