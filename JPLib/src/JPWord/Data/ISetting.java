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
public interface ISetting {

    List<String> getList(String key);

    String getString(String key);

    void setString(String key, String value) throws Exception;

    void setList(String key, List<String> value) throws Exception;

    boolean containsKey(String key);
}
