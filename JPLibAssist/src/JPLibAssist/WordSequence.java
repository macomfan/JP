/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPLibAssist;

import JPWord.Data.Filter.IItemFilter;
import JPWord.Data.Filter.ItemGroup;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class WordSequence {

    private static final String SEQUENCE = "SEQUENCE";
    private static final String CURRENT_INDEX = "CURRENT_INDEX";

    private IWordDictionary dict_ = null;
    private List<IWord> words_ = new LinkedList<>();
    private int currentIndex_ = -1; // From 0 to count_ - 1;

    private ICurrentWordChangeListener currentWordChangeListener_ = null;

    public WordSequence(IWordDictionary dict) {
        dict_ = dict;
    }

    public int count() {
        return words_.size();
    }

    public int getCurrentIndex() {
        return currentIndex_;
    }

    public boolean readFromSetting() {
        List<String> sequence = dict_.getSetting().getList(SEQUENCE);
        if (sequence == null || sequence.isEmpty()) {
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
        return true;
    }

    public void saveToSetting() {
        List<String> sequence = new LinkedList<>();
        for (IWord word : words_) {
            sequence.add(word.getID());
        }
        try {
            dict_.getSetting().setList(SEQUENCE, sequence);
            dict_.getSetting().setString(CURRENT_INDEX, Integer.toString(currentIndex_));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void reSort(Filters filters) {
        words_.clear();
        List<IItemFilter> tempFilterList = new LinkedList<>();
        if (filters != null) {
            for (FilterEntity entity : filters.getCurrentFilters()) {
                try {
                    IItemFilter filter = entity.filterTemplate_.createFilter(entity.param_);
                    if (filter != null) {
                        tempFilterList.add(filter);
                    }
                } catch (Exception e) {

                }
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
        if (currentWordChangeListener_ != null) {
            currentWordChangeListener_.onCurrentWordChange();
        }
    }

    public IWord next() {
        IWord word = null;
        if (currentIndex_ < count() - 1) {
            word = words_.get(++currentIndex_);
        } else {
            currentIndex_ = count();
        }
        if (currentWordChangeListener_ != null) {
            currentWordChangeListener_.onCurrentWordChange();
        }
        return word;
    }

    public IWord current() {
        if (currentIndex_ < 0 || currentIndex_ > words_.size() - 1) {
            return null;
        }
        return words_.get(currentIndex_);
    }

    public IWord prev() {
        IWord word = null;
        if (currentIndex_ > 0) {
            word = words_.get(--currentIndex_);
        } else {
            currentIndex_ = -1;
        }
        if (currentWordChangeListener_ != null) {
            currentWordChangeListener_.onCurrentWordChange();
        }
        return word;
    }

    public void setCurrentWordChangeListener(ICurrentWordChangeListener listener) {
        currentWordChangeListener_ = listener;
    }
}
