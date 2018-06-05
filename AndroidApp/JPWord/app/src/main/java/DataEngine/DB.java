package DataEngine;

import android.content.Context;
import android.os.Environment;

import com.jpword.ma.sqliteengine_android.Android_SQLEngine;

import java.io.File;

import JPLibAssist.FilterGenerator;
import JPLibAssist.FilterEntity;
import JPLibAssist.Filters;
import JPLibAssist.WordSequence;
import JPLibAssist.DisplaySetting;
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
    private Filters filters_ = null;
    private DisplaySetting displaySetting_ = null;

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

        AppLogging.showLog("Start read DB");
        String defaultDictname = settingFile.getString(DICTNAME);
        if (defaultDictname.equals("")) {
            defaultDictname = "Dictionary";
        }

        changeDatabase(defaultDictname, false);
        AppLogging.showLog("DB read successfully");
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
        if (dict_ != null) {
            FilterGenerator.getInstance().initialize(dict_);
            filters_ = new Filters(dict_);
            filters_.readFromSetting();
            displaySetting_ = new DisplaySetting(dict_);
            displaySetting_.readFromSetting();
            wordSequence_ = new WordSequence(dict_);
            wordSequence_.readFromSetting();
        }
        return dict;
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
        SettingFile settingFile = new SettingFile();
        settingFile.setString(DICTNAME, dict_.getName());
        settingFile.save(context);
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
