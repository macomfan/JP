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
class WordDictionary extends Tagable implements IWordDictionary {

    private String name_ = "";
    private File file_ = null;
    private Map<String, Word> quickKey_ = new HashMap<>();
    private List<Word> words_ = new LinkedList<>();
    public boolean mIsUpdated = false;

    private IJPFileReader reader_ = null;
    private IJPFileWriter writer_ = null;

    public WordDictionary(String name, IJPFileReader reader, IJPFileWriter writer) {
        reader_ = reader;
        writer_ = writer;
        name_ = name;
    }

    @Override
    public String getName() {
        return name_;
    }

    @Override
    public String getVersion() {
        return getTagValue("Version");
    }

    @Override
    public void load() throws Exception {
        if (!quickKey_.isEmpty()) {
            return;
        }
        reader_.open();
        String line;
        while ((line = reader_.readline()) != null) {
            if (line.indexOf(":") == 0) {
                Persistence.getInstance().getCurrentTagCodec().decodeFromString(this, line);
                Persistence.getInstance().setCurrentCodecVersion(this.getVersion());
            } else {
                IWord word = new Word();
                ((Word) word).parent_ = this;
                if (word.decodeFromString(line)) {
                    ((Word) word).parent_ = this;
                    quickKey_.put(word.getID(), (Word) word);
                    words_.add((Word) word);
                    if(word.getTagValue("Cls").equals("")) {
                        int a = 0;
                        a++;
                    }
                }
            }
        }
        reader_.close();
        mIsUpdated = false;
        if (getVersion().equals("V1") || getVersion().equals("")) {
            setTag("Version", "V2");
            Persistence.getInstance().setCurrentCodecVersion(this.getVersion());
        }
    }

    @Override
    public void save() throws Exception {
        if (!mIsUpdated) {
            return;
        }
        writer_.open();
        String tagline = Persistence.getInstance().getCurrentTagCodec().encodeToString(this);
        tagline = ":" + tagline;
        writer_.writeline(tagline);
        for (Word word : words_) {
            String line = word.encodeToString();
            if (line != null && !line.equals("")) {
                writer_.writeline(line);
            }
        }
        writer_.close();
        mIsUpdated = false;
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
        w.parent_ = this;
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
            ((Word) word).parent_ = this;
            ((Word) word).updatedFlag();
        }
    }

    @Override
    public boolean isUpdated() {
        return mIsUpdated;
    }
}
