/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPLibAssist;

import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author u0151316
 */
public class WordStatistics {

    private static final String STATISTICS = "STATISTICS";
    private IWordDictionary dict_ = null;

    private final Map<Integer, Integer> skillStatistics_ = new HashMap<>();

    public WordStatistics(IWordDictionary dict) {
        dict_ = dict;
        for (IWord word : dict.getWords()) {
            int skill = word.getSkill();
            if (word.getReviewDate().equals("")) {
                skill = -999;
            }
            Integer s = skillStatistics_.get(skill); 
            if (s != null) {
                s += 1;
                skillStatistics_.put(skill, s);
            }
            else {
                s = new Integer(1);
                skillStatistics_.put(skill, s);
            }
        }
    }

    public boolean readFromSetting() {
//        List<String> sequence = dict_.getSetting().getList(STATISTICS);
//        if (sequence == null || sequence.isEmpty()) {
//            return false;
//        }
//        for (String id : sequence) {
//            IWord word = dict_.getWord(id);
//            if (word == null) {
//                return false;
//            }
//            words_.add(word);
//        }
//        String currentIndex = dict_.getSetting().getString(CURRENT_INDEX);
//        if (!currentIndex.equals("")) {
//            currentIndex_ = Integer.parseInt(currentIndex, 10);
//        }
        return true;
    }

    public void saveToSetting() {
//        List<String> sequence = new LinkedList<>();
//        for (IWord word : words_) {
//            sequence.add(word.getID());
//        }
//        try {
//            dict_.getSetting().setList(SEQUENCE, sequence);
//            dict_.getSetting().setString(CURRENT_INDEX, Integer.toString(currentIndex_));
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
    }

    public void pass(IWord word) {
        word.increaseSkill();
    }

    public void fail(IWord word) {
        word.updateSkill(-5);
    }
}
