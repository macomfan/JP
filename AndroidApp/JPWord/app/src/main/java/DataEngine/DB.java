package DataEngine;

import android.content.Context;
import android.os.Environment;

import com.jpword.ma.sqliteengine_android.Android_SQLEngine;

import java.io.File;
import java.util.List;

import JPLibAssist.FilterGenerator;
import JPLibAssist.FilterEntity;
import JPLibAssist.Filters;
import JPLibAssist.WordSequence;
import JPLibAssist.DisplaySetting;
import JPWord.Data.IWordDictionary;
import JPWord.Data.Setting;

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
    private Filters filters_ = null;
    private DisplaySetting displaySetting_ = null;
    private SettingFile settingFile_ = null;

    public void initialize(Context context) throws Exception {
        if (dict_ != null) {
            return;
        }

        settingFile_ = new SettingFile();
        settingFile_.load(context);

        File path = Environment.getExternalStorageDirectory();
        File jpPath = new File(path.getCanonicalFile() + "/JP/");

        if (!jpPath.exists()) {
            jpPath.mkdir();
        }

        JPWord.Data.Database.getInstance().initialize(jpPath.getCanonicalPath(), new Android_SQLEngine());

//        String defaultDictname = settingFile_.getString(DICTNAME);
//        if (defaultDictname.equals("")) {
//            AppLogging.showDebug(DB.class, "Cannot read DB name from setting.ini");
//            throw new Exception("Cannot read DB name from setting.ini");
//        } else {
//            AppLogging.showDebug(DB.class, "Got the DB name from setting.ini: " + defaultDictname);
//        }
//        AppLogging.showLog("Start read DB");
//        changeDatabase(defaultDictname, false);
//        AppLogging.showLog("DB read successfully");
    }

    public void loadDatabase(String dbName, boolean createIfNotExist) throws Exception {
        String defaultDictname = "";
        if (dbName == null || dbName.equals("")) {
            defaultDictname = settingFile_.getString(DICTNAME);
            if (defaultDictname.equals("")) {
                AppLogging.showDebug(DB.class, "Cannot read DB name from setting.ini");
                throw new Exception("Cannot read DB name from setting.ini");
            } else {
                AppLogging.showDebug(DB.class, "Got the DB name from setting.ini: " + defaultDictname);
            }
        } else {
            defaultDictname = dbName;
        }
        AppLogging.showLog("Start read DB");
        changeDatabase(defaultDictname, createIfNotExist);
        AppLogging.showLog("DB read successfully");

    }

    private void changeDatabase(String dictname, boolean createIfNotExist) throws Exception {
        if (dict_ != null) {
            if (dict_.getName().equals(dictname)) {
                return;
            }
        }
        IWordDictionary dict = JPWord.Data.Database.getInstance().loadDictionary(dictname);
        if (dict == null) {
            if (createIfNotExist) {
                dict = JPWord.Data.Database.getInstance().createDictionary(dictname);
            }
        }
        if (dict == null) {
            throw new Exception("Cannot open the database: " + dictname);
        }
        dict_ = dict;
        if (dict_ != null) {
            FilterGenerator.getInstance().initialize(dict_);
            filters_ = new Filters(dict_);
            filters_.readFromSetting();
            displaySetting_ = new DisplaySetting(dict_);
            displaySetting_.readFromSetting();
            wordSequence_ = new WordSequence(dict_);
            wordSequence_.readFromSetting();
        }
    }

    public List<String> getDatabaseList() {
        return JPWord.Data.Database.getInstance().getDictList();
    }

    public IWordDictionary getDatabase() {
        return dict_;
    }

    public WordSequence getWordSequence() {
        return wordSequence_;
    }

    public Filters getFilters() {
        return filters_;
    }

    public DisplaySetting getDisplaySetting() {
        return displaySetting_;
    }

    public void persist(Context context) throws Exception {
        if (dict_ == null) {
            return;
        }
        settingFile_.setString(DICTNAME, dict_.getName());
        settingFile_.save(context);
        if (dict_ != null) {
            try {
                wordSequence_.saveToSetting();
                filters_.saveToSetting();
                displaySetting_.saveToSetting();
            } catch (Exception e) {

            }
            dict_.saveToDB();
        }
    }
}
