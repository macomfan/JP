/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author u0151316
 */
class WordDictionary implements IWordDictionary {

    private File file_ = null;
    private Map<String, Word> quickKey_ = new HashMap<>();
    private List<Word> words_ = new LinkedList<>();

    public WordDictionary(File file) {
        file_ = file;
        try {
            if (!file_.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
        }
        LoadFromFile();
    }

//    @Override
//    public String encodeWord(IWord word) {
//        if (word.getContent().equals("")) {
//            return null;
//        }
//        String line = "\"" + word.getID();
//        line += "\"" + "|";
//        line += word.getContent() + "|";
//        line += word.getKana() + "|";
//        line += word.getTone();
//        boolean meanExist = false;
//        for (IMeaning mean : word.getMeanings()) {
//            meanExist = true;
//            line += "|";
//            if (mean.getType().equals("") && mean.getInCHS().equals("") && mean.getInJP().equals("")) {
//                continue;
//            }
//            line += mean.getType() + ":";
//            if (!mean.getInCHS().equals("")) {
//                line += mean.getInCHS();
//            }
//            if (!mean.getInJP().equals("")) {
//                line += "\\" + mean.getInJP();
//            }
//
//            for (IExample example : mean.getExamples()) {
//                if (example.getExampleInCHS().equals("") && example.getExampleInJP().equals("")) {
//                    continue;
//                }
//                line += "-";
//                line += example.getExampleInJP() + "\\";
//                line += example.getExampleInCHS();
//            }
//        }
//        if (!meanExist) {
//            line += "|";
//        }
//        line += "|";
//        String tagString = "";
//        for (ITag tag : word.getTags()) {
//            tagString += tag.getName();
//            tagString += "=";
//            tagString += tag.getValue();
//            tagString += SOH;
//        }
//        if (tagString.length() != 0) {
//            tagString = SOH + tagString;
//            tagString += "|";
//            line += tagString;
//        }
//        return line;
//    }
//
//    @Override
//    public IWord decodeWord(String line) {
//        String items[] = line.split("\\" + ITEM_SEP);
//        if (items.length < 4) {
//            System.out.println(String.format("Pares error at: %s", line));
//            return null;
//        }
//        if (items[0].length() != 38) {
//            System.out.println(String.format("Not start with GUID: %s", line));
//            return null;
//        }
//        UUID id = UUID.fromString(items[0].substring(1, 36));
//        Word word = new Word(id);
//        word.setContent(items[1]);
//        word.setKana(items[2]);
//        word.setTone(items[3]);
//        if (!word.getTone().equals("")) {
//            Constant.getInstance().addTone(word.getTone());
//        }
//        for (int i = 4; i < items.length; i++) {
//            if (items[i].length() != 0) {
//                if (items[i].charAt(0) != SOH.charAt(0)) {
//                    parseMeaning(word, items[i]);
//                } else {
//                    parseTag(word, items[i]);
//                }
//            }
//
//        }
//        word.changeFlag_ = false;
//        return word;
//    }
    @Override
    public void save() {
        try {
            boolean needSave = false;
            for (Word w : words_) {
                if (w.changeFlag_ == true) {
                    needSave = true;
                    break;
                }
            }
            if (!needSave) {
                return;
            }
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file_), "utf-8"));
            for (Word word : words_) {
                String line = word.encodeToString();
                if (line != null && !line.equals("")) {
                    writer.write(line);
                    writer.write("\r\n");
                }
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
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

    private void LoadFromFile() {
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
        }
    }

}
