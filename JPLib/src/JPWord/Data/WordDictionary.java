/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import SqliteEngine_Interface.*;

/**
 *
 * @author u0151316
 */
class WordDictionary implements IWordDictionary {

    private String name_ = "";
    private File file_ = null;
    private Map<String, Word> quickKey_ = new HashMap<>();
    private List<Word> words_ = new LinkedList<>();
    public boolean mIsUpdated = false;

    private ISQLEngine engine_ = null;

    public WordDictionary(String name, ISQLEngine engine) {
        engine_ = engine;
        name_ = name;
    }

    @Override
    public String getName() {
        return name_;
    }

    @Override
    public void loadFromDB() throws Exception {
        if (!engine_.isConnected()) {
            throw new Exception("SQL is not connected");
        }
        //ISQLResult rs = engine_.executeQuery("select * from WORD order by WORD.rowid");
        Word.DBS.queryAll();
        ISQLResult rs = Word.DBS.executeQuery(engine_);
        while (rs.next()) {
            Word word = new Word();
            word.decodeFromSQL(rs);
            addWord(word);
        }
    }

    public void saveToDB() throws Exception {
        for (Word word : words_) {
            if (word.type_ == Word.Type.OTF) {
                word.encodeToSQL(engine_);
            }
        }
        Word.DBS.executeChange(engine_);
        engine_.executeBatch();
        engine_.commit();
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
        }
    }
}
