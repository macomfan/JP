/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.util.List;
import java.util.Map;

/**
 *
 * @author u0151316
 */
public interface IWord {

    static String Tag_MYWORD = "MY";
    
    boolean isEmpty();
    
    void setContent(String value);

    String getContent();

    void setKana(String value);

    String getKana();

    void setTone(String value);

    String getTone();

    String getID();

    IRoma getRoma();

    int getSkill();
    
    String getReviewDate();
    
    int getCls();
    
    void setCls(int cls);

    String getNote();

    void setNote(String value);

    void increaseSkill();

    void decreaseSkill();

    void updateSkill(int skill);

    void setAsMyWord(boolean flag);

    void addMeaning(IMeaning meaning);

    void updateMeaning(List<IMeaning> meanings);
    
    List<IMeaning> getMeanings();

    // Tags
    Map<String, String> getTags();

    void setTag(String name, String value);

    String getTag(String name);
    
    void removeTag(String name);
}
