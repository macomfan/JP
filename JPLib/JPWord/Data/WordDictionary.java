/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import JPWord.File.IJPFileReader;
import JPWord.File.IJPFileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
                if (w.changeFlag_ == true) {
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

    private void loadFromFile() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file_), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                IWord word = new Word();
                if (word.decodeFromString(line)) {
                    quickKey_.put(word.getID(), (Word) word);
                    words_.add((Word) word);
                }
            }
            reader.close();
        } catch (Exception e) {
        } finally {
        }
    }

//    private void parseExample(IMeaning mean, String item) {
//        if (item == null || item.equals("")) {
//            return;
//        }
//        IExample example = null;
//        String exampleItem = item.replace('/', '\\');
//        String[] exampleCHSAndJP = exampleItem.split("\\" + EXAMPLE_SEP);
//        if (exampleCHSAndJP.length > 0) {
//            example = mean.createExample();
//            example.setExampleInJP(exampleCHSAndJP[0]);
//        }
//        if (exampleCHSAndJP.length > 1) {
//            if (example == null) {
//                example = mean.createExample();
//            }
//            example.setExampleInCHS(exampleCHSAndJP[1]);
//        }
//    }
//
//    private void parseMeaning(IWord word, String item) {
//        if (item == null || item.equals("")) {
//            return;
//        }
//
//        String[] meaningItems = item.split("\\" + MEAN_EXAMPLE_SEP);
//        IMeaning mean = null;
//        if (meaningItems.length > 0) {
//            String meaningString = meaningItems[0];
//            meaningString = meaningString.replace('/', '\\');
//            int typeIndex = meaningString.indexOf(TYPE_SEP);
//            String typeString = meaningString.substring(0, typeIndex);
//            meaningString = meaningString.substring(typeIndex + 1);
//            mean = createMeaning();
//            mean.setType(typeString);
//            if (!mean.getType().equals("")) {
//                Constant.getInstance().addType(mean.getType());
//            }
//            String[] meaningCHSAndJP = meaningString.split("\\" + MEAN_SEP);
//            if (meaningCHSAndJP.length > 0) {
//                mean.setInCHS(meaningCHSAndJP[0]);
//            }
//            if (meaningCHSAndJP.length > 1) {
//                mean.setInJP(meaningCHSAndJP[1]);
//            }
//            word.addMeaning(mean);
//        }
//        for (int i = 1; i < meaningItems.length; i++) {
//            parseExample(mean, meaningItems[i]);
//        }
//    }
    @Override
    public void addWord(IWord word) {
        if (!quickKey_.containsKey(word.getID())) {
            quickKey_.put(word.getID(), (Word) word);
            words_.add((Word) word);
            ((Word) word).changeFlag_ = true;
        }
    }

}
