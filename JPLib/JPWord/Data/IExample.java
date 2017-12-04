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

    public void setExampleInJP(String value);

    public String getExampleInJP();

    public void setExampleInCHS(String value);

    public String getExampleInCHS();

    public String encodeToString();

    public boolean decodeFromString(String str);
}
