/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import JPWord.File.IJPFileReader;
import JPWord.File.IJPFileWriter;
import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @author u0151316
 */
class WordDictionary implements IWordDictionary {

    private File file_ = null;
    private Map<String, Word> quickKey_ = new HashMap<>();
    private List<Word> words_ = new LinkedList<>();

    private IJPFileReader reader_ = null;
    private IJPFileWriter writer_ = null;

    public WordDictionary(IJPFileReader reader, IJPFileWriter writer) {
        reader_ = reader;
        writer_ = writer;
    }

    @Override
    public boolean load() {
        if (!reader_.open()) {
            return false;
        }
        String line;
        while ((line = reader_.readline()) != null) {
            IWord word = new Word();
            if (word.decodeFromString(line)) {
                quickKey_.put(word.getID(), (Word) word);
                words_.add((Word) word);
            }
        }
        return true;
    }

    @Override
    public boolean save() {
        try {
            boolean needSave = false;
            for (Word w : words_) {
                if (w.isUpdated() == true) {
                    needSave = true;
                    break;
                }
            }
            if (!needSave) {
                return true;
            }
            if (!writer_.open()) {
                return false;
            }

            for (Word word : words_) {
                String line = word.encodeToString();
                if (line != null && !line.equals("")) {
                    writer_.writeline(line);
                }
            }
            writer_.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public IWord getWord(String id) {
        if (quickKey_.containsKey(id)) {
            return quickKey_.get(id);
        }
        return null;
    }

    @Override
    public IWord createWord() {
        Word w = new Word(java.util.UUID.randomUUID());
        return (IWord) w;
    }

    @Override
    public IMeaning createMeaning() {
        return new Meaning();
    }

    @Override
    public IExample createExample() {
        return new Example();
    }

    @Override
    public List<IWord> getWords() {
        List<IWord> temp = new LinkedList<>();
        for (IWord w : words_) {
            temp.add(w);
        }
        return temp;
    }

    @Override
    public void addWord(IWord word) {
        if (!quickKey_.containsKey(word.getID())) {
            quickKey_.put(word.getID(), (Word) word);
            words_.add((Word) word);
            ((Word) word).updatedFlag();
        }
    }

}
