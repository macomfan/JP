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
    public boolean open() {
        try {
            if (!file_.exists()) {
                file_.createNewFile();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        try {
            reader_ = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file_), "utf-8"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public String readline() {
        if (reader_ == null) {
            return null;
        }
        try {
            String value = reader_.readLine();
            return value;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void close() {
        if (reader_ != null) {
            try {
                reader_.close();
                reader_ = null;
            } catch (Exception e) {
                reader_ = null;
                System.err.println(e.getMessage());
            }
        }
    }
}
