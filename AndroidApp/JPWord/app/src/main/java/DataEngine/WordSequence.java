package DataEngine;

import java.util.LinkedList;
import java.util.List;

import JPWord.Data.Filter.IItemFilter;
import JPWord.Data.Filter.ItemGroup;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;

/**
 * Created by u0151316 on 1/8/2018.
 */

public class WordSequence {

    private static final String SEQUENCE = "SEQUENCE";
    private static final String CURRENT_INDEX = "CURRENT_INDEX";

    private IWordDictionary dict_ = null;
    private List<IWord> words_ = new LinkedList<>();
    private int currentIndex_ = -1; // From 0 to count_ - 1;
    private Filter filter_ = new Filter();


    public WordSequence(IWordDictionary dict) {
        dict_ = dict;
    }

    public int count() {
        return words_.size();
    }

    public int getCurrentIndex() {
        return currentIndex_;
    }

    public Filter getFilter() {
        return filter_;
    }

    public boolean readConfig() {
        List<String> sequence = dict_.getSetting().getList(SEQUENCE);
        if (sequence == null || sequence.size() == 0) {
            return false;
        }
        for (String id : sequence) {
            IWord word = dict_.getWord(id);
            if (word == null) {
                return false;
            }
            words_.add(word);
        }
        String currentIndex = dict_.getSetting().getString(CURRENT_INDEX);
        if (!currentIndex.equals("")) {
            currentIndex_ = Integer.parseInt(currentIndex, 10);
        }
        filter_.readConfig(dict_);
        return true;
    }

    public void saveConfig() {
        List<String> sequence = new LinkedList<>();
        for (IWord word : words_) {
            sequence.add(word.getID());
        }
        try {
            dict_.getSetting().setList(SEQUENCE, sequence);
            dict_.getSetting().setString(CURRENT_INDEX, Integer.toString(currentIndex_));
            filter_.saveConfig(dict_);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void reSort() {
        words_.clear();
        List<IItemFilter> tempFilterList = new LinkedList<>();
        for (FilterEntity entity : filter_.getCurrentFilter()) {
            try {
                IItemFilter filter = entity.filterTemplate_.createFilter(entity.param_);
                if (filter != null) {
                    tempFilterList.add(filter);
                }
            } catch (Exception e) {

            }
        }

        ItemGroup group = new ItemGroup(dict_.getWords());
        group.shuffle();
        group.sort(tempFilterList);
        int count = group.getCount();
        currentIndex_ = -1;
        for (int i = 0; i < count; i++) {
            words_.add((IWord) group.next());
        }
    }

    public IWord next() {
        if (currentIndex_ < count() - 1) {
            return words_.get(++currentIndex_);
        } else {
            currentIndex_ = count();
            return null;
        }
    }

    public IWord current() {
        if (currentIndex_ < 0 || currentIndex_ > words_.size() - 1) {
            return null;
        }
        return words_.get(currentIndex_);
    }

    public IWord prev() {
        if (currentIndex_ > 0) {
            return words_.get(--currentIndex_);
        } else {
            currentIndex_ = -1;
            return null;
        }
    }
}
