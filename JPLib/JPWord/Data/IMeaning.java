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
public interface IMeaning {

    boolean isEmpty();
    
    public String getInJP();

    public void setInJP(String value);

    public String getInCHS();

    public void setInCHS(String value);

    public String getType();

    public void setType(String value);

    public void addExample(IExample example);

    public List<IExample> getExamples();

    public String encodeToString();

    public boolean decodeFromString(String str);
}
