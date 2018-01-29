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
import java.util.List;

/**
 *
 * @author u0151316
 */
public class Database {

    private static Database instance_ = null;
    private IJPFileReader reader_ = null;
    private IJPFileWriter writer_ = null;
    private String rootFolder_ = "";
    private List<String> dictList_ = new LinkedList<>();

    public static Database getInstance() {
        if (instance_ == null) {
            instance_ = new Database();
        }
        return instance_;
    }

    private Database() {
        
    }
    
    public void initialize(String footFolder, IJPFileReader reader, IJPFileWriter writer) {
        reader_ = reader;
        writer_ = writer;
        rootFolder_ = footFolder.replace('\\', '/');
        if (rootFolder_.charAt(footFolder.length() - 1) != '/') {
            rootFolder_ += "/";
        }

        File folder = new File(rootFolder_);
        File[] subFile = folder.listFiles();
        for (File file : subFile) {
            if (!file.isDirectory()) {
                String filename = file.getName();
                if(filename.endsWith(".dat")) {
                    String dictname = filename.substring(0, filename.length() - 4);
                    dictList_.add(dictname);
                }
            }
        }
        
        Persistence.getInstance().addCodec(Word.class, "V1", new Word.Codec_V1());
        Persistence.getInstance().addCodec(Meaning.class, "V1", new Meaning.Codec_V1());
        Persistence.getInstance().addCodec(Meaning.class, "V2", new Meaning.Codec_V2());
        Persistence.getInstance().addCodec(Example.class, "V1", new Example.Codec_V1());
        Persistence.getInstance().addCodec(Tagable.class, "V1", new Tagable.Codec_V1());
        
        Persistence.getInstance().setCurrentCodecVersion("V2");
    }

    public List<String> getDictList() {
        return dictList_;
    }
    
    public IWordDictionary loadDictionary(String dictname) {
        if (!dictList_.contains(dictname)) {
            return null;
        }
        IJPFileReader reader = reader_.clone(rootFolder_ + dictname + ".dat");
        IJPFileWriter writer = writer_.clone(rootFolder_ + dictname + ".dat");
        IWordDictionary dict = new WordDictionary(dictname, reader, writer);
        try {
            dict.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return dict;
    }

    public IWordDictionary createDictionary(String dictname) {
        IJPFileReader reader = reader_.clone(rootFolder_ + dictname + ".dat");
        IJPFileWriter writer = writer_.clone(rootFolder_ + dictname + ".dat");
        IWordDictionary dict = new WordDictionary(dictname, reader, writer);
        return dict;
    }
}
