/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

/**
 *
 * @author u0151316
 */
public interface IExample {

    boolean isEmpty();
    
    void setExampleInJP(String value);

    String getExampleInJP();

    void setExampleInCHS(String value);

    String getExampleInCHS();

    String encodeToString();

    boolean decodeFromString(String str);
}
