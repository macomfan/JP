/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.File;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *
 * @author u0151316
 */
public class DefaultFileReader implements IJPFileReader {

    private File file_ = null;
    private BufferedReader reader_ = null;

    public DefaultFileReader(String filename) {
        file_ = new File(filename);
    }

    public DefaultFileReader(File file) {
        file_ = file;
    }

    @Override
    public void open() throws Exception {
        if (!file_.exists()) {
            file_.createNewFile();
        }
        reader_ = new BufferedReader(
                new InputStreamReader(new FileInputStream(file_), "utf-8"));
    }

    @Override
    public String readline() throws Exception{
        if (reader_ == null) {
            return null;
        }
        String value = reader_.readLine();
        return value;
    }

    @Override
    public void close() throws Exception{
        if (reader_ != null) {
                reader_.close();
                reader_ = null;
        }
    }
}
