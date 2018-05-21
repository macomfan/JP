/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data.Filter;

import JPWord.Data.IWord;

/**
 *
 * @author u0151316
 */
public interface IStringChecker {

    boolean checkString(IWord word, String value);
}
