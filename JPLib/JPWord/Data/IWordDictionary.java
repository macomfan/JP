/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.util.List;

/**
 *
 * @author u0151316
 */
public interface IWordDictionary {
//    public String encodeWord(IWord word);
//    public IWord decodeWord(String line);
    public IWord getWord(String id);
    public void save();
    public List<IWord> getWords();
    public void addWord(IWord word);
    public IWord createWord();
    public IMeaning createMeaning();
    public IExample createExample();
}