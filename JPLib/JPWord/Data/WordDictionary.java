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

    private String name_ = "";
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
    public void load() throws Exception {
        reader_.open();
        name_ = reader_.readline();
        
        String line;
        while ((line = reader_.readline()) != null) {
            IWord word = new Word();
            if (word.decodeFromString(line)) {
                quickKey_.put(word.getID(), (Word) word);
                words_.add((Word) word);
            }
        }
        reader_.close();
    }

    @Override
    public void save() throws Exception {
        boolean needSave = false;
        for (Word w : words_) {
            if (w.isUpdated() == true) {
                needSave = true;
                break;
            }
        }
        if (!needSave) {
            return;
        }
        writer_.open();

        writer_.writeline(name_);
        for (Word word : words_) {
            String line = word.encodeToString();
            if (line != null && !line.equals("")) {
                writer_.writeline(line);
            }
        }
        writer_.close();
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
