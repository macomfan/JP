package DataEngine;

import JPLibAssist.DisplaySetting;
import JPLibAssist.Filters;
import JPLibAssist.WordSequence;
import JPWord.Data.IWordDictionary;

/**
 * Created by u0151316 on 6/7/2018.
 */

public class DBEntity {
    public IWordDictionary dict_ = null;
    public WordSequence wordSequence_ = null;
    public Filters filters_ = null;
    public DisplaySetting displaySetting_ = null;

    public DBEntity(IWordDictionary dict, WordSequence wordSequence, Filters filters, DisplaySetting displaySetting) {
        dict_ = dict;
        wordSequence_ = wordSequence;
        filters_ = filters;
        displaySetting_ = displaySetting;
    }
}
