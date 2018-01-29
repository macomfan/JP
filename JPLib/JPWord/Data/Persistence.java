/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class Persistence {

    class CodecEntity {

        public String mVersion;
        public ICodec mCodec;

        public CodecEntity(String version, ICodec codec) {
            this.mVersion = version;
            this.mCodec = codec;
        }

    }

    private List<CodecEntity> mWordListCodec = new LinkedList<>();
    private List<CodecEntity> mMeanListCodec = new LinkedList<>();
    private List<CodecEntity> mExampleListCodec = new LinkedList<>();
    private List<CodecEntity> mTagListCodec = new LinkedList<>();
    private String mCurrentCodecVersion = "";

    private ICodec mCurrentWordCodec = null;
    private ICodec mCurrentMeanCodec = null;
    private ICodec mCurrentExampleCodec = null;
    private ICodec mCurrentTagCodec = null;

    private static Persistence mInstance = null;

    public static Persistence getInstance() {
        if (mInstance == null) {
            mInstance = new Persistence();
        }
        return mInstance;
    }

    private Persistence() {

    }

    public ICodec getCurrentWordCodec() {
        return mCurrentWordCodec;
    }

    public ICodec getCurrentMeanCodec() {
        return mCurrentMeanCodec;
    }

    public ICodec getCurrentExampleCodec() {
        return mCurrentExampleCodec;
    }

    public ICodec getCurrentTagCodec() {
        return mCurrentTagCodec;
    }

    public void addCodec(Class c, String codecVersion, ICodec codec) {
        if (c == Word.class) {
            mWordListCodec.add(new CodecEntity(codecVersion, codec));
        } else if (c == Meaning.class) {
            mMeanListCodec.add(new CodecEntity(codecVersion, codec));
        } else if (c == Example.class) {
            mExampleListCodec.add(new CodecEntity(codecVersion, codec));
        } else if (c == Tagable.class) {
            mTagListCodec.add(new CodecEntity(codecVersion, codec));
        }
    }

    public void setCurrentCodecVersion(String codecVersion) {
        mCurrentCodecVersion = codecVersion;
        mCurrentWordCodec = findCodec(codecVersion, mWordListCodec);
        mCurrentMeanCodec = findCodec(codecVersion, mMeanListCodec);
        mCurrentExampleCodec = findCodec(codecVersion, mExampleListCodec);
        mCurrentTagCodec = findCodec(codecVersion, mTagListCodec);
    }

    private ICodec findCodec(String codecVersion, List<CodecEntity> codecList) {
        for (CodecEntity codecEntity : codecList) {
            if (codecEntity.mVersion.equals(codecVersion)) {
                return codecEntity.mCodec;
            }
        }
        return codecList.get(codecList.size() - 1).mCodec;
    }

//    public String encodeToString() {
//        
//    }
//
//    public boolean decodeFromString(String str) {
//        
//    }
}
