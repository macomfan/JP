/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.io.File;

/**
 *
 * @author u0151316
 */
public class Database {

    public static IWordDictionary createWordDictionary(File file) {
        return (IWordDictionary) (new WordDictionary(file));
    }
}
