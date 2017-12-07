/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import JPWord.File.IJPFileReader;
import JPWord.File.IJPFileWriter;
import java.io.File;

/**
 *
 * @author u0151316
 */
public class Database {

    public static IWordDictionary createWordDictionary(IJPFileReader reader, IJPFileWriter writer) {
        return (IWordDictionary) (new WordDictionary(reader, writer));
    }
}
