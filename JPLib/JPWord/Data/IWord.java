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
public interface IWord {

    void setContent(String value);

    String getContent();

    void setKana(String value);

    String getKana();

    void setTone(String value);

    String getTone();

    String getID();

    IRoma getRoma();

    int getSkill();

    String getNote();

    void setNote(String value);

    void increaseSkill();

    void decreaseSkill();

    void updateSkill(int skill);

    void setAsMyWord(boolean flag);

    void addMeaning(IMeaning meaning);

    void updateMeaning(List<IMeaning> meanings);
    
    List<IMeaning> getMeanings();

    List<ITag> getTags();

    ITag addTag(String Name, String Value);

    String getTagValue(String Name);

    public String encodeToString();

    public boolean decodeFromString(String str);
}
